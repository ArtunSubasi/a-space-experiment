package ui.views

import com.soywiz.klock.milliseconds
import com.soywiz.korge.view.Container
import com.soywiz.korge.view.addFixedUpdater
import com.soywiz.korge.view.text
import com.soywiz.korma.math.roundDecimalPlaces
import ui.model.GameState
import kotlin.math.max

class GameInfoView(private val gameState: GameState): Container() {

	init {
		text("") {
			addFixedUpdater(100.milliseconds) {
				text = "Space ticks: " + gameState.spaceship.spaceTicksSoFar
			}
		}
		text("") {
			y = 20.0
			addFixedUpdater(100.milliseconds) {
				text = "Velocity in spoks per space ticks: " + gameState.spaceship.velocityInSpoksPerSpaceTicks.roundDecimalPlaces(2)
			}
		}
		text("") {
			y = 40.0
			addFixedUpdater(100.milliseconds) {
				gameState.stats.maxSpeedInSpoks = max(gameState.stats.maxSpeedInSpoks,
					gameState.spaceship.maxSpeedInSpoksPerSpaceTicks).roundDecimalPlaces(2)
				text = "Max speed in spoks per space ticks: ${gameState.stats.maxSpeedInSpoks}"
			}
		}
		text("") {
			y = 60.0
			addFixedUpdater(100.milliseconds) {
				text = "Finished laps: ${gameState.stats.finishedLaps}"
			}
		}
		text("") {
			y = 80.0
			addFixedUpdater(100.milliseconds) {
				text = "Best finish time in space ticks: ${gameState.stats.fastestLapTimeInSpaceTicks}"
			}
		}
	}

}
