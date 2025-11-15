package levelGenerators.list3.generator;

import engine.core.MarioLevelModel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Random;


@Getter
@AllArgsConstructor
public class Enemy extends Decorator {
    private static final int MAX_WIDTH = 5;
    private static final int MIN_WIDTH = 3;

    private char enemyType;

    public Enemy() {
        char[] enemyTypes = MarioLevelModel.getEnemyCharacters(false);
        this.enemyType = enemyTypes[new Random().nextInt(enemyTypes.length)];
        this.width = new Random().nextInt(MAX_WIDTH - MIN_WIDTH + 1) + MIN_WIDTH;
    }

    @Override
    public void mutate() {
        char[] enemyTypes = MarioLevelModel.getEnemyCharacters(false);
        this.enemyType = enemyTypes[new Random().nextInt(enemyTypes.length)];
        this.width = new Random().nextInt(MAX_WIDTH - MIN_WIDTH + 1) + MIN_WIDTH;
    }

    @Override
    public int generate(MarioLevelModel model, int x, int floorLevel) {
        int enemyX = x + this.width / 2;
        int enemyY = floorLevel - 1;

        if (enemyY >= 0 && enemyX >= 0 && enemyX < model.getWidth()) {
            model.setBlock(enemyX, enemyY, this.enemyType);
        }

        return this.width;
    }
}

