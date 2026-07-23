package chess;
import java.awt.image.BufferedImage;
public abstract class Pieces {
	private int row, col;
	private PColor color;
	public Pieces(int x, int y, PColor c) {
		this.color = c;
		this.col = x;
		this.row = y;
	}
	
	public abstract BufferedImage getImage();
	
	public abstract boolean isValidMove(int toCol, int toRow, Pieces[][] board);
	
	public abstract boolean preMove(int toCol, int toRow, Pieces[][] board);
	
	public abstract Pieces clone();
	
	public int getCol() {
		return col;
	}
	public void setCol(int x) {
		this.col = x;
	}
	
	public int getRow() {
		return row;
	}
	public void setRow(int y) {
		this.row = y;
	}
	
	public PColor getColor() {
		return color;
	}
	public void setColor(PColor color) {
		this.color = color;
	}
	
	public String toString() {
		return super.toString() + "\nX: " + col + "\nY: " + row + "\nColor: " + color + "\n";
	}
}
