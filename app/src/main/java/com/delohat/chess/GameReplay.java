package com.delohat.chess;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Deadb on 4/25/2017.
 */

public class GameReplay implements Serializable{

    private static final long serialVersionUID = 3L;

    private ArrayList<ChessBoard> replayFrames = new ArrayList<ChessBoard>();
    private String name = "";

    public GameReplay(){
    }

    public String getName(){ return name; }
    public void setName(String newName){ name = newName; }

    public ArrayList<ChessBoard> getChessBoard(){
        return replayFrames;
    }
    public void setChessBoard(ArrayList<ChessBoard> cB){
        replayFrames = cB;
    }
}
