package com.markushager.kniffel;

public class Game
{
    private static final int MAX_ROLLS = 3;
    private Player[] mPlayer = new Player[Const.MAX_PLAYER];
    private static final int STARTING_PLAYER = 0;
    private Dice[] mDice = new Dice[Const.NUMBER_OF_DICES];
    private boolean[] mDiceSelected = new boolean[Const.NUMBER_OF_DICES];
    private int mActivePlayer = STARTING_PLAYER; // 0 .. Const.MAX_PLAYER-1
    private int mPlayerCnt;
    private int mRounds = 0;
    private boolean mRollRealized;
    private int mRollCnt = 0;
    private String mOutput;

    private boolean[] mPlayerActive = new boolean[Const.MAX_PLAYER];

    public Game()
    {
        init();
        restart();
    }

    private void init()
    {
        for (int id = STARTING_PLAYER; id < Const.MAX_PLAYER; id++)
        {
            mPlayer[id] = new Player(id);
        }
        for (int id = 0; id < Const.NUMBER_OF_DICES; id++)
        {
            mDice[id] = new Dice(id);
        }
    }

    private void initActivePlayer()
    {
        for (int id = STARTING_PLAYER; id < Const.MAX_PLAYER; id++)
        {
            mPlayerActive[id] = false;
        }
        mActivePlayer = STARTING_PLAYER;
        mPlayerActive[STARTING_PLAYER] = true;
    }

    private void initPlayer()
    {
        for (int id = STARTING_PLAYER; id < Const.MAX_PLAYER; id++)
        {
            mPlayer[id].init();
        }
    }

    public void restart()
    {
        mRounds = 0;
        initPlayer();
        initActivePlayer();
        resetRollCnt();
        deselectAllDices();
        mRollRealized = true;
        resetOutput();
    }

    private void resetRollCnt()
    {
        mRollCnt = 0;
    }

    public int nextPlayer()
    {
        resetOutput();
        mPlayerActive[mActivePlayer] = false;
        mActivePlayer++;
        if (mActivePlayer >= mPlayerCnt)
        {
            mActivePlayer = 0;
            mRounds++;
        }
        mPlayerActive[mActivePlayer] = true;
        resetRollCnt();
        return mActivePlayer;
    }

    public void rollDices()
    {
        resetOutput();
        if (mRounds >= Const.NUMBER_OF_CATEGORIES)
        {
            mOutput = "Das Spiel ist beendet!";
            return;
        }
        if (mRollCnt < MAX_ROLLS)
        {
            for (int id = 0; id < Const.NUMBER_OF_DICES; id++)
            {
                if (!mDiceSelected[id])
                {
                    mDice[id].roll();
                }
            }
            mRollCnt++;
        }
        else
        {
            mOutput = "Maximale Wurfzahl erreicht!";
        }
        if (mRollCnt == 1)
        {
            mRollRealized = false;
        }
        deselectAllDices();
    }

    public void deselectAllDices()
    {
        for (int id = 0; id < Const.NUMBER_OF_DICES; id++)
        {
            mDiceSelected[id] = false;
        }
    }

    public void selectDice(int diceId)
    {
        mDiceSelected[diceId] = true;
    }

    public void deselectDice(int diceId)
    {
        mDiceSelected[diceId] = false;
    }

    public boolean[] getDiceSelected()
    {
        return mDiceSelected;
    }

    public void selectCategory(int catId)
    {
        int catValue = 0;
        resetOutput();
        if (!mRollRealized)
        {
            catValue = getCategoryValue(catId);
            mRollRealized = mPlayer[mActivePlayer].setCategoryValue(
                catId, catValue);
            if (mRollRealized)
            {
                mPlayer[mActivePlayer].calcTotal();
                nextPlayer();
            }
            else
            {
                mOutput = "Kategorie bereits belegt";
            }
        }
        else
        {
            mOutput = "Werte können nicht übernommmen werden";
        }
    }

    private int getCategoryValue(int catId)
    {
        int catValue = 0;

        if (catId < Const.NUMBER_OF_UP_CATEGORIES)
        {
            catValue = getTotalOfSameValue(catId + 1);
        }
        else
        {
            // calc für untere Kategorien !!!
            switch (catId)
            {
                case 6: // Dreierpasch
                    catValue = (isThreeOfAKind()) ? getTotalOfAllDices() : 0;
                    break;
                case 7: // Viererpasch
                    catValue = (isFourOfAKind()) ? getTotalOfAllDices() : 0;
                    break;
                case 8: // Full House
                    catValue = (isFullHouse() ? 25 : 0);
                    break;
                case 9: // kl. Strasse
                    catValue = (isSmallStraight() ? 30 : 0);
                    break;
                case 10: // gr. Strasse
                    catValue = (isBigStraight() ? 40 : 0);
                    break;
                case 11: // Kniffel
                    catValue = (isKniffel() ? 50 : 0);
                    break;
                case 12: // Chance
                    catValue = getTotalOfAllDices();
                    break;
                default:
                    catValue = 0;
                    mOutput = "Kategory "+catId+ " not valid!";
                    break;
            }
        }
        return catValue;
    }

    private boolean isThreeOfAKind()
    {
        for (int i = 1; i <= Const.DICE_SIDES; i++)
        {
            if (getNumberOfSameValue(i) >= 3)
                return true;
        }
        return false;
    }

    private boolean isFourOfAKind()
    {
        for (int i = 1; i <= Const.DICE_SIDES; i++)
        {
            if (getNumberOfSameValue(i) >= 4)
                return true;
        }
        return false;
    }

    private boolean isFullHouse()
    {
        boolean isTwoOfAKind = false;
        boolean isThreeOfAKind = false;
        int numberOfSameValue;

        for (int i = 1; i <= Const.DICE_SIDES; i++)
        {
            numberOfSameValue = getNumberOfSameValue(i);
            if (numberOfSameValue == 2)
                isTwoOfAKind = true;
            else if (numberOfSameValue == 3)
                isThreeOfAKind = true;
        }
        return isTwoOfAKind && isThreeOfAKind;
    }

    private boolean isSmallStraight()
    {
        int[] numberOfSameValue = new int[Const.DICE_SIDES];

        for (int i = 1; i <= Const.DICE_SIDES; i++)
            numberOfSameValue[i-1] = getNumberOfSameValue(i);

        if (numberOfSameValue[0] >= 1 && numberOfSameValue[1] >= 1 &&
            numberOfSameValue[2] >= 1 && numberOfSameValue[3] >= 1)
            return true; // 1, 2, 3, 4
        else if (numberOfSameValue[1] >= 1 && numberOfSameValue[2] >= 1 &&
            numberOfSameValue[3] >= 1 && numberOfSameValue[4] >= 1)
            return true; // 2, 3, 4, 5
        else if (numberOfSameValue[2] >= 1 && numberOfSameValue[3] >= 1 &&
            numberOfSameValue[4] >= 1 && numberOfSameValue[5] >= 1)
            return true; // 3, 4, 5, 6
        else
            return false;
    }

    private boolean isBigStraight()
    {
        int[] numberOfSameValue = new int[Const.DICE_SIDES];

        for (int i = 1; i <= Const.DICE_SIDES; i++)
            numberOfSameValue[i-1] = getNumberOfSameValue(i);

        if (numberOfSameValue[0] == 1 && numberOfSameValue[1] == 1 &&
            numberOfSameValue[2] == 1 && numberOfSameValue[3] == 1 &&
            numberOfSameValue[4] == 1)
            return true; // 1, 2, 3, 4, 5
        else if (numberOfSameValue[1] == 1 && numberOfSameValue[2] == 1 &&
            numberOfSameValue[3] == 1 && numberOfSameValue[4] == 1 &&
            numberOfSameValue[5] == 1)
            return true; // 2, 3, 4, 5, 6
        else
            return false;
    }

    private boolean isKniffel()
    {
        for (int i = 1; i <= Const.DICE_SIDES; i++)
        {
            if (getNumberOfSameValue(i) == 5)
                return true;
        }
        return false;
    }

    private int getTotalOfAllDices()
    {
        int total = 0;

        for (int id = 0; id < Const.NUMBER_OF_DICES; id++)
        {
            total += mDice[id].getValue();
        }
        return total;
    }

    private int getTotalOfSameValue(int value)
    {
        int total = 0;

        for (int id = 0; id < Const.NUMBER_OF_DICES; id++)
        {
            if (mDice[id].getValue() == value)
                total += value;
        }

        return total;
    }

    private int getNumberOfSameValue(int value)
    {
        int number = 0;

        for (int id = 0; id < Const.NUMBER_OF_DICES; id++)
        {
            if (mDice[id].getValue() == value)
                number++;
        }

        return number;
    }

    public String[] getDiceValue()
    {
        String[] diceValue = new String[Const.NUMBER_OF_DICES];

        for (int id = 0; id < Const.NUMBER_OF_DICES; id++)
        {
            diceValue[id] = Integer.toString(mDice[id].getValue());
        }
        return diceValue;
    }

    public String[][] getPlayerValue()
    {
        String[][] playerValue =
            new String[Const.NUMBER_OF_CATEGORIES][Const.MAX_PLAYER];

        for (int catId = 0; catId < Const.NUMBER_OF_CATEGORIES; catId++)
        {
            for (int playerId = 0; playerId < Const.MAX_PLAYER; playerId++)
            {
                playerValue[catId][playerId] =
                    Integer.toString(mPlayer[playerId].getCategoryValue(catId));
            }
        }
        return playerValue;
    }

    public void setPlayerCnt(int playerCnt)
    {
        if (playerCnt > Const.MAX_PLAYER)
        {
            mPlayerCnt = Const.MAX_PLAYER;
        }
        mPlayerCnt = playerCnt;
    }

    public String getPlayerCnt()
    {
        return Integer.toString(mPlayerCnt);
    }

    public boolean[] getPlayerActive()
    {
        return mPlayerActive;
    }

    public boolean hasRolls()
    {
        if (mRollCnt < MAX_ROLLS && mRounds < Const.NUMBER_OF_CATEGORIES)
        {
            return true;
        }
        return false;
    }

    public int getRollCnt()
    {
        return mRollCnt;
    }

    public String[] getPlayerTotalUpValue()
    {
        String[] playerTotalUpValue = new String[Const.MAX_PLAYER];
        for (int playerId = 0; playerId < Const.MAX_PLAYER; playerId++)
        {
            playerTotalUpValue[playerId] =
                Integer.toString(mPlayer[playerId].getTotalUpValue());
        }
        return playerTotalUpValue;
    }

    public String[] getPlayerTotalUpValuePlusBonus()
    {
        String[] playerTotalUpValue = new String[Const.MAX_PLAYER];
        for (int playerId = 0; playerId < Const.MAX_PLAYER; playerId++)
        {
            playerTotalUpValue[playerId] =
                Integer.toString(mPlayer[playerId].getTotalUpValuePlusBonus());
        }
        return playerTotalUpValue;
    }

    public String[] getPlayerTotalDownValue()
    {
        String[] playerTotalDownValue = new String[Const.MAX_PLAYER];
        for (int playerId = 0; playerId < Const.MAX_PLAYER; playerId++)
        {
            playerTotalDownValue[playerId] =
                Integer.toString(mPlayer[playerId].getTotalDownValue());
        }
        return playerTotalDownValue;
    }

    public String[] getPlayerTotalValue()
    {
        String[] playerTotalValue = new String[Const.MAX_PLAYER];
        for (int playerId = 0; playerId < Const.MAX_PLAYER; playerId++)
        {
            playerTotalValue[playerId] =
                Integer.toString(mPlayer[playerId].getTotalValue());
        }
        return playerTotalValue;
    }

    public String getOutput()
    {
        return mOutput;
    }

    public String getRoundsState()
    {
        return Integer.toString(mRounds) + "/" +
            Integer.toString(Const.NUMBER_OF_CATEGORIES);
    }

    public String getRollsState()
    {
        return Integer.toString(mRollCnt) + "/" +
            Integer.toString(MAX_ROLLS);
    }

    private void resetOutput()
    {
        mOutput = "";
    }

    public String[] getPlayerBonusValue()
    {
        String[] playerBonus = new String[Const.MAX_PLAYER];

        for (int id = 0; id < playerBonus.length; id++)
        {
            playerBonus[id] = Integer.toString(mPlayer[id].getBonus());
        }
        return playerBonus;
    }

    public boolean[] getHasHighscore()
    {
        boolean[] hasHighscore = new boolean[Const.MAX_PLAYER];
        int playerWithHighscore = -1;
        int highScore = 0;

        for (int id = 0; id < Const.MAX_PLAYER; id++)
        {
            hasHighscore[id] = false;
            if (mPlayer[id].getTotalValue() > highScore)
            {
                highScore = mPlayer[id].getTotalValue();
                playerWithHighscore = id;
            }
        }
        if (playerWithHighscore > -1)
            hasHighscore[playerWithHighscore] = true;

        return hasHighscore;
    }

    public boolean[][] isUsed()
    {
        boolean[][] selectedPlayerCategory =
            new boolean[Const.NUMBER_OF_CATEGORIES][Const.MAX_PLAYER];

        for (int catId = 0; catId < Const.NUMBER_OF_CATEGORIES; catId++)
        {
            for (int playerId = 0; playerId < Const.MAX_PLAYER; playerId++)
            {
                selectedPlayerCategory[catId][playerId] =
                    mPlayer[playerId].getCategorySelected(catId);
            }
        }

        return selectedPlayerCategory;
    }
}
