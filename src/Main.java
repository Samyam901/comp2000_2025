import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main extends JFrame {
    public static void main(String[] args) throws Exception {
        Main window = new Main();
        window.run();
    }

    class Canvas extends JPanel {
        Stage stage = new Stage();
        
        public Canvas() {
            // Get screen dimensions but leave room for window borders
            Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
            // Set window slightly smaller than screen to account for window borders
            setPreferredSize(new Dimension(screenSize.width - 50, screenSize.height - 50));
            setFocusable(true);
            addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    stage.handleKeyPress(e);
                }
            });
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            stage.paint(g, getMousePosition());
        }
    }

    private Canvas canvas;

    private Main() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        canvas = new Canvas();
        this.setContentPane(canvas);
        this.pack();
        this.setLocationRelativeTo(null); // Center on screen
        this.setVisible(true);
    }

    public void run() {
        while (true) {
            canvas.stage.update();
            repaint();
            try {
                Thread.sleep(16); // Approx 60 FPS
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
