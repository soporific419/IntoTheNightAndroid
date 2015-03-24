package com.evankim.recmath.sigmaout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;


public class TutorialActivity extends ActionBarActivity {

    private ArrayList<Integer> tutorial_tile_ids = new ArrayList<Integer>();
    private final int tutorial_rows = 3;
    private final int tutorial_columns = 3;
    private GameLogic tutorial_board = new GameLogic();

    private int background_color_on = 0xff50cc50;
    private int background_color_off = Color.DKGRAY;
    private int background_color_disabled = 0xff282828;

    public boolean win_message_displayed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Start with tutorial screen 1
        tutorial1Creation();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tutorial, menu);
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

    public void doTutorialClickLookup(int button_id) {
        /**
         * This function converts a button click within the tutorial into the
         * appropriate function call for the board's handle_click method.
         *
         * The dynamically generated boards contain button ids 1..N, but
         * the tutorial has generated ids.  It might be a good idea to do
         * something similar in the future.
         */

        // Convert the button id into the sequential order in which it appears on the board
        int sequential_button_number = tutorial_tile_ids.indexOf(button_id);
        // Check if the button lookup was successful, otherwise do nothing
        if (sequential_button_number < 0) return;
        // Perform the actual click action on the board
        tutorial_board.handle_click(sequential_button_number);
    }

    public void update_tutorial_board() {

        /**
         * This function updates the game board tile backgrounds based on the game board states.
         */

        int board_rows = tutorial_board.getBoard_rows();
        int board_columns = tutorial_board.getBoard_columns();
        int current_id;

        // Loop through each button on the board and set the correct
        // background color for its on/off/whatever state.
        for(int row = 0; row < board_rows; row++) {
            for (int column = 0; column < board_columns; column++) {
                current_id = (row * board_columns) + column;
                Button current_button = (Button) findViewById(tutorial_tile_ids.get(current_id));
                if (current_button == null) continue;
                GradientDrawable button_background = (GradientDrawable) current_button.getBackground();
                switch(tutorial_board.getCurrent_board_states().get(row).get(column)) {
                    case 0:
                        button_background.setColor(background_color_off);
                        current_button.setText("Off");
                        break;
                    case 1:
                        button_background.setColor(background_color_on);
                        current_button.setText("On");
                        break;
                    default:
                        button_background.setColor(background_color_disabled);
                }

            }
        }

        // Check if the board is entirely off, and if so, do things.
        if (tutorial_board.getGame_won_condition()) doTutorialWin();
    }

    public void tutorial1Creation() {

        /**
         * This function displays tutorial page 1.
         */

        setContentView(R.layout.tutorial_activity_1);

        tutorial_board.generate_initial_static_board(tutorial_rows, tutorial_columns);
        win_message_displayed = false;

        /** hardcode this in for now...
         * might regret it at some point.
         */
        tutorial_tile_ids.clear();
        tutorial_tile_ids.add(R.id.tutorial_1_button11);
        tutorial_tile_ids.add(R.id.tutorial_1_button12);
        tutorial_tile_ids.add(R.id.tutorial_1_button13);
        tutorial_tile_ids.add(R.id.tutorial_1_button21);
        tutorial_tile_ids.add(R.id.tutorial_1_button22);
        tutorial_tile_ids.add(R.id.tutorial_1_button23);
        tutorial_tile_ids.add(R.id.tutorial_1_button31);
        tutorial_tile_ids.add(R.id.tutorial_1_button32);
        tutorial_tile_ids.add(R.id.tutorial_1_button33);

        GradientDrawable button_off_background = (GradientDrawable) ((Button)findViewById(R.id.tutorial_1_off)).getBackground();
        GradientDrawable button_on_background = (GradientDrawable) ((Button)findViewById(R.id.tutorial_1_on)).getBackground();
        button_off_background.setColor(background_color_off);
        button_on_background.setColor(background_color_on);

        // Add click actions to each of the board tiles
        for (int current_tutorial_tile = 0; current_tutorial_tile < tutorial_tile_ids.size(); current_tutorial_tile++) {
            Button current_tutorial_button = (Button) findViewById(tutorial_tile_ids.get(current_tutorial_tile));
            if (current_tutorial_button == null) continue;
            current_tutorial_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View current_view) {
                    int button_id = current_view.getId();
                    doTutorialClickLookup(button_id);
                    update_tutorial_board();
                }
            });
        }

        update_tutorial_board();

        // Add click actions to the bottom navigation buttons
        findViewById(R.id.next_tutorial_page_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View current_view) {
                tutorial2Creation();
            }
        });

        findViewById(R.id.tutorial_1_main_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View current_view) {
                finish();
            }
        });
    }

    public void tutorial2Creation() {

        /**
         * This function displays tutorial page 2
         */

        setContentView(R.layout.tutorial_activity_2);

        /** hardcode this in for now...
         * might regret it at some point.
         */
        tutorial_tile_ids.clear();
        tutorial_tile_ids.add(R.id.tutorial_2_button11);
        tutorial_tile_ids.add(R.id.tutorial_2_button12);
        tutorial_tile_ids.add(R.id.tutorial_2_button21);
        tutorial_tile_ids.add(R.id.tutorial_2_button22);

        tutorial_board.generate_random_initial_board("Tutorial_2");
        win_message_displayed = false;

        // Add click actions to each of the board tiles
        for (int current_tutorial_tile = 0; current_tutorial_tile < tutorial_tile_ids.size(); current_tutorial_tile++) {
            Button current_tutorial_button = (Button) findViewById(tutorial_tile_ids.get(current_tutorial_tile));
            if (current_tutorial_button == null) continue;
            current_tutorial_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View current_view) {
                    int button_id = current_view.getId();
                    doTutorialClickLookup(button_id);
                    update_tutorial_board();
                }
            });
        }

        update_tutorial_board();

        // Add click actions to each of the navigation buttons at the bottom
        findViewById(R.id.tutorial_2_next_page).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View current_view) {
                tutorial3Creation();
            }
        });

        findViewById(R.id.tutorial_2_prev_page).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View current_view) {
                tutorial1Creation();
            }
        });

    }

    public void tutorial3Creation() {

        /**
         * This function displays tutorial page 3
         */

        setContentView(R.layout.tutorial_activity_3);

        win_message_displayed = false;

        // Add click actions to the bottom navigation buttons
        findViewById(R.id.tutorial_3_menu_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View current_view) {
                finish();
            }
        });

        findViewById(R.id.tutorial_3_prev_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View current_view) {
                tutorial2Creation();
            }
        });
    }

    public void doTutorialWin() {

        /**
         * This function does whatever we want to do when the board is "won"
         */

        // Don't keep doing this each time the user clicks the board after the first time
        if (win_message_displayed) return;

        // Create an alert congratulating the user.
        AlertDialog winDialog = new AlertDialog.Builder(this).create();
        winDialog.setTitle("Congratulations!");
        winDialog.setMessage("Try out your new skills on a harder level.");
        winDialog.setButton(DialogInterface.BUTTON_POSITIVE,"OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // TODO do something here if we want to, in the future.
            }
        });
        winDialog.show();

        // Set the flag to not show this message again while the current board is still active.
        win_message_displayed = true;
    }
}
