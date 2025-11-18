package levelGenerators.list3.optimization;

import levelGenerators.list3.evaluation.Weights;
import levelGenerators.list3.structure.LevelStructure;

public class Subtask2 extends SimulatedAnnealing {
    @Override
    public LevelStructure getBestLevel() {
        return getBestLevel(
                Weights.SUBTASK2,
                agents.killer.Agent::new,
                5
        );
    }

    @Override
    public String getGeneratorName() {
        return "SimulatedAnnealing2";
    }
}