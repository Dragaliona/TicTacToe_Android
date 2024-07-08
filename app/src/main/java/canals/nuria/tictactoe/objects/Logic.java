package canals.nuria.tictactoe.objects;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author nuria
 */

// To see the board
//  0   1   2
//  3   4   5
//  6   7   8

public class Logic  {

    private static Logic logic;
    //private Logger log;
    private ArrayList<Integer[]> win = new ArrayList<>();

    private Logic() {
        //log = Logger.getLogger(Logic.class.getName());
        //log.info("Logic class started inicialization");

        //log.info("Started populating win cases");
        win.add(new Integer[] {0, 1, 2});
        win.add(new Integer[] {3, 4, 5});
        win.add(new Integer[] {6, 7, 8});
        win.add(new Integer[] {0, 3, 6});
        win.add(new Integer[] {1, 4, 7});
        win.add(new Integer[] {2, 5, 8});
        win.add(new Integer[] {0, 4, 8});
        win.add(new Integer[] {2, 4, 6});
        //log.info("Populated win cases");
    }

    public static Logic getInstance() {
        if(logic == null) {
            logic = new Logic();
        }

        return logic;
    }

    public boolean hasWon(ArrayList<Integer> a) {

        /*
         * The shitty logic consists in if the position array contains at least
         * the three positions of one of the winning cases, the player wins
         */

        for(Integer[] cas : win) {
            int counter = 0;

            for(Integer ins : cas)
                if(a.contains(ins))
                    counter++;

            if(counter >= 3)
                return true;
        }

        return false;
    }



}

