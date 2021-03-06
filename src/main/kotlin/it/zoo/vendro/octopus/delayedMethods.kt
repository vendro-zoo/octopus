package it.zoo.vendro.octopus

import it.zoo.vendro.octopus.tentacle.Sucker
import it.zoo.vendro.octopus.tentacle.Tentacle

/**
 * Adds a new [Sucker] to the [Tentacle] registered with the given name.
 *
 * @param tentacleName The name of the [Tentacle] to add the [Sucker] to.
 * @param sucker The [Sucker] to add to the [Tentacle].
 * @throws IllegalArgumentException If no [Tentacle] with the given name is registered.
 */
suspend fun delayedAction(tentacleName: String, sucker: Sucker) {
    val tentacle = Octopus.getTentacle(tentacleName)
        ?: throw IllegalArgumentException("it.zoo.vendro.Tentacle \"$tentacleName\" not found")
    tentacle.queueCall(sucker)
}