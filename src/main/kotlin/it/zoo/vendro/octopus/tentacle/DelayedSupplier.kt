package it.zoo.vendro.octopus.tentacle

import kotlinx.coroutines.sync.Mutex
import java.util.function.Supplier

/**
 * A [Supplier] with a [Mutex] used to wait for the async action to complete.
 */
data class DelayedSupplier<T> (
    val callMutex: Mutex,
    val supplier: Supplier<T>,
    var result: T? = null,
    var exception: Throwable? = null
) {
    fun execute() {
        try {
            result = supplier.get()
        } catch (e: Throwable) {
            result = null
            exception = e
        }
    }
}