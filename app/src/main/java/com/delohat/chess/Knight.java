package com.delohat.chess;


/**
 * Contains the set of rules for knight's movement
 * @author Abdulellah Abualshour
 * @author Fahad Syed
 * 
 */
public class Knight extends ChessPiece {

	
	
	/**
	 * default constructor for Knight.
	 */
	public Knight() {}

	/**
	 * one arg constructor for Knight specifying color.
	 * @param colorNew color of instance
	 */
	public Knight(String colorNew) {
		super(colorNew);
	}
	
	/**
	 * converts instance to string.
	 */
	public String toString(){
		return getColor().substring(0, 1).toLowerCase() + "N ";
	}

	/**
	 * method that contains the rules for knight's movement. Tests if a move is legal.
	 * @param board board
	 * @param locRow original row
	 * @param locCol original column
	 * @param destRow destination row
	 * @param destCol destination column
	 * @return true or false
	 */
	public boolean canMove(ChessBoard board, int locRow, int locCol, int destRow, int destCol) {
		//System.out.println("here");
		if(board.doesCoordExist(destRow, destCol) && board.doesCoordExist(locRow, locCol)){
			String pieceColor = getColor().trim();
			if(((destRow == locRow+2) && (destCol == locCol+1)) //up
					|| ((destRow == locRow+2) && (destCol == locCol-1)) //up
					|| ((destRow == locRow-2) && (destCol == locCol+1)) //down
					|| ((destRow == locRow-2) && (destCol == locCol-1)) //down
					|| ((destRow == locRow+1) && (destCol == locCol+2)) //right
					|| ((destRow == locRow-1) && (destCol == locCol+2)) //right
					|| ((destRow == locRow+1) && (destCol == locCol-2)) //left
					|| ((destRow == locRow-1) && (destCol == locCol-2))){ ///left
				if(board.getPiece(destRow, destCol) != null && board.getPiece(destRow, destCol).getColor().equals(pieceColor)){ //same color
					return false;
				}
				return true;
			}
		}
		return false;
	}

}
