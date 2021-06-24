package ui.views

import com.soywiz.korge.view.*
import com.soywiz.korim.bitmap.Bitmap
import com.soywiz.korma.geom.cos
import com.soywiz.korma.geom.degrees
import com.soywiz.korma.geom.plus
import com.soywiz.korma.geom.sin
import ui.model.GameState
import kotlin.math.max
import kotlin.math.min

class SpaceshipView(spaceshipBitmap: Bitmap,
					thrusterAnimation: SpriteAnimation,
					spaceKTree: View,
					private val gameState: GameState): Container() {

	init {
		val spaceshipImage = image(spaceshipBitmap) {

			position(130, 770) // TODO extract this from ktree somehow
			anchor(.5, .5)
			scale(.8)

			onCollisionShape(filter = { it is SolidRect }) {
				// TODO collision handling should not be done here
				val shapeName = it.name
				if (shapeName == "finishLane") {
					gameState.stats.finishedLaps++
					gameState.stats.fastestLapTimeInSpaceTicks = when (gameState.stats.fastestLapTimeInSpaceTicks) {
						0 -> gameState.spaceship.spaceTicksSoFar
						else -> min(gameState.stats.fastestLapTimeInSpaceTicks, gameState.spaceship.spaceTicksSoFar)
					}
					gameState.lapJustFinished = true
				} else if (shapeName != null && shapeName.startsWith("checkpoint")) {
					val checkpointNo = shapeName.substringAfter("checkpoint").toInt()
					gameState.checkpointReached = max(gameState.checkpointReached, checkpointNo)
				} else {
					gameState.spaceship.crashed = true
				}
			}

			addUpdater {
				rotation += gameState.spaceship.rotationInDegreePerSpaceTicks.degrees
				val xDelta = -sin(rotation) * gameState.spaceship.velocityInSpoksPerSpaceTicks
				val yDelta = cos(rotation) * gameState.spaceship.velocityInSpoksPerSpaceTicks
				x -= xDelta
				y -= yDelta
			}
		}

		addChild(SpaceshipAnimationsView(spaceshipImage, thrusterAnimation, gameState))
		addChild(SpaceshipSensorsView(spaceshipImage, spaceKTree, gameState))
	}
}
