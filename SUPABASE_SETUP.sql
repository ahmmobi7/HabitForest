-- ============================================================
-- HABIT FOREST — Supabase SQL Setup
-- Run this entire script in your Supabase SQL Editor
-- ============================================================

-- 1. USERS TABLE (extends Supabase auth.users)
create table if not exists public.users (
  id         uuid references auth.users on delete cascade primary key,
  xp         integer default 0  not null,
  level      integer default 1  not null,
  created_at timestamptz default now()
);

-- 2. HABITS TABLE
create table if not exists public.habits (
  id           uuid primary key default gen_random_uuid(),
  user_id      uuid references public.users(id) on delete cascade not null,
  name         text not null,
  icon         text default '🌱',
  frequency    text default 'daily',
  streak       integer default 0,
  growth_stage integer default 0,
  created_at   timestamptz default now()
);

-- 3. LOGS TABLE
create table if not exists public.logs (
  id         uuid primary key default gen_random_uuid(),
  habit_id   uuid references public.habits(id) on delete cascade not null,
  user_id    uuid references public.users(id)  on delete cascade not null,
  date       date not null,
  status     text not null check (status in ('YES','NO')),
  created_at timestamptz default now(),
  unique(habit_id, date)
);

-- 4. REWARDS TABLE
create table if not exists public.rewards (
  id          uuid primary key default gen_random_uuid(),
  user_id     uuid references public.users(id) on delete cascade not null,
  type        text not null,
  name        text not null,
  unlocked_at timestamptz default now(),
  unique(user_id, name)
);

-- ============================================================
-- ROW LEVEL SECURITY
-- ============================================================
alter table public.users   enable row level security;
alter table public.habits  enable row level security;
alter table public.logs    enable row level security;
alter table public.rewards enable row level security;

create policy "users_own"   on public.users   for all using (auth.uid() = id);
create policy "habits_own"  on public.habits  for all using (auth.uid() = user_id);
create policy "logs_own"    on public.logs    for all using (auth.uid() = user_id);
create policy "rewards_own" on public.rewards for all using (auth.uid() = user_id);

-- ============================================================
-- AUTO-CREATE USER PROFILE ON SIGN-UP
-- ============================================================
create or replace function public.handle_new_user()
returns trigger language plpgsql security definer as $$
begin
  insert into public.users (id) values (new.id)
  on conflict (id) do nothing;
  return new;
end;
$$;

drop trigger if exists on_auth_user_created on auth.users;
create trigger on_auth_user_created
  after insert on auth.users
  for each row execute procedure public.handle_new_user();

-- ============================================================
-- XP + LEVEL-UP FUNCTION
-- ============================================================
create or replace function public.add_xp(uid uuid, xp_delta integer)
returns void language plpgsql security definer as $$
declare
  cur_xp    integer;
  cur_level integer;
  needed    integer;
begin
  select xp, level into cur_xp, cur_level
  from public.users where id = uid;

  cur_xp := cur_xp + xp_delta;
  needed  := cur_level * 100;

  if cur_xp >= needed then
    cur_level := cur_level + 1;
  end if;

  update public.users
  set xp = cur_xp, level = cur_level
  where id = uid;
end;
$$;
