package domain

import kotlin.test.Test
import kotlin.test.assertEquals

class SpaceshipTest {

    @Test
    fun shapeship_does_not_accelerate_or_rotate_if_control_positions_are_centered() {
        val spaceship = Spaceship()
        spaceship.advanceOneSpaceTick(0.0, 0.0)
        assertEquals(spaceship.velocityInSpoksPerSpaceTicks, 0.0)
        assertEquals(spaceship.rotationInDegreePerSpaceTicks, 0.0)
        assertEquals(1, spaceship.spaceTicksSoFar)
        assertEquals(0.0, spaceship.spoksTravelledSinceStart)
    }

    @Test
    fun shapeship_gains_max_possible_positive_velocity_if_thruster_is_pushed_fully_forwards() {
        val spaceship = Spaceship()
        spaceship.advanceOneSpaceTick(0.0, 1.0)
        assertEquals(spaceship.acceleration, spaceship.velocityInSpoksPerSpaceTicks)
        assertEquals(1, spaceship.spaceTicksSoFar)
        assertEquals(spaceship.acceleration, spaceship.spoksTravelledSinceStart)
    }

    @Test
    fun shapeship_keeps_its_velocity_if_thruster_gets_reset() {
        val spaceship = Spaceship()
        spaceship.advanceOneSpaceTick(0.0, 1.0)
        spaceship.advanceOneSpaceTick(0.0, 0.0)
        assertEquals(spaceship.acceleration, spaceship.velocityInSpoksPerSpaceTicks)
        assertEquals(2, spaceship.spaceTicksSoFar)
        assertEquals(spaceship.acceleration * 2, spaceship.spoksTravelledSinceStart)
    }

    @Test
    fun shapeship_gains_half_of_possible_positive_velocity_if_the_thruster_is_pushed_halfway_forward() {
        val spaceship = Spaceship()
        spaceship.advanceOneSpaceTick(0.0, 0.5)
        assertEquals(spaceship.acceleration * 0.5, spaceship.velocityInSpoksPerSpaceTicks)
        assertEquals(1, spaceship.spaceTicksSoFar)
        assertEquals(spaceship.acceleration * 0.5, spaceship.spoksTravelledSinceStart)
    }

    @Test
    fun shapeship_gains_max_possible_negative_velocity_if_the_thruster_is_pulled_fully_backwards() {
        val spaceship = Spaceship()
        spaceship.advanceOneSpaceTick(0.0, -1.0)
        assertEquals(spaceship.acceleration * -1, spaceship.velocityInSpoksPerSpaceTicks)
        assertEquals(1, spaceship.spaceTicksSoFar)
        assertEquals(spaceship.acceleration, spaceship.spoksTravelledSinceStart)
    }

    @Test
    fun shapeship_rotates_in_position_if_the_steering_wheel_position_at_max() {
        val spaceship = Spaceship()
        spaceship.advanceOneSpaceTick(1.0, 0.0)
        assertEquals(spaceship.rotationPower, spaceship.rotationInDegreePerSpaceTicks)
        assertEquals(0.0, spaceship.velocityInSpoksPerSpaceTicks)
        assertEquals(1, spaceship.spaceTicksSoFar)
        assertEquals(0.0, spaceship.spoksTravelledSinceStart)
    }

    @Test
    fun shapeship_keeps_rotating_in_position_if_the_steering_wheel_gets_reset() {
        val spaceship = Spaceship()
        spaceship.advanceOneSpaceTick(1.0, 0.0)
        spaceship.advanceOneSpaceTick(0.0, 0.0)
        assertEquals(spaceship.rotationPower, spaceship.rotationInDegreePerSpaceTicks)
        assertEquals(0.0, spaceship.velocityInSpoksPerSpaceTicks)
        assertEquals(2, spaceship.spaceTicksSoFar)
        assertEquals(0.0, spaceship.spoksTravelledSinceStart)
    }

}