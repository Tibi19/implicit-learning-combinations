package constraint

import Data
import FacetsPool
import io.Logger

object Checker {

    private lateinit var pool: Data.Pool
    private lateinit var facetsPool: FacetsPool

    fun init(pool: Data.Pool, facetsPool: FacetsPool) {
        this.pool = pool
        this.facetsPool = facetsPool
    }

    fun checkExperiment(experiment: Data.Experiment): Boolean {
        return checkGroup(experiment.groupA) && checkGroup(experiment.groupB)
    }

    fun checkGroup(group: Data.Group): Boolean {
        return checkGroupLight(group) && checkGroupHeavy(group)
    }

    fun checkGroupLight(group: Data.Group): Boolean {
        return checkPhase(group.acquisition) &&
            checkPhase(group.testing) &&
            group.checkAssociationsRepeating()
    }

    fun checkGroupHeavy(group: Data.Group): Boolean {
        return group.checkWordSequencesRepeating()
    }

    fun checkGroupHeavyWithLogger(group: Data.Group, isParallelGroupPassed: Boolean = false): Boolean {
        val passed = checkGroupHeavy(group)
        if (isParallelGroupPassed) {
            Logger.countExperiment(passed)
        } else {
            Logger.countGroup(passed)
        }
        return passed
    }

    fun checkPhase(phase: Data.Phase): Boolean {
        return phase.checkCPropsRepeating(pool) &&
            phase.checkEoPropsRepeating(pool) &&
            phase.checkCPropsPolarization(pool) &&
            phase.checkFacetPairRepeating(facetsPool)
    }

}