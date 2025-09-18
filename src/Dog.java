import java.awt.Color;
import java.awt.Polygon;
import java.awt.Graphics;
import java.util.ArrayList;

public class Dog extends Actor {
    private static final Color DOG_COLOR = new Color(255, 215, 0); // Golden for bonus points
    private long spawnTime;
    public static final int BONUS_POINTS = 50;
    
    public Dog(Cell inLoc) {
        super(inLoc);
        this.spawnTime = System.currentTimeMillis();
        color = DOG_COLOR;
        display = new ArrayList<>();
        updateDisplay();
    }
    
    private void updateDisplay() {
        Polygon ear1 = new Polygon();
        ear1.addPoint(currentCell.x + 5, currentCell.y + 5);
        ear1.addPoint(currentCell.x + 15, currentCell.y + 5);
        ear1.addPoint(currentCell.x + 5, currentCell.y + 15);
        
        Polygon ear2 = new Polygon();
        ear2.addPoint(currentCell.x + 20, currentCell.y + 5);
        ear2.addPoint(currentCell.x + 30, currentCell.y + 5);
        ear2.addPoint(currentCell.x + 30, currentCell.y + 15);
        
        Polygon face = new Polygon();
        face.addPoint(currentCell.x + 8, currentCell.y + 7);
        face.addPoint(currentCell.x + 27, currentCell.y + 7);
        face.addPoint(currentCell.x + 27, currentCell.y + 25);
        face.addPoint(currentCell.x + 8, currentCell.y + 25);
        
        display.add(face);
        display.add(ear1);
        display.add(ear2);
    }
    
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        long currentTime = System.currentTimeMillis();
        if (currentTime - spawnTime > 8000) {
            float alpha = (float)(Math.sin((currentTime - spawnTime) / 200.0) + 1) / 2;
            g.setColor(new Color(color.getRed()/255f, color.getGreen()/255f, 
                               color.getBlue()/255f, alpha));
        }
    }
    
    public boolean isExpired() {
        return System.currentTimeMillis() - spawnTime > 12000; // Disappear after 12 seconds
    }
}
