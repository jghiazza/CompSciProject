import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
    
    // For highlighting suggested moves
    private List<Point> highlightedSquares = new ArrayList<>();
    
    // Panels to display captured pieces
    private JPanel whiteCapturedPanel;
    private JPanel blackCapturedPanel;
    
    // Track captured pieces
    private List<String> whiteCapturedPieces = new ArrayList<>();
    private List<String> blackCapturedPieces = new ArrayList<>();
    
    // Chess piece images
    private Map<String, ImageIcon> pieceImages = new HashMap<>();
    
    // Board colors
    private final Color lightSquareColor = new Color(240, 240, 210); // Light beige
    private final Color darkSquareColor = new Color(120, 150, 90);  // Olive green
    
    public ChessBoard() {
        setTitle("Chess Board");
        setSize(650, 800); // Increased height for captured pieces panels
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Load chess piece images
        loadPieceImages();
        
        // Main panel with border layout
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Board panel with grid layout
        JPanel boardPanel = new JPanel(new GridLayout(BOARD_SIZE + 1, BOARD_SIZE + 1));
        
        // Create panels for captured pieces
        createCapturedPiecesPanels();
        
        // Labels for the top row (A-H)
        boardPanel.add(new JLabel("")); // Empty top-left corner
        for (char c = 'A'; c <= 'H'; c++) {
            JLabel label = new JLabel(String.valueOf(c), SwingConstants.CENTER);
            label.setFont(new Font("Arial", Font.BOLD, 14));
            boardPanel.add(label);
        }

        // Initialize the chess pieces
        initializeBoard();
        
        // Initialize game logic
        game = new ChessGame(board);
        
        // Create the chessboard with alternating light and dark tiles
        for (int row = 0; row < BOARD_SIZE; row++) {
            JLabel rowLabel = new JLabel(String.valueOf(8 - row), SwingConstants.CENTER);
            rowLabel.setFont(new Font("Arial", Font.BOLD, 14));
            boardPanel.add(rowLabel); // Row labels (8-1)

            for (int col = 0; col < BOARD_SIZE; col++) {
                JButton square = new JButton();
                square.setBorderPainted(false);
                square.setFocusPainted(false);
                
                // Set piece images if present
                if (board[row][col] != null) {
                    square.setIcon(pieceImages.get(board[row][col]));
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

                // Alternating light & dark pattern
                if ((row + col) % 2 == 0) {
                    square.setBackground(lightSquareColor);
                } else {
                    square.setBackground(darkSquareColor);
                    square.setOpaque(true);
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
        
        // Create suggestion buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        
        JButton suggestMoveButton = new JButton("Suggest Move");
        suggestMoveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                suggestValidMove();
            }
        });
        
        JButton suggestCaptureButton = new JButton("Suggest Capture");
        suggestCaptureButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                suggestCapture();
            }
        });
        
        JButton resetButton = new JButton("Clear Suggestions");
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearHighlights();
                statusLabel.setText(game.isWhiteTurn() ? "White's turn to move" : "Black's turn to move");
            }
        });
        
        buttonPanel.add(suggestMoveButton);
        buttonPanel.add(suggestCaptureButton);
        buttonPanel.add(resetButton);
        
        // Create a panel for the center section (captured pieces + board + status)
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(blackCapturedPanel, BorderLayout.NORTH);
        centerPanel.add(boardPanel, BorderLayout.CENTER);
        centerPanel.add(whiteCapturedPanel, BorderLayout.SOUTH);
        
        // Add components to main panel
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(statusLabel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Set content pane
        setContentPane(mainPanel);
        setVisible(true);
    }
    
    private void loadPieceImages() {
        try {
            // Path to the image resources
            String imagePath = "resources/chess_pieces/";
            
            // Load white pieces
            pieceImages.put("W-Pawn", new ImageIcon(new File(imagePath + "white_pawn.png").getAbsolutePath()));
            pieceImages.put("W-Rook", new ImageIcon(new File(imagePath + "white_rook.png").getAbsolutePath()));
            pieceImages.put("W-Knight", new ImageIcon(new File(imagePath + "white_knight.png").getAbsolutePath()));
            pieceImages.put("W-Bishop", new ImageIcon(new File(imagePath + "white_bishop.png").getAbsolutePath()));
            pieceImages.put("W-Queen", new ImageIcon(new File(imagePath + "white_queen.png").getAbsolutePath()));
            pieceImages.put("W-King", new ImageIcon(new File(imagePath + "white_king.png").getAbsolutePath()));
            
            // Load black pieces
            pieceImages.put("B-Pawn", new ImageIcon(new File(imagePath + "black_pawn.png").getAbsolutePath()));
            pieceImages.put("B-Rook", new ImageIcon(new File(imagePath + "black_rook.png").getAbsolutePath()));
            pieceImages.put("B-Knight", new ImageIcon(new File(imagePath + "black_knight.png").getAbsolutePath()));
            pieceImages.put("B-Bishop", new ImageIcon(new File(imagePath + "black_bishop.png").getAbsolutePath()));
            pieceImages.put("B-Queen", new ImageIcon(new File(imagePath + "black_queen.png").getAbsolutePath()));
            pieceImages.put("B-King", new ImageIcon(new File(imagePath + "black_king.png").getAbsolutePath()));
        } catch (Exception e) {
            System.err.println("Error loading chess piece images: " + e.getMessage());
            JOptionPane.showMessageDialog(this, 
                "Could not load chess piece images. Using text instead.\n" + e.getMessage(),
                "Image Loading Error", JOptionPane.ERROR_MESSAGE);
            
            // If images can't be loaded, we'll continue with text-based pieces
        }
    }
    
    private void createCapturedPiecesPanels() {
        // Panel for white captured pieces (displayed at the bottom)
        whiteCapturedPanel = new JPanel();
        whiteCapturedPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        whiteCapturedPanel.setBorder(BorderFactory.createTitledBorder("White Captured Pieces"));
        whiteCapturedPanel.setPreferredSize(new Dimension(650, 60));
        
        // Panel for black captured pieces (displayed at the top)
        blackCapturedPanel = new JPanel();
        blackCapturedPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        blackCapturedPanel.setBorder(BorderFactory.createTitledBorder("Black Captured Pieces"));
        blackCapturedPanel.setPreferredSize(new Dimension(650, 60));
    }
    
    private void updateCapturedPiecesDisplay() {
        // Clear both panels
        whiteCapturedPanel.removeAll();
        blackCapturedPanel.removeAll();
        
        // Add white captured pieces
        for (String piece : whiteCapturedPieces) {
            JLabel pieceLabel;
            if (pieceImages.containsKey(piece)) {
                pieceLabel = new JLabel(pieceImages.get(piece));
            } else {
                pieceLabel = new JLabel(piece);
                pieceLabel.setFont(new Font("Arial", Font.BOLD, 14));
            }
            whiteCapturedPanel.add(pieceLabel);
        }
        
        // Add black captured pieces
        for (String piece : blackCapturedPieces) {
            JLabel pieceLabel;
            if (pieceImages.containsKey(piece)) {
                pieceLabel = new JLabel(pieceImages.get(piece));
            } else {
                pieceLabel = new JLabel(piece);
                pieceLabel.setFont(new Font("Arial", Font.BOLD, 14));
            }
            blackCapturedPanel.add(pieceLabel);
        }
        
        // Refresh the display
        whiteCapturedPanel.revalidate();
        whiteCapturedPanel.repaint();
        blackCapturedPanel.revalidate();
        blackCapturedPanel.repaint();
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
        // Clear any existing highlights
        clearHighlights();
        
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
                
                // Check if this is a capture
                boolean isCapture = destinationPieceText != null;
                
                // Try to make the move
                if (game.makeMove(selectedRow, selectedCol, row, col)) {
                    // Update the UI with images
                    squares[row][col].setIcon(pieceImages.get(board[row][col]));
                    selectedPiece.setIcon(null);
                    
                    // If it was a capture, add to the appropriate captured list
                    if (isCapture) {
                        boolean capturedWhitePiece = destinationPieceText.startsWith("W");
                        if (capturedWhitePiece) {
                            whiteCapturedPieces.add(destinationPieceText);
                        } else {
                            blackCapturedPieces.add(destinationPieceText);
                        }
                        updateCapturedPiecesDisplay();
                    }
                    
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
            squares[row][col].setBackground(lightSquareColor);
        } else {
            squares[row][col].setBackground(darkSquareColor);
        }
    }
    
    private void suggestValidMove() {
        clearHighlights();
        
        boolean isWhiteTurn = game.isWhiteTurn();
        List<MoveOption> validMoves = new ArrayList<>();
        
        // Find a valid move for the current player
        for (int startRow = 0; startRow < BOARD_SIZE; startRow++) {
            for (int startCol = 0; startCol < BOARD_SIZE; startCol++) {
                // Check if there's a piece belonging to the current player
                if (board[startRow][startCol] != null && 
                    (board[startRow][startCol].startsWith("W") == isWhiteTurn)) {
                    
                    // Look for valid moves for this piece
                    for (int endRow = 0; endRow < BOARD_SIZE; endRow++) {
                        for (int endCol = 0; endCol < BOARD_SIZE; endCol++) {
                            if (game.isValidMove(startRow, startCol, endRow, endCol) && 
                                board[endRow][endCol] == null) { // Only non-capturing moves
                                validMoves.add(new MoveOption(startRow, startCol, endRow, endCol));
                            }
                        }
                    }
                }
            }
        }
        
        if (validMoves.isEmpty()) {
            statusLabel.setText("No valid moves found!");
            return;
        }
        
        // Select a random valid move
        Random rand = new Random();
        MoveOption move = validMoves.get(rand.nextInt(validMoves.size()));
        
        // Highlight the move
        squares[move.startRow][move.startCol].setBackground(Color.GREEN);
        squares[move.endRow][move.endCol].setBackground(Color.CYAN);
        
        // Add to highlighted squares list
        highlightedSquares.add(new Point(move.startRow, move.startCol));
        highlightedSquares.add(new Point(move.endRow, move.endCol));
        
        // Display suggestion
        String piece = board[move.startRow][move.startCol];
        statusLabel.setText("Suggestion: Move " + piece + " from " + 
                           getSquareName(move.startRow, move.startCol) + " to " + 
                           getSquareName(move.endRow, move.endCol));
    }
    
    private void suggestCapture() {
        clearHighlights();
        
        boolean isWhiteTurn = game.isWhiteTurn();
        List<MoveOption> captures = new ArrayList<>();
        
        // Find a capture move for the current player
        for (int startRow = 0; startRow < BOARD_SIZE; startRow++) {
            for (int startCol = 0; startCol < BOARD_SIZE; startCol++) {
                // Check if there's a piece belonging to the current player
                if (board[startRow][startCol] != null && 
                    (board[startRow][startCol].startsWith("W") == isWhiteTurn)) {
                    
                    // Look for valid capture moves for this piece
                    for (int endRow = 0; endRow < BOARD_SIZE; endRow++) {
                        for (int endCol = 0; endCol < BOARD_SIZE; endCol++) {
                            if (board[endRow][endCol] != null && 
                                (board[endRow][endCol].startsWith("W") != isWhiteTurn) && 
                                game.isValidMove(startRow, startCol, endRow, endCol)) {
                                captures.add(new MoveOption(startRow, startCol, endRow, endCol));
                            }
                        }
                    }
                }
            }
        }
        
        if (captures.isEmpty()) {
            statusLabel.setText("No capture moves available!");
            return;
        }
        
        // Select a random capture
        Random rand = new Random();
        MoveOption move = captures.get(rand.nextInt(captures.size()));
        
        // Highlight the move
        squares[move.startRow][move.startCol].setBackground(Color.GREEN);
        squares[move.endRow][move.endCol].setBackground(Color.RED);
        
        // Add to highlighted squares list
        highlightedSquares.add(new Point(move.startRow, move.startCol));
        highlightedSquares.add(new Point(move.endRow, move.endCol));
        
        // Display suggestion
        String attacker = board[move.startRow][move.startCol];
        String target = board[move.endRow][move.endCol];
        statusLabel.setText("Capture suggestion: Use " + attacker + " at " + 
                           getSquareName(move.startRow, move.startCol) + " to capture " + 
                           target + " at " + getSquareName(move.endRow, move.endCol));
    }
    
    private void clearHighlights() {
        // Reset colors of all previously highlighted squares
        for (Point p : highlightedSquares) {
            resetSquareColor(p.x, p.y);
        }
        highlightedSquares.clear();
    }
    
    private String getSquareName(int row, int col) {
        char file = (char)('A' + col);
        int rank = 8 - row;
        return "" + file + rank;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ChessBoard::new);
    }
    
    // Inner class to store move suggestions
    private static class MoveOption {
        int startRow, startCol, endRow, endCol;
        
        public MoveOption(int startRow, int startCol, int endRow, int endCol) {
            this.startRow = startRow;
            this.startCol = startCol;
            this.endRow = endRow;
            this.endCol = endCol;
        }
    }
}
