package ui.scenes

import com.soywiz.korev.Key
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.view.*
import com.soywiz.korge.view.ktree.readKTree
import com.soywiz.korio.file.std.resourcesVfs
import ui.model.GameState
import ui.views.GameInfoView

class StartScene(private val gameState: GameState) : Scene() {
    override suspend fun Container.sceneInit() {
        addChild(GameInfoView(gameState))
        val textSuffix = if (gameState.spaceship.crashed) "try again" else "start"
        text("Press space to $textSuffix") {
            y = 330.0
            textSize = 48.0
            centerXOnStage()
        }
        val keyBindingsView = resourcesVfs["key_bindings.ktree"].readKTree(views)
        keyBindingsView.position(340, 550)
        addChild(keyBindingsView)
        addUpdater {
            if (views.input.keys.justPressed(Key.SPACE)) {
                sceneContainer.changeToAsync(MainScene::class)
            }
        }
    }
}