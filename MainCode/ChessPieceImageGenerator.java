import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ChessPieceImageGenerator {
    
    private static final int IMAGE_SIZE = 60;
    private static final String OUTPUT_DIR = "resources/chess_pieces/";
    
    public static void main(String[] args) {
        try {
            // Create output directory if it doesn't exist
            File outputDir = new File(OUTPUT_DIR);
            if (!outputDir.exists()) {
                outputDir.mkdirs();
            }
            
            // Generate and save all pieces
            generatePieceImages();
            
            System.out.println("All chess piece images have been generated successfully.");
        } catch (IOException e) {
            System.err.println("Error generating chess piece images: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void generatePieceImages() throws IOException {
        // White pieces
        generatePawn(true);
        generateRook(true);
        generateKnight(true);
        generateBishop(true);
        generateQueen(true);
        generateKing(true);
        
        // Black pieces
        generatePawn(false);
        generateRook(false);
        generateKnight(false);
        generateBishop(false);
        generateQueen(false);
        generateKing(false);
    }
    
    private static void generatePawn(boolean isWhite) throws IOException {
        BufferedImage image = new BufferedImage(IMAGE_SIZE, IMAGE_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        setupGraphics(g2d);
        
        Color pieceColor = isWhite ? Color.WHITE : Color.DARK_GRAY;
        g2d.setColor(pieceColor);
        
        // Draw pawn
        int baseDiameter = IMAGE_SIZE / 3;
        int topDiameter = IMAGE_SIZE / 4;
        
        // Base
        g2d.fillOval(IMAGE_SIZE/2 - baseDiameter/2, IMAGE_SIZE - baseDiameter - 5, baseDiameter, baseDiameter/2);
        
        // Stem
        g2d.fillRect(IMAGE_SIZE/2 - baseDiameter/6, IMAGE_SIZE/2, baseDiameter/3, IMAGE_SIZE/3);
        
        // Head
        g2d.fillOval(IMAGE_SIZE/2 - topDiameter/2, IMAGE_SIZE/3, topDiameter, topDiameter);
        
        g2d.dispose();
        savePieceImage(image, (isWhite ? "white" : "black") + "_pawn.png");
    }
    
    private static void generateRook(boolean isWhite) throws IOException {
        BufferedImage image = new BufferedImage(IMAGE_SIZE, IMAGE_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        setupGraphics(g2d);
        
        Color pieceColor = isWhite ? Color.WHITE : Color.DARK_GRAY;
        g2d.setColor(pieceColor);
        
        // Base
        g2d.fillRect(IMAGE_SIZE/4, IMAGE_SIZE*3/4, IMAGE_SIZE/2, IMAGE_SIZE/6);
        
        // Body
        g2d.fillRect(IMAGE_SIZE/3, IMAGE_SIZE/3, IMAGE_SIZE/3, IMAGE_SIZE*2/4);
        
        // Top battlements
        int battlementWidth = IMAGE_SIZE/10;
        g2d.fillRect(IMAGE_SIZE/4, IMAGE_SIZE/5, battlementWidth, battlementWidth);
        g2d.fillRect(IMAGE_SIZE/2 - battlementWidth/2, IMAGE_SIZE/5, battlementWidth, battlementWidth);
        g2d.fillRect(IMAGE_SIZE*3/4 - battlementWidth, IMAGE_SIZE/5, battlementWidth, battlementWidth);
        
        g2d.dispose();
        savePieceImage(image, (isWhite ? "white" : "black") + "_rook.png");
    }
    
    private static void generateKnight(boolean isWhite) throws IOException {
        BufferedImage image = new BufferedImage(IMAGE_SIZE, IMAGE_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        setupGraphics(g2d);
        
        Color pieceColor = isWhite ? Color.WHITE : Color.DARK_GRAY;
        g2d.setColor(pieceColor);
        
        // Base
        g2d.fillRect(IMAGE_SIZE/4, IMAGE_SIZE*3/4, IMAGE_SIZE/2, IMAGE_SIZE/6);
        
        // Knight shape (horse head)
        int[] xPoints = {
            IMAGE_SIZE/4, 
            IMAGE_SIZE/3, 
            IMAGE_SIZE/2, 
            IMAGE_SIZE*2/3, 
            IMAGE_SIZE*2/3, 
            IMAGE_SIZE/2, 
            IMAGE_SIZE/3
        };
        
        int[] yPoints = {
            IMAGE_SIZE*3/4, 
            IMAGE_SIZE/2, 
            IMAGE_SIZE/3, 
            IMAGE_SIZE/4, 
            IMAGE_SIZE/2, 
            IMAGE_SIZE*2/3, 
            IMAGE_SIZE*3/4
        };
        
        g2d.fillPolygon(xPoints, yPoints, xPoints.length);
        
        // Eye
        g2d.setColor(isWhite ? Color.BLACK : Color.LIGHT_GRAY);
        g2d.fillOval(IMAGE_SIZE*9/20, IMAGE_SIZE*2/5, IMAGE_SIZE/10, IMAGE_SIZE/10);
        
        g2d.dispose();
        savePieceImage(image, (isWhite ? "white" : "black") + "_knight.png");
    }
    
    private static void generateBishop(boolean isWhite) throws IOException {
        BufferedImage image = new BufferedImage(IMAGE_SIZE, IMAGE_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        setupGraphics(g2d);
        
        Color pieceColor = isWhite ? Color.WHITE : Color.DARK_GRAY;
        g2d.setColor(pieceColor);
        
        // Base
        g2d.fillRect(IMAGE_SIZE/4, IMAGE_SIZE*3/4, IMAGE_SIZE/2, IMAGE_SIZE/6);
        
        // Body
        g2d.fillOval(IMAGE_SIZE/3, IMAGE_SIZE/3, IMAGE_SIZE/3, IMAGE_SIZE/2);
        
        // Top
        g2d.fillOval(IMAGE_SIZE*2/5, IMAGE_SIZE/5, IMAGE_SIZE/5, IMAGE_SIZE/5);
        
        // Cross on top
        g2d.setColor(isWhite ? Color.BLACK : Color.LIGHT_GRAY);
        g2d.fillRect(IMAGE_SIZE/2 - 2, IMAGE_SIZE/8, 4, IMAGE_SIZE/5);
        g2d.fillRect(IMAGE_SIZE*2/5, IMAGE_SIZE/5, IMAGE_SIZE/5, 4);
        
        g2d.dispose();
        savePieceImage(image, (isWhite ? "white" : "black") + "_bishop.png");
    }
    
    private static void generateQueen(boolean isWhite) throws IOException {
        BufferedImage image = new BufferedImage(IMAGE_SIZE, IMAGE_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        setupGraphics(g2d);
        
        Color pieceColor = isWhite ? Color.WHITE : Color.DARK_GRAY;
        g2d.setColor(pieceColor);
        
        // Base
        g2d.fillRect(IMAGE_SIZE/4, IMAGE_SIZE*3/4, IMAGE_SIZE/2, IMAGE_SIZE/6);
        
        // Body
        g2d.fillOval(IMAGE_SIZE/3, IMAGE_SIZE/3, IMAGE_SIZE/3, IMAGE_SIZE/2);
        
        // Crown
        int crownWidth = IMAGE_SIZE/2;
        int crownHeight = IMAGE_SIZE/6;
        int crownX = IMAGE_SIZE/2 - crownWidth/2;
        int crownY = IMAGE_SIZE/5;
        
        int[] xPoints = {
            crownX, 
            crownX + crownWidth/5, 
            crownX + 2*crownWidth/5, 
            crownX + 3*crownWidth/5, 
            crownX + 4*crownWidth/5, 
            crownX + crownWidth, 
            crownX + crownWidth, 
            crownX
        };
        
        int[] yPoints = {
            crownY + crownHeight, 
            crownY, 
            crownY + crownHeight/2, 
            crownY, 
            crownY + crownHeight/2, 
            crownY, 
            crownY + crownHeight, 
            crownY + crownHeight
        };
        
        g2d.fillPolygon(xPoints, yPoints, xPoints.length);
        
        g2d.dispose();
        savePieceImage(image, (isWhite ? "white" : "black") + "_queen.png");
    }
    
    private static void generateKing(boolean isWhite) throws IOException {
        BufferedImage image = new BufferedImage(IMAGE_SIZE, IMAGE_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        setupGraphics(g2d);
        
        Color pieceColor = isWhite ? Color.WHITE : Color.DARK_GRAY;
        g2d.setColor(pieceColor);
        
        // Base
        g2d.fillRect(IMAGE_SIZE/4, IMAGE_SIZE*3/4, IMAGE_SIZE/2, IMAGE_SIZE/6);
        
        // Body
        g2d.fillOval(IMAGE_SIZE/3, IMAGE_SIZE/3, IMAGE_SIZE/3, IMAGE_SIZE/2);
        
        // Crown
        g2d.fillRect(IMAGE_SIZE/3, IMAGE_SIZE/5, IMAGE_SIZE/3, IMAGE_SIZE/8);
        
        // Cross on top
        int crossWidth = IMAGE_SIZE/8;
        g2d.fillRect(IMAGE_SIZE/2 - crossWidth/2, IMAGE_SIZE/12, crossWidth, IMAGE_SIZE/4);
        g2d.fillRect(IMAGE_SIZE/3, IMAGE_SIZE/6, IMAGE_SIZE/3, crossWidth);
        
        g2d.dispose();
        savePieceImage(image, (isWhite ? "white" : "black") + "_king.png");
    }
    
    private static void setupGraphics(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setBackground(new Color(0, 0, 0, 0)); // Transparent background
        g2d.clearRect(0, 0, IMAGE_SIZE, IMAGE_SIZE);
    }
    
    private static void savePieceImage(BufferedImage image, String fileName) throws IOException {
        File outputFile = new File(OUTPUT_DIR + fileName);
        ImageIO.write(image, "png", outputFile);
        System.out.println("Generated: " + outputFile.getAbsolutePath());
    }
} 