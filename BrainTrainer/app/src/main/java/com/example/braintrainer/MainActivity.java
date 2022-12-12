package com.example.braintrainer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    Button goButton;
    Button button0;
    Button button1;
    Button button2;
    Button button3;
    TextView resultTextView;
    TextView sumTextView;
    TextView scooreTextView;
    TextView timerTextView;
    Button playAgainButton;
    ConstraintLayout gameLayout;
    ArrayList <Integer> answer = new ArrayList<Integer>();
    int locationOfCorrectAnswer;
    int score = 0;
    int numberOfQuestion = 0;
    int a;
    int b;

    public void chooseAnswer(View view){
        resultTextView.setVisibility(View.VISIBLE);
        if(Integer.toString(locationOfCorrectAnswer).equals(view.getTag().toString())){
            resultTextView.setText("CORRECT!");
            score++;
        }
        else{
            resultTextView.setText("WRONG!:(");
        }
        numberOfQuestion++;
        scooreTextView.setText(Integer.toString(score)+"/"+Integer.toString(numberOfQuestion));
        newQuestion();
    }

    public void playAgain(View view){
        score = 0;
        numberOfQuestion = 0;
        timerTextView.setText("30s");
        scooreTextView.setText(Integer.toString(score)+"/"+Integer.toString(numberOfQuestion));
        newQuestion();
        playAgainButton.setVisibility(View.INVISIBLE);

        new CountDownTimer(30100,1000){
            @Override
            public void onTick(long l){
                timerTextView.setText(String.valueOf(l/1000)+"s");
            }
            @Override
            public void onFinish(){
                resultTextView.setText("DONE!");
                Toast.makeText(MainActivity.this, "Your Score is:"+Integer.toString(score)+"/"+Integer.toString(numberOfQuestion), Toast.LENGTH_LONG).show();
                playAgainButton.setVisibility(View.VISIBLE);
                MediaPlayer mpplayer = MediaPlayer.create(getApplicationContext(), R.raw.music);
                mpplayer.start();
                gameLayout.setVisibility(View.INVISIBLE);
                goButton.setVisibility(View.VISIBLE);
            }
        }.start();
    }

    public void newQuestion(){
        Random rand = new Random();
        a = rand.nextInt(121);
        b = rand.nextInt(121);
        sumTextView.setText(Integer.toString(a)+" + " + Integer.toString(b));

        locationOfCorrectAnswer = rand.nextInt(4);

        answer.clear();

        for(int i = 0; i<4; i++){
            if(i == locationOfCorrectAnswer)
                answer.add(a+b);
            else{
                int wrongAnswer = rand.nextInt(224);
                if(wrongAnswer == a+b)
                    wrongAnswer--;
                answer.add(wrongAnswer);
            }
        }

        button0.setText(Integer.toString(answer.get(0)));
        button1.setText(Integer.toString(answer.get(1)));
        button2.setText(Integer.toString(answer.get(2)));
        button3.setText(Integer.toString(answer.get(3)));
    }

    public void Start(View view){
        goButton.setVisibility(View.INVISIBLE);
        gameLayout.setVisibility(View.VISIBLE);
        playAgain(timerTextView);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        goButton = findViewById(R.id.goButton);
        button0 = findViewById(R.id.button0);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        sumTextView = findViewById(R.id.sumTextView);
        scooreTextView = findViewById(R.id.scoreTextView);
        resultTextView = findViewById(R.id.result);
        timerTextView = findViewById(R.id.timerTextView);
        playAgainButton = findViewById(R.id.playAgainButton);
        gameLayout = findViewById(R.id.gameLayout);
        resultTextView.setVisibility(View.INVISIBLE);
        playAgainButton.setVisibility(View.INVISIBLE);
        goButton.setVisibility(View.VISIBLE);
        gameLayout.setVisibility(View.INVISIBLE);

    }
}