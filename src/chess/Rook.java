package chess;
import java.awt.image.BufferedImage;
public class Rook extends Pieces {
	private boolean castle = true;
	public Rook(int x, int y, PColor c) {
		super(x, y, c);
	}

	public BufferedImage getImage() {
		return SpriteManager.getPieceImage("rook", getColor());
	}

	public boolean preMove(int toCol, int toRow, Pieces[][] board) {
		int fromCol = super.getCol();
	    int fromRow = super.getRow();

	    if (fromRow != toRow && fromCol != toCol) {
	        return false; 
	    }
	    
	    if (toCol < 0 || toCol > 7 || toRow < 0 || toRow > 7) {
	        return false;
	    }
	    
	    int colStep = Integer.compare(toCol, fromCol); 
	    int rowStep = Integer.compare(toRow, fromRow); 

	    int currentCol = fromCol + colStep;
	    int currentRow = fromRow + rowStep;

	    while (currentCol != toCol || currentRow != toRow) {
	        if (board[currentCol][currentRow] != null) {
	            return false; 
	        }
	        currentRow += rowStep;
	        currentCol += colStep;
	    }
	    if (board[toCol][toRow] != null && board[toCol][toRow].getColor() == this.getColor()) {
	        return false; 
	    }
		return true;
	}
	
	public boolean isValidMove(int toCol, int toRow, Pieces[][] board) {
		if (preMove(toCol, toRow, board) == false)
			return false;
		Pieces[][] simBoard = Check.copyBoard(board);
	    simBoard[toCol][toRow] = simBoard[getCol()][getRow()];
	    simBoard[getCol()][getRow()] = null;

	    simBoard[toCol][toRow].setCol(toCol);
	    simBoard[toCol][toRow].setRow(toRow);
	    
	    if (Check.isInCheck(super.getColor(), simBoard) == true)
	    	return false;
	    return true;
	}
	
	public Pieces clone() {
		Rook copy = new Rook(super.getCol(), super.getRow(), super.getColor());
		copy.castle = this.castle;
		return copy;
	}
	
	public void setCastle(boolean castle) {
		this.castle = castle;
	}
	
	public boolean getCastle() {
		return castle;
	}
	
	public String toString() {
		return super.toString() + "Castle: " + castle + "\n";
	}
}
