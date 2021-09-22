package com.markushager.kniffel;

import androidx.databinding.Bindable;
import androidx.databinding.BaseObservable;

public class GameViewModel extends BaseObservable
{
    private Game mGame;
    public final static String NUMBER_OF_PLAYERS_TITLE = "Spieleranzahl";
    public final static String ROLL_DICES_TITLE = "Würfeln";
    public final static String NEW_GAME_TITLE = "Neues Spiel";
    public final static String NEXT_PLAYER_TITLE = "Nächster Spieler";
    public final static String ROUNDS_TITLE = "Runden";

    public final static String PLAYER_NUMBER_TITLE = "Spieler";
    public final static String PLAYER_ONE_TITLE = "S1";
    public final static String PLAYER_TWO_TITLE = "S2";
    public final static String PLAYER_THREE_TITLE = "S3";
    public final static String PLAYER_FOUR_TITLE = "S4";
    public final static String PLAYER_FIVE_TITLE = "S5";
    public final static String PLAYER_SIX_TITLE = "S6";

    public final static String ONES_TITLE = "Einser";
    public final static String TWOS_TITLE = "Zweier";
    public final static String THREES_TITLE = "Dreier";
    public final static String FOURS_TITLE = "Vierer";
    public final static String FIVES_TITLE = "Fünfer";
    public final static String SIXES_TITLE = "Sechser";

    public final static String TOTAL_UP_TITLE = "Gesamt oben";
    public final static String BONUS_TITLE = "Bonus bei 63+";
    public final static String TOTAL_UP_PLUS_BONUS_TITLE = "Gesamt oben (Bonus)";

    public final static String TRIPS_TITLE = "Dreierpasch";
    public final static String QUADS_TITLE = "Viererpasch";
    public final static String FULL_HOUSE_TITLE = "Full House";
    public final static String SMALL_STRAIGHT_TITLE = "Kl. Strasse";
    public final static String BIG_STRAIGHT_TITLE = "Gr. Strasse";
    public final static String KNIFFEL_TITLE = "Kniffel";
    public final static String CHANCE_TITLE = "Chance";

    public final static String TOTAL_DOWN_TITLE = "Gesamt unten";
    public final static String TOTAL_TITLE = "Gesamt";

    public static final String TWO_PLAYER = "2";
    public static final String THREE_PLAYER = "3";
    public static final String FOUR_PLAYER = "4";
    public static final String FIVE_PLAYER = "5";
    public static final String SIX_PLAYER = "6";


    public GameViewModel(Game game)
    {
        mGame = game;
        setNumberOfPlayers(TWO_PLAYER);
        restart();
        notifyPropertyChanges();
    }

    public void restart()
    {
        mGame.restart();
        notifyPropertyChanges();
    }

    public void rollDices()
    {
        mGame.rollDices();
        deselectDices();
        notifyPropertyChanges();
    }

    public void notifyPropertyChanges()
    {

        notifyPropertyChanged(BR.diceValue);
        notifyPropertyChanged(BR.playerActive);
        notifyPropertyChanged(BR.diceSelected);
        notifyPropertyChanged(BR.hasRolls);
        notifyPropertyChanged(BR.playerValue);
        notifyPropertyChanged(BR.playerTotalUpValue);
        notifyPropertyChanged(BR.playerBonusValue);
        notifyPropertyChanged(BR.playerTotalUpValuePlusBonus);
        notifyPropertyChanged(BR.playerTotalDownValue);
        notifyPropertyChanged(BR.playerTotalValue);
        notifyPropertyChanged(BR.output);
        notifyPropertyChanged(BR.numberOfPlayers);
        notifyPropertyChanged(BR.rollsState);
        notifyPropertyChanged(BR.roundsState);
        notifyPropertyChanged(BR.hasHighscore);
        notifyPropertyChanged(BR.isUsed);
    }

    public void deselectDices()
    {
        mGame.deselectAllDices();
        notifyPropertyChanges();
    }

    public void nextPlayer()
    {
        mGame.nextPlayer();
        notifyPropertyChanges();
    }

    public void selectDice(int diceId)
    {
        if (mGame.getRollCnt() == 0)
            return;
        if (mGame.getDiceSelected()[diceId])
        {
            mGame.deselectDice(diceId);
        }
        else
        {
            mGame.selectDice(diceId);
        }
        notifyPropertyChanges();
    }

    public void selectCategory(int category)
    {
        mGame.selectCategory(category);
        notifyPropertyChanges();
    }

    @Bindable
    public String[] getDiceValue()
    {
        return mGame.getDiceValue();
    }

    @Bindable
    public String getNumberOfPlayers()
    {
        return NUMBER_OF_PLAYERS_TITLE + ": " + mGame.getPlayerCnt();
    }

    @Bindable
    public String getOutput()
    {
        return mGame.getOutput();
    }

    /*
    public void setNumberOfPlayers(String numberOfPlayers)
    {
        // Build an AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(GameViewModel.this);

        // Set a title for alert dialog
        builder.setTitle("Das wechseln der Spieleranzahl hat ein Löschen "+
                "des Spielstandes zur Folge");

        // Ask the final question
        builder.setMessage("Wollen Sie wirklich die Anzahl der Spieler wechseln?");

        // Set the alert dialog yes button click listener
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                // Do something when user clicked the Yes button
                // Set the TextView visibility GONE
                Toast.makeText(getApplicationContext(),
                    "Spielstand wird gelöscht", Toast.LENGTH_LONG).show();
                mDeleteExercise = 1;
            }
        });

        // Set the alert dialog no button click listener
        builder.setNegativeButton("No", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                // Do something when No button clicked
                Toast.makeText(getApplicationContext(),
                    "Spielstand wird behalten", Toast.LENGTH_LONG).show();
            }
        });

        AlertDialog dialog = builder.create();
        // Display the alert dialog on interface
        dialog.show();
    }*/

    public void setNumberOfPlayers(String numberOfPlayers)
    {
        mGame.setPlayerCnt(Integer.parseInt(numberOfPlayers));
        restart();
        notifyPropertyChanges();
    }

    @Bindable
    public String[][] getPlayerValue()
    {
        return mGame.getPlayerValue();
    }

    @Bindable
    public String[] getPlayerTotalUpValue()
    {
        return mGame.getPlayerTotalUpValue();
    }

    @Bindable
    public String[] getPlayerTotalUpValuePlusBonus()
    {
        return mGame.getPlayerTotalUpValuePlusBonus();
    }

    @Bindable
    public String[] getPlayerTotalDownValue()
    {
        return mGame.getPlayerTotalDownValue();
    }

    @Bindable
    public String[] getPlayerTotalValue()
    {
        return mGame.getPlayerTotalValue();
    }

    @Bindable
    public boolean[] getDiceSelected()
    {
        return mGame.getDiceSelected();
    }

    @Bindable
    public boolean[] getPlayerActive()
    {
        return mGame.getPlayerActive();
    }

    @Bindable
    public boolean getHasRolls()
    {
        return mGame.hasRolls();
    }

    @Bindable
    public String getRollsState()
    {
        return ROLL_DICES_TITLE + ": " + mGame.getRollsState();
    }

    @Bindable
    public String[] getPlayerBonusValue()
    {
        return mGame.getPlayerBonusValue();
    }

    @Bindable
    public String getRoundsState()
    {
        return ROUNDS_TITLE + ": " + mGame.getRoundsState();
    }

    @Bindable
    public boolean[] getHasHighscore()
    {
        return mGame.getHasHighscore();
    }

    @Bindable
    public boolean[][] getIsUsed()
    {
        return mGame.isUsed();
    }
}
