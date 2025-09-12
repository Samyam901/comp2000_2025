import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.util.List;

public abstract class Actor {
  protected Color color;
  protected Cell currentCell;
  protected List<Polygon> display;

  public Actor(Cell startCell) {
      this.currentCell = startCell;
  }

  public void paint(Graphics g) {
    for(Polygon p: display) {
      g.setColor(color);
      g.fillPolygon(p);
      g.setColor(Color.GRAY);
      g.drawPolygon(p);
    }
  }
}
