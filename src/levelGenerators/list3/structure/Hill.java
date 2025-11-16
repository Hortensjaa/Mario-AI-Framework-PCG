package levelGenerators.list3.structure;

import java.util.Random;

public class Hill extends Terrain {

    public Hill(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public Hill() {
        this(
            new Random().nextInt(MAX_WIDTH - MIN_WIDTH + 1) + MIN_WIDTH,
            new Random().nextInt(MAX_HEIGHT - MIN_HEIGHT + 1) + MIN_HEIGHT
        );
    }

    @Override
    public void mutate() {
        this.height = new Random().nextInt(MAX_HEIGHT - MIN_HEIGHT + 1) + MIN_HEIGHT;
    }

    @Override
    public int generate(engine.core.MarioLevelModel model, int x, int floorLevel) {
        // Generate ground base
        for (int i = 0; i < this.width; i++) {
            for (int y = floorLevel; y < model.getHeight(); y++) {
                model.setBlock(x + i, y, engine.core.MarioLevelModel.GROUND);
            }
        }

        // Place platform rectangle above ground
        for (int i = 0; i < this.width; i++) {
            for (int h = 0; h < this.height; h++) {
                int y = floorLevel - this.height + h;
                if (y >= 0) {
                    model.setBlock(x + i, y, engine.core.MarioLevelModel.PLATFORM);
                }
            }
        }

        return this.width;
    }
}
