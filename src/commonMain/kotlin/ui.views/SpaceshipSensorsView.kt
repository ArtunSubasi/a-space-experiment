package ui.views

import com.soywiz.klock.milliseconds
import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import com.soywiz.korim.vector.StrokeInfo
import com.soywiz.korma.geom.*
import com.soywiz.korma.geom.vector.line
import com.soywiz.korma.math.roundDecimalPlaces
import domain.DistanceSensor
import ui.model.GameState

class SpaceshipSensorsView(spaceshipImage: Image,
                           spaceKTree: View,
                           private val gameState: GameState): Container() {

	init {
		graphics {
			useNativeRendering = true

			val walls = spaceKTree.getDescendantsOfType<SolidRect>().filter {
				val shapeName = it.name
				shapeName == null || !shapeName.startsWith("checkpoint")
			}

			addUpdater {
				clear()
				visible = gameState.config.drawSensors
				gameState.spaceship.distanceSensors.forEach { castDistanceRay(spaceshipImage, it, walls) }
			}
		}
		gameState.spaceship.distanceSensors.forEach { sensor ->
			text("") {
				addUpdater {
					visible = gameState.config.drawSensors
					text = sensor.distance.toInt().toString()
					val absoluteRayRotation = spaceshipImage.rotation + sensor.distanceSensorType.rayCastingAngleInDegrees.degrees
					x = spaceshipImage.x + (absoluteRayRotation.sine * 50)
					y = spaceshipImage.y - (absoluteRayRotation.cosine * 50)
				}
			}
		}
	}

	private fun Graphics.castDistanceRay(spaceshipImage: Image, distanceSensor: DistanceSensor, walls: List<View>) {
		val maxRayLength = 600
		val absoluteRayRotation = spaceshipImage.rotation + distanceSensor.distanceSensorType.rayCastingAngleInDegrees.degrees
		val rayStartPoint = Point(
			spaceshipImage.x + (absoluteRayRotation.sine * distanceSensor.distanceSensorType.rayCastingStartOffset),
			spaceshipImage.y - (absoluteRayRotation.cosine * distanceSensor.distanceSensorType.rayCastingStartOffset)
		)
		var rayEndPoint = rayStartPoint
		var distance = 0.0

		for (rayLength in distanceSensor.distanceSensorType.rayCastingStartOffset..maxRayLength) {
			rayEndPoint = Point(
				spaceshipImage.x + (absoluteRayRotation.sine * rayLength),
				spaceshipImage.y - (absoluteRayRotation.cosine * rayLength)
			)
			val globalRayEndPoint = localToGlobal(rayEndPoint)
			val collidingWall = walls.find { it.hitTestAny(globalRayEndPoint.x, globalRayEndPoint.y) }
			if (collidingWall != null) {
				distance = rayStartPoint.distanceTo(rayEndPoint)
				break
			}
			if (rayLength == maxRayLength) {
				distance = maxRayLength.toDouble()
			}
		}

		stroke(Colors.GREEN, StrokeInfo(thickness = 1.0)) {
			line(rayStartPoint, rayEndPoint)
		}

		distanceSensor.distance = distance
	}
}
