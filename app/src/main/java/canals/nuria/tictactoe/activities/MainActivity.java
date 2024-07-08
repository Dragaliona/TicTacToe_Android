package canals.nuria.tictactoe.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import canals.nuria.tictactoe.R;
import canals.nuria.tictactoe.objects.Game;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO: Make name customizable

        Button single = (Button) findViewById(R.id.btnSingle);

        single.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                Game game = new Game("Player 1");
                Game.setGameInstance(game);
                intent.putExtra("firstPlayer", "Player 1");
                MainActivity.this.startActivity(intent);
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