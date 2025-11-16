package levelGenerators.list3.structure;

import engine.core.MarioLevelModel;
import lombok.Getter;

import java.util.Random;

@Getter
public class Coins extends Decorator {
    private static final int MAX_WIDTH = 5;
    private static final int MIN_WIDTH = 1;
    private static final int MAX_HEIGHT = 4;
    private static final int MIN_HEIGHT = 1;
    @Getter private static final int MAX_SIZE = 4;

    @Getter private int size;
    @Getter private int height;

    public Coins() {
        this(
            new Random().nextInt(MAX_WIDTH - MIN_WIDTH + 1) + MIN_WIDTH,
            new Random().nextInt(MAX_HEIGHT - MIN_HEIGHT + 1) + MIN_HEIGHT,
            new Random().nextInt(MAX_SIZE - 1) + 1
        );
    }

    public Coins(int w, int h, int s) {
        this.width = w;
        this.height = h;
        this.size = s;
    }

    public int getCoinsNumber() {
        return this.width * this.size;
    }

    @Override
    public void mutate() {
        this.height = new Random().nextInt(MAX_HEIGHT - MIN_HEIGHT + 1) + MIN_HEIGHT;
        this.size = new Random().nextInt(MAX_SIZE - 1) + 1;
    }

    @Override
    public int generate(MarioLevelModel model, int x, int floorLevel) {
        int startY = floorLevel - this.height;

        for (int i = 0; i < this.width; i++) {
            for (int h = 0; h < this.size; h++) {
                int coinX = x + i;
                int coinY = startY - h;
                if (coinX >= 0 && coinX < model.getWidth() && coinY >= 0 && coinY < model.getHeight()) {
                    model.setBlock(coinX, coinY, MarioLevelModel.COIN);
                }
            }
        }

        return this.width;
    }
}
