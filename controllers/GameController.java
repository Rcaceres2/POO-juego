package controllers;

import javax.swing.Timer;
import models.*;
import views.GamePanel;

public class GameController {
    private WorldModel model;
    private GamePanel view;
    private InputController inputController;
    private Timer timer;

    public GameController(WorldModel model, GamePanel view, InputController inputController) {
        this.model = model;
        this.view = view;
        this.inputController = inputController;

        // Vincular teclado a la vista
        this.view.addKeyListener(this.inputController);
        this.view.setFocusable(true);
        this.view.requestFocusInWindow();

        // Game Loop a ~60 FPS (16 ms)
        this.timer = new Timer(16, e -> updateGame());
    }

    public void startGame() {
        timer.start();
    }

    public void restartGame() {
        model.reset();
        inputController.left = false;
        inputController.right = false;
        inputController.up = false;
        inputController.down = false;
        inputController.space = false;
        timer.start();
    }

    private void updateGame() {
        if (model.isGameOver()) {
            view.repaint();
            return;
        }

        PlayerModel player = model.getPlayer();

        // Manejo de movimiento según teclas
        player.dx = 0;
        player.dy = 0;

        if (inputController.left) player.dx -= 5;
        if (inputController.right) player.dx += 5;
        if (inputController.up) player.dy -= 5;
        if (inputController.down) player.dy += 5;

        // Disparo
        if (inputController.space) {
            model.getBullets().add(new BulletModel(player.getX() + player.getWidth() / 2 - 3, player.getY(), 8, -1));
            inputController.space = false; // Disparo uno a la vez por pulsación
        }

        // Actualizar posiciones de las estrellas
        for (java.awt.Point star : model.getStars()) {
            star.y += 2;
            if (star.y > core.Constants.HEIGHT) star.y = 0;
        }

        player.update();

        // Actualizar proyectiles, enemigos y enemyTanks
        model.getBullets().removeIf(b -> {
            b.update();
            return b.getY() < -20;
        });

        model.getEnemyBullets().removeIf(b -> {
            b.update();
            return b.getY() > core.Constants.HEIGHT + 20;
        });

        if (model.getEnemies().size() < 5) {
            model.spawnEnemy();
        }

        for (EnemyModel enemy : model.getEnemies()) enemy.update();
        for (EnemyTank enemyTank : model.getEnemyTanks()) {
            enemyTank.update();
            if (enemyTank.tryShoot()) {
                model.getEnemyBullets().add(new BulletModel(enemyTank.getX() + enemyTank.getWidth() / 2 - 3, enemyTank.getY() + enemyTank.getHeight(), 4, 1));
            }
        }

        checkCollisions();

        // Redibujar la pantalla
        view.repaint();
    }

    private void checkCollisions() {
        // Colisiones de Balas con Enemigos y Campanas
        java.util.Iterator<BulletModel> bIt = model.getBullets().iterator();
        while (bIt.hasNext()) {
            BulletModel b = bIt.next();
            boolean removed = false;

            java.util.Iterator<EnemyModel> eIt = model.getEnemies().iterator();
            while (eIt.hasNext()) {
                EnemyModel e = eIt.next();
                if (b.getBounds().intersects(e.getBounds())) {
                    model.addScore(100);
                    eIt.remove();
                    model.spawnEnemy();
                    removed = true;
                    break;
                }
            }

            if (!removed) {
                java.util.Iterator<EnemyTank> enemyTankIt = model.getEnemyTanks().iterator();
                while (enemyTankIt.hasNext()) {
                    EnemyTank enemyTank = enemyTankIt.next();
                    if (b.getBounds().intersects(enemyTank.getBounds())) {
                        if (enemyTank.hit()) {
                            model.addScore(200);
                            enemyTankIt.remove();
                            if (model.getEnemyTanks().size() < 1) {
                                model.getEnemyTanks().add(new EnemyTank((int)(Math.random() * (core.Constants.WIDTH - 40)), -50));
                            }
                        }
                        removed = true;
                        break;
                    }
                }
            }

            if (removed) bIt.remove();
        }

        java.util.Iterator<BulletModel> enemyBulletIt = model.getEnemyBullets().iterator();
        while (enemyBulletIt.hasNext()) {
            BulletModel enemyBullet = enemyBulletIt.next();
            if (enemyBullet.getBounds().intersects(model.getPlayer().getBounds())) {
                finishGame();
                break;
            }
        }

        for (EnemyTank enemyTank : model.getEnemyTanks()) {
            if (model.getPlayer().getBounds().intersects(enemyTank.getBounds())) {
                finishGame();
                break;
            }
        }

        if (model.getEnemyTanks().size() < 1) {
            model.getEnemyTanks().add(new EnemyTank((int)(Math.random() * (core.Constants.WIDTH - 40)), -50));
        }

        for (EnemyModel enemy : model.getEnemies()) {
            if (model.getPlayer().getBounds().intersects(enemy.getBounds())) {
                finishGame();
                break;
            }
        }
    }

    private void finishGame() {
        model.setGameOver(true);
        timer.stop();
    }
}