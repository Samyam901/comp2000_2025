import java.awt.Color;
import java.awt.Graphics;

public class Apple extends Actor {
    public Apple(Cell cell) {
        super(cell);
    }
    
    @Override
    public void paint(Graphics g) {
        g.setColor(Color.RED);
        g.fillOval(currentCell.x + 5, currentCell.y + 5, currentCell.size - 10, currentCell.size - 10);
    }
}
