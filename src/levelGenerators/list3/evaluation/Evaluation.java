package levelGenerators.list3.evaluation;

import engine.core.MarioAgent;
import engine.core.MarioGame;
import engine.core.MarioLevelModel;
import engine.core.MarioResult;
import levelGenerators.list3.LevelRenderer;
import levelGenerators.list3.structure.*;

import java.util.List;
import java.util.function.Supplier;


public final class Evaluation {
    private final static int LEVEL_WIDTH = 150;
    private final static int TIME_LIMIT = 10;
    private final static int NUMBER_OF_TERRAINS = 5;
    private final static int NUMBER_OF_DECORATORS = 4;

    // ---------------- helpers ----------------
    private static <T> float countSelectedScore(
            Class<? extends T> selectedClass,
            Supplier<? extends List<T>> supplier,
            int numberOfTypes
    ) {
        List<T> list = supplier.get();
        int c = 0;
        for (var item : list) {
            if (selectedClass.isInstance(item)) {
                c += 1;
            }
        }
        return (float) c / list.size() * numberOfTypes;
    }

    private static float genericDiversity(List<? extends Mutable> list, List<Class<? extends Mutable>> types) {
        int numTypes = types.size();
        int[] counts = new int[numTypes];

        // Count occurrences of each type
        for (Mutable s : list) {
            for (int i = 0; i < numTypes; i++) {
                if (types.get(i).isInstance(s)) {
                    counts[i]++;
                    break;
                }
            }
        }

        int total = list.size();
        if (total == 0) return 0.0f;

        double entropy = 0.0;
        for (int c : counts) {
            if (c == 0) continue;
            double p = (double) c / total;
            entropy -= p * Math.log(p);
        }

        // Normalize by log(numTypes)
        entropy /= Math.log(numTypes);
        return (float) entropy; // between 0 and 1
    }

    // Counting methods using generic countSelectedScore
    private static float countEnemiesScore(LevelStructure level) {
        return countSelectedScore(Enemy.class, level::getDecorators, NUMBER_OF_DECORATORS);
    }

    private static float countBlocksScore(LevelStructure level) {
        return countSelectedScore(Bumpable.class, level::getDecorators, NUMBER_OF_DECORATORS);
    }

    private static float countCoinsScore(LevelStructure level) {
        return countSelectedScore(Coins.class, level::getDecorators, NUMBER_OF_DECORATORS);
    }

    private static float countGapsScore(LevelStructure level) {
        return countSelectedScore(Gap.class, level::getTerrains, NUMBER_OF_TERRAINS);
    }

    private static float countHillsScore(LevelStructure level) {
        return countSelectedScore(Hill.class, level::getTerrains, NUMBER_OF_TERRAINS);
    }

    private static float countPlainsScore(LevelStructure level) {
        return countSelectedScore(Plain.class, level::getTerrains, NUMBER_OF_TERRAINS);
    }

    private static float countBillsScore(LevelStructure level) {
        return countSelectedScore(BulletBill.class, level::getTerrains, NUMBER_OF_TERRAINS);
    }

    private static float countPipesScore(LevelStructure level) {
        return countSelectedScore(Pipe.class, level::getTerrains, NUMBER_OF_TERRAINS);
    }

    /** Count enemy groups (3 or more enemies in a row) and divide its number by the number of all decorators */
    private static float enemyGroupsScore(LevelStructure level) {
        int curDensity = 0;
        int numberOfGroups = 0;
        for (var decorator : level.getDecorators()) {
            if (decorator instanceof Enemy) {
                curDensity += 1;
            } else {
                if (curDensity > 2) {
                    numberOfGroups += 1;
                }
                curDensity = 0;
            }
        }
        return (float) numberOfGroups / level.getDecorators().size();
    }

    /** Count gaps that are not passable (longer than 4) */
    private static float notPassableGapsPenalty(LevelStructure level) {
        int notPassableGapCount = 0;
        int curGapLength = 0;
        for (var terrain : level.getTerrains()) {
            if (terrain instanceof Gap) {
                curGapLength += terrain.getWidth();
                if (curGapLength > 4) {
                    notPassableGapCount += 1;
                }
            } else {
                curGapLength = 0;
            }
        }
        return (notPassableGapCount + (curGapLength > 4 ? 1 : 0)) / (float) level.getTerrains().size();
    }

    /** Count jumps that are not passable (higher than 4) */
    private static float notPassableJumpsPenalty(LevelStructure level) {
        int notPassableJumpCount = 0;
        for (int i = 0; i < level.getTerrains().size() - 1; i++) {
            var terrain = level.getTerrains().get(i);
            var nextTerrain = level.getTerrains().get(i + 1);
            if (!(terrain instanceof Gap)) {
                // if next is not gap, just check height diff
                if (!(nextTerrain instanceof Gap)) {
                    int heightDiff = nextTerrain.getHeight() - terrain.getHeight();
                    if (heightDiff > 4) {
                        notPassableJumpCount += 1;
                    }
                // if next is gap, check the terrain after gap and gap width
                } else if (i + 2 < level.getTerrains().size() && !(level.getTerrains().get(i + 2) instanceof Gap)) {
                    var nextNextTerrain = level.getTerrains().get(i + 2);
                    int heightDiff = nextNextTerrain.getHeight() - terrain.getHeight();
                    int gapWidth = nextTerrain.getWidth();
                    if (heightDiff  + gapWidth > 5) {
                        notPassableJumpCount += 1;
                    }
                }
            }
        }
        return (float) notPassableJumpCount / (float) level.getTerrains().size();
    }

    /** Is gap a first terrain structure? */
    private static float gapFirstPenalty(LevelStructure level) {
        if (!level.getTerrains().isEmpty() && level.getTerrains().getFirst() instanceof Gap) {
            return 1;
        }
        return 0;
    }

    /** Are we starting directly above enemy?  */
    private static float enemyFirstPenalty(LevelStructure level) {
        if (!level.getDecorators().isEmpty() && level.getDecorators().getFirst() instanceof Enemy) {
            return 1;
        }
        return 0;
    }

    /** Check for monotonous terrain fragments (same type or height) */
    private static float localTerrainDiversity(LevelStructure level) {
        int s = 0;
        List<Terrain> terrains = level.getTerrains();
        int size = terrains.size();
        for (int i = 1; i < size; i++) {
            if (
                    terrains.get(i).getClass() != terrains.get(i - 1).getClass()
                    && terrains.get(i).getHeight() != terrains.get(i - 1).getHeight()
            ) {
                s += 1;
            }
        }
        return (float) s / terrains.size();
    }

    /** Penalty for monotonous decor fragments (same type) */
    private static float localDecorDiversity(LevelStructure level) {
        int s = 0;
        List<Decorator> decorators = level.getDecorators();
        int size = decorators.size();
        for (int i = 1; i < size; i++) {
            if (decorators.get(i).getClass() != decorators.get(i - 1).getClass()) {
                s += 1;
            }
        }
        return (float) s / decorators.size();
    }

    /** Number of different terrain structures overall and its proportion */
    private static float overallTerrainDiversity(LevelStructure level) {
        List<Terrain> terrains = level.getTerrains();
        List<Class<? extends Mutable>> types = List.of(Hill.class, BulletBill.class, Gap.class, Pipe.class, Plain.class);
        return genericDiversity(terrains, types);
    }

    /** Number of different decorators overall and its proportion */
    private static float overallDecorDiversity(LevelStructure level) {
        List<Decorator> decors = level.getDecorators();
        List<Class<? extends Mutable>> types = List.of(Bumpable.class, Coins.class, Enemy.class, EmptyDecor.class);
        return genericDiversity(decors, types);
    }

    // ---------------- task heuristics ----------------
    public static float task1heuristic(LevelStructure level) {

        return
            + Weights.TASK1_HEURISTIC_WEIGHTS.get(HeuristicComponent.TERRAIN_DIVERSITY)
            * overallTerrainDiversity(level)

            + Weights.TASK1_HEURISTIC_WEIGHTS.get(HeuristicComponent.DECOR_DIVERSITY)
            * overallDecorDiversity(level)

            + Weights.TASK1_HEURISTIC_WEIGHTS.get(HeuristicComponent.LOCAL_TERRAIN_DIVERSITY)
            * localTerrainDiversity(level)

            + Weights.TASK1_HEURISTIC_WEIGHTS.get(HeuristicComponent.LOCAL_DECOR_DIVERSITY)
            * localDecorDiversity(level)

            + Weights.TASK1_HEURISTIC_WEIGHTS.get(HeuristicComponent.ENEMIES_COUNT)
            * countEnemiesScore(level)

            + Weights.TASK1_HEURISTIC_WEIGHTS.get(HeuristicComponent.BLOCKS_COUNT)
            * countBlocksScore(level)

            + Weights.TASK1_HEURISTIC_WEIGHTS.get(HeuristicComponent.COINS_COUNT)
            * countCoinsScore(level)

            + Weights.TASK1_HEURISTIC_WEIGHTS.get(HeuristicComponent.GAPS_COUNT)
            * countGapsScore(level)

            + Weights.TASK1_HEURISTIC_WEIGHTS.get(HeuristicComponent.HILLS_COUNT)
            * countHillsScore(level)

            + Weights.TASK1_HEURISTIC_WEIGHTS.get(HeuristicComponent.PLAINS_COUNT)
            * countPlainsScore(level)

            + Weights.TASK1_HEURISTIC_WEIGHTS.get(HeuristicComponent.PIPES_COUNT)
            * countPipesScore(level)

            + Weights.TASK1_HEURISTIC_WEIGHTS.get(HeuristicComponent.BILLS_COUNT)
            * countBillsScore(level)

            + Weights.TASK1_HEURISTIC_WEIGHTS.get(HeuristicComponent.ENEMY_GROUPS)
            * enemyGroupsScore(level)

            + Weights.TASK1_HEURISTIC_WEIGHTS.get(HeuristicComponent.ENEMY_FIRST)
            * enemyFirstPenalty(level)

            + Weights.TASK1_HEURISTIC_WEIGHTS.get(HeuristicComponent.NOT_PASSABLE_GAPS)
            * notPassableGapsPenalty(level)

            + Weights.TASK1_HEURISTIC_WEIGHTS.get(HeuristicComponent.NOT_PASSABLE_JUMPS)
            * notPassableJumpsPenalty(level)

            + Weights.TASK1_HEURISTIC_WEIGHTS.get(HeuristicComponent.GAP_FIRST)
            * gapFirstPenalty(level);
    }


    // ---------------- task simulations ----------------
    public static float task1simulation(LevelStructure level) {
        MarioLevelModel model = new MarioLevelModel(LEVEL_WIDTH, 16);
        String renderedLevel = new LevelRenderer().getRenderedLevel(
                model, level.getTerrains(), level.getDecorators()
        );
        MarioAgent marioagent = new agents.robinBaumgarten.Agent();
        MarioGame game = new MarioGame();
        MarioResult result = game.runGame(marioagent, renderedLevel, TIME_LIMIT, 0, false);

        int notPassedPenalty = result.getCompletionPercentage() == 1.0f ? 0 : 1;

        return
            + Weights.TASK1_SIMULATION_WEIGHTS.get(SimulationComponent.COMPLETION)
            * result.getCompletionPercentage()

            + Weights.TASK1_SIMULATION_WEIGHTS.get(SimulationComponent.KILLS)
            * (result.getKillsTotal() / 5.0f)

            + Weights.TASK1_SIMULATION_WEIGHTS.get(SimulationComponent.JUMPS)
            * (result.getNumJumps() / 15.0f)

            + Weights.TASK1_SIMULATION_WEIGHTS.get(SimulationComponent.COINS)
            * (result.getNumCollectedTileCoins() / 30.0f)

            + Weights.TASK1_SIMULATION_WEIGHTS.get(SimulationComponent.NOT_PASSED)
            * notPassedPenalty;
    }
}
