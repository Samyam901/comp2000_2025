# Snake Game with AI Characters

A Java-based snake game featuring AI-controlled characters including birds, cats, and dogs. The game demonstrates object-oriented programming principles including inheritance, generics, and polymorphism.

## Game Features

- Classic snake gameplay with modern twists
- AI-controlled birds that fly across the screen with realistic wing animations
- Special characters:
  - Cats (purple) - Provide speed boosts
  - Dogs (golden) - Award bonus points
  - Birds (random colors) - Create dynamic background atmosphere
- Score tracking system with top 5 scores
- Responsive grid system that scales with window size

## Architecture

The game uses several key design patterns and OOP concepts:

1. **Inheritance Hierarchy**:
   - `Actor` base class
   - `Snake`, `Bird`, `Cat`, and `Dog` subclasses
   - Each actor has unique behaviors and rendering

2. **Grid System**:
   - `Grid` manages the game board
   - Uses `Optional<Cell>` for safe cell access
   - Responsive scaling based on window size

## Compilation Instructions

### Using Visual Studio Code

1. Open the project in Visual Studio Code
2. Install the "Extension Pack for Java" if you haven't already
3. Click the "Run" button above the `main` method in `Main.java`

### Using Command Line

1. Navigate to the project directory
2. Compile all Java files:
```sh
javac src/*.java
```
3. Run the game:
```sh
java -cp src Main
```

## Controls

- Arrow keys to control snake direction
- Space bar to restart after game over
- Close window to exit

## Requirements

- Java Development Kit (JDK) 11 or higher
- Screen resolution of at least 1024x720

## Implementation Details

### Power-up System
1. **Cat Power-up (Speed Boost)**
   - Appears as a purple cat
   - Increases snake movement speed temporarily
   - Visual effects indicate power-up duration
   - Reappears periodically

2. **Dog Power-up (Bonus Points)**
   - Appears as a golden dog
   - Awards 50 bonus points when collected
   - Visual pulsing effect after 8 seconds
   - Disappears after 12 seconds if not collected
   - Spawns every 15 seconds

3. **Bird System**
   - Multiple birds with unique colors
   - Animated wing movement
   - Random flight patterns
   - Creates dynamic background movement

### Grid System
- Responsive design that scales with window size
- Maintains square aspect ratio
- Centered in window
- Clean cell-based collision detection

### Score System
- Basic points for collecting apples
- Bonus points from Dog power-ups
- Top 5 scores tracking
- Score display during gameplay

## Design Decisions

1. **Power-up Implementation**: 
   - Used inheritance from Actor class for consistency
   - Implemented timed appearance/disappearance for balance
   - Added visual feedback for better user experience

2. **Grid Design**:
   - Created scalable grid system for different screen sizes
   - Used cell-based movement for precise collision detection
   - Implemented optional pattern for safe cell access

3. **Animation System**:
   - Smooth bird animations using coordinated movement
   - Power-up visual effects for clear status indication
   - Efficient repaint system for performance
