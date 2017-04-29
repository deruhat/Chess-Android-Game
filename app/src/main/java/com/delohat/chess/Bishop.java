package com.delohat.chess;


/**
 * Contains the set of rules for bishops movement.
 * @author Abdulellah Abualshour
 * @author Fahad Syed
 * 
 */
public class Bishop extends ChessPiece {

	/**
	 * default constructor for Bishop.
	 */
	public Bishop() {
	}

	/**
	 * one arg constructor for Bishop, specifying color.
	 * @param colorNew color of instance
	 */
	public Bishop(String colorNew) {
		super(colorNew);
	}
	
	/**
	 * converting to string
	 */
	public String toString(){
		return getColor().substring(0, 1).toLowerCase() + "B ";
	}
	
	/**
	 * method that contains the rules for bishop's movement. Tests if a move is legal.
	 * @param board board
	 * @param locRow original row
	 * @param locCol original column
	 * @param destRow destination row
	 * @param destCol destination column
	 * @return true or false
	 */
	public boolean canMove(ChessBoard board, int locRow, int locCol, int destRow, int destCol) {
		if(board.doesCoordExist(destRow, destCol) == false || board.doesCoordExist(locRow, locCol) == false){
			return false;
		}
		String pieceColor = getColor().trim();
		
		int diagRow = 0;
		int diagCol = 0; 
		int hDirection = 0; 
		int vDirection = 0; 
		//Checks if destination is a true diagonal of location.
		diagRow = Math.abs(locRow - destRow);
		diagCol = Math.abs(locCol - destCol);
		int diagCounter = 0; 
		if(diagRow != diagCol){
			return false;
		}
		
		if(destCol > locCol){ 
			hDirection = 1; 
		}
		else{
			hDirection = -1; 
		}
		if(destRow > locRow){
			vDirection = 1; 
		}
		else{
			vDirection = -1; 
		}
		
		int rowCheck = locRow + vDirection;
		int colCheck = locCol + hDirection; 
		while(diagCounter != diagRow-1){
			
			if(!board.doesCoordExist(rowCheck, colCheck) || board.getPiece(rowCheck, colCheck) != null){
				return false; 
			}
			rowCheck += vDirection;
			colCheck += hDirection; 
			diagCounter++; 
		}
			//check to see if allied piece occupies destination. 
			if(board.getPiece(destRow, destCol) != null && board.getPiece(destRow,destCol).getColor().trim().equals(pieceColor)){
				return false;
			}
		return true; 
	}

}
