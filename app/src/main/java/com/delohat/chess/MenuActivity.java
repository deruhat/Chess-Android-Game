package com.delohat.chess;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {

    ArrayList<GameReplay> previousReplays = new ArrayList<GameReplay>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Button playChess = (Button)findViewById(R.id.PlayChess);
        Button replay = (Button)findViewById(R.id.replayWatch);

        Intent intent = getIntent();
        if(intent != null){
            Bundle b = intent.getExtras();
            if(b != null){
                previousReplays = (ArrayList<GameReplay>)b.get("replays");
                try{
                    //String path = getFilesDir().getPath().toString() + "/replay.dat";
                    FileOutputStream fos= openFileOutput("replay.dat", Context.MODE_PRIVATE);
                    ObjectOutputStream oos= new ObjectOutputStream(fos);
                    oos.writeObject(previousReplays);
                    oos.close();
                    fos.close();
                    for(GameReplay g: previousReplays){
                        Log.d("REPLAYS SAVE", g.getName());
                    }
                }catch(IOException ioe){
                    ioe.printStackTrace();
                }

            }
            else {
                ArrayList<GameReplay> loadedReplays = new ArrayList<GameReplay>();
                try {
                    FileInputStream fis = openFileInput("replay.dat");
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    loadedReplays = (ArrayList) ois.readObject();
                    ois.close();
                    fis.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                    //return;
                } catch (ClassNotFoundException c) {
                    System.out.println("Class not found");
                    c.printStackTrace();
                    //return;
                }
                previousReplays = loadedReplays;
                for (GameReplay g : previousReplays) {
                    Log.d("REPLAYS LOAD", g.getName());
                }
            }
        }

        replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View view){
                replay();
            }
        });
        playChess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play();
            }
        });
    }

    public void play(){
        Intent intent = new Intent(this, FullscreenActivity.class);
        Bundle b = new Bundle();
        b.putSerializable("replays", previousReplays);
        b.putBoolean("mode", true);
        intent.putExtras(b);
        startActivity(intent);
    }

    public void replay(){

        AlertDialog actions;
        String[] options = {"View by Date", "View Alphabetically"};
        DialogInterface.OnClickListener actionListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0){
                    replayByDate();
                }
                else{
                    replayByName();
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose an Option");
        builder.setItems(options, actionListener);
        builder.setNegativeButton("Cancel", null);
        actions = builder.create();
        actions.show();

    }


    public void replayByDate(){
        ArrayList<String> names = new ArrayList<String>();
        for(GameReplay g: previousReplays){
            names.add(g.getName());
        }
        AlertDialog actions;
        DialogInterface.OnClickListener actionListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getApplicationContext(), FullscreenActivity.class);
                Bundle b = new Bundle();
                b.putSerializable("replays", previousReplays);
                b.putSerializable("currentReplay", previousReplays.get(which));
                b.putBoolean("mode", false);
                intent.putExtras(b);
                startActivity(intent);
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose an Option");

        String [] toPass = names.toArray(new String[names.size()]);
        builder.setItems(toPass, actionListener);
        builder.setNegativeButton("Cancel", null);
        actions = builder.create();
        actions.show();
    }

    public void replayByName(){
        ArrayList<String> names = new ArrayList<String>();
        for(GameReplay g: previousReplays){
            names.add(g.getName());
        }
        names.sort(String::compareToIgnoreCase);
        AlertDialog actions;
        DialogInterface.OnClickListener actionListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getApplicationContext(), FullscreenActivity.class);
                Bundle b = new Bundle();
                b.putSerializable("replays", previousReplays);

                String nameToFind = names.get(which);
                GameReplay replay = new GameReplay();
                for(GameReplay g: previousReplays){
                    if(g.getName().equals(nameToFind)){
                        replay = g;
                    }
                }

                b.putSerializable("currentReplay", replay);
                b.putBoolean("mode", false);
                intent.putExtras(b);
                startActivity(intent);
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose an Option");

        String [] toPass = names.toArray(new String[names.size()]);
        builder.setItems(toPass, actionListener);
        builder.setNegativeButton("Cancel", null);
        actions = builder.create();
        actions.show();

    }
}
