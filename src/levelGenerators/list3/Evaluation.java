package levelGenerators.list3;

import levelGenerators.list3.structure.*;

import java.util.List;


public final class Evaluation {
    private final static int LEVEL_WIDTH = 150;

    private static float countDecoratorsScore(LevelStructure level, Class<? extends Decorator> decoratorClass) {
        int c = 0;
        for (var decorator : level.getDecorators()) {
            if (decoratorClass.isInstance(decorator)) {
                c += 1;
            }
        }
        return (float) c / level.getDecorators().size();
    }

    private static float countEnemiesScore(LevelStructure level) {
        return countDecoratorsScore(level, Enemy.class);
    }

    private static float countCoinsScore(LevelStructure level) {
        return countDecoratorsScore(level, Coins.class);
    }

    private static float countBlocksScore(LevelStructure level) {
        return countDecoratorsScore(level, Bumpable.class);
    }

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
        return (float) notPassableJumpCount / level.getTerrains().size();
    }

    private static float gapFirstPenalty(LevelStructure level) {
        if (!level.getTerrains().isEmpty() && level.getTerrains().getFirst() instanceof Gap) {
            return 1;
        }
        return 0;
    }

    private static float monotonyTerrainPenalty(LevelStructure level) {
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

    private static float monotonyDecorPenalty(LevelStructure level) {
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

    // task1 - passability >= 99%
    public static float task1Score(LevelStructure level) {
        return (
            + 15 * countEnemiesScore(level)
            + 10 * countBlocksScore(level)
            + 10 * countCoinsScore(level)
            - 5 * enemyGroupsScore(level)
            - 10 * monotonyTerrainPenalty(level)
            - 10 * monotonyDecorPenalty(level)
            - 500 * notPassableGapsPenalty(level)
            - 500 * notPassableJumpsPenalty(level)
            - 1000 * gapFirstPenalty(level)
        );
    }
}
