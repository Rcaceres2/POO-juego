package models;

import core.Constants;

public class EnemyModel extends GameObject {
    private int speed = 10;
    private int horizontalSpeed = 15;
    private int horizontalDirection = 10;

    public EnemyModel(int x, int y) {
        super(x, y, 32, 32);
    }

    @Override
    public void update() {
        y += speed;
        x += horizontalSpeed * horizontalDirection;

        if (x <= 0 || x >= Constants.WIDTH - width) {
            horizontalDirection *= -1;
            x = Math.max(0, Math.min(x, Constants.WIDTH - width));
        }

        if (y > Constants.HEIGHT) {
            y = -40;
            x = (int)(Math.random() * (Constants.WIDTH - width));
            horizontalDirection = Math.random() < 0.5 ? -1 : 1;
        }
    }
}