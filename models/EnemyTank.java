package models;

import core.Constants;

public class EnemyTank extends GameObject {
    private double dy = 0.3;
    private int maxHits = 4;
    private int hits = 0;
    private int shootCooldown = 0;

    public EnemyTank(int x, int y) {
        super(x, y, 32, 32);
    }

    @Override
    public void update() {
        if (shootCooldown > 0) {
            shootCooldown--;
        }

        y += (int) dy;
        dy += 0.02;
        if (y > Constants.HEIGHT) {
            y = -50;
            x = (int)(Math.random() * (Constants.WIDTH - 32));
            dy = 0.3;
            hits = 0;
            shootCooldown = 0;
        }
    }

    public boolean tryShoot() {
        if (shootCooldown > 0) {
            return false;
        }
        shootCooldown = 90;
        return true;
    }

    public boolean hit() {
        hits++;
        if (hits >= maxHits) {
            return true;
        }
        dy = -1.2;
        return false;
    }

    public boolean isDestroyed() {
        return hits >= maxHits;
    }
}