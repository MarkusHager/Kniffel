package com.markushager.kniffel;

public class Player
{
    private String mName;
    private int mId;
    private int[] mCategoryValue = new int[Const.NUMBER_OF_CATEGORIES];
    private boolean[] mCategorySelected = new boolean[Const.NUMBER_OF_CATEGORIES];
    private int mBonus;
    private int mTotal;
    private int mTotalUp;
    private int mTotalUpPlusBonus;
    private int mTotalDown;

    public Player(int id)
    {
        mId = id;
        init();
    }

    public void init()
    {
        for (int catId = 0; catId < Const.NUMBER_OF_CATEGORIES; catId++)
        {
            mCategoryValue[catId] = 0;
            mCategorySelected[catId] = false;
        }
        mBonus = 0;
        mTotal = 0;
        mTotalDown = 0;
        mTotalUp = 0;
        mTotalUpPlusBonus = 0;
    }

    public int getCategoryValue(int catId)
    {
        return mCategoryValue[catId];
    }

    public boolean setCategoryValue(int catId, int value)
    {
        if (!mCategorySelected[catId])
        {
            mCategoryValue[catId] = value;
            mCategorySelected[catId] = true;
            return true;
        }
        return false;
    }

    public void calcTotal()
    {
        mTotalUp = 0;
        mTotalDown = 0;

        for (int catId = 0; catId < Const.NUMBER_OF_UP_CATEGORIES; catId++)
        {
            mTotalUp += mCategoryValue[catId];
        }
        if (mTotalUp >= Const.BONUS_POINTS_NEEDED)
        {
            mTotalUpPlusBonus = mTotalUp + Const.BONUS_SIZE;
            mBonus = Const.BONUS_SIZE;
        }
        else
        {
            mTotalUpPlusBonus = mTotalUp;
        }

        for (int catId = Const.NUMBER_OF_UP_CATEGORIES; catId < Const.NUMBER_OF_CATEGORIES; catId++)
        {
            mTotalDown += mCategoryValue[catId];
        }
        mTotal = mTotalUpPlusBonus + mTotalDown;
    }

    public int getTotalUpValue() { return mTotalUp; }
    public int getTotalUpValuePlusBonus() { return mTotalUpPlusBonus; }
    public int getTotalDownValue() { return mTotalDown; }
    public int getTotalValue() { return mTotal; }
    public int getBonus() { return mBonus; }
    public boolean getCategorySelected(int catId) { return mCategorySelected[catId]; }
}
