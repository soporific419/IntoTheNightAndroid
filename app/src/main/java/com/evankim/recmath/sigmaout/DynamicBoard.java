package com.evankim.recmath.sigmaout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


public class DynamicBoard extends ActionBarActivity {

    private GameLogic current_board = new GameLogic();

    private int background_color_on = 0xff50cc50;
    private int background_color_off = Color.DKGRAY;
    private int background_color_disabled = 0xff282828;

    public boolean win_message_displayed = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the game difficulty level from the main menu
        Intent root_intent = getIntent();
        String game_difficulty = root_intent.getStringExtra(SigmaMain.DIFFICULTY_LEVEL);
        win_message_displayed = false;

        current_board.generate_random_initial_board(game_difficulty);

        // Build the game board
        final LinearLayout vertical_stack = new LinearLayout(this);
        vertical_stack.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
                );

        LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        buttonLayoutParams.weight = 1.0f;
        buttonLayoutParams.setMargins(6,6,6,6);

        LinearLayout.LayoutParams titleLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        titleLayoutParams.weight = 1.0f;

            LinearLayout title_bar = new LinearLayout(this);
            title_bar.setOrientation(LinearLayout.HORIZONTAL);

                TextView title_name = new TextView(this);

                title_name.setText(game_difficulty.concat(" Game"));
                title_name.setTextSize(25.0f);
                title_bar.addView(title_name, titleLayoutParams);

        vertical_stack.addView(title_bar, linearLayoutParams);

            TextView move_counter = new TextView(this);

            move_counter.setText("(Total Moves / Target Number Of Moves): 0 / ".concat(Integer.toString(current_board.getGoal_number_moves_to_solve())));
            move_counter.setTextSize(10.0f);
            move_counter.setId(current_board.getMove_counter_id());
            move_counter.setPadding(10,0,0,0);

        vertical_stack.addView(move_counter, titleLayoutParams);

            LinearLayout board_rows = new LinearLayout(this);
            board_rows.setOrientation(LinearLayout.VERTICAL);


            for (int row = 0; row < current_board.getBoard_rows(); row++) {
                LinearLayout current_row = new LinearLayout(this);
                current_row.setOrientation(LinearLayout.HORIZONTAL);

                for (int column = 0; column < current_board.getBoard_columns(); column++) {
                    Button current_button = new Button(this);
                    // Set the text of the current button to "$row,$column"
                    //current_button.setText(Integer.toString(row+1).concat(",").concat(Integer.toString(column+1)));
                    // Set the current button's id to the order in which it was created
                    final int current_button_id = (row*current_board.getBoard_columns()) + column;
                    current_button.setId(current_button_id);
                    current_button.setWidth(0);
                    ///** hold off on click functionality for debug
                    current_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View current_view) {
                            int button_id = current_view.getId();
                            current_board.handle_click(button_id);
                            update_board_display();
                        }
                    });
                    current_button.setBackgroundResource(R.drawable.board_button_styles_rounded_green);

                    current_row.addView(current_button, buttonLayoutParams);
                }

                board_rows.addView(current_row, linearLayoutParams);
            }

            vertical_stack.addView(board_rows, linearLayoutParams);

        setContentView(vertical_stack);
        update_board_display();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dynamic_board, menu);
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

    public void update_board_display() {

        /**
         * This function updates the backgrounds of the tiles, and move counter, based on their on/off state
         */

        int board_rows = current_board.getBoard_rows();
        int board_columns = current_board.getBoard_columns();
        int current_id;

        for(int row = 0; row < board_rows; row++) {
            for (int column = 0; column < board_columns; column++) {
                current_id = (row * board_columns) + column;
                Button current_button = (Button) findViewById(current_id);
                if (current_button == null) continue;
                GradientDrawable button_background = (GradientDrawable) current_button.getBackground();
                switch(current_board.getCurrent_board_states().get(row).get(column)) {
                    case 0:
                        button_background.setColor(background_color_off);
                        break;
                    case 1:
                        button_background.setColor(background_color_on);
                        break;
                    default:
                        button_background.setColor(background_color_disabled);
                }

            }
        }

        TextView move_counter = (TextView) findViewById(current_board.getMove_counter_id());
        move_counter.setText("(Total Moves / Target Number Of Moves): ".concat(
                Integer.toString(current_board.getCurrent_move_count())).concat(" / ").concat(
                Integer.toString(current_board.getGoal_number_moves_to_solve())));

        // Check if all the lights are out, and perform the win actions
        if (current_board.getGame_won_condition()) doWin();
    }

    public void doWin() {

        /**
         * Do whatever we want to do when the user wins.
         */

        // Check if we have already displayed this message
        if (win_message_displayed) return;

        // Alert the user that s/he has won the game
        AlertDialog winDialog = new AlertDialog.Builder(this).create();
        winDialog.setTitle("Congratulations!");
        winDialog.setMessage("You won!\nHave some cake?");
        winDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // TODO do something here if we want to, in the future.
            }
        });
        // Set the icon (who doesn't love green cake...?)
        winDialog.setIcon(R.drawable.win_dialog_green_cake_icon);
        winDialog.show();

        // Set the flag to not display this message again
        win_message_displayed = true;
    }
}
