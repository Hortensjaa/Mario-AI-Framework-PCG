package levelGenerators.list1;

import engine.core.MarioLevelModel;
import engine.core.MarioTimer;

public class Task5Generator extends List1Generator {
    private final int LEVEL_HEIGHT = 20;
    private final int SINGLE_FALL_HEIGHT = 6;

    @Override
    public String getGeneratedLevel(MarioLevelModel model, MarioTimer timer) {
        model.clearMap();

        int currentX = 0;
        int OffsetY = -3;
        int ceilingY = -2 - OffsetY;
        model.setBlock(0, 3, MarioLevelModel.MARIO_START);

        /* todo mushroom trigger at top */
        // (flat + ceiling) x2
        model.setBlock(currentX, 4 - OffsetY, MarioLevelModel.GROUND);
        model.setBlock(currentX, 1 - OffsetY, MarioLevelModel.SPECIAL_BRICK);
        model.setBlock(currentX, ceilingY, MarioLevelModel.NORMAL_BRICK);
        currentX++;
        model.setBlock(currentX, 4 - OffsetY, MarioLevelModel.GROUND);
        model.setBlock(currentX, 1 - OffsetY, MarioLevelModel.SPECIAL_BRICK);
        model.setBlock(currentX, ceilingY, MarioLevelModel.NORMAL_BRICK);
        currentX++;
        // stair up
        model.setBlock(currentX, 4 - OffsetY, MarioLevelModel.GROUND);
        model.setBlock(currentX, 3 - OffsetY, MarioLevelModel.GROUND);
        model.setBlock(currentX, 1 - OffsetY, MarioLevelModel.SPECIAL_BRICK);
        model.setBlock(currentX, ceilingY, MarioLevelModel.NORMAL_BRICK);
        currentX++;

        // left side of fall
        for (int y = 4; y < 4 + SINGLE_FALL_HEIGHT; y++) {
            model.setBlock(currentX - 1, y - OffsetY, MarioLevelModel.GROUND);
        }
        int FLOOR_HEIGHT = 4 + SINGLE_FALL_HEIGHT - OffsetY;
        model.setBlock(currentX, FLOOR_HEIGHT, MarioLevelModel.GROUND);
        model.setBlock(currentX, FLOOR_HEIGHT - 1, MarioLevelModel.SPIKY);
        model.setBlock(currentX, ceilingY, MarioLevelModel.NORMAL_BRICK);
        currentX++;
//        model.setBlock(currentX,  FLOOR_HEIGHT, MarioLevelModel.GROUND);
//        model.setBlock(currentX, FLOOR_HEIGHT - 1, MarioLevelModel.SPIKY);
//        model.setBlock(currentX, ceilingY, MarioLevelModel.NORMAL_BRICK);
//        currentX++;

        for (int y = -2; y < SINGLE_FALL_HEIGHT + 2; y++) {
            model.setBlock(currentX, y - OffsetY, MarioLevelModel.GROUND);
        }
        model.setBlock(currentX,  FLOOR_HEIGHT, MarioLevelModel.GROUND);
        model.setBlock(currentX, FLOOR_HEIGHT - 1, MarioLevelModel.SPIKY);
        currentX++;

        model.setBlock(currentX,  FLOOR_HEIGHT, MarioLevelModel.GROUND);
        model.setBlock(currentX, FLOOR_HEIGHT - 1, MarioLevelModel.SPIKY);
//        model.setBlock(currentX,  FLOOR_HEIGHT - 3, MarioLevelModel.NORMAL_BRICK);
        model.setBlock(currentX,  FLOOR_HEIGHT - 6, MarioLevelModel.GROUND);
        currentX++;

        model.setBlock(currentX,  FLOOR_HEIGHT, MarioLevelModel.GROUND);
        model.setBlock(currentX,  FLOOR_HEIGHT - 1, MarioLevelModel.GROUND);
        model.setBlock(currentX,  FLOOR_HEIGHT - 2, MarioLevelModel.GROUND);
//        model.setBlock(currentX,  FLOOR_HEIGHT - 5, MarioLevelModel.SPIKY);
        model.setBlock(currentX,  FLOOR_HEIGHT - 6, MarioLevelModel.GROUND);
        currentX++;

        model.setBlock(currentX,  FLOOR_HEIGHT - 2, MarioLevelModel.GROUND);
        model.setBlock(currentX,  FLOOR_HEIGHT - 6, MarioLevelModel.GROUND);
//        model.setBlock(currentX,  FLOOR_HEIGHT - 5, MarioLevelModel.SPIKY);
        currentX++;

        model.setBlock(currentX,  FLOOR_HEIGHT - 2, MarioLevelModel.GROUND);
        model.setBlock(currentX,  FLOOR_HEIGHT - 3, MarioLevelModel.GROUND);
        model.setBlock(currentX,  FLOOR_HEIGHT - 4, MarioLevelModel.GROUND);
        model.setBlock(currentX,  FLOOR_HEIGHT - 6, MarioLevelModel.GROUND);
        currentX++;

        model.setBlock(currentX,  FLOOR_HEIGHT - 6, MarioLevelModel.GROUND);

        model.setBlock(currentX, FLOOR_HEIGHT, MarioLevelModel.MARIO_EXIT);

        return model.getMap();
    }

    @Override
    public String getGeneratorName() {
        return "Task4Generator";
    }

    public int getLevelWidth() {
        return 150;
    }
}

