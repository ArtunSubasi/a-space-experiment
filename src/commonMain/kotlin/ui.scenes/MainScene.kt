package ui.scenes

import com.soywiz.korau.sound.readSound
import com.soywiz.korev.Key
import com.soywiz.korge.input.Input
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.view.Container
import com.soywiz.korge.view.addUpdater
import com.soywiz.korge.view.getSpriteAnimation
import com.soywiz.korge.view.ktree.readKTree
import com.soywiz.korim.atlas.readAtlas
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.std.resourcesVfs
import domain.Spaceship
import ui.model.GameState
import ui.views.GameInfoView
import ui.views.SpaceshipView

class MainScene(private val gameState: GameState) : Scene() {

	override suspend fun Container.sceneInit() {
		gameState.spaceship = Spaceship()
		gameState.checkpointReached = 0

		val gameMapKTree = resourcesVfs["space.ktree"].readKTree(views)
		val spaceSprites = resourcesVfs["sprite_sheet.xml"].readAtlas()
		val thrusterSound = resourcesVfs["22455__nathanshadow__thruster-level-ii.wav"].readSound()
		val explosionSound = resourcesVfs["191694__deleted-user-3544904__explosion-4.wav"].readSound()
		val spaceshipBitmap = resourcesVfs["ship_sidesA.png"].readBitmap()

		val thrusterAnimation = spaceSprites.getSpriteAnimation(prefix = "engine")
		val input = views.input

		val thrusterSoundChannel = thrusterSound.playForever()
		thrusterSoundChannel.pause()

		addChild(gameMapKTree)
		addChild(GameInfoView(gameState))
		addChild(SpaceshipView(spaceshipBitmap, thrusterAnimation, gameState))

		addUpdater {
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

			if (gameState.lapJustFinished || gameState.spaceship.crashed) {
				if (gameState.spaceship.crashed) explosionSound.play(views.coroutineContext)
				gameState.lapJustFinished = false
				thrusterSoundChannel.pause()
				sceneContainer.changeToAsync(GameOverScene::class)

			} else {
				// TODO find a better place to handle sounds
				if (justPressedThrusterKey(input)) thrusterSoundChannel.resume()
				if (justReleasedThrusterKey(input)) thrusterSoundChannel.pause()

				gameState.spaceship.advanceOneSpaceTick(steeringWheelPosition, thrusterPosition)
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
