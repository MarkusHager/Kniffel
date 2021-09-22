package com.markushager.kniffel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.markushager.kniffel.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    private ActivityMainBinding mActivityMainBinding;
    private GameViewModel mGameViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mActivityMainBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_main);

        mGameViewModel = new GameViewModel(new Game());
        mActivityMainBinding.setGame(mGameViewModel);
    }
}
