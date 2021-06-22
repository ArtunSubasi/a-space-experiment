package ui.scenes

import com.soywiz.korev.Key
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.view.Container
import com.soywiz.korge.view.addUpdater
import com.soywiz.korge.view.text
import ui.model.GameState

class GameOverScene(private val gameState: GameState) : Scene() {
    override suspend fun Container.sceneInit() {
        text("Press space to try again") {
            x = 550.0
            y = 400.0
        }
        addUpdater {
            if (views.input.keys.justPressed(Key.SPACE)) {
                sceneContainer.changeToAsync(MainScene::class)
            }
        }
    }
}