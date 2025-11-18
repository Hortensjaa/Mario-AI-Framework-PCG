package levelGenerators.list3.structure;

import engine.core.MarioLevelModel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Random;


@Getter
@AllArgsConstructor
public class Enemy extends Decorator {
    private char enemyType;

    public Enemy() {
        char[] enemyTypes = MarioLevelModel.getEnemyCharacters(false);
        this.enemyType = enemyTypes[new Random().nextInt(enemyTypes.length)];
        this.width = 1;
    }

    @Override
    public void mutate() {
        char[] enemyTypes = MarioLevelModel.getEnemyCharacters(false);
        this.enemyType = enemyTypes[new Random().nextInt(enemyTypes.length)];
    }

    @Override
    public int generate(MarioLevelModel model, int x, int floorLevel) {
        int enemyX = x;
        int enemyY = floorLevel - 1;

        if (enemyY >= 0 && enemyX >= 0 && enemyX < model.getWidth()) {
            model.setBlock(enemyX, enemyY, this.enemyType);
        }

        return this.width;
    }
}

