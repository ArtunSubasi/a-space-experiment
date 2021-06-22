package ui.scenes

import com.soywiz.klock.milliseconds
import com.soywiz.korau.sound.readSound
import com.soywiz.korev.Key
import com.soywiz.korge.input.Input
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.view.*
import com.soywiz.korge.view.ktree.readKTree
import com.soywiz.korim.atlas.readAtlas
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.std.resourcesVfs
import com.soywiz.korma.geom.*
import domain.Spaceship
import ui.model.GameState
import ui.views.GameInfoView
import kotlin.math.min

class MainScene(private val gameState: GameState) : Scene() {

	override suspend fun Container.sceneInit() {
		gameState.spaceship = Spaceship()

		val ktree = resourcesVfs["space.ktree"].readKTree(views)
		addChild(ktree)

		val spaceSprites = resourcesVfs["sprite_sheet.xml"].readAtlas()
		val animation = spaceSprites.getSpriteAnimation(prefix = "engine")

		val thrusterSound = resourcesVfs["22455__nathanshadow__thruster-level-ii.wav"].readSound()
		val explosionSound = resourcesVfs["191694__deleted-user-3544904__explosion-4.wav"].readSound()

		val input = views.input

		val thrusterSoundChannel = thrusterSound.playForever()
		thrusterSoundChannel.pause()

		addChild(GameInfoView(gameState))

		val spaceshipView = image(resourcesVfs["ship_sidesA.png"].readBitmap()) {

			goToStartPosition()

			onCollisionShape(filter = { it is SolidRect }) {
				if (it.name == "finishLane") {
					gameState.stats.finishedLaps++
					gameState.stats.fastestLapTimeInSpaceTicks = when (gameState.stats.fastestLapTimeInSpaceTicks) {
						0 -> gameState.spaceship.spaceTicksSoFar
						else -> min(gameState.stats.fastestLapTimeInSpaceTicks, gameState.spaceship.spaceTicksSoFar)
					}
				} else {
					explosionSound.play(views.coroutineContext)
				}
				thrusterSoundChannel.pause()
				sceneContainer.changeToAsync(GameOverScene::class)
			}

			addUpdater {
				if (input.keys.justPressed(Key.SPACE)) {
					gameState.spaceship = Spaceship()
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

				gameState.spaceship.advanceOneSpaceTick(steeringWheelPosition, thrusterPosition)

				rotation += gameState.spaceship.rotationInDegreePerSpaceTicks.degrees
				val xDelta = -sin(rotation) * gameState.spaceship.velocityInSpoksPerSpaceTicks
				val yDelta = cos(rotation) * gameState.spaceship.velocityInSpoksPerSpaceTicks
				x -= xDelta
				y -= yDelta
			}

		}

		// Thruster sprite
		sprite(animation) {
			scale(0.3)
			playAnimationLooped(spriteDisplayTime = 150.0.milliseconds)
			addUpdater {
				if (gameState.spaceship.thrusterPosition > 0) {
					setPositionRelativeTo(spaceshipView, Point(-8, 25))
					rotation = spaceshipView.rotation
					visible = true
				} else if (gameState.spaceship.thrusterPosition < 0) {
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
				if (gameState.spaceship.steeringWheelPosition > 0) {
					setPositionRelativeTo(spaceshipView, Point(-30, 10))
					rotation = spaceshipView.rotation + Angle.fromDegrees(80)
					visible = true
				} else if (gameState.spaceship.steeringWheelPosition < 0) {
					setPositionRelativeTo(spaceshipView, Point(25, 25))
					rotation = spaceshipView.rotation - Angle.fromDegrees(80)
					visible = true
				} else {
					visible = false
				}
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
	position(130, 770)
}