package ui.scenes

import com.soywiz.korev.Key
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.view.Container
import com.soywiz.korge.view.addUpdater
import com.soywiz.korge.view.text
import ui.model.GameState
import ui.views.GameInfoView

class StartScene(private val gameState: GameState) : Scene() {
    override suspend fun Container.sceneInit() {
        addChild(GameInfoView(gameState))
        val textSuffix = if (gameState.spaceship.crashed) "try again" else "start"
        text("Press space to $textSuffix") {
            x = 495.0
            y = 390.0
            textSize = 30.0
        }
        addUpdater {
            if (views.input.keys.justPressed(Key.SPACE)) {
                sceneContainer.changeToAsync(MainScene::class)
            }
        }
    }
}