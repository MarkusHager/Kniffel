package com.markushager.kniffel;

import java.util.Random;

public class Dice
{
    private int mValue;
    private int mId;

    public Dice(int id)
    {
        mId = id;
        roll();
    }

    public void roll()
    {
        Random rand = new Random();
        mValue = rand.nextInt(Const.DICE_SIDES) + 1;
    }

    public int getValue() { return mValue; }
}
