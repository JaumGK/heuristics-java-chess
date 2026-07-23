package chess;
public class Check {
	private static int kingRow = -1, kingCol = -1, attackCol = -1, attackRow = -1;
	public static boolean isInCheck(PColor color, Pieces[][] board) {
		for (int col = 0; col < 8; col++) {
			for (int row = 0; row < 8; row++) {
	            if (board[col][row] != null && board[col][row] instanceof King && board[col][row].getColor() == color) {
	                kingRow = row;
	                kingCol = col;
	                break;
	            }
	        }
	    }
		
	    for (int col = 0; col < 8; col++) {
	    	for (int row = 0; row < 8; row++) {
	            if (board[col][row] != null && board[col][row].getColor() != color) {
	                if (board[col][row].preMove(kingCol, kingRow, board)) {
	                	attackCol = col;
	                	attackRow = row;
	                    return true;
	                }
	            }
	        }
	    }
		return false;
	}
	public static boolean isCheckMate(Pieces[][] board, PColor color) {
		if (isInCheck(color, board) == true) {
			for (int col = 0; col < 8; col++) {
				for (int row = 0; row < 8; row++) {
					if (board[col][row] != null && board[col][row].getColor() == color) {
						for (int mCol = 0; mCol < 8; mCol++) {
							for (int mRow = 0; mRow < 8; mRow++) {
								if (board[col][row] != null && board[col][row].isValidMove(mCol, mRow, board) == true) {									
									return false;									
								}
							}
						}
					}
				}
			}
		}
		return true;
	}
	public static Pieces[][] copyBoard(Pieces[][] board) {
	    Pieces[][] newBoard = new Pieces[8][8];
	    for (int col = 0; col < 8; col++) {
	        for (int row = 0; row < 8; row++) {
	            if (board[col][row] != null) {
	                newBoard[col][row] = board[col][row].clone();
	            }
	        }
	    }
	    return newBoard;
	}
	public static int getKingRow() {
		return kingRow;
	}
	public static int getKingCol() {
		return kingCol;
	}
	
	public static int getAttackRow() {
		return attackRow;
	}
	public static int getAttackCol() {
		return attackCol;
	}
}
