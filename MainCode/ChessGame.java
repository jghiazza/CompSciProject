import java.util.ArrayList;
import java.util.List;

public class ChessGame {
    private static final int BOARD_SIZE = 8;
    private String[][] board;
    private boolean isWhiteTurn = true;
    
    // Lists to track pieces for each player
    private List<ChessPiece> whitePieces = new ArrayList<>();
    private List<ChessPiece> blackPieces = new ArrayList<>();
    
    public ChessGame(String[][] board) {
        this.board = board;
        initializePieceLists();
    }
    
    private void initializePieceLists() {
        // Scan the board and populate piece lists
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                String pieceStr = board[row][col];
                if (pieceStr != null) {
                    boolean isWhite = pieceStr.startsWith("W");
                    String pieceType = pieceStr.substring(2); // Remove "W-" or "B-" prefix
                    
                    ChessPiece piece = new ChessPiece(pieceType, isWhite, row, col);
                    
                    if (isWhite) {
                        whitePieces.add(piece);
                    } else {
                        blackPieces.add(piece);
                    }
                }
            }
        }
    }
    
    public boolean isValidMove(int startRow, int startCol, int endRow, int endCol) {
        // Check if there's a piece at the start position
        if (board[startRow][startCol] == null) {
            return false;
        }
        
        // Check if it's the correct player's turn
        boolean isWhitePiece = board[startRow][startCol].startsWith("W");
        if (isWhitePiece != isWhiteTurn) {
            return false;
        }
        
        // Check if the destination has a piece of the same color
        if (board[endRow][endCol] != null) {
            boolean isWhiteDestination = board[endRow][endCol].startsWith("W");
            if (isWhitePiece == isWhiteDestination) {
                return false; // Can't capture your own piece
            }
        }
        
        // Get the piece type and validate movement based on piece rules
        String pieceType = board[startRow][startCol].substring(2);
        
        switch (pieceType) {
            case "Pawn":
                return isValidPawnMove(startRow, startCol, endRow, endCol);
            case "Rook":
                return isValidRookMove(startRow, startCol, endRow, endCol);
            case "Knight":
                return isValidKnightMove(startRow, startCol, endRow, endCol);
            case "Bishop":
                return isValidBishopMove(startRow, startCol, endRow, endCol);
            case "Queen":
                return isValidQueenMove(startRow, startCol, endRow, endCol);
            case "King":
                return isValidKingMove(startRow, startCol, endRow, endCol);
            default:
                return false;
        }
    }
    
    private boolean isValidPawnMove(int startRow, int startCol, int endRow, int endCol) {
        boolean isWhite = board[startRow][startCol].startsWith("W");
        int direction = isWhite ? -1 : 1; // White moves up (decreasing row), Black moves down
        
        // Regular move (1 square forward)
        if (startCol == endCol && endRow == startRow + direction && board[endRow][endCol] == null) {
            return true;
        }
        
        // First move can be 2 squares
        if (startCol == endCol && 
            ((isWhite && startRow == 6 && endRow == 4) || (!isWhite && startRow == 1 && endRow == 3)) &&
            board[startRow + direction][startCol] == null && board[endRow][endCol] == null) {
            return true;
        }
        
        // Capture diagonally
        if ((endCol == startCol + 1 || endCol == startCol - 1) && 
             endRow == startRow + direction && 
             board[endRow][endCol] != null && 
             isWhite != board[endRow][endCol].startsWith("W")) {
            return true;
        }
        
        return false;
    }
    
    private boolean isValidRookMove(int startRow, int startCol, int endRow, int endCol) {
        // Rooks move horizontally or vertically
        if (startRow != endRow && startCol != endCol) {
            return false;
        }
        
        // Check if path is clear
        return isPathClear(startRow, startCol, endRow, endCol);
    }
    
    private boolean isValidKnightMove(int startRow, int startCol, int endRow, int endCol) {
        // Knights move in L-shape: 2 in one direction and 1 in perpendicular direction
        int rowDiff = Math.abs(endRow - startRow);
        int colDiff = Math.abs(endCol - startCol);
        
        return (rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2);
    }
    
    private boolean isValidBishopMove(int startRow, int startCol, int endRow, int endCol) {
        // Bishops move diagonally
        if (Math.abs(endRow - startRow) != Math.abs(endCol - startCol)) {
            return false;
        }
        
        // Check if path is clear
        return isPathClear(startRow, startCol, endRow, endCol);
    }
    
    private boolean isValidQueenMove(int startRow, int startCol, int endRow, int endCol) {
        // Queen combines rook and bishop movement
        boolean movingDiagonally = Math.abs(endRow - startRow) == Math.abs(endCol - startCol);
        boolean movingStraight = startRow == endRow || startCol == endCol;
        
        if (!movingDiagonally && !movingStraight) {
            return false;
        }
        
        // Check if path is clear
        return isPathClear(startRow, startCol, endRow, endCol);
    }
    
    private boolean isValidKingMove(int startRow, int startCol, int endRow, int endCol) {
        // King moves one square in any direction
        int rowDiff = Math.abs(endRow - startRow);
        int colDiff = Math.abs(endCol - startCol);
        
        return rowDiff <= 1 && colDiff <= 1;
    }
    
    private boolean isPathClear(int startRow, int startCol, int endRow, int endCol) {
        int rowStep = 0;
        int colStep = 0;
        
        if (startRow < endRow) rowStep = 1;
        else if (startRow > endRow) rowStep = -1;
        
        if (startCol < endCol) colStep = 1;
        else if (startCol > endCol) colStep = -1;
        
        int row = startRow + rowStep;
        int col = startCol + colStep;
        
        // Check all squares between start and end (not including start and end)
        while (row != endRow || col != endCol) {
            if (board[row][col] != null) {
                return false; // Path is blocked
            }
            row += rowStep;
            col += colStep;
        }
        
        return true;
    }
    
    public boolean makeMove(int startRow, int startCol, int endRow, int endCol) {
        if (!isValidMove(startRow, startCol, endRow, endCol)) {
            return false;
        }
        
        // Check if this is a capture
        if (board[endRow][endCol] != null) {
            capturePiece(endRow, endCol);
        }
        
        // Move the piece
        board[endRow][endCol] = board[startRow][startCol];
        board[startRow][startCol] = null;
        
        // Update piece position in the list
        updatePiecePosition(startRow, startCol, endRow, endCol);
        
        // Switch turns
        isWhiteTurn = !isWhiteTurn;
        
        return true;
    }
    
    private void capturePiece(int row, int col) {
        String capturedPiece = board[row][col];
        boolean isWhitePiece = capturedPiece.startsWith("W");
        
        List<ChessPiece> pieceList = isWhitePiece ? whitePieces : blackPieces;
        
        // Find and remove the piece from the appropriate list
        for (int i = 0; i < pieceList.size(); i++) {
            ChessPiece piece = pieceList.get(i);
            if (piece.getRow() == row && piece.getCol() == col) {
                pieceList.remove(i);
                System.out.println("Captured: " + capturedPiece);
                break;
            }
        }
    }
    
    private void updatePiecePosition(int startRow, int startCol, int endRow, int endCol) {
        boolean isWhitePiece = board[endRow][endCol].startsWith("W");
        List<ChessPiece> pieceList = isWhitePiece ? whitePieces : blackPieces;
        
        for (ChessPiece piece : pieceList) {
            if (piece.getRow() == startRow && piece.getCol() == startCol) {
                piece.setRow(endRow);
                piece.setCol(endCol);
                break;
            }
        }
    }
    
    public List<ChessPiece> getWhitePieces() {
        return whitePieces;
    }
    
    public List<ChessPiece> getBlackPieces() {
        return blackPieces;
    }
    
    public boolean isWhiteTurn() {
        return isWhiteTurn;
    }
    
    // Inner class to represent a chess piece
    public class ChessPiece {
        private String type;
        private boolean isWhite;
        private int row;
        private int col;
        
        public ChessPiece(String type, boolean isWhite, int row, int col) {
            this.type = type;
            this.isWhite = isWhite;
            this.row = row;
            this.col = col;
        }
        
        public String getType() {
            return type;
        }
        
        public boolean isWhite() {
            return isWhite;
        }
        
        public int getRow() {
            return row;
        }
        
        public void setRow(int row) {
            this.row = row;
        }
        
        public int getCol() {
            return col;
        }
        
        public void setCol(int col) {
            this.col = col;
        }
        
        @Override
        public String toString() {
            return (isWhite ? "W-" : "B-") + type + " at (" + row + "," + col + ")";
        }
    }
} 