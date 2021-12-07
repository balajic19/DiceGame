package balaji.ch.dicegame_balaji;

import java.util.Random;

public class Dice {
    private int sideUp;
    private int sides;
    private String name;
    Dice(){
        name = "d6";
        sides = 6;
        roll();
    }


    public void roll(){
        Random random = new Random();
        sideUp = random.nextInt(sides) + 1;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSides(int sides) {
        this.sides = sides;
    }

    public void setSideUp(int sideUp) {
        this.sideUp = sideUp;
    }

    public int getSides() {
        return sides;
    }

    public int getSideUp() {
        return sideUp;
    }

    public String getName() {
        return name;
    }
}
