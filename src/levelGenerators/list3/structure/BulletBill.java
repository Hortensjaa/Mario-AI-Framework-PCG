package levelGenerators.list3.structure;

import engine.core.MarioLevelModel;

import java.util.Random;

public class BulletBill extends Terrain {
    private static final int MIN_HEIGHT = 2;

    public BulletBill(int height) {
        this.width = 1;
        this.height = height;
    }

    public BulletBill() {
        this(
                new Random().nextInt(MAX_HEIGHT - MIN_HEIGHT + 1) + MIN_HEIGHT
        );
    }

    @Override
    public void mutate() {
        this.height = new Random().nextInt(MAX_HEIGHT - MIN_HEIGHT + 1) + MIN_HEIGHT;
    }

    @Override
    public int generate(MarioLevelModel model, int x, int floorLevel) {
        for (int i = 0; i < this.width; i++) {
            for (int y = floorLevel; y < model.getHeight(); y++) {
                model.setBlock(x + i, y, MarioLevelModel.GROUND);
            }
        }

        for (int i = 0; i < this.width; i++) {
            for (int y = floorLevel - this.height; y < floorLevel; y++) {
                if (y >= 0) {
                    model.setBlock(x + i, y, MarioLevelModel.BULLET_BILL);
                }
            }
        }

        return this.width;
    }
}

