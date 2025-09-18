import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.Random;

public class Bird extends Actor {
    private static final Random random = new Random();
    private double x, y; // Floating point position for smooth movement
    private double dx, dy; // Movement direction
    private double wingAngle; // For wing animation
    private boolean wingUp; // Direction of wing movement
    private static final double WING_SPEED = 0.2;
    private static final double FLIGHT_SPEED = 2.0;
    
    public Bird(Cell inLoc) {
        super(inLoc);
        this.x = inLoc.x;
        this.y = inLoc.y;
        color = new Color(119, 187, 63); // Light green color
        randomizeDirection();
        display = new ArrayList<>();
        updatePolygons();
    }
    
    private void randomizeDirection() {
        // Random direction but mainly horizontal
        dx = (random.nextDouble() * 2 - 1) * FLIGHT_SPEED;
        dy = (random.nextDouble() - 0.5) * FLIGHT_SPEED * 0.5;
    }
    
    private void updatePolygons() {
        display.clear();
        
        // Create wing polygons with animation
        Polygon wing1 = new Polygon();
        Polygon wing2 = new Polygon();
        Polygon body = new Polygon();
        
        // Body
        body.addPoint((int)x + 15, (int)y + 10);
        body.addPoint((int)x + 20, (int)y + 10);
        body.addPoint((int)x + 20, (int)y + 25);
        body.addPoint((int)x + 15, (int)y + 25);
        
        // Wings with animation
        int wingOffset = (int)(Math.sin(wingAngle) * 5);
        
        wing1.addPoint((int)x + 5, (int)y + 5 + wingOffset);
        wing1.addPoint((int)x + 15, (int)y + 17);
        wing1.addPoint((int)x + 5, (int)y + 17);
        
        wing2.addPoint((int)x + 30, (int)y + 5 + wingOffset);
        wing2.addPoint((int)x + 20, (int)y + 17);
        wing2.addPoint((int)x + 30, (int)y + 17);
        
        display.add(body);
        display.add(wing1);
        display.add(wing2);
        
        // Update current cell position for collision detection
        if (currentCell != null) {
            currentCell = grid.cellAtPoint(new Point((int)x, (int)y)).orElse(currentCell);
        }
    }
    
    public void update(Grid grid) {
        // Update position
        x += dx;
        y += dy;
        
        // Wrap around screen
        if (x < -30) x = grid.cells[grid.cells.length-1][0].x + 30;
        if (x > grid.cells[grid.cells.length-1][0].x + 30) x = -30;
        if (y < 0) dy = Math.abs(dy);
        if (y > grid.cells[0][grid.cells[0].length-1].y) dy = -Math.abs(dy);
        
        // Animate wings
        if (wingUp) {
            wingAngle += WING_SPEED;
            if (wingAngle > Math.PI/4) wingUp = false;
        } else {
            wingAngle -= WING_SPEED;
            if (wingAngle < -Math.PI/4) wingUp = true;
        }
        
        // Occasionally change direction
        if (random.nextDouble() < 0.02) { // 2% chance per update
            randomizeDirection();
        }
        
        updatePolygons();
    }
    
    @Override
    public void paint(Graphics g) {
        g.setColor(color);
        for(Polygon p: display) {
            g.fillPolygon(p);
        }
    }
}
