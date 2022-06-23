import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import tentacle.Tentacle

object Octopus {
    private val tentacles = mutableMapOf<String, Tentacle>()

    fun addTentacle(name: String) = apply {
        if (!tentacles.containsKey(name)) {
            tentacles[name] = Tentacle()
        }
    }

    fun getTentacle(name: String) = tentacles[name]

    @OptIn(DelicateCoroutinesApi::class)
    suspend fun awake(scope: CoroutineScope) {
        tentacles.forEach {
            scope.launch(newSingleThreadContext("tentacle.Tentacle#${it.key}")) {
                it.value.run()
            }
        }
    }
}