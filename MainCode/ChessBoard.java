import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChessBoard extends JFrame {
    private static final int BOARD_SIZE = 8; // 8x8 Chessboard
    private JButton[][] squares = new JButton[BOARD_SIZE][BOARD_SIZE];
    private JButton selectedPiece = null;
    
    // Represent chess pieces
    private String[][] board = new String[BOARD_SIZE][BOARD_SIZE];
    
    // Game logic handler
    private ChessGame game;
    
    // Status label
    private JLabel statusLabel;
    
    // Remember the position of the selected piece
    private int selectedRow = -1;
    private int selectedCol = -1;
    
    public ChessBoard() {
        setTitle("Chess Board");
        setSize(650, 700); // Increased height to accommodate status label
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Main panel with border layout
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Board panel with grid layout
        JPanel boardPanel = new JPanel(new GridLayout(BOARD_SIZE + 1, BOARD_SIZE + 1));
        
        // Labels for the top row (A-H)
        boardPanel.add(new JLabel("")); // Empty top-left corner
        for (char c = 'A'; c <= 'H'; c++) {
            boardPanel.add(new JLabel(String.valueOf(c), SwingConstants.CENTER));
        }

        // Initialize the chess pieces
        initializeBoard();
        
        // Initialize game logic
        game = new ChessGame(board);
        
        // Create the chessboard with alternating black and white tiles
        for (int row = 0; row < BOARD_SIZE; row++) {
            boardPanel.add(new JLabel(String.valueOf(8 - row), SwingConstants.CENTER)); // Row labels (8-1)

            for (int col = 0; col < BOARD_SIZE; col++) {
                JButton square = new JButton();
                square.setFont(new Font("Arial", Font.BOLD, 12));
                
                // Set piece names if present
                if (board[row][col] != null) {
                    square.setText(board[row][col]);
                }
                
                // Add click listener for piece movement
                final int finalRow = row;
                final int finalCol = col;
                square.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        handleSquareClick(finalRow, finalCol);
                    }
                });

                // Alternating black & white pattern
                if ((row + col) % 2 == 0) {
                    square.setBackground(Color.WHITE);
                    square.setForeground(Color.BLACK);
                } else {
                    square.setBackground(Color.GRAY);
                    square.setForeground(Color.WHITE);
                    square.setOpaque(true);
                    square.setBorderPainted(false);
                }

                squares[row][col] = square;
                boardPanel.add(square);
            }
        }
        
        // Create status label
        statusLabel = new JLabel("White's turn to move");
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 16));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        // Add components to main panel
        mainPanel.add(boardPanel, BorderLayout.CENTER);
        mainPanel.add(statusLabel, BorderLayout.SOUTH);
        
        // Set content pane
        setContentPane(mainPanel);
        setVisible(true);
    }
    
    private void initializeBoard() {
        // Initialize white pieces (bottom)
        board[7][0] = "W-Rook";
        board[7][1] = "W-Knight";
        board[7][2] = "W-Bishop";
        board[7][3] = "W-Queen";
        board[7][4] = "W-King";
        board[7][5] = "W-Bishop";
        board[7][6] = "W-Knight";
        board[7][7] = "W-Rook";
        
        // White pawns
        for (int col = 0; col < BOARD_SIZE; col++) {
            board[6][col] = "W-Pawn";
        }
        
        // Initialize black pieces (top)
        board[0][0] = "B-Rook";
        board[0][1] = "B-Knight";
        board[0][2] = "B-Bishop";
        board[0][3] = "B-Queen";
        board[0][4] = "B-King";
        board[0][5] = "B-Bishop";
        board[0][6] = "B-Knight";
        board[0][7] = "B-Rook";
        
        // Black pawns
        for (int col = 0; col < BOARD_SIZE; col++) {
            board[1][col] = "B-Pawn";
        }
    }
    
    private void handleSquareClick(int row, int col) {
        // If no piece is selected and the clicked square has a piece
        if (selectedPiece == null && board[row][col] != null) {
            boolean isWhitePiece = board[row][col].startsWith("W");
            
            // Check if it's the correct player's turn
            if (isWhitePiece != game.isWhiteTurn()) {
                statusLabel.setText(game.isWhiteTurn() ? "It's White's turn!" : "It's Black's turn!");
                return;
            }
            
            // Select the piece
            selectedPiece = squares[row][col];
            selectedRow = row;
            selectedCol = col;
            selectedPiece.setBackground(Color.YELLOW); // Highlight selected piece
            statusLabel.setText("Selected: " + board[row][col]);
            System.out.println("Selected: " + board[row][col] + " at " + row + "," + col);
        } 
        // If a piece is already selected
        else if (selectedPiece != null) {
            // If player clicked the same square again, deselect it
            if (row == selectedRow && col == selectedCol) {
                resetSquareColor(selectedRow, selectedCol);
                selectedPiece = null;
                selectedRow = -1;
                selectedCol = -1;
                statusLabel.setText(game.isWhiteTurn() ? "White's turn to move" : "Black's turn to move");
                return;
            }
            
            // Check if the move is valid before attempting it
            if (game.isValidMove(selectedRow, selectedCol, row, col)) {
                // Remember the previous state in case we need to revert
                String selectedPieceText = board[selectedRow][selectedCol];
                String destinationPieceText = board[row][col];
                
                // Try to make the move
                if (game.makeMove(selectedRow, selectedCol, row, col)) {
                    // Update the UI
                    squares[row][col].setText(board[row][col]);
                    selectedPiece.setText("");
                    
                    // Reset colors
                    resetSquareColor(selectedRow, selectedCol);
                    resetSquareColor(row, col);
                    
                    // Update status
                    statusLabel.setText(game.isWhiteTurn() ? "White's turn to move" : "Black's turn to move");
                    
                    // Display piece counts
                    System.out.println("White pieces: " + game.getWhitePieces().size());
                    System.out.println("Black pieces: " + game.getBlackPieces().size());
                } else {
                    // This should not happen if isValidMove returns true, but just in case
                    statusLabel.setText("Error making move!");
                    
                    // Revert any changes
                    board[selectedRow][selectedCol] = selectedPieceText;
                    board[row][col] = destinationPieceText;
                }
            } else {
                // Invalid move
                String pieceType = board[selectedRow][selectedCol].substring(2);
                statusLabel.setText("Invalid move for " + pieceType + "! Try again.");
                
                // Flash red briefly to indicate invalid move
                squares[row][col].setBackground(Color.RED);
                Timer timer = new Timer(500, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        resetSquareColor(row, col);
                    }
                });
                timer.setRepeats(false);
                timer.start();
            }
            
            // Reset selection
            selectedPiece = null;
            selectedRow = -1;
            selectedCol = -1;
        }
    }
    
    private void resetSquareColor(int row, int col) {
        if ((row + col) % 2 == 0) {
            squares[row][col].setBackground(Color.WHITE);
        } else {
            squares[row][col].setBackground(Color.GRAY);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ChessBoard::new);
    }
}
