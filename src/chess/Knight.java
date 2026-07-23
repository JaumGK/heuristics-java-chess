package chess;
import java.awt.image.BufferedImage;
public class Knight extends Pieces {
	public Knight(int x, int y, PColor c) {
		super(x, y, c);
	}
	
	public BufferedImage getImage() {
		return SpriteManager.getPieceImage("knight", getColor());
	}

	public boolean preMove(int toCol, int toRow, Pieces[][] board) {
		int dCol = Math.abs(toCol - super.getCol());
		int dRow = Math.abs(toRow - super.getRow());
        if ((dCol == 2 && dRow == 1) || (dCol == 1 && dRow == 2)) {
        	if (board[toCol][toRow] == null || board[toCol][toRow].getColor() != super.getColor())
        		return true;
        }
        return false;
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
		Knight copy = new Knight(super.getCol(), super.getRow(), super.getColor());
		return copy;
	}
}
