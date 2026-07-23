package chess;
import java.awt.image.BufferedImage;
public class Queen extends Pieces {
	public Queen(int x, int y, PColor c) {
		super(x, y, c);
	}

	public BufferedImage getImage() {
		return SpriteManager.getPieceImage("queen", getColor());
	}

	public boolean preMove(int toCol, int toRow, Pieces[][] board) {
		int fromCol = super.getCol();
	    int fromRow = super.getRow();

	    int colDiff = toCol - fromCol;
	    int rowDiff = toRow - fromRow;

	    int rowStep = Integer.signum(rowDiff);
	    int colStep = Integer.signum(colDiff);

	    boolean isStraight = (rowDiff == 0 || colDiff == 0);
	    boolean isDiagonal = Math.abs(rowDiff) == Math.abs(colDiff);

	    if (!isStraight && !isDiagonal) {
	        return false;
	    }

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
	    
	    if (toCol < 0 || toCol > 7 || toRow < 0 || toRow > 7)
	        return false;
	    
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
		Queen copy = new Queen(super.getCol(), super.getRow(), super.getColor());
		return copy;
	}
}
