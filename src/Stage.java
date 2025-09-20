import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Stage {
    private static final int MAX_TOP_SCORES = 5;
    private static final int NUM_BACKGROUND_BIRDS = 5;
    private static final long MIN_MOVE_DELAY = 50;
    private static final int SPEED_INCREASE = 10;
    private static final long DOG_SPAWN_DELAY = 15000; // Spawn a new dog every 15 seconds
    
    private Grid grid;
    private Snake snake;
    private Apple apple;
    private GameCollection<Bird> backgroundBirds;
    private GameCollection<PowerUpActor> powerUps;
    private boolean gameOver;
    private int score;
    private Random random;
    private long lastMoveTime;
    private long lastDogSpawnTime;
    private Dog currentDog;
    private static long moveDelay = 200;
    private List<Integer> topScores;
    
    public static long getMoveDelay() {
        return moveDelay;
    }
    
    public static void setMoveDelay(long delay) {
        moveDelay = Math.max(MIN_MOVE_DELAY, delay);
    }

    public Stage() {
        grid = new Grid();
        random = new Random();
        gameOver = false;
        score = 0;
        topScores = new ArrayList<>();
        backgroundBirds = new GameCollection<>(Bird.class);
        powerUps = new GameCollection<>(PowerUpActor.class);
        
        for (int i = 0; i < NUM_BACKGROUND_BIRDS; i++) {
            Cell randomCell = grid.cellAtColRow(
                random.nextInt(20),
                random.nextInt(20)
            ).get();
            backgroundBirds.add(new Bird(randomCell));
        }
        
        snake = new Snake(grid.cellAtColRow(10, 10).get());
        spawnApple();
        
        lastMoveTime = System.currentTimeMillis();
        lastDogSpawnTime = System.currentTimeMillis();
    }
    
    private void spawnApple() {
        Cell cell = getRandomEmptyCell();
        apple = new Apple(cell);
    }
    
    private void spawnDog() {
        Cell cell = getRandomEmptyCell();
        Dog dog = new Dog(cell);
        powerUps.add(dog);
        lastDogSpawnTime = System.currentTimeMillis();
    }
    
    private Cell getRandomEmptyCell() {
        Cell cell = null;
        do {
            int col = random.nextInt(20);
            int row = random.nextInt(20);
            Optional<Cell> optionalCell = grid.cellAtColRow(col, row);
            if (optionalCell.isPresent()) {
                cell = optionalCell.get();
            }
        } while (cell == null || snake.contains(cell) || 
                (apple != null && cell.equals(apple.currentCell)) ||
                (currentDog != null && cell.equals(currentDog.currentCell)));
        
        return cell;
    }
    
    private void updateTopScores() {
        topScores.add(score);
        Collections.sort(topScores, Collections.reverseOrder());
        if (topScores.size() > MAX_TOP_SCORES) {
            topScores = topScores.subList(0, MAX_TOP_SCORES);
        }
        // Keep the list sorted and trimmed
        if (topScores.size() > MAX_TOP_SCORES) {
            topScores = topScores.subList(0, MAX_TOP_SCORES);
        }
    }
    
    public void update() {
        if (gameOver) return;
        
        if (backgroundBirds != null) {
            for (Bird bird : backgroundBirds) {
                bird.update(grid);
            }
        }
        
        long currentTime = System.currentTimeMillis();
        
        // Check if it's time to spawn a new dog
        if (currentDog == null && currentTime - lastDogSpawnTime >= DOG_SPAWN_DELAY) {
            spawnDog();
        }
        
        // Check if current dog should expire
        if (currentDog != null && currentDog.isExpired()) {
            currentDog = null;
        }
        
        if (currentTime - lastMoveTime >= moveDelay) {
            snake.move(grid);
            lastMoveTime = currentTime;
            
            Cell head = snake.getHead();
            if (head == null) {
                gameOver = true;
                updateTopScores();
                return;
            }
            
            if (snake.checkCollision()) {
                gameOver = true;
                updateTopScores();
                return;
            }
            
            if (head.equals(apple.currentCell)) {
                score += 10;
                snake.grow();
                spawnApple();
                moveDelay = Math.max(MIN_MOVE_DELAY, moveDelay - SPEED_INCREASE);
            }
            
            // Check for power-up collisions
            if (powerUps != null) {
                powerUps.getAll().removeIf(powerUp -> {
                    if (powerUp.isExpired()) {
                        powerUps.remove(powerUp);
                        return true;
                    }
                    if (head.equals(powerUp.currentCell)) {
                        score += powerUp.getPointsValue();
                        powerUp.applyEffect(snake);
                        powerUps.remove(powerUp);
                        return true;
                    }
                    return false;
                });
            }
            
            if (currentDog != null && head.equals(currentDog.currentCell)) {
                score += Dog.BONUS_POINTS;
                currentDog = null;
                lastDogSpawnTime = currentTime;
            }
        }
    }
    
    public void handleKeyPress(KeyEvent e) {
        if (gameOver) {
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                resetGame();
            }
            return;
        }
        
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                snake.setDirection(Snake.Direction.UP);
                break;
            case KeyEvent.VK_DOWN:
                snake.setDirection(Snake.Direction.DOWN);
                break;
            case KeyEvent.VK_LEFT:
                snake.setDirection(Snake.Direction.LEFT);
                break;
            case KeyEvent.VK_RIGHT:
                snake.setDirection(Snake.Direction.RIGHT);
                break;
        }
    }
    
    /**
     * Interpolate between two colors
     * @param c1 First color
     * @param c2 Second color
     * @param fraction Fraction between 0 and 1
     * @return Interpolated color
     */
    private Color interpolateColor(Color c1, Color c2, float fraction) {
        float r = c1.getRed() + (c2.getRed() - c1.getRed()) * fraction;
        float g = c1.getGreen() + (c2.getGreen() - c1.getGreen()) * fraction;
        float b = c1.getBlue() + (c2.getBlue() - c1.getBlue()) * fraction;
        return new Color(r/255f, g/255f, b/255f);
    }

    private void resetGame() {
        snake = new Snake(grid.cellAtColRow(10, 10).get());
        gameOver = false;
        score = 0;
        moveDelay = 200;
        currentDog = null;
        backgroundBirds.clear();
        powerUps.clear();
        
        for (int i = 0; i < NUM_BACKGROUND_BIRDS; i++) {
            Cell randomCell = grid.cellAtColRow(
                random.nextInt(20),
                random.nextInt(20)
            ).get();
            backgroundBirds.add(new Bird(randomCell));
        }
        
        spawnApple();
        lastMoveTime = System.currentTimeMillis();
        lastDogSpawnTime = System.currentTimeMillis();
    }

    public void paint(Graphics g, Point mouseLoc) {
        // Get the size of the window
        java.awt.Rectangle bounds = g.getClipBounds();
        if (bounds == null) {
            bounds = new java.awt.Rectangle(0, 0, 2000, 1200);
        }
        
        // Create sunset gradient background
        Graphics2D g2d = (Graphics2D) g;
        GradientPaint gradient = new GradientPaint(
            0, 0, new Color(135, 206, 235),    // Sky blue at top
            0, bounds.height, new Color(255, 190, 150)  // Soft sunset orange at bottom
        );
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, bounds.width, bounds.height);
        
        for (Bird bird : backgroundBirds) {
            bird.paint(g);
        }
        
        grid.paint(g, mouseLoc);
        snake.paint(g);
        apple.paint(g);
        
        // Paint all power-ups
        if (powerUps != null) {
            powerUps.paintAll(g);
        }
        
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + score, 10, 30);
        
        // Draw top scores at the right edge
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        String topScoresHeader = "Top Scores:";
        java.awt.FontMetrics fm = g.getFontMetrics();
        int rightMargin = 20; // Space from right edge
        
        int x = bounds.width - fm.stringWidth(topScoresHeader) - rightMargin;
        g.drawString(topScoresHeader, x, 30);
        
        // Find the widest score text to align all scores
        int maxWidth = 0;
        String[] scoreTexts = new String[topScores.size()];
        for (int i = 0; i < topScores.size(); i++) {
            scoreTexts[i] = (i + 1) + ". " + topScores.get(i);
            maxWidth = Math.max(maxWidth, fm.stringWidth(scoreTexts[i]));
        }
        
        x = bounds.width - maxWidth - rightMargin;
        for (int i = 0; i < scoreTexts.length; i++) {
            g.drawString(scoreTexts[i], x, 60 + (i * 25));
        }
        
        if (gameOver) {
            // Semi-transparent dark overlay
            g.setColor(new Color(0, 0, 0, 180));
            g.fillRect(0, 0, bounds.width, bounds.height);
            
            int centerX = bounds.width / 2;
            int centerY = bounds.height / 2;
            
            // Draw decorative box with gradient and rounded corners
            int boxWidth = 500;
            int boxHeight = 300;
            int boxX = centerX - boxWidth/2;
            int boxY = centerY - boxHeight/2;
            int cornerRadius = 20;
            
            // Save the original state
            java.awt.Composite originalComposite = g2d.getComposite();
            
            // Draw the background with gradient
            GradientPaint boxGradient = new GradientPaint(
                boxX, boxY, new Color(40, 40, 40, 230),
                boxX, boxY + boxHeight, new Color(20, 20, 20, 230)
            );
            g2d.setPaint(boxGradient);
            g2d.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, 0.95f));
            g2d.fillRoundRect(boxX, boxY, boxWidth, boxHeight, cornerRadius, cornerRadius);
            
            // Add subtle inner glow
            g2d.setColor(new Color(255, 255, 255, 30));
            g2d.setStroke(new java.awt.BasicStroke(2));
            g2d.drawRoundRect(boxX + 3, boxY + 3, boxWidth - 6, boxHeight - 6, cornerRadius - 2, cornerRadius - 2);
            
            // Draw border with gradient
            GradientPaint borderGradient = new GradientPaint(
                boxX, boxY, new Color(255, 255, 255, 100),
                boxX, boxY + boxHeight, new Color(255, 255, 255, 50)
            );
            g2d.setPaint(borderGradient);
            g2d.setStroke(new java.awt.BasicStroke(2));
            g2d.drawRoundRect(boxX, boxY, boxWidth, boxHeight, cornerRadius, cornerRadius);
            
            // Restore original composite
            g2d.setComposite(originalComposite);
            
            // Game Over text with glow effect
            String gameOverText = "Game Over!";
            g.setFont(new Font("Arial", Font.BOLD, 60));
            FontMetrics gameOverFm = g.getFontMetrics();
            int gameOverWidth = gameOverFm.stringWidth(gameOverText);
            
            // Draw multiple layers for glow effect
            for (int i = 4; i > 0; i--) {
                g2d.setColor(new Color(255, 50, 50, 50/i));
                g2d.drawString(gameOverText, centerX - gameOverWidth/2 + i, centerY - 50 + i);
                g2d.drawString(gameOverText, centerX - gameOverWidth/2 - i, centerY - 50 - i);
            }
            
            // Main text
            g2d.setColor(Color.WHITE);
            g2d.drawString(gameOverText, centerX - gameOverWidth/2, centerY - 50);
            
            // Score with modern style
            g.setFont(new Font("Arial", Font.BOLD, 32));
            String scoreText = "Final Score: " + score;
            int textWidth = fm.stringWidth(scoreText);
            
            // Score shadow
            g2d.setColor(new Color(0, 0, 0, 80));
            g2d.drawString(scoreText, centerX - textWidth/2 + 2, centerY + 20);
            
            // Score text with gradient
            GradientPaint textGradient = new GradientPaint(
                0, centerY + 20 - fm.getAscent(), Color.WHITE,
                0, centerY + 20, new Color(200, 200, 200)
            );
            g2d.setPaint(textGradient);
            g2d.drawString(scoreText, centerX - textWidth/2, centerY + 20);
            
            // High score notification with animation
            if (topScores.size() > 0 && score > topScores.get(0)) {
                g.setFont(new Font("Arial", Font.BOLD, 28));
                String highScoreText = "New High Score!";
                textWidth = fm.stringWidth(highScoreText);
                
                // Animate color
                float pulse = (float)(Math.sin(System.currentTimeMillis() / 200.0) + 1) / 2;
                Color goldColor = new Color(255, 215, 0);
                Color orangeColor = new Color(255, 140, 0);
                g2d.setColor(interpolateColor(goldColor, orangeColor, pulse));
                
                // Draw with glow
                for (int i = 3; i > 0; i--) {
                    g2d.setColor(new Color(255, 215, 0, 50/i));
                    g2d.drawString(highScoreText, centerX - textWidth/2 + i, centerY + 65);
                }
                g2d.drawString(highScoreText, centerX - textWidth/2, centerY + 65);
            }
            
            // Press SPACE prompt with smooth pulsing effect
            g.setFont(new Font("Arial", Font.BOLD, 24));
            String promptText = "Press SPACE to play again";
            textWidth = fm.stringWidth(promptText);
            float alpha = (float)(Math.sin(System.currentTimeMillis() / 400.0) + 1) / 2;
            g2d.setColor(new Color(1f, 1f, 1f, 0.4f + alpha * 0.6f));
            g2d.drawString(promptText, centerX - textWidth/2, centerY + 120);
        }
    }
}
