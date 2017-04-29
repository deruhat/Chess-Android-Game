package com.delohat.chess;


import android.util.Log;

/**
 * Contains the set of rules for queen's movement
 * @author Abdulellah Abualshour
 * @author Fahad Syed
 * 
 */
public class Queen extends ChessPiece {

	
	
	/**
	 * default constructor for Queen.
	 */
	public Queen() {}

	/**
	 * one arg constructor for Queen.
	 * @param colorNew color of instance
	 */
	public Queen(String colorNew) {
		super(colorNew);
	}
	
	/**
	 * convert instance to string.
	 */
	public String toString(){
		return getColor().substring(0, 1).toLowerCase() + "Q ";
	}
	
	/**
	 * method that contains the rules for queen's movement. Tests if a move is legal.
	 * @param board board
	 * @param locRow original row
	 * @param locCol original column
	 * @param destRow destination row
	 * @param destCol destination column
	 * @return true or false
	 */
	public boolean canMove(ChessBoard board, int locRow, int locCol, int destRow, int destCol) {
		if(board.doesCoordExist(destRow, destCol) && board.doesCoordExist(locRow, locCol)){
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
				//movement not diagonal, test if it's left-right or up-down
				if(locRow == destRow && locCol != destCol){ //moving left-right
					
					//check to see if path up to destination is clear. 
					int startCol = 0;
					int endCol = 0;
					if(locCol < destCol){
						startCol = locCol;
						endCol = destCol; 
					}
					else{
						startCol = destCol;
						endCol = locCol; 
					}
					
					for (int colCheck = startCol+1; colCheck < endCol; colCheck++){
						if(board.getPiece(destRow, colCheck) != null){
							return false; 
						}
					}
					
					//check to see if an allied piece occupies the destination. 
					if(board.getPiece(destRow, destCol) != null && board.getPiece(destRow, destCol).getColor().equals(pieceColor)){
						return false;
					}
					return true; 
				}
				else if(locRow != destRow && locCol == destCol){ //moving up-down
					//check to see if path up to destination is clear. 
					int startRow = 0;
					int endRow = 0;
					if(locRow < destRow){
						startRow = locRow;
						endRow = destRow; 
					}
					else{
						startRow = destRow;
						endRow = locRow; 
					}
					
					for(int rowCheck = startRow+1; rowCheck < endRow; rowCheck++){
						if(board.getPiece(rowCheck, destCol) != null){
							return false;
						}
					}
					if(board.getPiece(destRow, destCol) != null && board.getPiece(destRow, destCol).getColor().equals(pieceColor)){
						return false;
					}
					
					return true;
				}
				else{
					return false; 
				}
			}	
			else{
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
				//Log.d("QUEEN", "diagRow-1: "+(diagRow-1));
				int rowCheck = locRow + vDirection;
				int colCheck = locCol + hDirection; 
				while(diagCounter != diagRow -1 && diagRow > 0 && diagRow < 8){

					if(board.doesCoordExist(rowCheck, colCheck) && board.getPiece(rowCheck, colCheck) != null){
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
			}
		}
		return true;
	}

}
