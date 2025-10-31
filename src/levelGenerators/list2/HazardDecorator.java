package levelGenerators.list2;

import java.util.Random;


public class HazardDecorator {
    private final Random random = new Random();

    // Probability settings
    private final double coinChance = 0.4;    // 70% of suitable segments get some coins
    private final double enemyChance = 0.35;  // 40% of suitable segments get an enemy
    private final double blockChance = 0.4;  // 20% of suitable segments get block

    /**
     * Adds coins, blocks, and enemies to a generated terrain sequence.
     * Allows combinations, e.g. coin + enemy.
     */
    public String decorate(String terrain) {
        StringBuilder decorated = new StringBuilder(64);

        for (int i = 0; i < terrain.length(); i++) {
            char tile = terrain.charAt(i);

            // skip places where player or base block already exists
            if (tile == 'P' || tile == 'B') {
                decorated.append('0');
                continue;
            }

            boolean hasEnemy = random.nextDouble() < enemyChance;
            boolean hasBlock = random.nextDouble() < blockChance;
            boolean hasCoin = random.nextDouble() < coinChance;

            // Build decoration character
            if (hasEnemy && hasCoin) {
                decorated.append('X'); // both enemy and coin
            } else if (hasEnemy && hasBlock) {
                decorated.append('Y'); // both enemy and block
            } else if (hasEnemy) {
                decorated.append('E');
            } else if (hasBlock) {
                decorated.append('B');
            } else if (hasCoin) {
                decorated.append('C');
            } else {
                decorated.append('0');
            }
        }

        return decorated.toString();
    }
}


