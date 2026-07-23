package chess;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
public class Ai2 {
	public static class Move {
		public int col, row, mCol, mRow;
		
		public Move(int col, int row, int mCol, int mRow) {
			this.col = col;
			this.row = row;
			this.mCol = mCol;
			this.mRow = mRow;
		}
	}
	
	public static Pieces[][] makeMove(Pieces[][] board, Move move){
	    Pieces[][] simBoard = Check.copyBoard(board);
	    Pieces moved = simBoard[move.col][move.row].clone();
	    moved.setCol(move.mCol);
	    moved.setRow(move.mRow);
	    simBoard[move.mCol][move.mRow] = moved;
	    simBoard[move.col][move.row] = null;
	    return castle(simBoard, move);
	}
	
	private static Pieces[][] castle(Pieces[][] simBoard, Move move){
		if (simBoard[move.mCol][move.mRow] instanceof King && Math.abs(move.mCol - move.col) == 2) {
  			if (move.mCol > move.col) {
  				simBoard[5][move.row] = simBoard[7][move.row]; 
  				simBoard[7][move.row] = null;
  				simBoard[5][move.row].setCol(5);
		 	} else {
		 		simBoard[3][move.row] = simBoard[0][move.row];
		 		simBoard[0][move.row] = null;
		 		simBoard[3][move.row].setCol(3);
		  	}
		}
		return simBoard;
	}
	
	private static int scoreMove(Pieces[][] board, Move move) {
	    Pieces moving = board[move.col][move.row];
	    Pieces target = board[move.mCol][move.mRow];

	    int score = 0;
	    if (target != null) {
	        int attackerValue = getPieceValue(moving);
	        int victimValue = getPieceValue(target);

	        score += 10 * (victimValue - attackerValue);
	    }

	    if (moving instanceof Pawn && (move.mRow == 0 || move.mRow == 7))
	        score += 90;

	    if (moving instanceof King && Math.abs(move.col - move.mCol) == 2)
	        score += 10;

	    return score;
	}

    private static int getPieceValue(Pieces p) {
        if (p instanceof Pawn) return 100;
        if (p instanceof Knight) return 320;
        if (p instanceof Bishop) return 330;
        if (p instanceof Rook) return 500;
        if (p instanceof Queen) return 900;
        if (p instanceof King) return 20000;
        return 0;
    }
	
    public static int evaluateBoard(Pieces[][] board) {
        int score = 0;
        int whiteBishops = 0, blackBishops = 0;
        int whiteDeveloped = 0, blackDeveloped = 0;
        int whiteCenterControl = 0, blackCenterControl = 0;

        for (int col = 0; col < 8; col++) {
            for (int row = 0; row < 8; row++) {
                Pieces piece = board[col][row];
                if (piece == null) continue;

                PColor color = piece.getColor();
                int sign = (color == PColor.WHITE) ? 1 : -1;

                // Material + Positional value
                int value = getPieceValue(piece);
                int positional = getPositionalValue(piece, col, row);
                score += sign * (value + positional);

                // Count bishops for bishop pair bonus
                if (piece instanceof Bishop) {
                    if (color == PColor.WHITE) whiteBishops++; else blackBishops++;
                }

                // Central control bonus
                if ((col == 3 || col == 4) && (row == 3 || row == 4)) {
                    if (color == PColor.WHITE) whiteCenterControl += 1;
                    else blackCenterControl += 1;
                }

                // Development: reward non-pawns/king/rook if moved from initial squares
                if (!(piece instanceof Pawn || piece instanceof King || piece instanceof Rook)) {
                    boolean developed = (color == PColor.WHITE && row < 6) || (color == PColor.BLACK && row > 1);
                    if (developed) {
                        if (color == PColor.WHITE) whiteDeveloped++;
                        else blackDeveloped++;
                    }
                }

                // Penalize early rook activation
                if (piece instanceof Rook) {
                    boolean earlyRook = (color == PColor.WHITE && row < 6) || (color == PColor.BLACK && row > 1);
                    if (earlyRook) score -= sign * 15; // Discourage bad rook shuffling
                }
            }
        }

        // Bishop pair bonus
        if (whiteBishops >= 2) score += 20;
        if (blackBishops >= 2) score -= 20;

        // Development bonus
        score += 6 * (whiteDeveloped - blackDeveloped);

        // Center control bonus
        score += 4 * (whiteCenterControl - blackCenterControl);

        return score;
    }
    
    private static int getPositionalValue(Pieces piece, int col, int row) {
        int[][] pawnTable = {
            {0, 5, 5, -10, -10, 5, 5, 0},
            {0, 10, -5, 0, 0, -5, 10, 0},
            {0, 10, 10, 20, 20, 10, 10, 0},
            {0, 5, 10, 25, 25, 10, 5, 0},
            {0, 0, 0, 20, 20, 0, 0, 0},
            {0, 5, 5, 10, 10, 5, 5, 0},
            {0, 5, 5, -5, -5, 5, 5, 0},
            {0, 0, 0, 0, 0, 0, 0, 0}
        };

        // You can expand this to include more tables per piece type.
        if (piece instanceof Pawn) {
            return (piece.getColor() == PColor.WHITE) ? pawnTable[col][row] : -pawnTable[col][row];
        }

        return 0;
    }
	
	public static List<Move> generateMoves(Pieces[][] board, PColor color) {
		List<Move> moves = new ArrayList<>();
		for (int col = 0; col < 8; col ++) {
			for (int row = 0; row < 8; row++) {
				if (board[col][row] != null && board[col][row].getColor() == color) {
					for (int mCol = 0; mCol < 8; mCol++) {
						for (int mRow = 0; mRow < 8; mRow++) {
							if (board[col][row].isValidMove(mCol, mRow, board)) {
								moves.add(new Move(col, row, mCol, mRow));
							}
						}
					}
				}
			}
		}
		return moves;
	}
	
	public static int minimax(Pieces[][] board, int depth, int alpha, int beta, boolean isMaximizing) {
	    if (depth == 0) return evaluateBoard(board);

	    PColor color = isMaximizing ? PColor.WHITE : PColor.BLACK;
	    List<Move> moves = generateMoves(board, color);
	    moves.sort((a, b) -> Integer.compare(scoreMove(board, b), scoreMove(board, a)));
	    
	    if (moves.isEmpty()) {
	        return Check.isInCheck(color, board) ? 
	            (isMaximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE) : 0;
	    }
	    
	    if (isMaximizing) {
	        int bestScore = Integer.MIN_VALUE;
	        for (Move move : moves) {
	        	Pieces[][] simBoard = makeMove(board, move);

	            int score = minimax(simBoard, depth - 1, alpha, beta, false);
	            bestScore = Math.max(bestScore, score);
	            alpha = Math.max(alpha, score);

	            if (beta <= alpha) break;
	        }
	        return bestScore;
	    } else {
	        int bestScore = Integer.MAX_VALUE;
	        for (Move move : moves) {
	            Pieces[][] simBoard = makeMove(board, move);

	            int score = minimax(simBoard, depth - 1, alpha, beta, true);
	            bestScore = Math.min(bestScore, score);
	            beta = Math.min(beta, score);

	            if (beta <= alpha) break;
	        }
	        return bestScore;
	    }
	}
	
	public static Move findBestMove(Pieces[][] board, PColor aiColor, int depth) {
	    List<Move> moves = generateMoves(board, aiColor);
	    moves.sort((a, b) -> Integer.compare(scoreMove(board, b), scoreMove(board, a)));
	    
	    Move bestMove = null;
	    int bestScore = aiColor == PColor.WHITE ? Integer.MIN_VALUE : Integer.MAX_VALUE;

	    ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()/2);
	    List<Future<ScoredMove>> futures = new ArrayList<>();

	    for (Move move : moves) {
	        Callable<ScoredMove> task = () -> {
	        	Pieces[][] simBoard = makeMove(board, move);
	        	
	            int score = minimax(simBoard, depth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, aiColor != PColor.WHITE);
	            return new ScoredMove(move, score);
	        };
	        futures.add(executor.submit(task));
	    }

	    for (Future<ScoredMove> future : futures) {
	        try {
	            ScoredMove result = future.get();
	            int score = result.score;
	            if ((aiColor == PColor.WHITE && score > bestScore) ||
	                (aiColor == PColor.BLACK && score < bestScore)) {
	                bestScore = score;
	                bestMove = result.move;
	            }
	        } catch (InterruptedException | ExecutionException e) {
	            e.printStackTrace();
	        }
	    }

	    executor.shutdown();

	    if (bestMove != null)
	        System.out.println("AI Col/Row " + bestMove.col + bestMove.row + "\nTo Col/Row " + bestMove.mCol + bestMove.mRow);
	    else {
	        if (moves.isEmpty())
	            System.out.println("No legal moves found");
	        else {
	            System.out.println("No best move determined, defaulting to first");
	            bestMove = moves.get(0);
	        }
	    }
	    System.out.println(moves.size() + " moves were evaluated");
	    return bestMove;
	}
	
	public static Move randomMove(Pieces[][] board, PColor aiColor) {
		List<Move> moves = generateMoves(board, aiColor);
		System.out.println(moves.size() + " moves where found");
		Move selectedMove = (Move) moves.get((int)(Math.random()*moves.size()));
		return selectedMove;
	}
	
	static class ScoredMove {
		Move move;
		int score;
		ScoredMove(Move move, int score) {
			this.move = move;
			this.score = score;
		}
	}
}
