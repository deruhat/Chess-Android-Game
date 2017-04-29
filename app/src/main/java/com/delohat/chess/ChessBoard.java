package com.delohat.chess;

import android.util.Log;
import android.widget.ImageView;
import android.annotation.SuppressLint;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.widget.GridLayout;
import android.view.MotionEvent;
import android.view.View;

import java.io.Serializable;

/**
 * Chessboard matrix implementation.
 * @author Abdulellah Abualshour
 * @author Fahad Syed
 * 
 */
public class ChessBoard implements Serializable {

	private static final long serialVersionUID = 2L;

	private ChessPiece[][] board = new ChessPiece[8][8];
	private ImageView[][] imageBoard = new ImageView[8][8];
	private Pawn[] enpassantWhite = new Pawn[1];
	private Pawn[] enpassantBlack = new Pawn[1];
	private Pawn[] promotionArray = new Pawn[1];

	/**
	 * no argument constructor for creating a chessboard. Sets the board as soon as it's created.
	 */
	public ChessBoard(){
		this.setBoard(); 
	}

	public ChessBoard(ImageView [][] iv){
		this.setBoard();
		this.imageBoard = iv;
		this.setBoardUI();
	}
	
	/**
	 * one argument constructor for debugging purposes.
	 * @param s "debugging"
	 */
	public ChessBoard(String s, ImageView [][] iv){

		this.imageBoard = iv;
		for(int i = 0; i < 8; i++){
			for (int j = 0; j < 8; j++){
				board[i][j] = null; 
			}
		}

		board[6][7] = new Pawn("white");
		board[1][0] = new Pawn("black");

		this.setBoardUI();
	}

	/**
	 * method for setting the chess pieces initially.
	 * 
	 */
	public void setBoard(){
		
		for(int i = 0; i < 8; i++){
			board[1][i] = new Pawn("white");
			board[6][i] = new Pawn("black");
			if(i ==0 || i == 7){
				board[0][i] = new Rook("white");
				board[7][i] = new Rook("black");
			}
			else if(i == 2 || i == 5){
				board[0][i] = new Bishop("white");
				board[7][i] = new Bishop("black");
			}
			else if(i == 3){
				board[0][i] = new Queen("white");
				board[7][i] = new Queen("black");
			}
			else if(i == 4){
				board[0][i] = new King("white");
				board[7][i] = new King("black");
			}
			else if(i == 1 || i == 6){
				board[0][i] = new Knight("white");
				board[7][i] = new Knight("black");
			}
		}


	}


	public void setImageBoard(ImageView[][] iv){
		imageBoard = iv;
	}

	public void setBoardUI(){
		for(ImageView[] X: imageBoard){
			for(ImageView Y: X){
				Y.setImageDrawable(null);
			}
		}

		for(int rows = 0; rows < 8; rows++){
			for(int cols = 0; cols < 8; cols++){
				ChessPiece cp = board[rows][cols];
				if(cp != null){
					if(cp.toString().equalsIgnoreCase("bp ")){
						imageBoard[rows][cols].setImageResource(R.drawable.bpawn);
					}
					else if(cp.toString().equalsIgnoreCase("wp ")){
						imageBoard[rows][cols].setImageResource(R.drawable.wpawn);
					}
					else if(cp.toString().equalsIgnoreCase("bB ")){
						imageBoard[rows][cols].setImageResource(R.drawable.bbishop);
					}
					else if(cp.toString().equalsIgnoreCase("bk ")){
						imageBoard[rows][cols].setImageResource(R.drawable.bking);
					}
					else if(cp.toString().equalsIgnoreCase("bn ")){
						imageBoard[rows][cols].setImageResource(R.drawable.bknight);
					}
					else if(cp.toString().equalsIgnoreCase("bq ")){
						imageBoard[rows][cols].setImageResource(R.drawable.bqueen);
					}
					else if(cp.toString().equalsIgnoreCase("br ")){
						imageBoard[rows][cols].setImageResource(R.drawable.brook);
					}
					else if(cp.toString().equalsIgnoreCase("wB ")){
						imageBoard[rows][cols].setImageResource(R.drawable.wbishop);
					}
					else if(cp.toString().equalsIgnoreCase("wk ")){
						imageBoard[rows][cols].setImageResource(R.drawable.wking);
					}
					else if(cp.toString().equalsIgnoreCase("wn ")){
						imageBoard[rows][cols].setImageResource(R.drawable.wknight);
					}
					else if(cp.toString().equalsIgnoreCase("wq ")){
						imageBoard[rows][cols].setImageResource(R.drawable.wqueen);
					}
					else if(cp.toString().equalsIgnoreCase("wr ")){
						imageBoard[rows][cols].setImageResource(R.drawable.wrook);
					}
				}
				else{
					//Log.d("NOTICE", "It is null");
				}
			}
		}
	}
	/**
	 * method for printing out the matrix in user-friendly interface.
	 */
	public String printBoard(){
		boolean squareColor = true;
		String boardString = "\n";
		for(int rows = 0; rows <= 8; rows++){
			for(int cols = 0; cols <= 8; cols++){
				if(rows < 8){
					if(cols < 8){
						if(board[rows][cols] == null){
							if(squareColor == true){
								boardString += "## ";
							}
							else{
								boardString += "   ";
							}
						}
						else{
							ChessPiece x = board[rows][cols];
							boardString += x;
						}
					}
					else{
						boardString += (8-rows)+"\n";
					}
					squareColor = !squareColor;
				}
				else{
					boardString += " a  b  c  d  e  f  g  h \n";
					cols = 9; 
				}
			}
		}
		return boardString;
	}
	
	/**
	 * method for checking if the coordinate exists in the matrix.
	 * @param x row
	 * @param y column
	 * @return true or false
	 */
	public boolean doesCoordExist(int x, int y){
		return (x < 8 && x >= 0 && y < 8 && y >= 0);
	}
	
	/**
	 * method for getting a piece from a coordinate
	 * @param row row 
	 * @param col col
	 * @return chess piece
	 */
	public ChessPiece getPiece(int row, int col){
		return board[row][col];
	}
	
	/**
	 * method for assigning a piece to a coordinate.
	 * @param piece piece instance
	 * @param row row
	 * @param col col
	 */
	public void setPiece(ChessPiece piece, int row, int col){
		board[row][col] = piece; 
	}
	
	/**
	 * method for getting the en passant white piece.
	 * @return enpassantWhite
	 */
	public Pawn[] getEnpassantWhite(){
		return enpassantWhite;
	}
	
	/**
	 * method for setting the en passant white piece when en passant is possible.
	 * @param newEnpassant Piece that jumped twice next to another piece of different color
	 */
	public void setEnpassantWhite(Pawn[] newEnpassant){
		this.enpassantWhite = newEnpassant;
	}
	
	/**
	 * method for getting the en passant black piece.
	 * @return enpassantBlack
	 */
	public Pawn[] getEnpassantBlack(){
		return enpassantBlack;
	}
	
	/**
	 * method for setting the en passant black piece when en passant is possible.
	 * @param newEnpassant Piece that jumped twice next to another piece of different color
	 */
	public void setEnpassantBlack(Pawn[] newEnpassant){
		this.enpassantBlack = newEnpassant;
	}

	public void setPromotionArray(Pawn[] pa){
		promotionArray = pa;
	}

	public Pawn[] getPromotionArray(){
		return promotionArray;
	}


	/**
	 * method that uses the rules of a specific pice the given coordinates and move it if legal.
	 * @param locRow original row
	 * @param locCol original column
	 * @param destRow destination row
	 * @param destCol destination column
	 * @return true or false
	 */
	public boolean movePiece(int locRow, int locCol, int destRow, int destCol){

		promotionArray[0] = null;

		//Log.d("MOVE", "movePiece() is called...");
        if(!doesCoordExist(locRow, locCol) || !doesCoordExist(destRow, destCol)){
            return false;
        }
		ChessPiece piece = board[locRow][locCol];
		if(piece != null && piece.canMove(this, locRow, locCol, destRow, destCol)){
			ChessPiece destPiece = board[destRow][destCol]; //possibly used later. 
			board[locRow][locCol] = null;
			board[destRow][destCol] = piece;
			if(piece instanceof Pawn){ //to keep track of move 2 on first turn.
				((Pawn)piece).setHasMoved(true);
				if(((Pawn)piece).getEnPassantLeft() || ((Pawn)piece).getEnPassantRight()){
					if(((Pawn)piece).getColor().trim().equalsIgnoreCase("black")){
						board[destRow+1][destCol] = null;
					}
					else{
						board[destRow-1][destCol] = null;
					}
				}
				//pawn promotion
				if((destRow == 0 && piece.getColor().equalsIgnoreCase("black"))
					|| (destRow == 7 && piece.getColor().equalsIgnoreCase("white"))){
					promotionArray[0] = (Pawn)piece;
				}

				if(!((Pawn)piece).getDoubleMove()){
					setEnpassantBlack(new Pawn[1]);
					setEnpassantWhite(new Pawn[1]);
				}
			}
			else if(piece instanceof King){//to keep track of castling.
                if(((King)piece).getCastleLeft()){
                    board[destRow][destCol+1] = board[destRow][0];
                    board[destRow][0] = null;
                }
                else if(((King)piece).getCastleRight()){
                    board[destRow][destCol-1] = board[destRow][7];
                    board[destRow][7] = null;
                }
				((King)piece).setHasMoved(true);
			}
			else if(piece instanceof Rook){// to keep track of castling.
				((Rook)piece).setHasMoved(true);
			}
			return true; 
		}
		else{
			return false; 
		}
	}

	/**
	 * implementation of check in chess.
	 * @param pieceColor color of the piece
	 * @return true or false
	 * @throws IllegalArgumentException not white or black, illegal
	 */
	public boolean check(String pieceColor)
	throws IllegalArgumentException{
		
		//Checks to make sure color was correctly given. 
		if(!pieceColor.trim().toLowerCase().equals("black") && !pieceColor.trim().toLowerCase().equals("white")){
			throw new IllegalArgumentException("");
		} 
		//Finds king. 
		int kingRow = -1;
		int kingCol = -1; 
		for(int row = 0; row < 8; row++){
			for(int col = 0; col < 8; col++){
				ChessPiece piece = board[row][col];
				if(piece != null && piece instanceof King && piece.getColor().trim().toLowerCase().equals(pieceColor)){
					kingRow = row; 
					kingCol = col; 
					row = 9;
					col = 9; 
				}
			}
		}
		//Makes sure king exists. 
		if(kingRow == -1 || kingCol == -1){
			return false; 
		}
		
		//Searches to see if the king is in check. 
		for(int row = 0; row < 8; row++){
			for(int col = 0; col < 8; col++){
				ChessPiece piece = board[row][col];
				if(piece != null && !piece.getColor().trim().equals(pieceColor) && piece.canMove(this, row, col, kingRow, kingCol)){
					return true; 
				}
			}
		}
		return false; 
	}

	public boolean checkmate(String pieceColor){
		//Finds king.
		int kingRow = -1;
		int kingCol = -1;
		for(int row = 0; row < 8; row++){
			for(int col = 0; col < 8; col++){
				ChessPiece piece = board[row][col];
				if(piece != null && piece instanceof King && piece.getColor().trim().toLowerCase().equals(pieceColor)){
					kingRow = row;
					kingCol = col;
					row = 9;
					col = 9;
				}
			}
		}
		//Makes sure king exists.
		if(kingRow == -1 || kingCol == -1){
			return true;
		}
		return false;
	}

}
