package canals.nuria.tictactoe.activities;

import android.app.Application;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.logging.Logger;

import canals.nuria.tictactoe.R;
import canals.nuria.tictactoe.objects.Game;
import canals.nuria.tictactoe.viewModels.GameViewModel;

public class GameActivity extends AppCompatActivity {

    private Logger log;
    private GameViewModel gameViewModel;


    private ImageView btn0;
    private ImageView btn1;
    private ImageView btn2;
    private ImageView btn3;
    private ImageView btn4;
    private ImageView btn5;
    private ImageView btn6;
    private ImageView btn7;
    private ImageView btn8;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        log = Logger.getLogger(GameActivity.class.getName());

        //ViewModels are gotten from ViewModelProvider, so we don't lose the instance every time config changes
        //This log shit of AndroidViewModelFactory is needed because of deprecated APIs, with AndroidX it's not needed
        gameViewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(GameViewModel.class);

        Intent intent = getIntent();
        gameViewModel.init(intent);
        log.info("ViewModel initialized!");

        TextView nametag = (TextView) findViewById(R.id.txtTurnName);



        View.OnClickListener ocl = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gameViewModel.processPlay(v.getId(), GameActivity.this);

                //Exit if finished game
                if(!gameViewModel.isGameInProgress()) {
                    finish(); //Todo: Ask for game repeat
                }

            }
        };


        //TODO: Solve bug, when rotated board gets massive

        /*
         * I suppose it's caused because it's checking the screen width, but it
         * should also take into account the height
         */

        //The buttons
        btn0 = (ImageView) findViewById(R.id.btn0);
        btn1 = (ImageView) findViewById(R.id.btn1);
        btn2 = (ImageView) findViewById(R.id.btn2);
        btn3 = (ImageView) findViewById(R.id.btn3);
        btn4 = (ImageView) findViewById(R.id.btn4);
        btn5 = (ImageView) findViewById(R.id.btn5);
        btn6 = (ImageView) findViewById(R.id.btn6);
        btn7 = (ImageView) findViewById(R.id.btn7);
        btn8 = (ImageView) findViewById(R.id.btn8);

        btn0.setOnClickListener(ocl);
        btn1.setOnClickListener(ocl);
        btn2.setOnClickListener(ocl);
        btn3.setOnClickListener(ocl);
        btn4.setOnClickListener(ocl);
        btn5.setOnClickListener(ocl);
        btn6.setOnClickListener(ocl);
        btn7.setOnClickListener(ocl);
        btn8.setOnClickListener(ocl);


        //Set text for player title by observing the playerName mutable data
        //TODO: Solve after win player set to null
        gameViewModel.getPlayerName().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                nametag.setText(getText(R.string.text_player_prev) + s);
            }
        });


        //Observe the Toast texts
        gameViewModel.getShortToastMsg().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                Toast.makeText(GameActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        });
        gameViewModel.getLongToastMsg().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                Toast.makeText(GameActivity.this, s, Toast.LENGTH_LONG).show();
            }
        });

        //Observe changes on tiles
        gameViewModel.getPlayedTile().observe(this, new Observer<ArrayList<Integer[]>>() {
            @Override
            public void onChanged(@Nullable ArrayList<Integer[]> integers) {

                for(Integer[] ints : integers) {
                    try {
                        ImageView button = (ImageView) findViewById(ints[0]);
                        button.setImageResource(ints[1]);

                    } catch(NullPointerException e) {
                        log.severe("View ID is invalid");
                    } catch (Exception e) {
                        new RuntimeException(e);
                    }
                }

            }
        });

    }


}