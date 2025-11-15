package levelGenerators.list3.generator;


import java.util.Random;

public class Gap extends Structure {

    public Gap(int width) {
        this.width = width;
        this.height = Integer.MAX_VALUE;
    }

    public Gap() {
        this(new Random().nextInt(MAX_WIDTH - MIN_WIDTH + 1) + MIN_WIDTH);
    }

    @Override
    public void mutate() {
        this.width = new Random().nextInt(MAX_WIDTH - MIN_WIDTH + 1) + MIN_WIDTH;
    }

    @Override
    public int generate(engine.core.MarioLevelModel model, int x, int floorLevel) {
        return this.width;
    }
}
