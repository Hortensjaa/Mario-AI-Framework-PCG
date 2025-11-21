package levelGenerators.list3.evaluation;

import levelGenerators.list3.structure.LevelStructure;
import java.util.function.Function;

public enum HeuristicComponent {
    TERRAIN_DIVERSITY(Evaluation::overallTerrainDiversity),
    DECOR_DIVERSITY(Evaluation::overallDecorDiversity),
    ENEMIES_COUNT(Evaluation::countEnemiesScore),
    BLOCKS_COUNT(Evaluation::countBlocksScore),
    COINS_COUNT(Evaluation::countCoinsScore),
    ENEMY_GROUPS(Evaluation::enemyGroupsScore),
    ENEMY_FIRST(Evaluation::enemyFirstPenalty),
    NOT_PASSABLE_GAPS(Evaluation::notPassableGapsPenalty),
    NOT_PASSABLE_JUMPS(Evaluation::notPassableJumpsPenalty),
    LOCAL_TERRAIN_DIVERSITY(Evaluation::localTerrainDiversity),
    LOCAL_DECOR_DIVERSITY(Evaluation::localDecorDiversity),
    GAPS_COUNT(Evaluation::countGapsScore),
    PLAINS_COUNT(Evaluation::countPlainsScore),
    PIPES_COUNT(Evaluation::countPipesScore),
    BILLS_COUNT(Evaluation::countBillsScore),
    HILLS_COUNT(Evaluation::countHillsScore),
    GAP_FIRST(Evaluation::gapFirstPenalty),
    WIDER_TERRAINS_BONUS(Evaluation::widerTerrainsBonus),
    ENEMY_GAP_DISTANCE_BONUS(Evaluation::enemyDistanceFromGapBonus);

    private final Function<LevelStructure, Float> evaluator;

    HeuristicComponent(Function<LevelStructure, Float> evaluator) {
        this.evaluator = evaluator;
    }

    public float evaluate(LevelStructure level) {
        return evaluator.apply(level);
    }
}
