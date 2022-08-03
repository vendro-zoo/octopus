package it.zoo.vendro.octopus.tentacle

import kotlinx.coroutines.sync.Mutex
import it.zoo.vendro.octopus.tryUnlock
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.function.Supplier

/**
 * A thread-safe queue that can process different type of functions.
 */
class Tentacle {
    private var started = false
    private val executionMutex: Mutex = Mutex(true)
    private val callMutexes = ConcurrentLinkedQueue<DelayedSupplier<*>>()

    /**
     * Starts the queue and processes the functions.
     *
     * The process pauses if there are no functions in the queue.
     * When a function is added to the queue, the process will resume until the queue is empty.
     *
     * @throws IllegalStateException if the [Tentacle] is already started.
     */
    suspend fun run() {
        if (started) throw IllegalStateException("Queue already started")
        started = true

        while (true) {
            executionMutex.lock()
            callMutexes.poll()?.let { dCall ->
                dCall.execute()
                dCall.callMutex.unlock()
                executionMutex.tryUnlock()
            }
        }
    }

    /**
     * Puts the function into the queue and waits for it to be executed.
     *
     * @param supplier The function to be executed.
     */
    suspend fun <T> queueCall(supplier: Supplier<T>): T? {
        val mutex = Mutex(true)

        val ds = DelayedSupplier(mutex, supplier)
        callMutexes.add(ds)

        executionMutex.tryUnlock()  // Starts the queue if it's not started yet.
        mutex.lock()  // Waits for the delayed call to be executed.

        return ds.result
    }
}