import controllers.GameController;
import controllers.InputController;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import models.WorldModel;
import views.GamePanel;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Yare Invasion");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
            frame.setResizable(false);

            WorldModel model = new WorldModel();
            GamePanel panel = new GamePanel(model);
            InputController inputController = new InputController();
            GameController controller = new GameController(model, panel, inputController);

            panel.setRestartAction(controller::restartGame);

            frame.add(panel);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            controller.startGame();
        });
    }
}
