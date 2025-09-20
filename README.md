# Snake Game with AI Characters

A Java-based snake game featuring AI-controlled characters including birds, cats, and dogs. The game demonstrates object-oriented programming principles including inheritance, generics, and polymorphism, along with advanced Java graphics capabilities.

## Technical Requirements
- Java Development Kit (JDK) 21 or higher (tested on OpenJDK 21.0.6 LTS)
- Screen resolution of at least 1024x720
- Graphics card supporting Java2D acceleration (recommended)

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
   The game utilizes inheritance to create a flexible and maintainable actor system:
   - `Actor` base class provides common functionality like position, movement, and rendering
   - `PowerUpActor` abstract class extends `Actor` to add power-up specific behaviors:
     * Defines abstract methods like `applyEffect()` and `getPointsValue()`
     * Implements common power-up logic like expiration timing
     * Enforces consistent power-up behavior across subclasses
   - `Snake`, `Bird` extend `Actor` with specialized behaviors:
     * `Bird` adds wing animation and flight patterns
     * `Snake` implements growing, collision detection, and direction control
   - `Cat`, `Dog` extend `PowerUpActor` with unique effects:
     * `Cat` implements speed boost effects
     * `Dog` implements bonus point mechanics
   This inheritance structure contributed to good design by:
   - Reducing code duplication through shared base functionality
   - Ensuring consistent behavior through abstract methods
   - Allowing easy addition of new actors and power-ups
   - Enabling polymorphic handling of different actor types

2. **Generic Collections**:
   Generics improved the design through type-safe and reusable collections:
   - `GameCollection<T extends Actor>` provides type-safe actor management:
     * Ensures only valid actor types can be added
     * Prevents runtime type errors through compile-time checking
     * Enables reuse for different actor types (birds, power-ups)
   - Type constraints enforce proper usage:
     * `T extends Actor` ensures all collection elements have actor capabilities
     * Collection methods preserve type information
     * Iterator implementation maintains type safety
   This generic approach contributed to good design by:
   - Eliminating need for type casting
   - Catching type errors at compile time
   - Providing reusable collection functionality
   - Enabling type-specific operations without code duplication

3. **Grid System**:
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

## Technical Architecture

### Core Components
1. **Graphics Pipeline**:
   - Java2D rendering with hardware acceleration
   - Double-buffering for smooth animations
   - Optimized composite operations
   - Advanced gradient and transparency effects

2. **Collection Management**:
   - Type-safe generic collections
   - Efficient entity lifecycle management
   - Iterator pattern implementation
   - Thread-safe operation

3. **Power-up System**:
   - Abstract factory pattern for power-up creation
   - Composite pattern for effect management
   - Observer pattern for state changes
   - Chain of responsibility for collision handling

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
- Top 5 scores tracking with right-aligned display
- Real-time score updates during gameplay
- High score celebration effects

### Visual Effects
1. **Game Over Screen**:
   - Smooth gradient overlay backdrop
   - Modern rounded-corner dialog box with inner glow
   - Dynamic text effects with shadows and gradients
   - Animated high score celebration
   - Pulsing "Press SPACE" prompt
   - Professional typography and layout

2. **Background**:
   - Dynamic sunset gradient background
   - Smooth color transitions
   - Optimized rendering performance

3. **Power-up Effects**:
   - Fade-in/fade-out animations
   - Visual duration indicators
   - Particle effects for collection

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
