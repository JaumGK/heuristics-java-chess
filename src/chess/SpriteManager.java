package chess;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
public class SpriteManager {
	private static BufferedImage spriteSheet;
	private static final int TILE_WIDTH = 100;
    private static final int TILE_HEIGHT = 100;
    private static final Map<String, BufferedImage> cache = new HashMap<>();
    
    {
        try {
            spriteSheet = ImageIO.read(getClass().getResource("/chess/Pecas.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static BufferedImage getPieceImage(String type, PColor color) {
        String key = type + "_" + color;
        if (cache.containsKey(key)) {
            return cache.get(key);
        }

        int row = (color == PColor.WHITE) ? 0 : 1;
        int col = getColumnForType(type);

        BufferedImage img = spriteSheet.getSubimage(
            col * TILE_WIDTH,
            row * TILE_HEIGHT,
            TILE_WIDTH,
            TILE_HEIGHT
        );

        cache.put(key, img);
        return img;
    }
    
    private static int getColumnForType(String type) {
        return switch (type.toLowerCase()) {
	        case "rook" -> 0;
	        case "knight" -> 1;
	        case "bishop" -> 2;
	        case "queen" -> 3;
	        case "king" -> 4;
            case "pawn" -> 5;
            default -> throw new IllegalArgumentException("Unknown piece type: " + type);
        };
    }
}
