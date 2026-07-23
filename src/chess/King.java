package chess;
import java.awt.image.BufferedImage;
public class King extends Pieces {
	private boolean castle = true;
	public King(int x, int y, PColor c) {
		super(x, y, c);
	}

	public BufferedImage getImage() {
		return SpriteManager.getPieceImage("king", getColor());
	}

	public boolean preMove(int toCol, int toRow, Pieces[][] board) {
		if (castle == true) {
			if (toCol == super.getCol() + 2 && toRow == super.getRow()) {
		        if (board[7][toRow] instanceof Rook) {
		        	if (((Rook) board[7][toRow]).getCastle() == true) {
		        		if (board[5][toRow] == null && board[6][toRow] == null) {
		        			Pieces[][] simBoard = Check.copyBoard(board);
		        			simBoard[5][toRow] = simBoard[4][toRow];
		        			simBoard[4][toRow] = null;
		        			if (Check.isInCheck(super.getColor(), simBoard) == true)
		        				return false;
		        			return true;
		        		}
		        	}
		        }
		    }
			if (toCol == super.getCol() - 2 && toRow == super.getRow()) {
		        if (board[0][toRow] instanceof Rook) {
		        	if (((Rook) board[0][toRow]).getCastle() == true) {
		        		if (board[1][toRow] == null && board[2][toRow] == null && board[3][toRow] == null) {
		        			Pieces[][] simBoard = Check.copyBoard(board);
		        			simBoard[3][toRow] = simBoard[4][toRow];
		        			simBoard[4][toRow] = null;
		        			if (Check.isInCheck(super.getColor(), simBoard) == true)
		        				return false;
		        			return true;
		        		}
		        	}
		        }
		    }
		}
		if (board[toCol][toRow] == null || board[toCol][toRow].getColor() != super.getColor()) {
			if (Math.abs(toCol - super.getCol()) <= 1 && Math.abs(toRow - super.getRow()) <= 1) {
				return true;
			}
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
		King copy = new King(super.getCol(), super.getRow(), super.getColor());
		copy.castle = this.castle;
		return copy;
	}
	
	public boolean getCastle() {
		return castle;
	}
	public void setCastle(boolean castle) {
		this.castle = castle;
	}

	public String toString() {
		return super.toString() + "Castle: " + castle + "\n";
	}
}
