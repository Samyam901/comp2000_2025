import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.Random;

public class Bird extends Actor {
    private static final Random random = new Random();
    private double x, y;
    private double dx, dy;
    private double wingAngle;
    private boolean wingUp;
    private static final double WING_SPEED = 0.2;
    private static final double FLIGHT_SPEED = 2.0;
    private static final int SCREEN_WIDTH = 1024;
    private static final int SCREEN_HEIGHT = 720;
    
    public Bird(Cell inLoc) {
        super(inLoc);
        this.x = inLoc.x;
        this.y = inLoc.y;
        color = new Color(random.nextInt(100, 200), random.nextInt(150, 255), random.nextInt(50, 150));
        randomizeDirection();
        display = new ArrayList<>();
        wingAngle = 0;
        wingUp = true;
        updatePolygons();
    }
    
    private void randomizeDirection() {
        dx = (random.nextDouble() * 2 - 1) * FLIGHT_SPEED;
        dy = (random.nextDouble() - 0.5) * FLIGHT_SPEED * 0.5;
    }
    
    private void updatePolygons() {
        Polygon wing1 = new Polygon();
        Polygon wing2 = new Polygon();
        Polygon body = new Polygon();
        
        int direction = dx > 0 ? 1 : -1;
        int wingOffset = (int)(Math.sin(wingAngle) * 5);
        
        body.addPoint((int)x + (15 * direction), (int)y + 10);
        body.addPoint((int)x + (20 * direction), (int)y + 10);
        body.addPoint((int)x + (20 * direction), (int)y + 25);
        body.addPoint((int)x + (15 * direction), (int)y + 25);
        
        if (direction > 0) {
            wing1.addPoint((int)x + 5, (int)y + 5 + wingOffset);
            wing1.addPoint((int)x + 15, (int)y + 17);
            wing1.addPoint((int)x + 5, (int)y + 17);
            
            wing2.addPoint((int)x + 30, (int)y + 5 + wingOffset);
            wing2.addPoint((int)x + 20, (int)y + 17);
            wing2.addPoint((int)x + 30, (int)y + 17);
        } else {
            wing1.addPoint((int)x - 5, (int)y + 5 + wingOffset);
            wing1.addPoint((int)x - 15, (int)y + 17);
            wing1.addPoint((int)x - 5, (int)y + 17);
            
            wing2.addPoint((int)x - 30, (int)y + 5 + wingOffset);
            wing2.addPoint((int)x - 20, (int)y + 17);
            wing2.addPoint((int)x - 30, (int)y + 17);
        }
        synchronized(display) {
            display.clear();
            display.add(body);
            display.add(wing1);
            display.add(wing2);
        }
    }
    
    public void update(Grid grid) {
        x += dx;
        y += dy;
        
        if (x < -50) x = SCREEN_WIDTH + 50;
        if (x > SCREEN_WIDTH + 50) x = -50;
        if (y < -50) y = SCREEN_HEIGHT + 50;
        if (y > SCREEN_HEIGHT + 50) y = -50;
        
        if (wingUp) {
            wingAngle += WING_SPEED;
            if (wingAngle > Math.PI / 4) {
                wingUp = false;
            }
        } else {
            wingAngle -= WING_SPEED;
            if (wingAngle < -Math.PI / 4) {
                wingUp = true;
            }
        }
        
        if (random.nextDouble() < 0.02) {
            randomizeDirection();
        }
        
        updatePolygons();
    }
    
    @Override
    public void paint(Graphics g) {
        ArrayList<Polygon> displayCopy;
        synchronized(display) {
            displayCopy = new ArrayList<>(display);
        }
        
        for (Polygon poly : displayCopy) {
            g.setColor(color);
            g.fillPolygon(poly);
            g.setColor(Color.BLACK);
            g.drawPolygon(poly);
        }
    }
}