package com.example.connect3game;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    int [][] winningPositions = {{0,1,2}, {3,4,5}, {6,7,8}, {0,3,6}, {1,4,7}, {2,5,8}, {0,4,8}, {2,4,6}};
    int [] gameState = {2, 2, 2, 2, 2, 2, 2, 2, 2};
    boolean gameActive = true;
    int activeplayer = 0;


    @SuppressLint("SetTextI18n")
    public void dropIn(View view){

        ImageView counter = (ImageView) view;
        counter.getTag().toString();
        int tappedCounter = Integer.parseInt(counter.getTag().toString());

        if(gameState[tappedCounter] == 2 && gameActive) {
            gameState[tappedCounter] = activeplayer;
            counter.setTranslationY(-1500);

            if (activeplayer == 0) {
                counter.setImageResource(R.drawable.yellow);
                activeplayer = 1;
            } else {
                counter.setImageResource(R.drawable.red);
                activeplayer = 0;
            }
            counter.animate().translationYBy(1500).rotation(3600).setDuration(300);

            for (int[] winningPosition : winningPositions) {

                if (gameState[winningPosition[0]] == gameState[winningPosition[1]] &&
                        gameState[winningPosition[1]] == gameState[winningPosition[2]] && gameState[winningPosition[0]] != 2) {
                    String winner;
                    gameActive = false;
                    if (activeplayer == 1) {
                        winner = "Yellow";
                    } else {
                        winner = "Red";
                    }

                    Button playAgainbutton = findViewById(R.id.playAgainbutton);
                    TextView winnertextView = findViewById(R.id.winnertextView);
                    winnertextView.setText(winner + " has won");

                    playAgainbutton.setVisibility(View.VISIBLE);
                    winnertextView.setVisibility(View.VISIBLE);

                }
            }
        }
    }

    public void playAgain(View view){

        Button playAgainbutton = (Button) findViewById(R.id.playAgainbutton);
        TextView winnertextView = findViewById(R.id.winnertextView);

        playAgainbutton.setVisibility(View.INVISIBLE);
        winnertextView.setVisibility(View.INVISIBLE);

        androidx.gridlayout.widget.GridLayout gridLayout = findViewById(R.id.gridLayout);

        for(int i = 0; i<gridLayout.getChildCount(); i++){
            ImageView counter = (ImageView) gridLayout.getChildAt(i);
            counter.setImageDrawable(null);
        }

        for(int i = 0; i<gameState.length; i++){
            gameState[i] = 2;
        }

        activeplayer = 0;
        gameActive = true;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}