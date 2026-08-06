package views;

import models.PlayerModel;
import java.awt.Color;
import java.awt.Graphics2D;

public class PlayerView {
    public static void draw(Graphics2D g, PlayerModel player) {
        g.setColor(Color.CYAN);
        g.fillOval(player.getX(), player.getY(), player.getWidth(), player.getHeight());
        g.setColor(Color.WHITE);
        g.fillRect(player.getX() + 13, player.getY() - 6, 6, 12);
    }
}