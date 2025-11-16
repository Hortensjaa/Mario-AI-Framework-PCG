package levelGenerators.list3;

import levelGenerators.list3.structure.*;

import java.util.List;


public final class Evaluation {
    private final static int LEVEL_WIDTH = 150;

    // ---------------- helpers ----------------
    private static float countDecoratorsScore(LevelStructure level, Class<? extends Decorator> decoratorClass) {
        int c = 0;
        for (var decorator : level.getDecorators()) {
            if (decoratorClass.isInstance(decorator)) {
                c += 1;
            }
        }
        return (float) c / level.getDecorators().size();
    }

    private static float genericDiversity(LevelStructure level, List<? extends Mutable> list, List<Class<? extends Mutable>> types) {
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



    private static float countEnemiesScore(LevelStructure level) {
        return countDecoratorsScore(level, Enemy.class);
    }

    /** Count blocks with coins and divide its number by the number of all decorators */
    private static float countCoinsScore(LevelStructure level) {
        return countDecoratorsScore(level, Coins.class);
    }

    /** Count bumpable blocks and divide its number by the number of all decorators */
    private static float countBlocksScore(LevelStructure level) {
        return countDecoratorsScore(level, Bumpable.class);
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
        return (float) numberOfGroups / LEVEL_WIDTH;
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
        return (notPassableGapCount + (curGapLength > 4 ? 1 : 0));
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
        return (float) notPassableJumpCount;
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

    /** Penalty for monotonous terrain fragments (same type or height) */
    private static float monotonousTerrainPenalty(LevelStructure level) {
        // square of length of monotonous fragments
        int penalty = 0;
        int curLength = 1;
        List<Terrain> terrains = level.getTerrains();
        int size = terrains.size();
        for (int i = 1; i < size; i++) {
            if (terrains.get(i).getClass() == terrains.get(i - 1).getClass()) {
                curLength += 1;
            } else if (terrains.get(i).getHeight() == terrains.get(i - 1).getHeight()) {
                curLength += 1;
            } else {
                penalty += (curLength - 1) * (curLength - 1);
                curLength = 1;
            }
        }
        penalty += (curLength - 1) * (curLength - 1);
        return (float) Math.sqrt((double) penalty / (size * size));
    }

    /** Penalty for monotonous decor fragments (same type) */
    private static float monotonousDecorPenalty(LevelStructure level) {
        // square of length of monotonous fragments
        int penalty = 0;
        int curLength = 1;
        List <Decorator> decorators = level.getDecorators();
        int size = decorators.size();
        for (int i = 1; i < size; i++) {
            if (decorators.get(i).getClass() == decorators.get(i - 1).getClass()) {
                curLength += 1;
            } else {
                penalty += (curLength - 1) * (curLength - 1);
                curLength = 1;
            }
        }
        penalty += (curLength - 1) * (curLength - 1);
        return (float) Math.sqrt((double) penalty / (size * size));
    }

    /** Number of gaps overall and its proportion */
    private static float gapsNumberScore(LevelStructure level) {
        int c = 0;
        for (var t : level.getTerrains()) {
            if (t instanceof Gap) {
                c += 1;
            }
        }
        return (float) c / level.getTerrains().size();
    }

    /** Number of different terrain structures overall and its proportion */
    private static float overallTerrainDiversity(LevelStructure level) {
        List<Terrain> terrains = level.getTerrains(); // assume returns List<Terrain>
        List<Class<? extends Mutable>> types = List.of(Hill.class, BulletBill.class, Gap.class, Pipe.class, Plain.class);
        return genericDiversity(level, terrains, types);
    }

    /** Number of different decorators overall and its proportion */
    private static float overallDecorDiversity(LevelStructure level) {
        List<Decorator> decors = level.getDecorators(); // assume returns List<Decorator>
        List<Class<? extends Mutable>> types = List.of(Bumpable.class, Coins.class, Enemy.class, EmptyDecor.class);
        return genericDiversity(level, decors, types);
    }

    // task1 - passability >= 99%
    public static float task1Score(LevelStructure level) {
        return (
            + 15 * countEnemiesScore(level)
            + 10 * countBlocksScore(level)
            + 10 * countCoinsScore(level)
            + 10 * overallTerrainDiversity(level)
            + 10 * overallDecorDiversity(level)
            - gapsNumberScore(level)
            - 5 * enemyGroupsScore(level)
            - 10 * monotonousTerrainPenalty(level)
            - 10 * monotonousDecorPenalty(level)
            - 50 * enemyFirstPenalty(level)
            - 500 * notPassableGapsPenalty(level)
            - 500 * notPassableJumpsPenalty(level)
            - 1000 * gapFirstPenalty(level)
        );
    }
}
