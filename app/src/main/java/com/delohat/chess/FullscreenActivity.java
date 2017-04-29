package com.delohat.chess;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {

    private ChessBoard myChessBoard;
    private ChessBoard prevChessBoard;
    private ImageView[][] iv;
    private ChessPiece currentlySelectedPiece;
    private int pieceRow = -1;
    private int pieceCol = -1;
    private boolean whitesTurn = true;
    private boolean gameIsRunning = true;
    private boolean whiteResigned = false;
    private boolean blackResigned = false;
    private GameReplay replay;
    private boolean mode;
    private int counter = 0;

    //will be gained from previous activities
    private ArrayList<GameReplay> previousReplays;

    protected void announceCheck(){
        //announces check for black.
        if(myChessBoard.check("black") && whitesTurn){
            Toast.makeText(this.getBaseContext(), "Black is under check!",
                    Toast.LENGTH_LONG).show();
        }
        //announces check for white.
        if(myChessBoard.check("white") && !whitesTurn){
            Toast.makeText(this.getBaseContext(), "White is under check!",
                    Toast.LENGTH_LONG).show();
        }
    }
    protected void announceCheckMate(Boolean flag){
        if(flag) {
            savePreviousBoard();
        }
        String title = "";
        String message = "Do you want to save a replay of this game?";
        if((whitesTurn && myChessBoard.checkmate("black")) || blackResigned){
            title = "Checkmate: White Wins!";
        }
        else if((!whitesTurn && myChessBoard.checkmate("white")) || whiteResigned) {
            title = "Checkmate: Black Wins!";
        }
        else{
            return;
        }
        gameIsRunning = false;
        whitesTurn = true;
        myChessBoard.setBoardUI();
        currentlySelectedPiece = null;
        pieceCol = -1;
        pieceRow = -1;
        new AlertDialog.Builder(FullscreenActivity.this)
                .setTitle(title)
                .setMessage("Do you want to save a replay of this game?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        saveReplay();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                        Bundle b = new Bundle();
                        b.putSerializable("replays", previousReplays);
                        intent.putExtras(b);
                        startActivity(intent);
                    }
                })
                .show();


        return;
    }

    protected void AI(){

        savePreviousBoard();

        //add all pieces of the right color to the array so long as they can move somewhere.
        ArrayList<ChessPiece> movesArray = new ArrayList<ChessPiece>();
        ArrayList<Integer> rows = new ArrayList<Integer>();
        ArrayList<Integer> cols = new ArrayList<Integer>();
        ChessPiece pick = null;

        int row = -1;
        int col = -1;
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                ChessPiece cp = myChessBoard.getPiece(i, j);
                if(cp != null){
                    if ((cp.getColor().equals("white") && whitesTurn) || (cp.getColor().equals("black") && !whitesTurn)){
                        int moveCount = 0;
                        for(int x = 0; x < 8; x++){
                            for(int y = 0; y < 8; y++){
                                if((i != x || j != y) && cp.canMove(myChessBoard, i, j, x, y)){
                                    moveCount++;
                                }
                            }
                        }
                        if(moveCount > 0){
                            movesArray.add(cp);
                        }
                    }
                }
            }
        }

        //pick a piece at random.
        int index = (int)(Math.random() * movesArray.size());
        if(index > movesArray.size() || index < 0){
            index = 0;
        }

        pick = movesArray.get(index);
        movesArray.clear();

        //populate row and col indices
        for(int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                if(pick.equals(myChessBoard.getPiece(i, j))){
                    row = i;
                    col = j;
                }
            }
        }

        //populate rows and cols arraylists
        for(int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                if(pick.canMove(myChessBoard, row, col, i, j) && (row != i || col != j)){
                    rows.add(i);
                    cols.add(j);
                }
            }
        }

        //pick a move at random.
        index = (int)(Math.random() * rows.size());
        if(index > rows.size() || index < 0){
            index = 0;
        }

        myChessBoard.movePiece(row, col, rows.get(index), cols.get(index));

        //Pawn promotion
        Pawn[] pa = myChessBoard.getPromotionArray();
        if(pa[0] != null){
            Log.d("MAIN", "PAWN PROMOTION SHOULD OCCUR");
            myChessBoard.setPiece(new Queen(pick.getColor()), rows.get(index), cols.get(index));
        }
        myChessBoard.setBoardUI();
        currentlySelectedPiece = null;
        pieceCol = -1;
        pieceRow = -1;
        whitesTurn = !whitesTurn;
        return;
    }

    protected void createBoard() {
        ImageView[][] iv_new = new ImageView[8][8];
        GridLayout gridLayout = (GridLayout) findViewById(R.id.gridBoard);
        for(int i = 0; i < 8; i++){
            for(int y = 0; y < 8; y++){
                iv_new[i][y] = (ImageView) gridLayout.getChildAt(i*8+y);
            }
        }
        this.iv = iv_new;
        prevChessBoard = null;
        myChessBoard = new ChessBoard(iv);

    }

    protected void draw(){
        new AlertDialog.Builder(FullscreenActivity.this)
                .setTitle("Draw")
                .setMessage("Are you sure you want to draw?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        new AlertDialog.Builder(FullscreenActivity.this)
                                .setTitle("Draw!")
                                .setMessage("Do you want to save a replay of this game?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // continue with delete
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // do nothing
                                    }
                                })
                                .show();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .show();
    }

    public void nextFrame(){
        if(counter >= replay.getChessBoard().size()){
            Toast.makeText(this, "Replay is done",
                    Toast.LENGTH_LONG).show();
            return;
        }
        myChessBoard = replay.getChessBoard().get(counter);
        Log.d("STUFF", myChessBoard.printBoard());
        myChessBoard.setImageBoard(iv);
        myChessBoard.setBoardUI();
        myChessBoard.setImageBoard(null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);

        //get previous replays from menu activity.
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        previousReplays = (ArrayList<GameReplay>)b.get("replays");
        mode = b.getBoolean("mode");

        if (mode == false) {
            Button undo = (Button)findViewById(R.id.undo);
            Button AI = (Button)findViewById(R.id.AI);
            Button resign = (Button)findViewById(R.id.Resign);
            Button draw = (Button)findViewById(R.id.Draw);
            Button next = (Button)findViewById(R.id.Next);

            undo.setVisibility(View.GONE);
            AI.setVisibility(View.GONE);
            resign.setVisibility(View.GONE);
            draw.setVisibility(View.GONE);

            replay = (GameReplay)b.get("currentReplay");
            GridLayout gridLayout = (GridLayout) findViewById(R.id.gridBoard);
            createBoard();

            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    nextFrame();
                    counter++;
                }
            });
        }
        //only happen if mode is true
        if(mode == true){
            GameReplay replay = new GameReplay();
            GridLayout gridLayout = (GridLayout) findViewById(R.id.gridBoard);
            createBoard();
            setClickEvents();

            Button undo = (Button)findViewById(R.id.undo);
            Button AI = (Button)findViewById(R.id.AI);
            Button resign = (Button)findViewById(R.id.Resign);
            Button draw = (Button)findViewById(R.id.Draw);
            Button next = (Button)findViewById(R.id.Next);
            next.setVisibility(View.GONE);

            undo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    undo();
                }
            });

            AI.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AI();
                }
            });

            draw.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    draw();
                }
            });

            resign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    resign();
                }
            });
        }

    }

    protected void play(View v){
        //set up variables.

        if(gameIsRunning == false){
            gameIsRunning = true;
            myChessBoard = null;
            prevChessBoard = null;
            whitesTurn = true;
            whiteResigned = false;
            blackResigned = false;
            createBoard();
            setClickEvents();
        }

        GridLayout gridLayout = (GridLayout) findViewById(R.id.gridBoard);
        int row = -1;
        int col = -1;
        //locate our currently clicked on imageView
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                ImageView temp = iv[i][j];
                temp.setBackgroundResource(0);
                if(temp == v) {
                    row = i;
                    col = j;
                }
            }
        }
        //move a piece if we had a piece already currently selected.
        if(currentlySelectedPiece != null && myChessBoard != null && currentlySelectedPiece.canMove(myChessBoard, pieceRow, pieceCol, row, col)){

            savePreviousBoard();
            Boolean moved = myChessBoard.movePiece(pieceRow, pieceCol, row, col);
            Log.d("MAIN", "moved piece: "+moved);
            if(moved == false){
                currentlySelectedPiece = null;
                pieceCol = -1;
                pieceRow = -1;
                return;
            }

            boolean suicialMove = preventSuicidalMoves(v, row, col);
            if(suicialMove){
                currentlySelectedPiece = null;
                pieceCol = -1;
                pieceRow = -1;
                return;
            }

            //Pawn promotion
            Pawn[] pa = myChessBoard.getPromotionArray();
            if(pa[0] != null){
                promotePawn(pa[0].getColor(), row, col);
            }

            //declare checkmate
            announceCheckMate(false);
            if(gameIsRunning == false){
                return;
            }

            //announces check.
            announceCheck();

            //clears board and moves images.
            whitesTurn = !whitesTurn;
            myChessBoard.setBoardUI();
            currentlySelectedPiece = null;
            pieceCol = -1;
            pieceRow = -1;
            return;
        }

        //selected the piece on the currently selected square.
        currentlySelectedPiece = myChessBoard.getPiece(row, col);
        if(currentlySelectedPiece != null){
            if( whitesTurn && currentlySelectedPiece.getColor().trim().equalsIgnoreCase("black")
                    || !whitesTurn && currentlySelectedPiece.getColor().trim().equalsIgnoreCase("white")){
                return;
            }
            pieceCol = col;
            pieceRow = row;
            v.setBackgroundResource(R.drawable.highlight);

            //highlight all squares the piece can move to.
            for(int i = 0; i < 8; i++){
                for(int j = 0; j < 8; j++){
                    if(currentlySelectedPiece.canMove(myChessBoard, row, col, i, j)){
                        gridLayout.getChildAt(i*8+j).setBackgroundResource(R.drawable.highlight);
                    }
                }
            }
        }
        else{
            pieceCol = -1;
            pieceRow = -1;
        }
    }

    protected boolean preventSuicidalMoves(View v, int row, int col){
        //prevents suicidal moves for black.
        if(!whitesTurn && myChessBoard.check("black")){
            Toast.makeText(this.getBaseContext(), "This move would put you in checkmate.",
                    Toast.LENGTH_LONG).show();
            myChessBoard.movePiece(row, col, pieceRow, pieceCol);
            for(int i = 0; i < 8; i++){
                for(int j = 0; j < 8; j++){
                    ImageView temp = iv[i][j];
                    temp.setBackgroundResource(0);
                    if(temp == v) {
                        row = i;
                        col = j;
                    }
                }
            }
            return true;
        }
        //prevents suicidal moves for white.
        if(whitesTurn && myChessBoard.check("white")){
            Toast.makeText(this.getBaseContext(), "This move would put you in checkmate.",
                    Toast.LENGTH_LONG).show();
            myChessBoard.movePiece(row, col, pieceRow, pieceCol);
            for(int i = 0; i < 8; i++){
                for(int j = 0; j < 8; j++){
                    ImageView temp = iv[i][j];
                    temp.setBackgroundResource(0);
                    if(temp == v) {
                        row = i;
                        col = j;
                    }
                }
            }
            return true;
        }
        return false;
    }

    protected void promotePawn(String color, int row, int col){
        CharSequence colors[] = new CharSequence[] {"bishop", "knight", "pawn","queen", "rook"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick a Piece to Promote to:");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0){
                    myChessBoard.setPiece(new Bishop(color), row, col);
                }
                else if(which == 1){
                    myChessBoard.setPiece(new Knight(color), row, col);
                }
                else if(which == 2){
                    myChessBoard.setPiece(new Pawn(color), row, col);
                }
                else if(which == 3){
                    myChessBoard.setPiece(new Queen(color), row, col);
                }
                else if(which == 4){
                    myChessBoard.setPiece(new Rook(color), row, col);
                }
                myChessBoard.setBoardUI();
            }
        });
        builder.show();
    }

    protected void resign(){
        new AlertDialog.Builder(FullscreenActivity.this)
                .setTitle("Resign")
                .setMessage("Are you sure you want to resign?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(whitesTurn){
                            whiteResigned = true;
                        }
                        else{
                            blackResigned = true;
                        }
                        announceCheckMate(true);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .show();
    }

    protected void savePreviousBoard(){

        //create a frame for the previous chessBoard;
        ChessBoard replayFrame = new ChessBoard();

        //set the previous board.
        if(prevChessBoard == null){
            prevChessBoard = new ChessBoard(iv);
        }
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                boolean foundEnPassantBlack = false;
                boolean foundEnPassantWhite = false;

                prevChessBoard.setPiece(null, i, j);
                replayFrame.setPiece(null, i, j);
                ChessPiece piece = myChessBoard.getPiece(i, j);
                if(piece != null) {
                    if(myChessBoard.getEnpassantBlack() != null && piece.equals(myChessBoard.getEnpassantBlack()[0])){
                        foundEnPassantBlack = true;
                    }
                    else{
                        foundEnPassantBlack = false;
                    }
                    if(myChessBoard.getEnpassantWhite() != null && piece.equals(myChessBoard.getEnpassantWhite()[0])){
                        foundEnPassantWhite = true;
                    }
                    else{
                        foundEnPassantWhite = false;
                    }
                    if(piece instanceof Rook){
                        Rook rookPiece = ((Rook)piece).clone();
                        prevChessBoard.setPiece(rookPiece, i, j);
                        replayFrame.setPiece(rookPiece, i, j);
                    }
                    else if(piece instanceof Pawn){
                        Pawn pawnPiece = ((Pawn)piece).clone();
                        prevChessBoard.setPiece(pawnPiece, i, j);
                        replayFrame.setPiece(pawnPiece, i, j);
                        if(foundEnPassantBlack){
                            Pawn[] pa = new Pawn[1];
                            pa[0] = pawnPiece;
                            prevChessBoard.setEnpassantBlack(pa);
                            replayFrame.setEnpassantBlack(pa);
                            foundEnPassantBlack = false;
                        }
                        else if(foundEnPassantWhite){
                            Pawn[] pa = new Pawn[1];
                            pa[0] = pawnPiece;
                            prevChessBoard.setEnpassantWhite(pa);
                            replayFrame.setEnpassantWhite(pa);
                            foundEnPassantWhite = false;
                        }
                    }
                    else if(piece instanceof King){
                        King kingPiece = ((King)piece).clone();
                        prevChessBoard.setPiece(kingPiece, i, j);
                        replayFrame.setPiece(kingPiece, i, j);
                    }
                    else {
                        prevChessBoard.setPiece(piece, i, j);
                        replayFrame.setPiece(piece, i, j);
                    }
                }
            }
        }
        Pawn[] promotionArray = new Pawn[1];
        Pawn[] otherPromotionArray = myChessBoard.getPromotionArray();
        if(otherPromotionArray[0] != null) {
            promotionArray[0] = otherPromotionArray[0].clone();
            prevChessBoard.setPromotionArray(promotionArray);
            replayFrame.setPromotionArray(promotionArray);
        }
        if(replay == null){
            replay = new GameReplay();
        }
        ArrayList<ChessBoard> replayFrames = replay.getChessBoard();
        replayFrames.add(replayFrame);
        replay.setChessBoard(replayFrames);
        Log.d("PREV BOARD SAVE: ", prevChessBoard.printBoard());
    }

    protected void saveReplay(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Replay Name:");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String mText = input.getText().toString();
                replay.setName(mText);
                previousReplays.add(replay);

                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                Bundle b = new Bundle();
                b.putSerializable("replays", previousReplays);
                intent.putExtras(b);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                announceCheckMate(true);
            }
        });

        builder.show();
    }

    protected void setClickEvents(){
        for(int i = 0; i < 8; i++){
            for(int y = 0; y < 8; y++){
                ImageView imageView = iv[i][y];
                imageView.setOnClickListener(new View.OnClickListener(){

                    public void onClick(View v){
                        play(v);
                    }
                });
            }
        }
    }

    protected void undo(){

        if(prevChessBoard == null){
            return;
        }
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){

                boolean foundEnPassantBlack = false;
                boolean foundEnPassantWhite = false;

                myChessBoard.setPiece(null, i, j);
                ChessPiece piece = prevChessBoard.getPiece(i, j);
                if(piece != null) {
                    if(prevChessBoard.getEnpassantBlack() != null && piece.equals(prevChessBoard.getEnpassantBlack()[0])){
                        foundEnPassantBlack = true;
                    }
                    if(prevChessBoard.getEnpassantWhite() != null && piece.equals(prevChessBoard.getEnpassantWhite()[0])){
                        foundEnPassantWhite = true;
                    }
                    if(piece instanceof Rook){
                        Rook rookPiece = ((Rook)piece).clone();
                        myChessBoard.setPiece(rookPiece, i, j);
                    }
                    else if(piece instanceof Pawn){
                        Pawn pawnPiece = ((Pawn)piece).clone();
                        myChessBoard.setPiece(pawnPiece, i, j);
                        if(foundEnPassantBlack){
                            Pawn[] pa = new Pawn[1];
                            pa[0] = pawnPiece;
                            myChessBoard.setEnpassantBlack(pa);
                        }
                        else if(foundEnPassantWhite){
                            Pawn[] pa = new Pawn[1];
                            pa[0] = pawnPiece;
                            myChessBoard.setEnpassantWhite(pa);
                        }
                    }
                    else if(piece instanceof King){
                        King kingPiece = ((King)piece).clone();
                        myChessBoard.setPiece(kingPiece, i, j);
                    }
                    else {
                        myChessBoard.setPiece(piece, i, j);
                    }
                }
            }
        }
        Pawn[] promotionArray = new Pawn[1];
        Pawn[] otherPromotionArray = prevChessBoard.getPromotionArray();
        if(otherPromotionArray[0] != null) {
            promotionArray[0] = otherPromotionArray[0].clone();
            myChessBoard.setPromotionArray(promotionArray);
        }

        prevChessBoard = null;
        currentlySelectedPiece = null;
        pieceCol = -1;
        pieceRow = -1;
        whitesTurn = !whitesTurn;
        myChessBoard.setBoardUI();
        replay.getChessBoard().remove(replay.getChessBoard().size()-1);
    }

}
