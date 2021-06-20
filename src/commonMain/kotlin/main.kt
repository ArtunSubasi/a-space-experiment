import com.soywiz.klock.milliseconds
import com.soywiz.klock.seconds
import com.soywiz.korev.Key
import com.soywiz.korge.Korge
import com.soywiz.korge.view.*
import com.soywiz.korge.view.ktree.KTreeRoot
import com.soywiz.korge.view.ktree.readKTree
import com.soywiz.korim.color.Colors
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.std.resourcesVfs
import com.soywiz.korma.geom.*
import com.soywiz.korma.math.roundDecimalPlaces
import domain.Spaceship

suspend fun main() = Korge(width = 1024, height = 1024, bgcolor = Colors["#2b2b2b"]) {

	val ktree = resourcesVfs["space.ktree"].readKTree(views)
	addChild(ktree)

	val input = views.input
	var spaceship = Spaceship()
	var collision = false

	text("") {
		addFixedUpdater(100.milliseconds) {
			text = "Velocity: " + spaceship.velocityInSpokPerSpaceTicks.roundDecimalPlaces(2)
		}
	}
	text("") {
		y = 20.0
		addFixedUpdater(100.milliseconds) {
			text = "Rotation: " + spaceship.rotationInDegreePerSpaceTicks.roundDecimalPlaces(2)
		}
	}

	text("") {
		y = 40.0
		addFixedUpdater(100.milliseconds) {
			text = "Collision: $collision"
		}
	}


	image(resourcesVfs["ship_sidesA.png"].readBitmap()) {

		goToStartPosition()

		onCollision(filter = { it is SolidRect }) {
			collision = true
		}
		onCollisionExit(filter = { it is SolidRect }) {
			collision = false
		}

		addUpdater {
			if (input.keys.justPressed(Key.SPACE)) {
				spaceship = Spaceship()
				goToStartPosition()
			}
			val thrusterPosition = when {
				input.keys.pressing(Key.UP) -> 1.0
				input.keys.pressing(Key.DOWN) -> -1.0
				else -> 0.0
			}
			val steeringWheelPosition = when {
				input.keys.pressing(Key.LEFT) -> -1.0
				input.keys.pressing(Key.RIGHT) -> 1.0
				else -> 0.0
			}
			spaceship.advanceOneSpaceTick(steeringWheelPosition, thrusterPosition)

			rotation += spaceship.rotationInDegreePerSpaceTicks.degrees
			val xDelta = -sin(rotation) * spaceship.velocityInSpokPerSpaceTicks
			val yDelta = cos(rotation) * spaceship.velocityInSpokPerSpaceTicks
			x -= xDelta
			y -= yDelta
		}

	}
}

private fun Image.goToStartPosition() {
	rotation = 0.0.degrees
	anchor(.5, .5)
	scale(.8)
	position(430, 800)
}