package com.delohat.chess;


import java.io.Serializable;

/**
 * Abstract class for instantiating and initializing a chess piece along with defining setters and getters for chess pieces
 * @author Abdulellah Abualshour
 * @author Fahad Syed
 * 
 */
public abstract class ChessPiece implements Serializable {

	private static final long serialVersionUID = 1L;

	private boolean alive = true; 
	private String color = "";
	
	/**
	 * default constructor for ChessPiece
	 */
	public ChessPiece(){
		this.color = "white";
	}
	
	/**
	 * one arg constructor for ChessPiece
	 * @param colorNew color of piece instance, must be white or black
	 */
	public ChessPiece(String colorNew){
		if(!colorNew.trim().equalsIgnoreCase("white") && !colorNew.trim().equalsIgnoreCase("black")){
			throw new IllegalArgumentException("");
		}
		else{
			this.color = colorNew.trim().toLowerCase();
		}
	}
	
	/**
	 * abstract method declaration for each chess piece type. Has to be implemented by each specific piece class that inherits ChessPice.
	 * @param board board
	 * @param locRow original row
	 * @param locCol original column
	 * @param destRow destination row
	 * @param destCol destination column
	 * @return true or false
	 */
	public abstract boolean canMove(ChessBoard board, int locRow, int locCol, int destRow, int destCol);
	
	/**
	 * converting to string
	 */
	public String toString(){
		return color.substring(0, 1) + "-";
	}
	
	/**
	 * setter for alive status
	 * @param aliveNew boolean status
	 */
	public void setAlive(boolean aliveNew){
		this.alive = aliveNew; 
	}
	
	/**
	 * getter for alive status
	 * @return true or false
	 */
	public boolean getAlive(){
		return this.alive; 
	}
	
	/**
	 * getter for color
	 * @return color black or white
	 */
	public String getColor(){
		return color;
	}
	
	/**
	 * setter for color
	 * @param colorNew new color
	 * @throws IllegalArgumentException if it's not black or white
	 */
	public void setColor(String colorNew)
	throws IllegalArgumentException{
		if(!colorNew.trim().equalsIgnoreCase("white") && !colorNew.trim().equalsIgnoreCase("black")){
			throw new IllegalArgumentException("");
		}
		else{
			this.color = colorNew.trim().toLowerCase();
		}
	}
}