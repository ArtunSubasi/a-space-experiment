package ui.views

import com.soywiz.klock.milliseconds
import com.soywiz.korge.view.*
import com.soywiz.korma.geom.Angle
import com.soywiz.korma.geom.Point
import com.soywiz.korma.geom.minus
import com.soywiz.korma.geom.plus
import ui.model.GameState

class SpaceshipAnimationsView(spaceshipImage: Image,
							  thrusterAnimation: SpriteAnimation,
							  private val gameState: GameState): Container() {

	init {
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
