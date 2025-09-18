import java.awt.Graphics;
import java.awt.Point;
import java.util.Optional;

public class Grid {
  Cell[][] cells = new Cell[20][20];
  private static final double GRID_SIZE_RATIO = 0.8; // Grid will take up 80% of the smaller window dimension
  private static final int MIN_MARGIN = 20; // Minimum margin from the window edges
  private int startX;
  private int startY;
  
  public Grid() {
    // Initialize with default positions
    for(int i=0; i<cells.length; i++) {
      for(int j=0; j<cells[i].length; j++) {
        cells[i][j] = new Cell(colToLabel(i), j, MIN_MARGIN + Cell.size*i, MIN_MARGIN + Cell.size*j);
      }
    }
  }
  
  public void updateGridMetrics(int windowWidth, int windowHeight) {
    // Calculate the maximum grid size that will fit in the window with proper margins
    int maxWidth = (int)(windowWidth * GRID_SIZE_RATIO);
    int maxHeight = (int)(windowHeight * GRID_SIZE_RATIO);
    int gridSize = Math.min(maxWidth, maxHeight);
    
    // Calculate new cell size based on grid size
    int cellSize = gridSize / cells.length;
    Cell.setCellSize(cellSize);
    
    // Center the grid
    startX = (windowWidth - (cellSize * cells.length)) / 2;
    startY = (windowHeight - (cellSize * cells.length)) / 2;
    
    // Update cell positions and sizes
    for(int i=0; i<cells.length; i++) {
      for(int j=0; j<cells[i].length; j++) {
        cells[i][j].x = startX + cellSize*i;
        cells[i][j].y = startY + cellSize*j;
        cells[i][j].resize(cellSize);
      }
    }
  }

  private char colToLabel(int col) {
    return (char) (col + Character.valueOf('A'));
  }

  private int labelToCol(char col) {
    return (int) (col - Character.valueOf('A'));
  }

  public void paint(Graphics g, Point mousePos) {
    // Get the current window size from graphics clip bounds
    java.awt.Rectangle bounds = g.getClipBounds();
    if (bounds != null) {
      updateGridMetrics(bounds.width, bounds.height);
    }
    
    for(int i=0; i<cells.length; i++) {
      for(int j=0; j<cells[i].length; j++) {
        cells[i][j].paint(g, mousePos);
      }
    }
  }

  public Optional<Cell> cellAtColRow(int c, int r) {
    if(c >= 0 && c < cells.length && r >=0 && r < cells[c].length) {
      return Optional.of(cells[c][r]);
    } else {
      return Optional.empty();
    }
  }

  public Optional<Cell> cellAtColRow(char c, int r) {
    return cellAtColRow(labelToCol(c), r);
  }

  public Optional<Cell> cellAtPoint(Point p) {
    for(int i=0; i < cells.length; i++) {
      for(int j=0; j < cells[i].length; j++) {
        if(cells[i][j].contains(p)) {
          return Optional.of(cells[i][j]);
        }
      }
    }
    return Optional.empty();
  }
}
