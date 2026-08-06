package views;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;
import models.*;

public class GamePanel extends JPanel {
    private WorldModel model;
    private BufferedImage playerSprite;
    private BufferedImage enemySprite;
    private BufferedImage bulletSprite;
    private BufferedImage enemyBulletSprite;
    private BufferedImage enemyTankSprite;
    private BufferedImage backgroundSprite;
    private Clip backgroundClip;
    private JLabel restartLabel;
    private Runnable restartAction;
    private ImageIcon restartIcon;
    private JTextField initialsField;
    private JButton saveButton;
    private JLabel scoreSavedLabel;
    private boolean scoreSaved;
    private boolean wasGameOver = false;
    private List<String> cachedTopScores; // Variable para almacenar y actualizar los scores en pantalla

    private Font loadArcadeClassicFont(int style, float size) {
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

    public GamePanel(WorldModel model) {
        this.model = model;
        this.setPreferredSize(new Dimension(480, 620));
        this.setBackground(Color.BLACK);
        this.setLayout(null);
        this.setDoubleBuffered(true);

        File againFile = new File("res/again.gif");
        if (!againFile.exists()) {
            againFile = new File("src/res/again.gif");
        }

        restartIcon = new ImageIcon(againFile.getAbsolutePath());
        restartLabel = new JLabel();
        restartLabel.setIcon(restartIcon);
        restartLabel.setHorizontalAlignment(SwingConstants.CENTER);
        restartLabel.setBounds(130, 450, 160, 60);
        restartLabel.setVisible(false);
        restartLabel.setOpaque(false);
        restartLabel.setBorder(BorderFactory.createEmptyBorder());
        restartLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                scoreSaved = false;
                initialsField.setText("AAA");
                initialsField.setEnabled(true);
                saveButton.setEnabled(true);
                scoreSavedLabel.setVisible(false);
                wasGameOver = false;

                if (restartAction != null) {
                    restartAction.run();
                }
            }
        });
        this.add(restartLabel);

        initialsField = new JTextField(3);
        initialsField.setBounds(150, 280, 70, 28);
        initialsField.setVisible(false);
        initialsField.setHorizontalAlignment(SwingConstants.CENTER);
        initialsField.setText("AAA");
        initialsField.setFocusable(true);
        PlainDocument initialsDocument = (PlainDocument) initialsField.getDocument();
        initialsDocument.setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                    throws BadLocationException {
                if (string == null)
                    return;
                StringBuilder sb = new StringBuilder();
                for (char ch : string.toCharArray()) {
                    if (Character.isLetter(ch) && sb.length() < 3) {
                        sb.append(Character.toUpperCase(ch));
                    }
                }
                if (fb.getDocument().getLength() + sb.length() <= 3) {
                    super.insertString(fb, offset, sb.toString(), attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                    throws BadLocationException {
                if (text == null)
                    return;
                StringBuilder sb = new StringBuilder();
                for (char ch : text.toCharArray()) {
                    if (Character.isLetter(ch) && sb.length() < 3) {
                        sb.append(Character.toUpperCase(ch));
                    }
                }
                int newLength = fb.getDocument().getLength() - length + sb.length();
                if (newLength <= 3) {
                    super.replace(fb, offset, length, sb.toString(), attrs);
                }
            }
        });
        initialsField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    saveScore();
                }
            }
        });
        this.add(initialsField);

        saveButton = new JButton("Guardar");
        saveButton.setBounds(235, 280, 95, 28);
        saveButton.setVisible(false);
        saveButton.addActionListener(e -> saveScore());
        this.add(saveButton);

        scoreSavedLabel = new JLabel("Score guardado");
        scoreSavedLabel.setBounds(140, 340, 200, 24);
        scoreSavedLabel.setVisible(false);
        scoreSavedLabel.setForeground(Color.WHITE);
        scoreSavedLabel.setHorizontalAlignment(SwingConstants.CENTER);
        scoreSavedLabel.setFont(loadArcadeClassicFont(Font.BOLD, 14));
        this.add(scoreSavedLabel);

        // Movimiento de jugador
        this.setFocusable(true);
        this.requestFocusInWindow();

        cargarRecursos();
    }

    public void setRestartAction(Runnable restartAction) {
        this.restartAction = restartAction;
    }

    private void saveScore() {
        if (scoreSaved || model == null) {
            return;
        }

        String initials = initialsField.getText() == null ? "" : initialsField.getText().trim().toUpperCase();
        if (initials.length() == 0) {
            initials = "AAA";
        }

        if (initials.length() > 3) {
            initials = initials.substring(0, 3);
        }

        HighScoreManager.saveScore(initials, model.getScore());

        // Actualizamos inmediatamente la lista de puntajes para que se refleje al
        // guardar
        cachedTopScores = HighScoreManager.getScoreLines();

        scoreSaved = true;
        initialsField.setEnabled(false);
        saveButton.setEnabled(false);
        scoreSavedLabel.setVisible(true);
        scoreSavedLabel.setText("Score     guardado" + initials + " - " + model.getScore());
    }

    private void resetScoreState() {
        scoreSaved = false;
        initialsField.setText(" ");
        initialsField.setEnabled(true);
        initialsField.setVisible(true);
        saveButton.setEnabled(true);
        saveButton.setVisible(true);
        scoreSavedLabel.setVisible(false);
        cachedTopScores = HighScoreManager.getScoreLines(); // Carga inicial de la tabla al morir
        initialsField.requestFocusInWindow();
    }

    private void hideGameOverUi() {
        restartLabel.setVisible(false);
        initialsField.setVisible(false);
        saveButton.setVisible(false);
        scoreSavedLabel.setVisible(false);
    }

    private void cargarRecursos() {
        // 1. Carga Protagonista
        try {
            File playerFile = new File("res/protagonista.png");
            if (!playerFile.exists()) {
                playerFile = new File("src/res/protagonista.png");
            }
            playerSprite = ImageIO.read(playerFile);
            System.out.println("Sprite de jugador cargado");
        } catch (IOException e) {
            System.out.println("ERROR: No se encontró la imagen 'protagonista.png'. Revisa que esté en src/res/");
        }

        // 2. Carga bala
        try {
            File bulletFile = new File("res/bullets.png");
            if (!bulletFile.exists()) {
                bulletFile = new File("src/res/bullets.png");
            }
            bulletSprite = ImageIO.read(bulletFile);
            System.out.println("Sprite de bala de jugador cargado");
        } catch (IOException e) {
            System.out.println("ERROR: No se encontró la imagen 'bullets.png'. Revisa que esté en src/res/");
        }

        // 3. Carga bala enemiga de Jaxi
        try {
            File enemyBulletFile = new File("res/jaxibullets.png");
            if (!enemyBulletFile.exists()) {
                enemyBulletFile = new File("src/res/jaxibullets.png");
            }
            enemyBulletSprite = ImageIO.read(enemyBulletFile);
            System.out.println("Sprite de bala de jaxi cargado");
        } catch (IOException e) {
            System.out.println("ERROR: No se encontró la imagen 'jaxibullets.png'. Revisa que esté en src/res/");
        }

        // 4. Carga enemigo fuerte
        try {
            File enemyTankFile = new File("res/jaxi.png");
            if (!enemyTankFile.exists()) {
                enemyTankFile = new File("src/res/jaxi.png");
            }
            enemyTankSprite = ImageIO.read(enemyTankFile);
            System.out.println("Sprite de jaxicargado");
        } catch (IOException e) {
            System.out.println("ERROR: No se encontró la imagen 'jaxi.png'. Revisa que esté en src/res/");
        }

        // 5. Carga enemigo
        try {
            File enemyFile = new File("res/denny.png");
            if (!enemyFile.exists()) {
                enemyFile = new File("src/res/denny.png");
            }
            enemySprite = ImageIO.read(enemyFile);
            System.out.println("Sprite de denny cargado");
        } catch (IOException e) {
            System.out.println("ERROR: No se encontró la imagen 'denny.png'. Revisa que esté en src/res/");
        }

        // 6. Carga fondo
        try {
            File backgroundFile = new File("res/bg.png");
            if (!backgroundFile.exists()) {
                backgroundFile = new File("src/res/bg.png");
            }
            backgroundSprite = ImageIO.read(backgroundFile);
            System.out.println("Fondo bg.png cargado");
        } catch (IOException e) {
            System.out.println("ERROR: No se encontró la imagen 'bg.png'. Revisa que esté en src/res/");
        }

        // 7. Carga musica
        try {
            File audioFile = new File("res/Yare_arrives.wav");
            if (!audioFile.exists()) {
                audioFile = new File("src/res/Yare_arrives.wav");
            }

            if (audioFile.exists()) {
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
                backgroundClip = AudioSystem.getClip();
                backgroundClip.open(audioStream);
                backgroundClip.loop(Clip.LOOP_CONTINUOUSLY); // Loop
                backgroundClip.start();
                System.out.println("Música sonando");
            } else {
                System.out.println("ERROR: No se encontró 'Yare_arrives.wav'. Revisa la carpeta src/res/");
            }
        } catch (Exception e) {
            System.out.println("ERROR al reproducir el audio: " + e.getMessage());
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        if (backgroundSprite != null) {
            g2d.drawImage(backgroundSprite, 0, 0, getWidth(), getHeight(), null);
        } else {
            g2d.setColor(Color.BLACK);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }

        // Jugador
        PlayerModel player = model.getPlayer();
        if (playerSprite != null) {
            g2d.drawImage(playerSprite, player.getX(), player.getY(), player.getWidth(), player.getHeight(), null);
        } else {
            g2d.setColor(Color.GREEN);
            g2d.fillRect(player.getX(), player.getY(), player.getWidth(), player.getHeight());
        }

        // Balas del jugador
        for (BulletModel b : model.getBullets()) {
            if (bulletSprite != null) {
                int drawWidth = b.getWidth() + 6;
                int drawHeight = b.getHeight() + 6;
                g2d.drawImage(bulletSprite, b.getX() - 3, b.getY() - 3, drawWidth, drawHeight, null);
            } else {
                g2d.setColor(Color.YELLOW);
                g2d.fillRect(b.getX(), b.getY(), b.getWidth(), b.getHeight());
            }
        }

        // Balas del enemigo fuerte
        for (BulletModel enemyBullet : model.getEnemyBullets()) {
            if (enemyBulletSprite != null) {
                int drawWidth = enemyBullet.getWidth() + 6;
                int drawHeight = enemyBullet.getHeight() + 6;
                g2d.drawImage(enemyBulletSprite, enemyBullet.getX() - 3, enemyBullet.getY() - 3, drawWidth, drawHeight,
                        null);
            } else {
                g2d.setColor(Color.RED);
                g2d.fillRect(enemyBullet.getX(), enemyBullet.getY(), enemyBullet.getWidth(), enemyBullet.getHeight());
            }
        }

        // Enemigo fuerte
        for (EnemyTank enemyTank : model.getEnemyTanks()) {
            if (enemyTankSprite != null) {
                int drawWidth = enemyTank.getWidth() + 12;
                int drawHeight = enemyTank.getHeight() + 12;
                g2d.drawImage(enemyTankSprite, enemyTank.getX() - 6, enemyTank.getY() - 6, drawWidth, drawHeight, null);
            } else {
                g2d.setColor(Color.ORANGE);
                g2d.fillOval(enemyTank.getX(), enemyTank.getY(), enemyTank.getWidth(), enemyTank.getHeight());
            }
        }

        // Enemigos
        for (EnemyModel e : model.getEnemies()) {
            if (enemySprite != null) {
                g2d.drawImage(enemySprite, e.getX(), e.getY(), e.getWidth(), e.getHeight(), null);
            } else {
                g2d.setColor(Color.RED);
                g2d.fillRect(e.getX(), e.getY(), e.getWidth(), e.getHeight());
            }
        }

        // Puntaje
        g2d.setColor(Color.WHITE);
        g2d.setFont(loadArcadeClassicFont(Font.BOLD, 16));
        g2d.drawString("SCORE  " + model.getScore(), 20, 30);

        if (model.isGameOver()) {
            if (!wasGameOver) {
                resetScoreState();
                wasGameOver = true;
            }

            g2d.setColor(new Color(0, 0, 0, 80));
            g2d.fillRect(0, 0, getWidth(), getHeight());

            g2d.setColor(Color.WHITE);
            g2d.setFont(loadArcadeClassicFont(Font.BOLD, 30));
            String title = "You died";
            int titleWidth = g2d.getFontMetrics().stringWidth(title);
            int titleY = getHeight() / 2 - 120;
            g2d.drawString(title, (getWidth() - titleWidth) / 2, titleY);

            g2d.setFont(loadArcadeClassicFont(Font.BOLD, 22));
            String scoreText = "Score " + model.getScore();
            int scoreWidth = g2d.getFontMetrics().stringWidth(scoreText);
            int scoreY = titleY + 42;
            g2d.drawString(scoreText, (getWidth() - scoreWidth) / 2, scoreY);

            g2d.setFont(loadArcadeClassicFont(Font.PLAIN, 14));
            String subtitle = "Ingresa tus iniciales";
            int subtitleWidth = g2d.getFontMetrics().stringWidth(subtitle);
            int subtitleY = scoreY + 42;
            g2d.drawString(subtitle, (getWidth() - subtitleWidth) / 2, subtitleY);

            if (!scoreSaved) {
                initialsField.setVisible(true);
                saveButton.setVisible(true);
            }
            restartLabel.setVisible(true);
            restartLabel.setBounds((getWidth() - 160) / 2, 455, 160, 60);
            restartLabel.setIcon(restartIcon);

            this.setComponentZOrder(initialsField, 0);
            this.setComponentZOrder(saveButton, 1);
            this.setComponentZOrder(restartLabel, 2);

            g2d.setFont(loadArcadeClassicFont(Font.BOLD, 15));
            int leaderboardY = 340;
            g2d.drawString("TOP SCORES", 200, leaderboardY);

            // Dibujamos usando la lista en caché (que se actualiza al guardar)
            if (cachedTopScores != null) {
                for (int i = 0; i < cachedTopScores.size(); i++) {
                    String line = cachedTopScores.get(i);
                    g2d.drawString(line, 135, leaderboardY + 20 + (i * 18));
                }
            }
        } else {
            wasGameOver = false;
            hideGameOverUi();
        }

        g2d.dispose();
    }
}