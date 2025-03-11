import javax.swing.*;
import java.awt.*;

public class ChessBoard extends JFrame {
    private static final int BOARD_SIZE = 8; // 8x8 Chessboard

    public ChessBoard() {
        setTitle("Chess Board");
        setSize(650, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(BOARD_SIZE + 1, BOARD_SIZE + 1)); // Extra row & column for labels

        // Labels for the top row (A-H)
        add(new JLabel("")); // Empty top-left corner
        for (char c = 'A'; c <= 'H'; c++) {
            add(new JLabel(String.valueOf(c), SwingConstants.CENTER));
        }

        // Create the chessboard with alternating black and white tiles
        for (int row = 0; row < BOARD_SIZE; row++) {
            add(new JLabel(String.valueOf(8 - row), SwingConstants.CENTER)); // Row labels (8-1)

            for (int col = 0; col < BOARD_SIZE; col++) {
                JButton square = new JButton();
                square.setEnabled(false); // Disable button interaction

                // Alternating black & white pattern
                if ((row + col) % 2 == 0) {
                    square.setBackground(Color.WHITE);
                } else {
                    square.setBackground(Color.BLACK);
                }

                add(square);
            }
        }

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ChessBoard::new);
    }
}
