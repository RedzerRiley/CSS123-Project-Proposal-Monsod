# CSS123P Project Proposal: Speed Typing Game

**Made by: Redzer Riley Monsod**

## 📖 Overview

A comprehensive typing speed game built with Java Swing that challenges players to type sentences correctly within a time limit. The game features multiple difficulty levels, progressive speed increases, and a persistent high score system.

## 🎮 Game Features

### Core Gameplay
- **Three Difficulty Levels:**
  - **Easy**: 45 seconds initial time, simple sentences
  - **Medium**: 30 seconds initial time, moderate complexity sentences
  - **Hard**: 20 seconds initial time, complex technical sentences

- **Dynamic Scoring System:**
  - +10 points for each correct sentence
  - +5 seconds added to timer for correct answers
  - 3 lives per game session

- **Progressive Difficulty:**
  - Speed increases every 5 rounds
  - Timer reduces by 2 seconds every 5 rounds (minimum 5 seconds)
  - Unlimited rounds until time runs out or lives are lost

### User Interface
- **Start Menu**: Player name input with validation and difficulty selection
- **Game Panel**: Real-time display of round, score, lives, and timer
- **Game Over Screen**: Final statistics, player ranking, and high scores table



## 🔧 Technical Implementation

### Key Classes and Responsibilities

#### `Game.java`
- Application entry point
- Initializes the start menu using SwingUtilities

#### `StartMenu.java`
- Player name input with focus management and validation
- Radio button difficulty selection (Easy as default)
- Error handling for empty player names
- Professional layout with instructions

#### `GamePanel.java`
- Main game loop implementation
- Real-time timer management with callback interface
- Input validation and feedback system
- Dynamic UI updates (color-coded timer, lives display)
- Round progression and speed adjustment logic

#### `GameOverFrame.java`
- Final score display and statistics
- Player ranking calculation
- Top 10 high scores table with current player highlighting
- New high score celebration
- Navigation options (Try Again, Exit)

#### `Player.java`
- Player data encapsulation (name, score, lives, round)
- Score and life management methods
- Round progression tracking

#### `TimerManager.java`
- Countdown timer with callback interface
- Dynamic speed adjustment for rounds
- Time bonus system (+5 seconds per correct answer)
- Pause, resume, and reset functionality

#### `SentenceLoader.java`
- File-based sentence loading with fallback system
- Resource and filesystem loading support
- Default sentences for each difficulty level
- Random sentence selection algorithm

#### `HighScoreManager.java`
- Persistent high score storage using file I/O
- Player ranking calculation
- Score comparison and updating logic
- Top scores retrieval with sorting

#### `Difficulty.java`
- Enum defining three difficulty levels
- Initial timer values and sentence file mappings
- Clean separation of difficulty parameters

## 🎯 Game Flow

1. **Initialization**: Player launches game through `Game.java`
2. **Start Menu**: 
   - Enter player name (validation required)
   - Select difficulty level (Easy/Medium/Hard)
   - Click "Start Game" or press Enter
3. **Main Game Loop**:
   - Display random sentence based on difficulty
   - Player types in input field
   - Press Enter to submit answer
   - Correct: +10 points, +5 seconds, advance round
   - Incorrect: -1 life, clear input, retry same sentence
   - Every 5 rounds: automatic speed increase (timer reduction)
4. **Game End Conditions**:
   - Timer reaches zero
   - Player loses all lives
5. **Game Over Screen**:
   - Display final statistics and ranking
   - Show high scores table
   - Options to play again or exit

## 📊 Scoring System

- **Base Points**: 10 points per correct sentence
- **Time Bonus**: +5 seconds per correct answer
- **Life System**: 3 lives, lose 1 for each wrong answer
- **High Score**: Only updates if new score exceeds previous best
- **Ranking**: Dynamic calculation based on all stored scores

## 🎨 User Experience Features

### Visual Feedback
- Color-coded timer (red when ≤5 seconds, orange when ≤10 seconds)
- Color-coded lives display (red when ≤1, orange when ≤2)
- Success/error feedback messages with appropriate colors
- Current player highlighting in high scores table

### Input Management
- Automatic focus on input field
- Input field clearing on wrong answers
- Enter key submission
- Placeholder text with focus management

### Responsive Design
- Professional border layouts with proper spacing
- Centered components and consistent styling
- Scrollable high scores table
- Appropriate component sizing and proportions








