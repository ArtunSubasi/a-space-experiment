package ui.views

import com.soywiz.klock.milliseconds
import com.soywiz.korge.view.Container
import com.soywiz.korge.view.addFixedUpdater
import com.soywiz.korge.view.text
import com.soywiz.korma.math.roundDecimalPlaces
import ui.model.GameState
import kotlin.math.max

class GameInfoView(private val gameState: GameState): Container() {

	private val updateInterval = 100.milliseconds

	init {
		text("") {
			addFixedUpdater(updateInterval) {
				text = "Space ticks: " + gameState.spaceship.spaceTicksSoFar
			}
		}
		text("") {
			y = 20.0
			addFixedUpdater(updateInterval) {
				text = "Velocity in spoks per space ticks: " + gameState.spaceship.velocityInSpoksPerSpaceTicks.roundDecimalPlaces(2)
			}
		}
		text("") {
			y = 40.0
			addFixedUpdater(updateInterval) {
				gameState.stats.maxSpeedInSpoks = max(gameState.stats.maxSpeedInSpoks,
					gameState.spaceship.maxSpeedInSpoksPerSpaceTicks).roundDecimalPlaces(2)
				text = "Max speed in spoks per space ticks: ${gameState.stats.maxSpeedInSpoks}"
			}
		}
		text("") {
			y = 60.0
			addFixedUpdater(updateInterval) {
				text = "Finished laps: ${gameState.stats.finishedLaps}"
			}
		}
		text("") {
			y = 80.0
			addFixedUpdater(updateInterval) {
				text = "Best finish time in space ticks: ${gameState.stats.fastestLapTimeInSpaceTicks}"
			}
		}

		text("") {
			y = 100.0
			addFixedUpdater(updateInterval) {
				text = "Checkpoints reached: ${gameState.checkpointReached}"
			}
		}
	}

}
