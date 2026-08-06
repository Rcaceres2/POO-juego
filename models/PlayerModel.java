package models;

import core.Constants;

public class PlayerModel extends GameObject {
    public int dx, dy;

    public PlayerModel(int x, int y) {
        super(x, y, 32, 32);
    }

    @Override
    public void update() {
        x += dx;
        y += dy;

        if (x < 0) x = 0;
        if (x > Constants.WIDTH - width) x = Constants.WIDTH - width;
        if (y < 0) y = 0;
        if (y > Constants.HEIGHT - height) y = Constants.HEIGHT - height;
    }
}