import com.soywiz.korev.Key
import com.soywiz.korge.Korge
import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.std.resourcesVfs
import com.soywiz.korma.geom.*

suspend fun main() = Korge(width = 512, height = 512, bgcolor = Colors["#2b2b2b"]) {

	val input = views.input
	val speed = 3

	image(resourcesVfs["ship_sidesA.png"].readBitmap()) {
		anchor(.5, .5)
		scale(.8)
		position(256, 256)

		addUpdater {
			if (input.keys.pressing(Key.UP)) {
				val rotationDelta = (3).degrees
				val xDelta = -sin(rotation) * speed
				val yDelta = cos(rotation) * speed
				when {
					input.keys.pressing(Key.RIGHT) -> rotation += rotationDelta
					input.keys.pressing(Key.LEFT) -> rotation -= rotationDelta
				}
				x -= xDelta
				y -= yDelta
			}
		}
	}

}