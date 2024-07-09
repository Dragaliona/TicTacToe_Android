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
                        Intent intent = new Intent(MainActivity.this, GameActivity.class);
                        Game game = new Game(name1);
                        Game.setGameInstance(game);
                        intent.putExtra("firstPlayer", name1);
                        MainActivity.this.startActivity(intent);
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
        });

        Button multi = (Button) findViewById(R.id.btnMulti);
        multi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                Game game = new Game("Player 1", "Player 2");
                Game.setGameInstance(game);
                intent.putExtra("firstPlayer", "Player 1");
                MainActivity.this.startActivity(intent);
            }
        });

    }
}