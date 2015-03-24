package com.evankim.recmath.sigmaout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class SigmaMain extends ActionBarActivity {

    public final static String DIFFICULTY_LEVEL = "com.evankim.recmath.sigmaout";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sigma_main);

        // Add the click actions for the buttons on the main menu
        Button button_difficulty_1 = (Button) findViewById(R.id.new_difficulty_1);
        button_difficulty_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDifficulty1(view);
            }
        });

        Button button_difficulty_2 = (Button) findViewById(R.id.new_difficulty_2);
        button_difficulty_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDifficulty2(view);
            }
        });

        Button button_difficulty_3 = (Button) findViewById(R.id.new_difficulty_3);
        button_difficulty_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDifficulty3(view);
            }
        });

        Button tutorial_button = (Button) findViewById(R.id.tutorial_button);
        tutorial_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTutorial(view);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sigma_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void startDifficulty1(View view) {
        Intent newGameDifficulty1 = new Intent(this, DynamicBoard.class);
        newGameDifficulty1.putExtra(DIFFICULTY_LEVEL, "Easy");
        startActivity(newGameDifficulty1);
    }

    public void startDifficulty2(View view) {
        Intent newGameDifficulty2 = new Intent(this, DynamicBoard.class);
        newGameDifficulty2.putExtra(DIFFICULTY_LEVEL, "Medium");
        startActivity(newGameDifficulty2);
    }

    public void startDifficulty3(View view) {
        Intent newGameDifficulty3 = new Intent(this, DynamicBoard.class);
        newGameDifficulty3.putExtra(DIFFICULTY_LEVEL, "Hard");
        startActivity(newGameDifficulty3);
    }

    public void startTutorial(View view) {
        Intent newTutorialActivity = new Intent(this, TutorialActivity.class);
        startActivity(newTutorialActivity);
    }
}
