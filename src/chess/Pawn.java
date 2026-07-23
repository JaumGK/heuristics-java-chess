package chess;
import java.awt.image.BufferedImage;
public class Pawn extends Pieces {
	public Pawn(int x, int y, PColor c) {
		super(x, y, c);
	}
	
	public BufferedImage getImage() {
		return SpriteManager.getPieceImage("pawn", getColor());
	}

	public boolean preMove(int toCol, int toRow, Pieces[][] board) {
		int dir = (super.getColor() == PColor.WHITE) ? -1 : 1;
		int startRow = (super.getColor() == PColor.WHITE) ? 6 : 1;
		
		if (toCol == super.getCol() && toRow == super.getRow() + dir && board[toCol][toRow] == null)
            return true;
		
		if (toCol == super.getCol() && super.getRow() == startRow && toRow == super.getRow() + 2 * dir &&
				board[super.getCol()][super.getRow() + dir] == null && board[toCol][toRow] == null) {
            return true;
		}
		
		if (Math.abs(toCol - super.getCol()) == 1 && toRow - super.getRow() == (super.getColor() == PColor.WHITE ? -1 : 1)) {
		    if (board[toCol][toRow] == null && Board.getLastMoved() != null && Board.getLastMoved() instanceof Pawn &&
		        Board.getLastMoved().getRow() == super.getRow() && Board.getLastMoved().getCol() == toCol) {
		        return true;
		    }
		}
		
		if (Math.abs(toCol - super.getCol()) == 1 && toRow == super.getRow() + dir &&
				board[toCol][toRow] != null && board[toCol][toRow].getColor() != super.getColor()) {
            return true;
        }
	
		return false;
	}
	
	public boolean isValidMove(int toCol, int toRow, Pieces[][] board) {
		int promotion = (super.getColor() == PColor.WHITE) ? 0 : 7;
		
		if (super.getRow() == promotion)
			board[super.getCol()][super.getRow()] = new Queen(super.getCol(), super.getRow(), super.getColor());
		
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
		Pawn copy = new Pawn(super.getCol(), super.getRow(), super.getColor());
		return copy;
	}
	
	public String toString() {
		return super.toString() + "EnP: " + (((Board.getLastMoved()) == this) ? "true\n" : "false\n");
	}
}
