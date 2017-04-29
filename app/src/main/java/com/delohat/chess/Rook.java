package com.delohat.chess;


/**
 * Contains the set of rules for rook's movement
 * @author Abdulellah Abualshour
 * @author Fahad Syed
 * 
 */
public class Rook extends ChessPiece {
	
	private boolean hasMoved = false; 
	
	/**
	 * default constructor for Rook.
	 */
	public Rook() {}

	/**
	 * one arg constructor for Rook specifying color.
	 * @param colorNew color of instance
	 */
	public Rook(String colorNew) {
		super(colorNew);
	}

	public Rook clone(){
		Rook clone = new Rook(this.getColor());
		clone.setHasMoved(this.hasMoved);
		return clone;
	}

	/**
	 * converting to string
	 */
	public String toString(){
		return getColor().substring(0, 1).toLowerCase() + "R ";
	}
	
	/**
	 * method to see if the Rook moved at least once.
	 * @return true or false
	 */
	public boolean getHasMoved(){
		return hasMoved; 
	}
	
	/**
	 * sets the Rook first movement indicator.
	 * @param newHasMoved true or false
	 */
	public void setHasMoved(boolean newHasMoved){
		hasMoved = newHasMoved; 
	}
	
	/**
	 * method that contains the rules for rook's movement. Tests if a move is legal.
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
		return false;
	}

}
