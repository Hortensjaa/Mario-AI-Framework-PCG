package levelGenerators.list1;

import engine.core.MarioLevelModel;
import engine.core.MarioTimer;

public class Task3Generator extends List1Generator {
    private final int GROUND_Y_LOCATION = 13;
    private final int LOOPS_NUMBER = 35;
    private final int FLAT_BLOCK_LENGTH = 1;
    private final int STAIRS_NUM = 2;

    @Override
    public String getGeneratedLevel(MarioLevelModel model, MarioTimer timer) {
        model.clearMap();

        int currentX = 0;

        for (int i = 0; i < LOOPS_NUMBER; i++) {
            // flat block
            for (int x = currentX; x < currentX + FLAT_BLOCK_LENGTH; x++) {
                model.setBlock(x, GROUND_Y_LOCATION, MarioLevelModel.GROUND);
                model.setBlock(x, GROUND_Y_LOCATION + 1, MarioLevelModel.GROUND);
                model.setBlock(x, GROUND_Y_LOCATION + 2, MarioLevelModel.GROUND);

                if (x > currentX && x < currentX + FLAT_BLOCK_LENGTH) {
                    model.setBlock(x, GROUND_Y_LOCATION - 3, MarioLevelModel.COIN);
                    model.setBlock(x, GROUND_Y_LOCATION - 5, MarioLevelModel.COIN);
                    model.setBlock(x, GROUND_Y_LOCATION - 7, MarioLevelModel.COIN);
                }
            }
            currentX += FLAT_BLOCK_LENGTH;

            // stair
            for (int step = 0; step < STAIRS_NUM; step++) {
                for (int x = currentX; x < currentX + 1; x++) {
                    for (int y = GROUND_Y_LOCATION; y >= GROUND_Y_LOCATION - step; y--) {
                        model.setBlock(x, y, MarioLevelModel.PYRAMID_BLOCK);
                    }

                    for (int y = GROUND_Y_LOCATION + 1; y < model.getHeight(); y++) {
                        model.setBlock(x, y, MarioLevelModel.PYRAMID_BLOCK);
                    }

                    // Add floating coins above stairs
                    model.setBlock(x, GROUND_Y_LOCATION - step - 2, MarioLevelModel.COIN);
                    model.setBlock(x, GROUND_Y_LOCATION - step - 4, MarioLevelModel.COIN);
                }
                currentX += 1;
            }
        }


        model.setBlock(2, GROUND_Y_LOCATION - 1, MarioLevelModel.MARIO_START);

        model.setBlock(currentX - 1, GROUND_Y_LOCATION - 1, MarioLevelModel.MARIO_EXIT);

        return model.getMap();
    }

    @Override
    public String getGeneratorName() {
        return "Task3Generator";
    }

    public int getLevelWidth() {
        return LOOPS_NUMBER * (FLAT_BLOCK_LENGTH + STAIRS_NUM) + 1;
    }
}
