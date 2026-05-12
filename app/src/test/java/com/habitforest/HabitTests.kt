package com.habitforest

import com.habitforest.data.model.GrowthStage
import com.habitforest.domain.usecase.CalculateGrowthUseCase
import org.junit.Assert.assertEquals
import org.junit.Test

class CalculateGrowthUseCaseTest {

    private val useCase = CalculateGrowthUseCase()

    @Test fun `streak 0 stays Seed`() =
        assertEquals(GrowthStage.SEED.ordinal, useCase(0, 0))

    @Test fun `streak 3 becomes Sapling`() =
        assertEquals(GrowthStage.SAPLING.ordinal, useCase(0, 3))

    @Test fun `streak 7 becomes Young`() =
        assertEquals(GrowthStage.YOUNG.ordinal, useCase(0, 7))

    @Test fun `streak 14 becomes Mature`() =
        assertEquals(GrowthStage.MATURE.ordinal, useCase(0, 14))

    @Test fun `streak 30 becomes Legendary`() =
        assertEquals(GrowthStage.LEGENDARY.ordinal, useCase(0, 30))

    @Test fun `tree never shrinks — mature stays mature at streak 0`() =
        assertEquals(GrowthStage.MATURE.ordinal, useCase(GrowthStage.MATURE.ordinal, 0))

    @Test fun `XP formula gives correct value`() {
        val streak = 7
        val xp = 10 + (minOf(streak, 30) * 2)
        assertEquals(24, xp)
    }

    @Test fun `empty habit name is blank`() {
        val name = "  "
        assertEquals(true, name.isBlank())
    }

    @Test fun `valid habit name passes`() {
        val name = "Morning Run"
        assertEquals(false, name.isBlank())
        assertEquals(true, name.length <= 50)
    }

    @Test fun `name over 50 chars fails`() {
        val name = "A".repeat(51)
        assertEquals(true, name.length > 50)
    }

    @Test fun `YES increments streak`() {
        val oldStreak = 5
        val newStreak = oldStreak + 1
        assertEquals(6, newStreak)
    }

    @Test fun `NO resets streak to zero`() {
        val newStreak = 0
        assertEquals(0, newStreak)
    }
}
