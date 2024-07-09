package canals.nuria.tictactoe.viewModels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.logging.Logger;

import canals.nuria.tictactoe.R;
import canals.nuria.tictactoe.activities.GameActivity;
import canals.nuria.tictactoe.objects.Game;


// Thanks to this: https://medium.com/androiddevelopers/viewmodels-and-livedata-patterns-antipatterns-21efaef74a54
// For the info with ViewModels
// And this kind indian guy: https://www.youtube.com/watch?v=OKKCPplPMeY

public class GameViewModel extends ViewModel {

    private Logger log;

    private boolean initialized;

    private Game game;
    private MutableLiveData<String> playerName;
    private MutableLiveData<String> longToastMsg;
    private MutableLiveData<String> shortToastMsg;

    //In order for the GameActivity to know it should change a button's image,
    //An array with the button's ID and the drawable ID should be passed
    //0 = Tile, 1 = Resource
    private MutableLiveData<ArrayList<Integer[]>> playedTile;
    private ArrayList<Integer[]> playedTileArray;

    public GameViewModel() {
         log = Logger.getLogger(GameViewModel.class.getName());
         log.info("ViewModel just created!");
         playerName = new MutableLiveData<>();
         playedTile = new MutableLiveData<>();
         longToastMsg = new MutableLiveData<>();
         shortToastMsg = new MutableLiveData<>();
         playedTileArray = new ArrayList<>();
         initialized = false;
    }


    public void init(Intent intent) {
        if(!initialized) {
            log.info("Initializing GameViewModel...");
            game = Game.getGameInstance();
            playerName.setValue(intent.getStringExtra("firstPlayer"));
            initialized = true;
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        log.info("Bye!");

    }

    public MutableLiveData<String> getPlayerName() {
        return playerName;
    }

    public MutableLiveData<String> getLongToastMsg() {
        return longToastMsg;
    }

    public MutableLiveData<String> getShortToastMsg() {
        return shortToastMsg;
    }

    public MutableLiveData<ArrayList<Integer[]>> getPlayedTile() {
        return playedTile;
    }

    public void processPlay(int id, Context context) {

        //Only deal if the selected place is empty
        if(game.isPlaceEmpty(id)) {
            Intent response = game.Deal(id);

            //Set played tile image and next player name
            int tmpInt = response.getIntExtra("playedTile", 0);
            if(tmpInt != 0) { //Only if img specified
                playedTileArray.add(new Integer[] {id, tmpInt});
                playedTile.setValue(playedTileArray);
            }
            playerName.setValue(response.getStringExtra("nextPlayer"));

            checkGameStatus(response, context);

            //CPU's turn (should only happen if player 2 is cpu)
            if(response.getBooleanExtra("CPU", false)) {
                response = game.Deal(id); //Should not matter what view is thrown
                int t = response.getIntExtra("tileUsed", -1);
                if(t != -1) {
                    playedTileArray.add(new Integer[]{t, Game.CIRCLE_PATH});
                    playedTile.setValue(playedTileArray);
                }

                playerName.setValue(response.getStringExtra("nextPlayer"));
                checkGameStatus(response, context);
            }


        } else {
            shortToastMsg.setValue((String) context.getText(R.string.toast_delt_not_empty));
        }
    }

    private void checkGameStatus(Intent response, Context context) {
        //Check if game has finished
        if(response.getBooleanExtra("finished", false)) {
            if(!response.getBooleanExtra("tie", false)) {
                longToastMsg.setValue(context.getText(R.string.player_win_first) +
                        response.getStringExtra("winnerName") +
                        context.getText(R.string.player_win_second));

            } else {
                longToastMsg.setValue((String) context.getText(R.string.result_tie));
            }
        }

    }

    public boolean isGameInProgress() {
        return game.isInGame();
    }
}


