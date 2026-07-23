package chess;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
public class Park extends JFrame {
    private static final long serialVersionUID = 1L;
	public Park() {
		Pieces[][] board = Setup.initializeBoard();
		Board panel = new Board(board);
        setTitle("Chess");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel.setPreferredSize(new java.awt.Dimension(800, 800));
        setContentPane(panel);
        pack();
        setLocationRelativeTo(null);
        setIconImage(SpriteManager.getPieceImage("knight", PColor.BLACK));
        

        setVisible(true);
    }

    public static void main(String[] args) {
    	new SpriteManager();
        SwingUtilities.invokeLater(() -> new Park());
    }
}
