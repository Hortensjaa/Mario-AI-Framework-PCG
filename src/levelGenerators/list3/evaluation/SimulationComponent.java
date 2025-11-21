package levelGenerators.list3.evaluation;

import engine.core.MarioResult;
import java.util.function.Function;

public enum SimulationComponent {
    COMPLETION(MarioResult::getCompletionPercentage),
    KILLS(result -> (float) result.getKillsTotal()),
    JUMPS(result -> (float) result.getNumJumps()),
    COINS(result -> (float) result.getNumCollectedTileCoins()),
    NOT_PASSED(result -> result.getCompletionPercentage() < 1.0f ? 1.0f : 0.0f);

    private final Function<MarioResult, Float> evaluator;

    SimulationComponent(Function<MarioResult, Float> evaluator) {
        this.evaluator = evaluator;
    }

    public float evaluate(MarioResult result) {
        return evaluator.apply(result);
    }
}
