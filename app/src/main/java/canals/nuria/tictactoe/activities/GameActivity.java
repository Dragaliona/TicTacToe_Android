package canals.nuria.tictactoe.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import canals.nuria.tictactoe.R;
import canals.nuria.tictactoe.objects.Game;

public class GameActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent intent = getIntent();
        Game game = Game.getGameInstance();

        TextView nametag = (TextView) findViewById(R.id.txtTurnName);

        View.OnClickListener ocl = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: After calling game.deal() should also process whether CPU turn happened
                
                //Only deal if the selected place is empty
                if(game.isPlaceEmpty(v)) {
                    Intent response = game.Deal(v);

                    //Change the current played tile
                    ImageView temp2 = (ImageView) findViewById(v.getId());

                    int tmpInt = response.getIntExtra("playedTile", 0);

                    if(tmpInt != 0) temp2.setImageResource(tmpInt); //Only if img specified


                    nametag.setText(response.getStringExtra("nextPlayer"));

                    checkGameStatus(response);

                    //CPU's turn (should only happen if player 2 is cpu)
                    if(response.getBooleanExtra("CPU", false)) {
                        response = game.Deal(v); //Should not matter what view is thrown
                        int t = response.getIntExtra("tileUsed", -1);
                        ImageView temp = null;
                        if(t != -1) {
                            temp = (ImageView) findViewById(t);
                        } else {

                        }
                        if(temp != null) {
                            temp.setImageResource(Game.CIRCLE_PATH);
                        }
                        nametag.setText(response.getStringExtra("nextPlayer"));
                        checkGameStatus(response);
                    }


                } else {
                    Toast.makeText(GameActivity.this, getText(R.string.toast_delt_not_empty), Toast.LENGTH_SHORT).show();
                }
                
                

            }
        };

        //The buttons
        ImageView btn0 = (ImageView) findViewById(R.id.btn0);
        ImageView btn1 = (ImageView) findViewById(R.id.btn1);
        ImageView btn2 = (ImageView) findViewById(R.id.btn2);
        ImageView btn3 = (ImageView) findViewById(R.id.btn3);
        ImageView btn4 = (ImageView) findViewById(R.id.btn4);
        ImageView btn5 = (ImageView) findViewById(R.id.btn5);
        ImageView btn6 = (ImageView) findViewById(R.id.btn6);
        ImageView btn7 = (ImageView) findViewById(R.id.btn7);
        ImageView btn8 = (ImageView) findViewById(R.id.btn8);

        btn0.setOnClickListener(ocl);
        btn1.setOnClickListener(ocl);
        btn2.setOnClickListener(ocl);
        btn3.setOnClickListener(ocl);
        btn4.setOnClickListener(ocl);
        btn5.setOnClickListener(ocl);
        btn6.setOnClickListener(ocl);
        btn7.setOnClickListener(ocl);
        btn8.setOnClickListener(ocl);

        nametag.setText(intent.getStringExtra("firstPlayer"));




    }

    private void checkGameStatus(Intent response) {
        //Check if game has finished
        if(response.getBooleanExtra("finished", false)) {
            if(!response.getBooleanExtra("tie", false)) {
                Toast.makeText(GameActivity.this, getText(R.string.player_win_first) +
                        response.getStringExtra("winnerName") +
                        getText(R.string.player_win_second), Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(GameActivity.this, getText(R.string.result_tie), Toast.LENGTH_LONG).show();
            }

            GameActivity.super.finish(); //Todo: Ask for game repeat
        }

    }


}