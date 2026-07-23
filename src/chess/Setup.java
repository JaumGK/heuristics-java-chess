package chess;
public class Setup {
	public static Pieces[][] initializeBoard(int a){
		Pieces[][] board = new Pieces[8][8];
		return board;
	}
	public static Pieces[][] initializeBoard() {
        Pieces[][] board = new Pieces[8][8];
             
        for (int col = 0; col < 8; col++) {
            board[col][0] = createPieceByTypeAndColor(col, 0, PColor.BLACK);
            board[col][1] = new Pawn(col, 1, PColor.BLACK);
        }
        
        for (int col = 0; col < 8; col++) {
            board[col][6] = new Pawn(col, 6, PColor.WHITE);
            board[col][7] = createPieceByTypeAndColor(col, 7, PColor.WHITE);
        }
        
        return board;
	}
	
    private static Pieces createPieceByTypeAndColor(int col, int row, PColor color) {
        switch (col) {
            case 0: 
            case 7:
                return new Rook(col, row, color);
            case 1:
            case 6:
                return new Knight(col, row, color);
            case 2:
            case 5:
                return new Bishop(col, row, color);
            case 3:
                return new Queen(col, row, color);
            case 4:
                return new King(col, row, color);
            default:
                throw new IllegalArgumentException("Invalid column for piece: " + col);
        }
    }
}
