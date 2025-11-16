package levelGenerators.list3.structure;

import engine.core.MarioLevelModel;

import java.util.Random;

public class Pipe extends Terrain {
    private static final int MIN_HEIGHT = 2;
    char pipeType;

    public Pipe(int height, char pipeType) {
        this.width = 2;
        this.height = height;
        this.pipeType = pipeType;
    }

    public Pipe() {
        this(
                new Random().nextInt(MAX_HEIGHT - MIN_HEIGHT + 1) + MIN_HEIGHT,
                new Random().nextDouble() > 0.05 ? MarioLevelModel.PIPE : MarioLevelModel.PIPE_FLOWER
        );
    }

    @Override
    public void mutate() {
        this.height = new Random().nextInt(MAX_HEIGHT - MIN_HEIGHT + 1) + MIN_HEIGHT;
        this.pipeType = new Random().nextDouble() > 0.1 ? MarioLevelModel.PIPE : MarioLevelModel.PIPE_FLOWER;
    }

    @Override
    public int generate(MarioLevelModel model, int x, int floorLevel) {
        for (int i = 0; i < this.width; i++) {
            for (int y = floorLevel; y < model.getHeight(); y++) {
                model.setBlock(x + i, y, engine.core.MarioLevelModel.GROUND);
            }
        }

        for (int i = 0; i < this.width; i++) {
            for (int y = floorLevel - this.height; y < floorLevel; y++) {
                if (y >= 0) {
                    model.setBlock(x + i, y, pipeType);
                }
            }
        }

        return this.width;
    }
}
