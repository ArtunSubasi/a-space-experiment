import com.soywiz.korev.Key
import com.soywiz.korge.Korge
import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.std.resourcesVfs
import com.soywiz.korma.geom.*
import domain.Spaceship

suspend fun main() = Korge(width = 1024, height = 1024, bgcolor = Colors["#2b2b2b"]) {

	val input = views.input
	var spaceship = Spaceship()

	image(resourcesVfs["ship_sidesA.png"].readBitmap()) {

		goToStartPosition()

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
	position(512, 512)
}