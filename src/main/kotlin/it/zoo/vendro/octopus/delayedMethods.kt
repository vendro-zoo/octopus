package it.zoo.vendro.octopus

import it.zoo.vendro.octopus.tentacle.Tentacle
import java.util.function.Supplier

/**
 * Adds a new function to the thread-queue registered with the given name.
 *
 * @param tentacleName The name of the [Tentacle] (thread-queue) to add the [Supplier] (function) to.
 * @param supplier The function to add to the queue.
 * @throws IllegalArgumentException If no [Tentacle] with the given name is registered.
 */
suspend fun <T> delayedAction(tentacleName: String, supplier: Supplier<T>): T? {
    val tentacle = Octopus.getTentacle(tentacleName)
        ?: throw IllegalArgumentException("it.zoo.vendro.Tentacle \"$tentacleName\" not found")
    return tentacle.queueCall(supplier)
}