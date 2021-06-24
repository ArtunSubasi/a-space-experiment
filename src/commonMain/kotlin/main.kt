import com.soywiz.korge.Korge
import com.soywiz.korge.scene.Module
import com.soywiz.korge.scene.Scene
import com.soywiz.korinject.AsyncInjector
import com.soywiz.korma.geom.SizeInt
import ui.model.GameState
import ui.scenes.StartScene
import ui.scenes.MainScene
import kotlin.reflect.KClass

suspend fun main() = Korge(Korge.Config(module = SpaceModule))

object SpaceModule : Module() {
	override val mainScene: KClass<out Scene> = StartScene::class
	override val size: SizeInt = SizeInt(1280, 850)

	override suspend fun AsyncInjector.configure() {
		mapInstance(GameState())
		mapPrototype { StartScene(get()) }
		mapPrototype { MainScene(get()) }
	}
}
