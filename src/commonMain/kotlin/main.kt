import com.soywiz.klock.milliseconds
import com.soywiz.korev.Key
import com.soywiz.korge.Korge
import com.soywiz.korge.view.*
import com.soywiz.korge.view.ktree.readKTree
import com.soywiz.korim.color.Colors
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.std.resourcesVfs
import com.soywiz.korma.geom.*
import com.soywiz.korma.math.roundDecimalPlaces
import domain.Spaceship
import kotlin.math.max
import kotlin.math.min

suspend fun main() = Korge(width = 1300, height = 1000, bgcolor = Colors["#2b2b2b"]) {

	val ktree = resourcesVfs["space.ktree"].readKTree(views)
	addChild(ktree)

	val input = views.input
	var spaceship = Spaceship()
	var finishedLaps = 0
	var fastestLapTimeInSpaceTicks = 0
	var maxSpeedInSpoks = 0.0

	text("") {
		addFixedUpdater(100.milliseconds) {
			text = "Space ticks: " + spaceship.spaceTicksSoFar
		}
	}
	text("") {
		y = 20.0
		addFixedUpdater(100.milliseconds) {
			text = "Velocity in spoks per space ticks: " + spaceship.velocityInSpoksPerSpaceTicks.roundDecimalPlaces(2)
		}
	}
	text("") {
		y = 40.0
		addFixedUpdater(100.milliseconds) {
			maxSpeedInSpoks = max(maxSpeedInSpoks, spaceship.maxSpeedInSpoksPerSpaceTicks).roundDecimalPlaces(2)
			text = "Max speed in spoks per space ticks: $maxSpeedInSpoks"
		}
	}
	text("") {
		y = 60.0
		addFixedUpdater(100.milliseconds) {
			text = "Finished laps: $finishedLaps"
		}
	}
	text("") {
		y = 80.0
		addFixedUpdater(100.milliseconds) {
			text = "Fast finish time in space ticks: $fastestLapTimeInSpaceTicks"
		}
	}


	image(resourcesVfs["ship_sidesA.png"].readBitmap()) {

		goToStartPosition()

		onCollisionShape(filter = { it is SolidRect }) {
			if (it.name == "finishLane") {
				finishedLaps++
				fastestLapTimeInSpaceTicks = when (fastestLapTimeInSpaceTicks) {
					0 -> spaceship.spaceTicksSoFar
					else -> min(fastestLapTimeInSpaceTicks, spaceship.spaceTicksSoFar)
				}
			}
			spaceship = Spaceship()
			goToStartPosition()
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
			val xDelta = -sin(rotation) * spaceship.velocityInSpoksPerSpaceTicks
			val yDelta = cos(rotation) * spaceship.velocityInSpoksPerSpaceTicks
			x -= xDelta
			y -= yDelta
		}

	}
}

private fun Image.goToStartPosition() {
	rotation = 0.0.degrees
	anchor(.5, .5)
	scale(.8)
	position(180, 820)
}