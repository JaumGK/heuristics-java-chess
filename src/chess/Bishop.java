package chess;
import java.awt.image.BufferedImage;
public class Bishop extends Pieces {
	public Bishop(int x, int y, PColor c) {
		super(x, y, c);
	}
	
	public BufferedImage getImage() {
		return SpriteManager.getPieceImage("bishop", getColor());
	}

	public boolean preMove(int toCol, int toRow, Pieces[][] board) {
		int fromCol = super.getCol();
	    int fromRow = super.getRow();

	    if (Math.abs(toRow - fromRow) != Math.abs(toCol - fromCol)) {
	        return false;
	    }

	    int colStep = (toCol > fromCol) ? 1 : -1;
	    int rowStep = (toRow > fromRow) ? 1 : -1;

	    int currentCol = fromCol + colStep;
	    int currentRow = fromRow + rowStep;

	    while (currentCol != toCol || currentRow != toRow) {
	        if (currentRow < 0 || currentRow > 7 || currentCol < 0 || currentCol > 7) {
	            return false; 
	        }
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
		Bishop copy = new Bishop(super.getCol(), super.getRow(), super.getColor());
		return copy;
	}
}
