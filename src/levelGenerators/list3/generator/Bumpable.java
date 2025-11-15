package levelGenerators.list3.generator;

import engine.core.MarioLevelModel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Random;


@Getter
@AllArgsConstructor
public class Bumpable extends Decorator {
    private static final int MAX_WIDTH = 5;
    private static final int MIN_WIDTH = 1;
    private static final int MAX_HEIGHT = 6;
    private static final int MIN_HEIGHT = 2;

    private int width;
    private int height;
    private char bumpableType;

    public Bumpable() {
        this.width = new Random().nextInt(MAX_WIDTH - MIN_WIDTH + 1) + MIN_WIDTH;
        this.height = new Random().nextInt(MAX_HEIGHT - MIN_HEIGHT + 1) + MIN_HEIGHT;
        char[] bumpableTypes = MarioLevelModel.getBumpableTiles();
        this.bumpableType = bumpableTypes[new Random().nextInt(bumpableTypes.length)];
    }

    @Override
    public void mutate() {
        this.width = new Random().nextInt(MAX_WIDTH - MIN_WIDTH + 1) + MIN_WIDTH;
        this.height = new Random().nextInt(MAX_HEIGHT - MIN_HEIGHT + 1) + MIN_HEIGHT;

        if (new Random().nextDouble() < 0.3) {
            char[] bumpableTypes = MarioLevelModel.getBumpableTiles();
            this.bumpableType = bumpableTypes[new Random().nextInt(bumpableTypes.length)];
        }
    }

    @Override
    public int generate(MarioLevelModel model, int x, int floorLevel) {
        for (int i = 0; i < this.width; i++) {
            int blockX = x + i;
            int blockY = floorLevel - this.height;

            if (blockX >= 0 && blockX < model.getWidth() && blockY >= 0 && blockY < model.getHeight()) {
                model.setBlock(blockX, blockY, this.bumpableType);
            }
        }

        return this.width;
    }
}
