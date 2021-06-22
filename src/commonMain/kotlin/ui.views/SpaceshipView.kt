package ui.views

import com.soywiz.klock.milliseconds
import com.soywiz.korge.view.*
import com.soywiz.korim.bitmap.Bitmap
import com.soywiz.korma.geom.*
import ui.model.GameState
import kotlin.math.min

class SpaceshipView(spaceshipBitmap: Bitmap,
					thrusterAnimation: SpriteAnimation,
					private val gameState: GameState): Container() {

	init {
		val spaceshipImage = image(spaceshipBitmap) {

			position(130, 770) // TODO extract this from ktree somehow
			anchor(.5, .5)
			scale(.8)

			onCollisionShape(filter = { it is SolidRect }) {
				// TODO collision handling should not be done here
				if (it.name == "finishLane") {
					gameState.stats.finishedLaps++
					gameState.stats.fastestLapTimeInSpaceTicks = when (gameState.stats.fastestLapTimeInSpaceTicks) {
						0 -> gameState.spaceship.spaceTicksSoFar
						else -> min(gameState.stats.fastestLapTimeInSpaceTicks, gameState.spaceship.spaceTicksSoFar)
					}
					gameState.lapJustFinished = true
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

		// Thruster sprite
		sprite(thrusterAnimation) {
			scale(0.3)
			playAnimationLooped(spriteDisplayTime = 150.0.milliseconds)
			addUpdater {
				if (gameState.spaceship.thrusterPosition > 0) {
					setPositionRelativeTo(spaceshipImage, Point(-8, 25))
					rotation = spaceshipImage.rotation
					visible = true
				} else if (gameState.spaceship.thrusterPosition < 0) {
					setPositionRelativeTo(spaceshipImage, Point(6, -30))
					rotation = spaceshipImage.rotation + Angle.fromDegrees(180)
					visible = true
				} else {
					visible = false
				}
			}
		}

		// Rotation sprite
		sprite(thrusterAnimation) {
			scale(0.3)
			playAnimationLooped(spriteDisplayTime = 150.0.milliseconds)
			addUpdater {
				if (gameState.spaceship.steeringWheelPosition > 0) {
					setPositionRelativeTo(spaceshipImage, Point(-30, 10))
					rotation = spaceshipImage.rotation + Angle.fromDegrees(80)
					visible = true
				} else if (gameState.spaceship.steeringWheelPosition < 0) {
					setPositionRelativeTo(spaceshipImage, Point(25, 25))
					rotation = spaceshipImage.rotation - Angle.fromDegrees(80)
					visible = true
				} else {
					visible = false
				}
			}
		}
	}
}
