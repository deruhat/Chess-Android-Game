package com.delohat.chess;
import java.util.Scanner;

/**
 * Main implementation of a chess game.
 * @author Abdulellah Abualshour
 * @author Fahad Syed
 * 
 */
public class Chess {
	/**
	 * inner class Input to be used in input fetching process.
	 */
	private class Input{
		int locRow;
		int locCol;
		int destRow;
		int destCol;
		String tail = "";
		
		/**
		 * default no-arg constructor.
		 */
		public Input(){}
		
		/**
		 * constructor for input instance.
		 * @param lR original row
		 * @param lC original column
		 * @param dR destination row
		 * @param dC destination column
		 * @param tail contains any extra commands
		 */
		public Input(int lR, int lC, int dR, int dC, String tail){
			locRow = lR;
			locCol = lC; 
			destRow = dR;
			destCol = dC; 
			this.tail = tail; 
		}
	}
	//private Scanner scan = new Scanner(System.in);
	private boolean whitesMove = true; 
	private boolean gameIsRunning = true; 
	private boolean drawCarry = false; 
	private String winner = ""; 
	
	/**
	 * default constructor for chess class.
	 */
	public Chess(){}
	
	/**
	 * input method for getting input.
	 * @return input instance
	 */
	/*public Input getInput(){
		String inputString = scan.nextLine().trim().toLowerCase(); 
		//if the input string has less than 4 characters, return a bad input. 
		if(inputString.length() < 4){
			return new Input(-1, -1, -1, -1, ""); 
		}
		//If a player responds with "draw"
		if(inputString.trim().toLowerCase().startsWith("draw") && drawCarry == true){
			gameIsRunning = false; 
			winner = "Draw";
			return new Input(-1, -1, -1, -1,"");
		}
		//If a player resigns. 
		if(inputString.trim().toLowerCase().equals("resign")){
			gameIsRunning = false;
			if(whitesMove){ winner = "Black wins"; }
			else{ winner = "White wins";}
			return new Input(-1,-1,-1,-1,"");
		}
		int locCol = inputString.charAt(0) - 'a';
		int locRow = inputString.charAt(1) - '1';
		int destCol = inputString.substring(2).trim().charAt(0) - 'a';
		int destRow = inputString.substring(2).trim().charAt(1) - '1';
		String tail = inputString.substring(2).trim().substring(2).trim();
		if(inputString.substring(2).trim().substring(2).trim().length() >= 5){
			if(tail.toLowerCase().equals("draw") || tail.toLowerCase().equals("draw?")){
				drawCarry = true; 
			}
			else{
				drawCarry = false; 
			}
		}
		else{
			
			drawCarry = false; 
		}
		return new Input(locRow, locCol, destRow, destCol, tail);
	}
	*/
	/**
	 * move method to check if a move is legal or not, calls canMove from the override-friendly method of each instance
	 * @param board board used
	 */
	/*
	public void move(ChessBoard board){
		boolean moveLegal = false; 
		String turnColor = "";
		do{
			if(whitesMove){
				board.setEnpassantWhite(new Pawn[1]);
				if(board.check("white")){
					//System.out.println("Check");
				}
				else if(board.check("black")){
					//System.out.println("Checkmate");
					gameIsRunning = false; 
					winner = "White wins";
					return;
				}
				//System.out.print("White's move: ");
				turnColor = "white";
			}
			else{
				board.setEnpassantBlack(new Pawn[1]);
				if(board.check("black")){
					//System.out.println("Check");
				}
				else if(board.check("white")){
					//System.out.println("Checkmate");
					gameIsRunning = false; 
					winner = "Black wins";
					return; 
				}
				//System.out.print("Black's move: ");
				turnColor = "black";
			}
			Input input = getInput();
			//Game ended in a draw or resignation.
			if(input.locCol == -1 && input.locRow == -1 && input.destRow == -1 && input.destCol == -1 
			   && (gameIsRunning == false || !winner.equals(""))){
				return; 
			}
			//Move was legal. 
			if(input.locRow < 8 && input.locCol < 8 && board.getPiece(input.locRow, input.locCol) != null 
			   && board.getPiece(input.locRow, input.locCol).getColor().trim().equals(turnColor)){
				moveLegal = board.movePiece(input.locRow, input.locCol, input.destRow, input.destCol);
				
				//Pawn promotion.
				if(board.getPiece(input.destRow, input.destCol) instanceof Pawn 
				   &&((input.destRow == 7 && board.getPiece(input.destRow, input.destCol).getColor().equals("white")) 
				     || (input.destRow == 0 && board.getPiece(input.destRow, input.destCol).getColor().equals("black")))){
					String pieceColor = board.getPiece(input.destRow, input.destCol).getColor();
					if(input.tail.trim().equalsIgnoreCase("N")){
						board.setPiece(new Knight(pieceColor), input.destRow, input.destCol);
					}
					else if(input.tail.trim().equalsIgnoreCase("B")){
						board.setPiece(new Bishop(pieceColor), input.destRow, input.destCol);
					}
					else if(input.tail.trim().equalsIgnoreCase("R")){
						board.setPiece(new Rook(pieceColor), input.destRow, input.destCol);
					}
					else if(input.tail.trim().equalsIgnoreCase("p")){
						board.setPiece(new Knight(pieceColor), input.destRow, input.destCol);
					}
					else
					{
						board.setPiece(new Queen(pieceColor), input.destRow, input.destCol);
					}
				}
			
			}
			else{
				moveLegal = false; 
			}
			if(moveLegal == false){ System.out.println("Illegal move, try again"); }

		}
		while(moveLegal == false);
		//System.out.println("");
		board.printBoard();
		//System.out.println("");
		whitesMove = !whitesMove;

	}
	*/
	/**
	 * initialization of the game.
	 */
	public void play(){
		ChessBoard board = new ChessBoard(); 
		//board.printBoard();
		//System.out.println("");
		
		//Play the game. 
		while(gameIsRunning){
			//move(board);
		}
		//Print victory/end message
		//System.out.println(winner);
		
		//Reset for next game. 
		drawCarry = false;
		winner = "";
		whitesMove = true;
		gameIsRunning = true; 
		
	}
	
	/**
	 * main method for running the game.
	 * @param args arguments (not relevant in this project)
	 */
//	public static void main(String[] args){
//		Chess game = new Chess();
//		game.play();
//	}
}

