package chess;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.*;
public class Board extends JPanel{
    private static final long serialVersionUID = 1L;
	private static final int TILE_SIZE = 100;
    private Pieces[][] board;
    private int selectedRow = -1;
    private int selectedCol = -1;
    private int lastRow = -1;
    private int lastCol = -1;
    private Pieces selectedPiece;
    private static Pieces lastMoved = null;
    private PColor turn = PColor.WHITE;
    private boolean useAI = true;
    public Board(Pieces[][] board) {
        this.board = board;
        setPreferredSize(new Dimension(8 * TILE_SIZE, 8 * TILE_SIZE));
        
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                int col = e.getX() / TILE_SIZE;
                int row = e.getY() / TILE_SIZE;
                
                System.out.println("Col: " + col +"\nRow: " + row);
                System.out.println(((board[col][row] == null) ? "null\n" : board[col][row].toString()));
                
                if (board[col][row] != null && board[col][row].getColor() == ((useAI == true) ? PColor.WHITE : turn)) {
                	selectedPiece = board[col][row];
                	selectedCol = col;
                	selectedRow = row;
                } else if (selectedPiece != null && selectedCol != -1 && selectedRow != -1) {
                	if (selectedPiece.isValidMove(col, row, board)) {
                		if (selectedPiece instanceof King && Math.abs(col - selectedCol) == 2) {
                			if (col > selectedCol) {
                				board[5][selectedRow] = board[7][selectedRow];
                				board[7][selectedRow] = null;
                				board[5][selectedRow].setCol(5);
                				((Rook) board[5][selectedRow]).setCastle(false);
                			} else {
                				board[3][selectedRow] = board[0][selectedRow];
                				board[0][selectedRow] = null;
                				board[3][selectedRow].setCol(3);
                				((Rook) board[3][selectedRow]).setCastle(false);
                			}
                		}
                		if (board[col][row] == null && lastMoved != null && 
                				board[selectedCol][selectedRow] instanceof Pawn && col == lastMoved.getCol() 
                				&& selectedRow == lastMoved.getRow()&& lastMoved instanceof Pawn) {
            			    board[col][selectedRow] = null;
                		}
                		board[col][row] = selectedPiece;
                		board[selectedCol][selectedRow] = null;
                		lastCol = selectedCol;
                		lastRow = selectedRow;
                		selectedPiece.setCol(col);
                		selectedPiece.setRow(row);
                		if (selectedPiece instanceof King)
                			((King) selectedPiece).setCastle(false);
                		else if (selectedPiece instanceof Rook)
                			((Rook) selectedPiece).setCastle(false);
                		lastMoved = selectedPiece;
                		selectedPiece = null;
                		if (useAI == false) {
                			if (turn == PColor.WHITE)
                				turn = PColor.BLACK;
                			else
                				turn = PColor.WHITE;
                		} else {
                			turn = PColor.BLACK;
                			long startTime = System.nanoTime();
	                		Ai.Move aiMove = Ai.findBestMove(board, PColor.BLACK, 8);
	                		long endTime = System.nanoTime();
	                		double durationInSeconds = (endTime - startTime) / 1_000_000_000.0;
	                		System.out.printf("AI move took %.3f seconds%n", durationInSeconds);
	                			if (aiMove != null) {
	                				Pieces aiPiece = board[aiMove.col][aiMove.row];
	                				if (aiPiece.isValidMove(aiMove.mCol, aiMove.mRow, board)) {
		                		        board[aiMove.mCol][aiMove.mRow] = aiPiece;
		                		        lastCol = aiMove.col;
		                		        lastRow = aiMove.row;
		                		        board[aiMove.col][aiMove.row] = null;
		                		        aiPiece.setCol(aiMove.mCol);
		                		        aiPiece.setRow(aiMove.mRow);
		                		        if (aiPiece instanceof King && Math.abs(aiMove.col - aiMove.mCol) == 2) {
		                        			if (aiMove.mCol > aiMove.col) {
		                        				board[5][aiMove.row] = board[7][aiMove.row];
		                        				board[7][aiMove.row] = null;
		                        				board[5][aiMove.row].setCol(5);
		                        				((Rook) board[5][aiMove.row]).setCastle(false);
		                        			} else {
		                        				board[3][aiMove.row] = board[0][aiMove.row];
		                        				board[0][aiMove.row] = null;
		                        				board[3][aiMove.row].setCol(3);
		                        				((Rook) board[3][aiMove.row]).setCastle(false);
		                        			}
		                        		}
		                		        if (board[aiMove.mCol][aiMove.mRow] == null && lastMoved != null && 
		                        				aiPiece instanceof Pawn && col == lastMoved.getCol() 
		                        				&& aiMove.mCol == lastMoved.getRow() && lastMoved instanceof Pawn) {
		                    			    board[col][aiMove.mCol] = null;
		                        		}
		                		        lastMoved = aiPiece;
		                		        if (aiPiece instanceof King)
		                		        	((King) aiPiece).setCastle(false);
		                		        else if (aiPiece instanceof Rook)
		                		        	((Rook) aiPiece).setCastle(false);
		                		        turn = PColor.WHITE;
		                		        selectedPiece = null;   
	                				}
	                		    }
                		}
                	} else {
                		selectedCol = -1;
                		selectedRow = -1;
                		selectedPiece = null;
                	}
                }
                
                if (Ai.generateMoves(board, (turn == PColor.WHITE) ? PColor.BLACK : PColor.WHITE) == null && Check.isCheckMate(board, turn) == false) {
                	System.out.println("Stalemate, no valid moves");
                }
                
                repaint();
            }
        });
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if ((row + col) % 2 == 0) {
                    g.setColor(Color.WHITE);
                } else {
                    g.setColor(Color.DARK_GRAY);
                }
                g.fillRect(col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.YELLOW);
        g2.setStroke(new BasicStroke(3));;
        g2.drawRect(lastCol * TILE_SIZE, lastRow * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        if (lastMoved != null)
        	g2.drawRect(lastMoved.getCol() * TILE_SIZE, lastMoved.getRow() * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        if (selectedRow != -1 && selectedCol != -1 && board[selectedCol][selectedRow] != null && selectedPiece != null) {
        	g.setColor(Color.YELLOW);
    		g.fillRect(selectedCol * TILE_SIZE, selectedRow * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }
        if (Check.isInCheck(turn, board) == true) {
        	g.setColor(Color.RED);
        	g.fillRect(Check.getKingCol() * TILE_SIZE, Check.getKingRow() * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        	g2.drawRect(Check.getAttackCol() * TILE_SIZE, Check.getAttackRow() * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        	if (Check.isCheckMate(board, turn) == true) {
        		System.out.println("Cheque mate");
        	}
        }
        for (int row = 0; row < 8; row++) {
        	for (int col = 0; col < 8; col++) {
        		Pieces piece = board[col][row];
                if (piece != null) {
                    BufferedImage pieceImage = piece.getImage();
                    g.drawImage(pieceImage, col * TILE_SIZE, row * TILE_SIZE, null);
                }
        	}
        }
        if (selectedRow != -1 && selectedCol != -1 && board[selectedCol][selectedRow] != null && selectedPiece != null) {
	        for (int row = 0; row < 8; row++) {
	        	for (int col = 0; col < 8; col++) {
	        		if (selectedPiece.isValidMove(col, row, board) == true) {
	        			g.setColor(Color.BLACK);
	        			g.fillRect(col * TILE_SIZE + 35, row * TILE_SIZE + 35, 30, 30);
	        		}
	        	}
	        }
        }
    }
    public static Pieces getLastMoved() {
    	return lastMoved;
    }
}
