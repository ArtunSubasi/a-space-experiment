package ui.model

import domain.Spaceship

data class GameState(
        var spaceship: Spaceship = Spaceship(),
        var stats: GameStats = GameStats(),
        var lapJustFinished: Boolean = false // TODO replace this flag after introducing an event bus
)

data class GameStats(
        var finishedLaps: Int = 0,
        var fastestLapTimeInSpaceTicks: Int = 0,
        var maxSpeedInSpoks: Double = 0.0
)
