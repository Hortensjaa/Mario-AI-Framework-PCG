package levelGenerators.list3.structure;

import java.util.Random;

public class Plain extends Terrain {
    private static final int MAX_WIDTH = 8;
    private static final int MIN_WIDTH = 3;

    public Plain(int width) {
        this.width = width;
        this.height = 0;
    }

    public Plain() {
        this(new Random().nextInt(MAX_WIDTH - MIN_WIDTH + 1) + MIN_WIDTH);
    }

    @Override
    public void mutate() {}

    @Override
    public int generate(engine.core.MarioLevelModel model, int x, int floorLevel) {
        for (int i = 0; i < this.width; i++) {
            for (int y = floorLevel; y < model.getHeight(); y++) {
                model.setBlock(x + i, y, engine.core.MarioLevelModel.GROUND);
            }
        }
        return this.width;
    }
}
