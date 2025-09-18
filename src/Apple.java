import java.awt.Color;
import java.awt.Graphics;

public class Apple extends Actor {
    public Apple(Cell cell) {
        super(cell);
    }
    
    @Override
    public void paint(Graphics g) {
        g.setColor(Color.RED);
        int cellSize = Cell.getCellSize(); // Get current cell size
        g.fillOval(currentCell.x + 5, currentCell.y + 5, cellSize - 10, cellSize - 10);
    }
}
