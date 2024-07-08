package canals.nuria.tictactoe.objects;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author nuria
 */
public class Player {

    private String name;
    private ArrayList<Integer> positions;
    private boolean CPU;
    private Random rnd = null;

    public Player(String n, boolean c) {
        name = n;
        positions = new ArrayList<>();
        CPU = c;
        if(c)
            rnd = new Random();
    }


    public boolean play(int pos) {
        if(!positions.contains(pos)) {
            positions.add(pos);
            return true;
        }
        return false;
    }

    public ArrayList<Integer> getPos() {
        return positions;
    }

    public String getName() {
        return name;
    }

    public boolean isCPU() {
        return CPU;
    }

    public int CPUDeal() {
        return rnd.nextInt(9); //Quite literally a random option
    }
}

