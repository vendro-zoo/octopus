package it.zoo.vendro.octopus.tentacle

import kotlinx.coroutines.sync.Mutex
import java.util.function.Supplier

/**
 * A [Sucker] with a [Mutex] used to wait for the async action to complete.
 */
data class DelayedSupplier<T> (
    val callMutex: Mutex,
    val supplier: Supplier<T>,
    var result: T? = null
) {
    fun execute() {
        result = supplier.get()
    }
}