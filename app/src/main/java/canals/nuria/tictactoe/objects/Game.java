package canals.nuria.tictactoe.objects;


import android.content.Intent;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import canals.nuria.tictactoe.R;

public class Game {

    private static Game gameInstance;

    private Logic logic = null;
    private Player one = null;
    private Player two = null;
    private boolean inGame;
    private boolean pOneDeals;
    private boolean[] gamePos = new boolean[9];
    private Map<Integer, Integer> btnMap;

    public static int CIRCLE_PATH = R.drawable.circle;
    public static int CROSS_PATH = R.drawable.cross;

    //Two constructors, for either 1 or 2 players
    public Game(String ply1) {
        logic = Logic.getInstance();
        one = new Player(ply1, false);
        two = new Player("CPU", true);
        inGame = true;
        pOneDeals = true;
        initGamePos();
        initBtnMap();

    }

    public Game(String ply1, String ply2) {
        logic = Logic.getInstance();
        one = new Player(ply1, false);
        two = new Player(ply2, false);
        inGame = true;
        pOneDeals = true;
        initGamePos();
        initBtnMap();
    }

    private void initGamePos() {
        for(int i = 0; i < 9; i++) //Init position array
            gamePos[i] = false;
    }

    private void initBtnMap() {
        //So I don't have to fucking use switches
        btnMap = new TreeMap<>();

        btnMap.put(R.id.btn0, 0);
        btnMap.put(R.id.btn1, 1);
        btnMap.put(R.id.btn2, 2);
        btnMap.put(R.id.btn3, 3);
        btnMap.put(R.id.btn4, 4);
        btnMap.put(R.id.btn5, 5);
        btnMap.put(R.id.btn6, 6);
        btnMap.put(R.id.btn7, 7);
        btnMap.put(R.id.btn8, 8);

    }

    //From https://www.baeldung.com/java-map-key-from-value
    private <K, V> K getButtonId(Map<K, V> map, V value) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public boolean isPlaceEmpty(int id) {

        return !gamePos[btnMap.get(id)];
    }

    public Intent Deal(int id) {

        Intent response = new Intent(); //0: current player name, 1: Resource name of either cross or circle
        boolean justDealt = false; //How then would the game know that player 1 dealt before 2?



        if(pOneDeals) {
            //Player 1 turn
            one.play(btnMap.get(id));
            gamePos[btnMap.get(id)] = true;

            if(checkWin(one.getPos())) {
                //Player 1 won
                response.putExtra("finished", true);
                response.putExtra("tie", false);
                response.putExtra("winnerName", one.getName());
                response.putExtra("playedTile", CROSS_PATH);
                response.putExtra("nextPlayer", one.getName()); //So doesn't show player: null
                return response; //No need to go further
            }
            pOneDeals = false;
            justDealt = true;
        }

        //Do we have a tie?
        if(checkTie()) {
            response.putExtra("finished", true);
            response.putExtra("tie", true);
            return response;
        }

        if (!pOneDeals && two.isCPU() && !justDealt) {
            //CPU turn
            int cache;
            boolean works = false;
            do { //It has to run at least once
                cache = two.CPUDeal();
                if(!gamePos[cache]) {
                    //Good deal
                    //CPU must also report the used game tile
                    response.putExtra("tileUsed", getButtonId(btnMap, cache));
                    works = true;
                    // Welp, how is the game supposed to know that cpu dealt?
                    gamePos[cache] = true;
                    two.play(cache);
                }
            } while(!works);

            pOneDeals = true;
            justDealt = true;


        } else if(!pOneDeals && !justDealt) {
            //Player 2 turn
            two.play(btnMap.get(id));
            gamePos[btnMap.get(id)] = true;
            pOneDeals = true;
        }


        //pOneDeals forces to only check when it just has been player 2's turn
        if(checkWin(two.getPos()) && pOneDeals) {
            //Player 2 / CPU won
            response.putExtra("finished", true);
            response.putExtra("tie", false);
            response.putExtra("winnerName", two.getName());
            response.putExtra("playedTile", CIRCLE_PATH);
            response.putExtra("nextPlayer", two.getName()); //So doesn't show player: null
            return response; //No need to go further
        }

            //Do we have a tie?
            if(checkTie()) {
            response.putExtra("finished", true);
            response.putExtra("tie", true);
            return response;
        }

        // Game still hasn't finished
        response.putExtra("finished", false);
        if(pOneDeals) {
            // Next turn is first's
            response.putExtra("nextPlayer", one.getName());
            response.putExtra("playedTile", CIRCLE_PATH); //Player 2 played


        } else {
            // Next turn is second's
            // Check if player 2 is CPU, so it can have a go just after player 1
            if(two.isCPU()) response.putExtra("CPU", true);

            response.putExtra("nextPlayer", two.getName());
            response.putExtra("playedTile", CROSS_PATH);
        }
        return response;
    }

    private boolean checkTie() {
        //Check if game ended in tie
        int countTie = 0;
        for(int i = 0; i < 9; i++) {
            if(gamePos[i]) {
                countTie++;
            }
        }

        //Just in case the program goes nuts and puts 1 more
        return countTie >= 9;
    }

    private boolean checkWin(ArrayList<Integer> pos) {

        if(logic.hasWon(pos)) {
            inGame = false;
            return true;

        }
        return false;
    }


    public boolean isInGame() {
        return inGame;
    }

    public static Game getGameInstance() {
        return gameInstance;
    }

    public static void setGameInstance(Game gameInstance) {
        Game.gameInstance = gameInstance;
    }


    //TODO: Add method to reinitiazize and repeat game

}
