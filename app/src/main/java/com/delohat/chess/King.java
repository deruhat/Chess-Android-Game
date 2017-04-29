package com.delohat.chess;


import android.util.Log;

/**
 * Contains the set of rules for king's movement
 * @author Abdulellah Abualshour
 * @author Fahad Syed
 * 
 */
public class King extends ChessPiece {
	
	private boolean hasMoved = false; 
	private boolean castleLeft = false;
	private boolean castleRight = false;
	/**
	 * default constructor for King.
	 */
	public King() {}

	/**
	 * one arg constructor for King specifying color.
	 * @param colorNew color of instance
	 */
	public King(String colorNew) {
		super(colorNew);
	}

	public King clone(){
		King clone = new King(this.getColor());
		clone.setHasMoved(this.hasMoved);
		clone.setCastleLeft(this.castleLeft);
		clone.setCastleRight(this.castleRight);
		return clone;
	}


	/**
	 * convert instance to string.
	 */
	public String toString(){
		return getColor().substring(0, 1).toLowerCase() + "K ";
	}
	
	/**
	 * method to see if the King moved at least once.
	 * @return true or false
	 */
	public boolean getHasMoved(){
		return hasMoved; 
	}
	
	/**
	 * sets the King first movement indicator.
	 * @param newHasMoved true or false
	 */
	public void setHasMoved(boolean newHasMoved){
		hasMoved = newHasMoved; 
	}

	public boolean getCastleLeft(){ return castleLeft; }
	public boolean getCastleRight(){ return castleRight; }
	public void setCastleLeft(boolean cL){castleLeft = cL;}
	public void setCastleRight(boolean cR){castleRight = cR;}

	/**
	 * method that contains the rules for king's movement. Tests if a move is legal.
	 * @param board board
	 * @param locRow original row
	 * @param locCol original column
	 * @param destRow destination row
	 * @param destCol destination column
	 * @return true or false
	 */
	public boolean canMove(ChessBoard board, int locRow, int locCol, int destRow, int destCol) {
		if(board.doesCoordExist(destRow, destCol) && board.doesCoordExist(locRow, locCol)){
			String pieceColor = this.getColor().trim();
			castleRight = false;
			castleLeft = false;

			//castle left
			if(board.doesCoordExist(destRow, destCol) && hasMoved == false && locRow == destRow && locCol == destCol+2){
				//check b, c, d,
				if(board.getPiece(destRow, destCol) == null && board.getPiece(locRow, 1) == null && board.getPiece(locRow, 2) == null && board.getPiece(locRow, 3) == null
					&& board.getPiece(destRow, 0) instanceof Rook && ((Rook)board.getPiece(destRow, 0)).getHasMoved() == false){
					castleLeft = true;
					Log.d("KING", "Castle Left.");
					return true; 
				}
				return false; 
			}
			//castle right
			if(board.doesCoordExist(destRow, destCol) && hasMoved == false && locRow == destRow && locCol == destCol-2){
				if(board.getPiece(destRow, destCol) == null && board.getPiece(locRow, 5) == null
					&& board.getPiece(locRow, 6) == null  && board.getPiece(destRow, 7) instanceof Rook
					&& ((Rook)board.getPiece(destRow, 7)).getHasMoved() == false){
					castleRight = true;
					Log.d("KING", "Castle Right.");
					return true; 
				}
				return false; 
			}
			//below and above.
			if((locRow == destRow+1 || locRow == destRow-1) && (locCol == destCol)){
				if(board.doesCoordExist(destRow, destCol) && board.getPiece(destRow, destCol) == null || !board.getPiece(destRow, destCol).getColor().equals(pieceColor)){
					return true;
				}
				else{
					return false;
				}
			}//diagonals
			else if((locRow == destRow+1) && (locCol == destCol+1 || locCol == destCol-1) || (locRow == destRow-1) && (locCol == destCol+1 || locCol == destCol-1)){
				if(board.doesCoordExist(destRow, destCol) && board.getPiece(destRow, destCol) == null || !board.getPiece(destRow, destCol).getColor().equals(pieceColor)){ //same color, do not attack
					return true; 
				}
				else{
					return false; 
				}
			}
			//left and right. 
			else if((locRow == destRow) && (locCol == destCol+1 || locCol == destCol-1)){
				if(board.doesCoordExist(destRow, destCol) && board.getPiece(destRow, destCol) == null || !board.getPiece(destRow, destCol).getColor().equals(pieceColor)){ //same color, do not attack
					return true; 
				}
				else{
					return false; 
				}
			}
		}
		return false;
	}

}
