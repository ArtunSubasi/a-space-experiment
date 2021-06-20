package domain

import kotlin.math.abs

class Spaceship(
    val acceleration: Double = 0.2,
    val rotationPower: Double = 0.1) {

    var steeringWheelPosition = 0.0
        private set

    var thrusterPosition = 0.0
        private set

    var velocityInSpokPerSpaceTicks = 0.0
        private set

    var rotationInDegreePerSpaceTicks = 0.0
        private set

    var spoksTravelledSinceStart = 0.0
        private set

    var spaceTicksSoFar = 0
        private set

    fun advanceOneSpaceTick(steeringWheelPosition: Double, thrusterPosition: Double) {
        this.steeringWheelPosition = steeringWheelPosition
        this.thrusterPosition = thrusterPosition
        velocityInSpokPerSpaceTicks += acceleration * thrusterPosition
        rotationInDegreePerSpaceTicks += rotationPower * steeringWheelPosition
        spoksTravelledSinceStart += abs(velocityInSpokPerSpaceTicks)
        spaceTicksSoFar++
    }

}
