import java.awt.Color;
import java.awt.Polygon;
import java.awt.Graphics;
import java.util.ArrayList;

public class Cat extends PowerUpActor {
    private static final Color CAT_COLOR = new Color(138, 43, 226); // Purple for speed boost
    private static final int SPEED_BOOST_DURATION = 5000; // 5 seconds
    private static final int SPEED_BOOST_AMOUNT = 50; // Reduce delay by 50ms
    private static final int BONUS_POINTS = 20; // Points awarded for collecting
    
    public Cat(Cell inLoc) {
        super(inLoc);
        color = CAT_COLOR;
        display = new ArrayList<>();
        updateDisplay();
    }
    
    private void updateDisplay() {
        Polygon ear1 = new Polygon();
        ear1.addPoint(currentCell.x + 11, currentCell.y + 5);
        ear1.addPoint(currentCell.x + 15, currentCell.y + 15);
        ear1.addPoint(currentCell.x + 7, currentCell.y + 15);
        
        Polygon ear2 = new Polygon();
        ear2.addPoint(currentCell.x + 22, currentCell.y + 5);
        ear2.addPoint(currentCell.x + 26, currentCell.y + 15);
        ear2.addPoint(currentCell.x + 18, currentCell.y + 15);
        
        Polygon face = new Polygon();
        face.addPoint(currentCell.x + 5, currentCell.y + 15);
        face.addPoint(currentCell.x + 29, currentCell.y + 15);
        face.addPoint(currentCell.x + 17, currentCell.y + 30);
        
        display.add(face);
        display.add(ear1);
        display.add(ear2);
    }
    
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        long currentTime = System.currentTimeMillis();
        if (currentTime - spawnTime > 10000) {
            float alpha = (float)(Math.sin((currentTime - spawnTime) / 200.0) + 1) / 2;
            g.setColor(new Color(color.getRed()/255f, color.getGreen()/255f, 
                               color.getBlue()/255f, alpha));
            for (Polygon shape : display) {
                g.fillPolygon(shape);
            }
        }
    }
    
    @Override
    public void applyEffect(Snake snake) {
        // Store current move delay and apply speed boost
        long currentDelay = Stage.getMoveDelay();
        Stage.setMoveDelay(Math.max(50, currentDelay - SPEED_BOOST_AMOUNT));
    }
    
    @Override
    public long getEffectDuration() {
        return SPEED_BOOST_DURATION;
    }
    
    @Override
    public int getPointsValue() {
        return BONUS_POINTS;
    }
    
    @Override
    public String getEffectDescription() {
        return "Speed Boost: Move " + SPEED_BOOST_AMOUNT + "ms faster for " + (SPEED_BOOST_DURATION/1000) + " seconds!";
    }
    
    @Override
    protected long getExpirationTime() {
        return SPEED_BOOST_DURATION * 3; // 15 seconds until expiration (3x the effect duration)
    }
}
