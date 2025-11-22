package levelGenerators.list3.evaluation;

import engine.core.MarioAgent;
import engine.core.MarioGame;
import engine.core.MarioLevelModel;
import engine.core.MarioResult;
import levelGenerators.list3.LevelRenderer;
import levelGenerators.list3.structure.*;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.IntStream;


public final class Evaluation {
    private final static int LEVEL_WIDTH = 150;
    private final static int TIME_LIMIT = 30;
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
    public static float countEnemiesScore(LevelStructure level) {
        return countSelectedScore(Enemy.class, level::getDecorators, NUMBER_OF_DECORATORS);
    }

    public static float countBlocksScore(LevelStructure level) {
        return countSelectedScore(Bumpable.class, level::getDecorators, NUMBER_OF_DECORATORS);
    }

    public static float countCoinsScore(LevelStructure level) {
        return countSelectedScore(Coins.class, level::getDecorators, NUMBER_OF_DECORATORS);
    }

    public static float countGapsScore(LevelStructure level) {
        return countSelectedScore(Gap.class, level::getTerrains, NUMBER_OF_TERRAINS);
    }

    public static float countHillsScore(LevelStructure level) {
        return countSelectedScore(Hill.class, level::getTerrains, NUMBER_OF_TERRAINS);
    }

    public static float countPlainsScore(LevelStructure level) {
        return countSelectedScore(Plain.class, level::getTerrains, NUMBER_OF_TERRAINS);
    }

    public static float countBillsScore(LevelStructure level) {
        return countSelectedScore(BulletBill.class, level::getTerrains, NUMBER_OF_TERRAINS);
    }

    public static float countPipesScore(LevelStructure level) {
        return countSelectedScore(Pipe.class, level::getTerrains, NUMBER_OF_TERRAINS);
    }

    public static float widerTerrainsBonus(LevelStructure level) {
        int totalWidthSquare = 0;
        for (var terrain : level.getTerrains()) {
            totalWidthSquare += terrain.getWidth() * terrain.getWidth();
        }
        return (float) Math.sqrt((double) totalWidthSquare / (LEVEL_WIDTH * LEVEL_WIDTH)) * 10.0f;
    }

    public static float enemyDistanceFromGapBonus(LevelStructure level) {
        List<Terrain> terrains = level.getTerrains();
        List<Decorator> decorators = level.getDecorators();
        Terrain currentTerrain = terrains.getFirst();
        Decorator currentDecorator = decorators.getFirst();
        int terrainStepsLeft = currentTerrain.getWidth();
        int decoratorStepsLeft = currentDecorator.getWidth();
        int currentDecoratorIndex = 0;
        int currentTerrainIndex = 0;
        int distanceFromGap = 0;
        int bonusSum = 0;
        for (int x = 0; x < LEVEL_WIDTH; x++) {
            if (currentTerrain instanceof Gap) {
                if (currentDecorator instanceof Enemy) {
                    if (distanceFromGap != Integer.MAX_VALUE) {
                        bonusSum += distanceFromGap;
                    }
                    distanceFromGap = 0;
                }
            } else {
                distanceFromGap += 1;
            }
//           move forward on terrain
            terrainStepsLeft -= 1;
            if (terrainStepsLeft == 0) {
                currentTerrainIndex += 1;
                if (currentTerrainIndex >= terrains.size()) {
                    break;
                }
                currentTerrain = terrains.get(currentTerrainIndex);
                terrainStepsLeft = currentTerrain.getWidth();
            }
//            move forward on decorator
            decoratorStepsLeft -= 1;
            if (decoratorStepsLeft == 0) {
                currentDecoratorIndex += 1;
                if (currentDecoratorIndex >= decorators.size()) {
                    break;
                }
                currentDecorator = decorators.get(currentDecoratorIndex);
                terrainStepsLeft = currentTerrain.getWidth();
            }

        }
        return (float) bonusSum / LEVEL_WIDTH;
    }

    /** Count enemy groups (3 or more enemies in a row) and divide its number by the number of all decorators */
    public static float enemyGroupsScore(LevelStructure level) {
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
    public static float notPassableGapsPenalty(LevelStructure level) {
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
    public static float notPassableJumpsPenalty(LevelStructure level) {
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
    public static float gapFirstPenalty(LevelStructure level) {
        if (!level.getTerrains().isEmpty() && level.getTerrains().getFirst() instanceof Gap) {
            return 1;
        }
        return 0;
    }

    /** Are we starting directly above enemy?  */
    public static float enemyFirstPenalty(LevelStructure level) {
        if (!level.getDecorators().isEmpty() && level.getDecorators().getFirst() instanceof Enemy) {
            return 1;
        }
        return 0;
    }

    /** Check for monotonous terrain fragments (same type or height) */
    public static float localTerrainDiversity(LevelStructure level) {
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

    /** Check for monotonous decor fragments (same type) */
    public static float localDecorDiversity(LevelStructure level) {
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
    public static float overallTerrainDiversity(LevelStructure level) {
        List<Terrain> terrains = level.getTerrains();
        List<Class<? extends Mutable>> types = List.of(Hill.class, BulletBill.class, Gap.class, Pipe.class, Plain.class);
        return genericDiversity(terrains, types);
    }

    /** Number of different decorators overall and its proportion */
    public static float overallDecorDiversity(LevelStructure level) {
        List<Decorator> decors = level.getDecorators();
        List<Class<? extends Mutable>> types = List.of(Bumpable.class, Coins.class, Enemy.class, EmptyDecor.class);
        return genericDiversity(decors, types);
    }

    // ---------------- task heuristics ----------------
    public static float heuristic(LevelStructure level, Weights weights) {
        var w = weights.getHeuristicWeights();
        float score = 0.0f;

        for (HeuristicComponent component : HeuristicComponent.values()) {
            score += w.getOrDefault(component, 0.0f) * component.evaluate(level);
        }

        return score;
    }


    // ---------------- task simulations ----------------
    public static float simulation(LevelStructure level, MarioAgent marioagent, Weights weights) {
        MarioLevelModel model = new MarioLevelModel(LEVEL_WIDTH, 16);
        String renderedLevel = new LevelRenderer().getRenderedLevel(
                model, level.getTerrains(), level.getDecorators()
        );
        MarioGame game = new MarioGame();
        MarioResult result = game.runGame(
                marioagent,
                renderedLevel,
                TIME_LIMIT,
                0,
                false
        );

        float score = 0.0f;

        var w = weights.getSimulationWeights();
        for (SimulationComponent component : SimulationComponent.values()) {
            score += w.getOrDefault(component, 0.0f) * component.evaluate(result);
        }

        return score;
    }

    public static float repeatedSimulation(
            LevelStructure level,
            Supplier<MarioAgent> agentSupplier,
            Weights weights,
            int repetitions
    ) {
        return (float) IntStream.range(0, repetitions)
                .parallel()
                .mapToDouble(_ -> simulation(level, agentSupplier.get(), weights))
                .average()
                .orElse(-100.0f);
    }
}
