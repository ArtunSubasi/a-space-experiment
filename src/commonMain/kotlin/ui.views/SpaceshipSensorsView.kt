package ui.views

import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import com.soywiz.korim.vector.StrokeInfo
import com.soywiz.korma.geom.Point
import com.soywiz.korma.geom.cosine
import com.soywiz.korma.geom.sine
import com.soywiz.korma.geom.vector.line
import ui.model.GameState

class SpaceshipSensorsView(spaceshipImage: Image,
                           spaceKTree: View,
                           private val gameState: GameState): Container() {

	init {
		graphics {
			useNativeRendering = true

			val rayCastingStartOffset = 20
			val maxRayLength = 600

			val walls = spaceKTree.getDescendantsOfType<SolidRect>().filter {
				val shapeName = it.name
				shapeName == null || !shapeName.startsWith("checkpoint")
			}

			addUpdater {
				alpha = if (gameState.config.drawSensors) 1.0 else 0.0
				val rot = spaceshipImage.rotation
				clear()

				val rayStartPoint = Point(spaceshipImage.x + (rot.sine * rayCastingStartOffset),
					spaceshipImage.y - (rot.cosine * rayCastingStartOffset))
				var rayEndPoint = rayStartPoint

				for (rayLength in rayCastingStartOffset..maxRayLength) {

					rayEndPoint = Point(spaceshipImage.x + (rot.sine * rayLength),
						spaceshipImage.y - (rot.cosine * rayLength))
					val globalRayEndPoint = localToGlobal(rayEndPoint)
					val collidingWall = walls.find { it.hitTestAny( globalRayEndPoint.x, globalRayEndPoint.y) }
					if (collidingWall != null) {
						gameState.spaceship.distanceSensors.front = rayStartPoint.distanceTo(rayEndPoint)
						break
					}
					if (rayLength == maxRayLength) {
						gameState.spaceship.distanceSensors.front = maxRayLength.toDouble()
					}
				}

				stroke(Colors.GREEN, StrokeInfo(thickness = 1.0)) {
					line(rayStartPoint, rayEndPoint)
				}

				println("front distance: ${gameState.spaceship.distanceSensors.front}")
			}
		}
	}
}
