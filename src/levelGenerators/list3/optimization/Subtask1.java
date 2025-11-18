package levelGenerators.list3.optimization;

import levelGenerators.list3.evaluation.Weights;
import levelGenerators.list3.structure.LevelStructure;

public class Subtask1 extends SimulatedAnnealing {
    @Override
    public LevelStructure getBestLevel() {
        return getBestLevel(
                Weights.SUBTASK1,
                agents.robinBaumgarten.Agent::new,
                50
        );
    }

    @Override
    public String getGeneratorName() {
        return "SimulatedAnnealing1";
    }
}
