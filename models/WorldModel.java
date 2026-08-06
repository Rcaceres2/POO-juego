package models;

import core.Constants;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class WorldModel {
    private PlayerModel player;
    private List<BulletModel> bullets;
    private List<BulletModel> enemyBullets;
    private List<EnemyModel> enemies;
    private List<EnemyTank> enemyTanks;
    private List<Point> stars;
    private int score;
    private boolean gameOver;

    public WorldModel() {
        reset();
    }

    public void reset() {
        player = new PlayerModel(Constants.WIDTH / 2 - 16, Constants.HEIGHT - 80);
        bullets = new ArrayList<>();
        enemyBullets = new ArrayList<>();
        enemies = new ArrayList<>();
        enemyTanks = new ArrayList<>();
        stars = new ArrayList<>();
        score = 0;
        gameOver = false;

        for (int i = 0; i < 50; i++) {
            stars.add(new Point((int)(Math.random() * Constants.WIDTH), (int)(Math.random() * Constants.HEIGHT)));
        }

        for (int i = 0; i < 5; i++) {
            enemies.add(new EnemyModel((i + 1) * 90, -50 - (i * 60)));
        }

        enemyTanks.add(new EnemyTank(200, -50));
    }

    public PlayerModel getPlayer() { return player; }
    public List<BulletModel> getBullets() { return bullets; }
    public List<BulletModel> getEnemyBullets() { return enemyBullets; }
    public List<EnemyModel> getEnemies() { return enemies; }
    public List<EnemyTank> getEnemyTanks() { return enemyTanks; }
    public List<Point> getStars() { return stars; }
    public int getScore() { return score; }
    public boolean isGameOver() { return gameOver; }
    public void setGameOver(boolean gameOver) { this.gameOver = gameOver; }
    public void addScore(int points) { this.score += points; }
    public void spawnEnemy() {
        enemies.add(new EnemyModel((int)(Math.random() * (Constants.WIDTH - 40)), -50));
    }
}