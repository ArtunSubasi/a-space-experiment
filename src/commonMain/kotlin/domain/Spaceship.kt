package domain

import kotlin.math.abs
import kotlin.math.max

class Spaceship(
    val acceleration: Double = 0.05,
    val rotationPower: Double = 0.05) {

    var steeringWheelPosition = 0.0
        private set

    var thrusterPosition = 0.0
        private set

    var velocityInSpoksPerSpaceTicks = 0.0
        private set

    var maxSpeedInSpoksPerSpaceTicks = 0.0
        private set

    var rotationInDegreePerSpaceTicks = 0.0
        private set

    var spoksTravelledSinceStart = 0.0
        private set

    var spaceTicksSoFar = 0
        private set

    var crashed = false

    val distanceSensors = DistanceSensorType.values().map { DistanceSensor(it, 0.0) }.toList()

    fun advanceOneSpaceTick(steeringWheelPosition: Double, thrusterPosition: Double) {
        this.steeringWheelPosition = steeringWheelPosition
        this.thrusterPosition = thrusterPosition
        velocityInSpoksPerSpaceTicks += acceleration * thrusterPosition
        val speedInSpotPerSpaceTicks = abs(velocityInSpoksPerSpaceTicks)
        rotationInDegreePerSpaceTicks += rotationPower * steeringWheelPosition
        spoksTravelledSinceStart += speedInSpotPerSpaceTicks
        maxSpeedInSpoksPerSpaceTicks = max(speedInSpotPerSpaceTicks, maxSpeedInSpoksPerSpaceTicks)
        spaceTicksSoFar++
    }

}
