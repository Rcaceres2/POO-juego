package models;

public class BulletModel extends GameObject {
    private int speed;
    private int direction;

    public BulletModel(int x, int y) {
        this(x, y, 8, -1);
    }

    public BulletModel(int x, int y, int speed, int direction) {
        super(x, y, 6, 12);
        this.speed = speed;
        this.direction = direction;
    }

    @Override
    public void update() {
        y += direction * speed;
    }
}