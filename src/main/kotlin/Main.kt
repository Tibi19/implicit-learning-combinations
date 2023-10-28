import constraint.*
import io.*
import kotlinx.coroutines.*
import producer.*

const val ROOT_PATH = "src/main/resources"
const val POOL_PATH = "${ROOT_PATH}/pool.csv"
const val POOL2_PATH = "${ROOT_PATH}/pool2.csv"
const val PHASES_TO_PRODUCE = 2200

fun main(args: Array<String>) {
    runRandomCombinations()
}

fun runRandomCombinations() {
    val pool = readPool2(POOL2_PATH)
    val facetsPool = produceFacets(pool)
    Checker.init(pool, facetsPool)

    val phases = List(PHASES_TO_PRODUCE) {
        produceRandomPhase(pool)
    }
    val groups = produceGroupsFromPhasesChunk(phases)

    runBlocking {
        groups.forEachIndexed { i, group ->
            println("Checking group ${i + 1} of ${groups.size}")
            launch(Dispatchers.IO) {
                resolveGroup(group)
            }
        }
    }

    writeLog()
    println("DONE")
}

fun resolveGroup(group: Data.Group) {
    val passedGroupA = Checker.checkGroupHeavyWithLogger(group)
    if (!passedGroupA) return
    writeGroup(group)

    val groupB = produceParallelGroup(group)
    val passedGroupB = Checker.checkGroupHeavyWithLogger(groupB, true)
    if (!passedGroupB) return

    val experiment = Data.Experiment(group, groupB)
    writeExperiment(experiment)
}
