package canals.nuria.tictactoe.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import canals.nuria.tictactoe.R;
import canals.nuria.tictactoe.objects.Game;

public class MainActivity extends AppCompatActivity {


    private String name1, name2;
    private boolean playerOneChose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Thanks to this: https://stackoverflow.com/a/35383103
        //For the AlertDialog for name input



        Button single = (Button) findViewById(R.id.btnSingle);

        single.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialog(); //One player
            }
        });

        Button multi = (Button) findViewById(R.id.btnMulti);
        multi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialog(false); //Two players, chooses player 1
            }
        });

    }


    private void showAlertDialog() {

        //---------------- Set name dialog --------------------
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(getText(R.string.hint_fist_player));

        //https://stackoverflow.com/a/33406134
        //I'm not on a fragment
        View viewInflated = LayoutInflater.from(MainActivity.this).inflate(R.layout.text_input_name,
                (ViewGroup) getWindow().getDecorView().getRootView(), false);

        // Set up the input
        final EditText input = (EditText) viewInflated.findViewById(R.id.txtNameIn);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        builder.setView(viewInflated);

        // Set up the buttons
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                name1 = input.getText().toString();
                if(name1.equals("")) name1 = (String) getText(R.string.hint_fist_player); //If name is empty, default to "Player 1"
                launchOnePlayer(name1);

            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
        //-------------- End of set name dialog ---------------
    }

    // https://stackoverflow.com/a/42132765
    private void showAlertDialog(boolean player2) {

        //---------------- Set name dialog --------------------
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        if(!player2) builder.setTitle(getText(R.string.hint_fist_player));
        else builder.setTitle(getText(R.string.hint_second_player));

        //https://stackoverflow.com/a/33406134
        //I'm not on a fragment
        View viewInflated = LayoutInflater.from(MainActivity.this).inflate(R.layout.text_input_name,
                (ViewGroup) getWindow().getDecorView().getRootView(), false);

        // Set up the input
        final EditText input = (EditText) viewInflated.findViewById(R.id.txtNameIn);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        builder.setView(viewInflated);

        // Set up the buttons
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(!player2) {
                    dialog.cancel();
                    name1 = input.getText().toString();
                    if(name1.equals("")) name1 = (String) getText(R.string.hint_fist_player);
                    showAlertDialog(true);
                } else {
                    dialog.dismiss();
                    name2 = input.getText().toString();
                    if(name2.equals("")) name2 = (String) getText(R.string.hint_fist_player);
                    launchTwoPlayer(name1, name2);
                }

            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
        //-------------- End of set name dialog ---------------
    }

    private void launchTwoPlayer(String name1, String name2) {
        Intent intent = new Intent(MainActivity.this, GameActivity.class);
        Game game = new Game(name1, name2);
        Game.setGameInstance(game);
        intent.putExtra("firstPlayer", name1);
        MainActivity.this.startActivity(intent);
    }

    private void launchOnePlayer(String name1) {
        Intent intent = new Intent(MainActivity.this, GameActivity.class);
        Game game = new Game(name1);
        Game.setGameInstance(game);
        intent.putExtra("firstPlayer", name1);
        MainActivity.this.startActivity(intent);
    }
}