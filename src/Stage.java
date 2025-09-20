import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
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
        
        if (currentDog == null && currentTime - lastDogSpawnTime >= DOG_SPAWN_DELAY) {
            spawnDog();
        }
        
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
        java.awt.Rectangle bounds = g.getClipBounds();
        if (bounds == null) {
            bounds = new java.awt.Rectangle(0, 0, 2000, 1200);
        }
        
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
        
        if (powerUps != null) {
            powerUps.paintAll(g);
        }
        
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + score, 10, 30);
        
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        g.drawString("Top Scores:", 800, 30);
        for (int i = 0; i < topScores.size(); i++) {
            g.drawString((i + 1) + ". " + topScores.get(i), 800, 60 + (i * 25));
        }
        
        if (gameOver) {
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, 1024, 720);
            
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.setColor(Color.WHITE);
            g.drawString("Game Over!", 400, 300);
            g.setFont(new Font("Arial", Font.BOLD, 24));
            g.drawString("Score: " + score, 430, 350);
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.drawString("Press SPACE to play again", 380, 400);
        }
    }
}
