# Chess Game Implementation

This is a Java-based chess game implementation with a graphical user interface using Swing. The game features a complete chess board with all standard pieces, move validation, and game state tracking.

## Project Structure

The project is organized into the following main components:

### Main Classes

1. **ChessBoard.java**
   - Main GUI class that extends JFrame
   - Handles the visual representation of the chess board
   - Manages piece movement and game state
   - Features:
     - 8x8 chess board with alternating colors
     - Piece selection and movement
     - Move suggestions
     - Captured pieces display
     - Game status updates
     - Chess piece images loading

2. **ChessGame.java**
   - Core game logic handler
   - Manages:
     - Turn-based gameplay
     - Move validation
     - Game state
     - Check and checkmate detection

### Movement Package

Contains classes for each chess piece type:
- `Pawn.java`
- `Rook.java`
- `Knight.java`
- `Bishop.java`
- `Queen.java`
- `King.java`

Each piece class will implement its specific movement rules and validation logic.

## Features

1. **Graphical Interface**
   - Clean, modern chess board design
   - Visual piece representation
   - Alternating light and dark squares
   - File (A-H) and rank (1-8) labels
   - Status display for current turn
   - Captured pieces display

2. **Game Mechanics**
   - Turn-based gameplay (White moves first)
   - Piece selection and movement
   - Move validation
   - Capture tracking
   - Move suggestions
   - Clear suggestions option

3. **Visual Feedback**
   - Highlighted valid moves
   - Selected piece indication
   - Captured pieces display
   - Status updates
   - Error messages for invalid moves

## How to Run

1. Compile the code:
   ```bash
   javac MainCode/*.java MainCode/Movement/*.java
   ```

2. Run the game:
   ```bash
   java -cp MainCode ChessBoard
   ```

## Game Controls

1. **Moving Pieces**
   - Click on a piece to select it
   - Click on a valid square to move the selected piece
   - Invalid moves will be prevented

2. **Special Features**
   - "Suggest Move" button: Highlights a valid move for the current player
   - "Suggest Capture" button: Highlights potential capture moves
   - "Clear Suggestions" button: Removes all move highlights

## Technical Details

- Built using Java Swing for the GUI
- Uses a 2D array for board representation
- Implements piece movement validation
- Tracks captured pieces
- Supports image-based piece representation
- Fallback to text-based representation if images fail to load

## Dependencies

- Java Development Kit (JDK) 17 or higher
- Swing GUI toolkit (included in JDK)
- Chess piece images in the resources/chess_pieces/ directory

## Future Improvements

1. Implement complete movement rules for all pieces
2. Add check and checkmate detection
3. Add game history
4. Implement save/load game functionality
5. Add multiplayer support
6. Add AI opponent
7. Implement special moves (castling, en passant, pawn promotion)
