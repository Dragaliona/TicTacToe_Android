package canals.nuria.tictactoe.activities;


import android.arch.lifecycle.ViewModelProvider;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.logging.Logger;

import canals.nuria.tictactoe.R;
import canals.nuria.tictactoe.viewModels.GameViewModel;

public class GameActivity extends AppCompatActivity {

    private Logger log;
    private GameViewModel gameViewModel;

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

        TextView nameTag = findViewById(R.id.txtTurnName);



        View.OnClickListener ocl = v -> gameViewModel.processPlay(v.getId());


        //TODO: Solve bug, when rotated board gets massive

        /*
         * I suppose it's caused because it's checking the screen width, but it
         * should also take into account the height
         */

        //The buttons


        ImageView btn0 = findViewById(R.id.btn0);
        ImageView btn1 = findViewById(R.id.btn1);
        ImageView btn2 = findViewById(R.id.btn2);
        ImageView btn3 = findViewById(R.id.btn3);
        ImageView btn4 = findViewById(R.id.btn4);
        ImageView btn5 = findViewById(R.id.btn5);
        ImageView btn6 = findViewById(R.id.btn6);
        ImageView btn7 = findViewById(R.id.btn7);
        ImageView btn8 = findViewById(R.id.btn8);

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
        gameViewModel.getPlayerName().observe(this, s -> nameTag.setText(getText(R.string.text_player_prev) + s));


        //Observe the Toast texts
        gameViewModel.getShortToastMsg().observe(this, s -> Toast.makeText(GameActivity.this, s, Toast.LENGTH_SHORT).show());
        gameViewModel.getLongToastMsg().observe(this, s -> Toast.makeText(GameActivity.this, s, Toast.LENGTH_LONG).show());

        //Observe whether someone won
        gameViewModel.getWinCase().observe(this, integer -> {
            boolean shouldEnd = false;

            switch(integer) {
                case 0:
                    //Not any win, just player played a non empty tile
                    Toast.makeText(GameActivity.this, getText(R.string.toast_delt_not_empty), Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    showWin(gameViewModel.getPlayerName().getValue());
                    shouldEnd = true;
                    break;
                case 2:
                    showTie();
                    shouldEnd = true;
                    break;
                default:
                    log.warning("Unrecognized winCase, standing by...");
                    break;
            }
            if(shouldEnd) finish(); //Todo: Ask for game repeat
        });

        //Observe changes on tiles
        gameViewModel.getPlayedTile().observe(this, integers -> {

            if (integers != null) {
                for(Integer[] ints : integers) {
                    try {
                        ImageView button = findViewById(ints[0]);
                        button.setImageResource(ints[1]);

                    } catch(NullPointerException e) {
                        log.severe("View ID is invalid");
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }

        });

    }

    private void showWin(String name) {
        Toast.makeText(this, getText(R.string.player_win_first) +
                name +
                getText(R.string.player_win_second), Toast.LENGTH_LONG).show();
    }

    private void showTie() {
        Toast.makeText(this, getText(R.string.result_tie), Toast.LENGTH_LONG).show();
    }



}