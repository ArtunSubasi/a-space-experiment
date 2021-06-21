import com.soywiz.klock.milliseconds
import com.soywiz.korau.sound.PlaybackTimes
import com.soywiz.korau.sound.readSound
import com.soywiz.korev.Key
import com.soywiz.korge.Korge
import com.soywiz.korge.input.Input
import com.soywiz.korge.view.*
import com.soywiz.korge.view.ktree.readKTree
import com.soywiz.korim.atlas.readAtlas
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

	val spaceSprites = resourcesVfs["sprite_sheet.xml"].readAtlas()
	val animation = spaceSprites.getSpriteAnimation(prefix = "engine")

	val thrusterSound = resourcesVfs["22455__nathanshadow__thruster-level-ii.wav"].readSound()
	val explosionSound = resourcesVfs["191694__deleted-user-3544904__explosion-4.wav"].readSound()

	val input = views.input
	var spaceship = Spaceship()
	var finishedLaps = 0
	var fastestLapTimeInSpaceTicks = 0
	var maxSpeedInSpoks = 0.0

	val thrusterSoundChannel = thrusterSound.playForever()
	thrusterSoundChannel.pause()

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

	val spaceshipView = image(resourcesVfs["ship_sidesA.png"].readBitmap()) {

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
			explosionSound.play(views.coroutineContext)
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
			if (justPressedThrusterKey(input)) thrusterSoundChannel.resume()
			if (justReleasedThrusterKey(input)) thrusterSoundChannel.pause()

			spaceship.advanceOneSpaceTick(steeringWheelPosition, thrusterPosition)

			rotation += spaceship.rotationInDegreePerSpaceTicks.degrees
			val xDelta = -sin(rotation) * spaceship.velocityInSpoksPerSpaceTicks
			val yDelta = cos(rotation) * spaceship.velocityInSpoksPerSpaceTicks
			x -= xDelta
			y -= yDelta
		}

	}

	// Thruster sprite
	sprite(animation) {
		scale(0.3)
		playAnimationLooped(spriteDisplayTime = 150.0.milliseconds)
		addUpdater {
			if (spaceship.thrusterPosition > 0) {
				setPositionRelativeTo(spaceshipView, Point(-8, 25))
				rotation = spaceshipView.rotation
				visible = true
			} else if (spaceship.thrusterPosition < 0) {
				setPositionRelativeTo(spaceshipView, Point(6, -30))
				rotation = spaceshipView.rotation + Angle.fromDegrees(180)
				visible = true
			} else {
				visible = false
			}
		}
	}

	// Rotation sprite
	sprite(animation) {
		scale(0.3)
		playAnimationLooped(spriteDisplayTime = 150.0.milliseconds)
		addUpdater {
			if (spaceship.steeringWheelPosition > 0) {
				setPositionRelativeTo(spaceshipView, Point(-30, 10))
				rotation = spaceshipView.rotation + Angle.fromDegrees(80)
				visible = true
			} else if (spaceship.steeringWheelPosition < 0) {
				setPositionRelativeTo(spaceshipView, Point(25, 25))
				rotation = spaceshipView.rotation - Angle.fromDegrees(80)
				visible = true
			} else {
				visible = false
			}
		}
	}
}

private fun justPressedThrusterKey(input: Input) = input.keys.justPressed(Key.UP)
		|| input.keys.justPressed(Key.DOWN)
		|| input.keys.justPressed(Key.LEFT)
		|| input.keys.justPressed(Key.RIGHT)

private fun justReleasedThrusterKey(input: Input) = input.keys.justReleased(Key.UP)
		|| input.keys.justReleased(Key.DOWN)
		|| input.keys.justReleased(Key.LEFT)
		|| input.keys.justReleased(Key.RIGHT)


private fun Image.goToStartPosition() {
	rotation = 0.0.degrees
	anchor(.5, .5)
	scale(.8)
	position(180, 820)
}