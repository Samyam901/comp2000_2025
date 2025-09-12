import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class Snake extends Actor {
    private List<Cell> body;
    private Direction direction;
    private boolean growing;
    
    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }
    
    public Snake(Cell startCell) {
        super(startCell);
        body = new ArrayList<>();
        body.add(startCell);
        direction = Direction.RIGHT;
        growing = false;
    }
    
    public Cell getHead() {
        return body.get(0);
    }
    
    public void move(Grid grid) {
        Cell head = body.get(0);
        Cell nextCell = null;
        
        switch (direction) {
            case UP:
                nextCell = grid.cellAtColRow(head.col, head.row - 1).orElse(null);
                break;
            case DOWN:
                nextCell = grid.cellAtColRow(head.col, head.row + 1).orElse(null);
                break;
            case LEFT:
                nextCell = grid.cellAtColRow((char)(head.col - 1), head.row).orElse(null);
                break;
            case RIGHT:
                nextCell = grid.cellAtColRow((char)(head.col + 1), head.row).orElse(null);
                break;
        }
        
        if (nextCell != null) {
            body.add(0, nextCell);
            if (!growing) {
                body.remove(body.size() - 1);
            }
            growing = false;
            currentCell = nextCell;
        }
    }
    
    public void setDirection(Direction newDirection) {
        // Prevent 180-degree turns
        if ((direction == Direction.UP && newDirection != Direction.DOWN) ||
            (direction == Direction.DOWN && newDirection != Direction.UP) ||
            (direction == Direction.LEFT && newDirection != Direction.RIGHT) ||
            (direction == Direction.RIGHT && newDirection != Direction.LEFT)) {
            direction = newDirection;
        }
    }
    
    public boolean checkCollision() {
        Cell head = body.get(0);
        // Check if snake collides with itself
        for (int i = 1; i < body.size(); i++) {
            if (head.equals(body.get(i))) {
                return true;
            }
        }
        return false;
    }
    
    public void grow() {
        growing = true;
    }
    
    public boolean contains(Cell cell) {
        return body.contains(cell);
    }
    
    @Override
    public void paint(Graphics g) {
        g.setColor(Color.GREEN);
        for (Cell cell : body) {
            g.fillRect(cell.x + 2, cell.y + 2, cell.size - 4, cell.size - 4);
        }
        // Paint head in darker green
        Cell head = body.get(0);
        g.setColor(Color.GREEN.darker());
        g.fillRect(head.x + 2, head.y + 2, head.size - 4, head.size - 4);
    }
}
