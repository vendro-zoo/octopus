import tentacle.Sucker
import tentacle.Tentacle

/**
 * Adds a new [Sucker] to the [Tentacle] registered with the given name.
 *
 * @param tentacleName The name of the [Tentacle] to add the [Sucker] to.
 * @param sucker The [Sucker] to add to the [Tentacle].
 * @throws IllegalArgumentException If no [Tentacle] with the given name is registered.
 */
suspend fun delayedAction(tentacleName: String, sucker: Sucker) {
    val tentacle = Octopus.getTentacle(tentacleName)
        ?: throw IllegalArgumentException("tentacle.Tentacle \"$tentacleName\" not found")
    tentacle.queueCall(sucker)
}