package views;

import java.awt.*;
import java.io.File;

public class HUDView {
    private static Font loadArcadeClassicFont(int style, float size) {
        try {
            File fontFile = new File("res/ARCADECLASSIC.TTF");
            if (!fontFile.exists()) {
                fontFile = new File("src/res/ARCADECLASSIC.TTF");
            }

            if (fontFile.exists()) {
                Font customFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);
                GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(customFont);
                return customFont.deriveFont(style, size);
            }
        } catch (Exception e) {
            System.out.println("ERROR al cargar la fuente arcadeclassic: " + e.getMessage());
        }

        return new Font("ARCADECLASSIC", style, Math.round(size));
    }

    public static void draw(Graphics2D g, int score) {
        g.setColor(Color.WHITE);
        g.setFont(loadArcadeClassicFont(Font.BOLD, 18));
        g.drawString("SCORE  " + score, 15, 30);
    }
}