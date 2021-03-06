package domain

data class DistanceSensor(
    val distanceSensorType: DistanceSensorType,
    var distance: Double
)

enum class DistanceSensorType(val rayCastingAngleInDegrees: Int, val rayCastingStartOffset: Int) {
    FRONT(0, 20),
    LEFT(-90, 14),
    RIGHT(90, 14),
    FRONT_LEFT(-45, 8),
    FRONT_RIGHT(45, 8),
    REAR(180, 14),
    REAR_LEFT(-135, 14),
    REAR_RIGHT(135, 14)
}