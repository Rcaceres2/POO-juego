package views;

import java.awt.*;
import models.*;

public class EnemyView {
    public static void drawEnemy(Graphics2D g, EnemyModel enemy) {
        g.setColor(Color.RED);
        g.fillRect(enemy.getX(), enemy.getY(), enemy.getWidth(), enemy.getHeight());
    }

    public static void drawEnemyTank(Graphics2D g, EnemyTank enemyTank) {
        g.setColor(Color.ORANGE);
        g.fillRoundRect(enemyTank.getX(), enemyTank.getY(), enemyTank.getWidth(), enemyTank.getHeight(), 10, 10);
    }

    public static void drawBullet(Graphics2D g, BulletModel bullet) {
        g.setColor(Color.YELLOW);
        g.fillRect(bullet.getX(), bullet.getY(), bullet.getWidth(), bullet.getHeight());
    }
}