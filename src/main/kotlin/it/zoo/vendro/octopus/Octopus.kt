package it.zoo.vendro.octopus

import it.zoo.vendro.octopus.tentacle.Tentacle
import kotlinx.coroutines.*

object Octopus {
    private val tentacles = mutableMapOf<String, Tentacle>()
    private var runningTentacles = listOf<Job>()

    fun addTentacle(name: String) = apply {
        if (!tentacles.containsKey(name)) {
            tentacles[name] = Tentacle()
        }
    }

    fun getTentacle(name: String) = tentacles[name]

    @OptIn(DelicateCoroutinesApi::class)
    suspend fun awake(scope: CoroutineScope) {
        runningTentacles = tentacles.map {
            scope.launch(newSingleThreadContext("it.zoo.vendro.Tentacle#${it.key}")) {
                it.value.run()
            }
        }
    }

    suspend fun kill() {
        runningTentacles.forEach {
            it.cancelAndJoin()
        }
    }
}