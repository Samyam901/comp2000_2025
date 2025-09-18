import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
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
    private Grid grid;
    private Snake snake;
    private Apple apple;
    private List<Bird> backgroundBirds;
    private boolean gameOver;
    private int score;
    private int highScore;
    private Random random;
    private long lastMoveTime;
    private long moveDelay = 200; // milliseconds, starting speed
    private static final long MIN_MOVE_DELAY = 50; // maximum speed
    private static final int SPEED_INCREASE = 10; // ms faster per apple
    private List<Integer> topScores;

    public Stage() {
        grid = new Grid();
        random = new Random();
        gameOver = false;
        score = 0;
        highScore = 0;
        topScores = new ArrayList<>();
        backgroundBirds = new ArrayList<>();
        
        // Initialize background birds
        for (int i = 0; i < NUM_BACKGROUND_BIRDS; i++) {
            Cell randomCell = grid.cellAtColRow(
                random.nextInt(20),
                random.nextInt(20)
            ).get();
            backgroundBirds.add(new Bird(randomCell));
        }
        
        // Initialize snake at the center
        snake = new Snake(grid.cellAtColRow(10, 10).get());
        spawnApple();
        
        lastMoveTime = System.currentTimeMillis();
    }
    
    private void spawnApple() {
        Cell cell = null;
        do {
            int col = random.nextInt(20);
            int row = random.nextInt(20);
            Optional<Cell> optionalCell = grid.cellAtColRow(col, row);
            if (optionalCell.isPresent()) {
                cell = optionalCell.get();
            }
        } while (cell == null || snake.contains(cell));
        
        // Remove old apple if exists
        if (apple != null) {
            apples.remove(apple);
        }
        
        apple = new Apple(cell);
        apples.add(apple);
    }
    
    private void updateTopScores() {
        topScores.add(score);
        Collections.sort(topScores, Collections.reverseOrder());
        if (topScores.size() > MAX_TOP_SCORES) {
            topScores = topScores.subList(0, MAX_TOP_SCORES);
        }
        if (!topScores.isEmpty()) {
            highScore = topScores.get(0);
        }
    }
    
    public void update() {
        if (gameOver) return;
        
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastMoveTime >= moveDelay) {
            snake.move(grid);
            lastMoveTime = currentTime;
            
            // Check for wall collision
            Cell head = snake.getHead();
            if (head == null) {
                gameOver = true;
                updateTopScores();
                return;
            }
            
            // Check for self collision
            if (snake.checkCollision()) {
                gameOver = true;
                updateTopScores();
                return;
            }
            
            // Check for apple collision
            if (head.equals(apple.currentCell)) {
                score += 10;
                snake.grow();
                spawnApple();
                
                // Increase speed
                moveDelay = Math.max(MIN_MOVE_DELAY, moveDelay - SPEED_INCREASE);
            }
        }
    }
    
    public void handleKeyPress(KeyEvent e) {
        if (gameOver) return;
        
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

    public void paint(Graphics g, Point mouseLoc) {
        // Draw background
        g.setColor(new Color(50, 150, 50)); // Forest green background
        g.fillRect(0, 0, 1024, 720);
        
        grid.paint(g, mouseLoc);
        snake.paint(g);
        apple.paint(g);
        
        // Draw scoreboard
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + score, 10, 30);
        g.drawString("High Score: " + highScore, 10, 60);
        
        // Draw top scores
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        g.drawString("Top Scores:", 800, 30);
        for (int i = 0; i < topScores.size(); i++) {
            g.drawString((i + 1) + ". " + topScores.get(i), 800, 60 + (i * 25));
        }
        
        if (gameOver) {
            // Semi-transparent overlay
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, 1024, 720);
            
            // Game Over text
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
