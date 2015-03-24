package com.evankim.recmath.sigmaout;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by evankim on 3/19/15.
 */
public class GameLogic {

    private int board_rows = 0;
    private int board_columns = 0;
    private int current_move_count = 0;
    private int total_lights_on = 0;
    private int goal_number_moves_to_solve = 0;

    private boolean game_won_condition = false;

    // I have no justifiable reason to do this...
    private int move_counter_id = (int) (Math.random()*10000 + board_rows*board_columns + 1);

    /**
     * Board states are as follows:
     *   1: "on"
     *   0: "off"
     *  -1: "deactivated"
     *
     *  Initial board states are currently stored if we want to implement a reset function in the future
     *  This is not currently implemented (initial_board is not seeded with random moves)
     */
    private ArrayList<ArrayList<Integer>> initial_board_states = new ArrayList<ArrayList<Integer>>();
    private ArrayList<ArrayList<Integer>> current_board_states = new ArrayList<ArrayList<Integer>>();

    public void generate_initial_static_board(int desired_rows, int desired_columns) {

        /**
         * This function is currently called by the tutorial page 1 to demonstrate board mechanics
         */

        // Check if we provided invalid board size
        if (desired_rows <= 0 || desired_columns <= 0) return;

        board_rows = desired_rows;
        board_columns = desired_columns;
        current_move_count = 0;
        game_won_condition = false;
        initial_board_states.clear();
        current_board_states.clear();

        // Ugly hack to ensure the tutorial never sets win condition...
        total_lights_on = board_rows*board_columns + 1;

        // Create a board state of alternating on/off lights, starting with on in the top left
        boolean alternate_switch = true;
        for (int rows = 0; rows < board_rows; rows++) {
            current_board_states.add(new ArrayList<Integer>());
            for(int columns = 0; columns < board_columns; columns++) {
                if (alternate_switch) { current_board_states.get(rows).add(1); alternate_switch = !alternate_switch;}
                else { current_board_states.get(rows).add(0); alternate_switch = !alternate_switch;}
            }
        }
    }

    public void generate_random_initial_board(String difficulty_level) {

        /**
         * This function generates a random, solvable board by creating a solved board, then doing random moves
         */

        // Determine board layout by difficulty level
        switch (difficulty_level) {
            case "Easy":
                board_rows = 4;
                board_columns = 4;
                break;
            case "Medium":
                board_rows = 6;
                board_columns = 6;
                break;
            case "Hard":
                board_rows = 8;
                board_columns = 8;
                break;
            case "Tutorial_2":
                board_rows = 2;
                board_columns = 2;
                break;
            default:
                return;
        }

        // Reset global variables in the event this is recursively called (check end of this function)
        total_lights_on = 0;
        goal_number_moves_to_solve = 0;
        game_won_condition = false;
        initial_board_states.clear();
        current_board_states.clear();

        // Controls how many moves to make as a function of total points when initially setting the board.
        double initial_move_factor = 0.67d;

        // This will store all the initial, randomized moves to set the initial board state
        boolean[] randomized_moves = new boolean[board_rows*board_columns];
        Arrays.fill(randomized_moves, false);

        // Store randomized initial moves
        for (int total_moves = 1; total_moves <= (int) Math.ceil(board_rows*board_columns*initial_move_factor); total_moves++) {
            randomized_moves[(int) (Math.random()*(board_rows*board_columns))] = true;
        }

        // Create the initial board structure
        for (int current_row = 0; current_row < board_rows; current_row++) {
            initial_board_states.add(new ArrayList<Integer>());
            current_board_states.add(new ArrayList<Integer>());
            for (int current_column = 0; current_column < board_columns; current_column++) {
                initial_board_states.get(current_row).add(0);
                current_board_states.get(current_row).add(0);
            }
        }

        for (int current_index = 0; current_index < board_rows*board_columns; current_index++) {
            if (!randomized_moves[current_index]) continue;

            // Perform moves and keep track of the computer's number of moves to solve this board, for user scorekeeping
            goal_number_moves_to_solve++;
            handle_click(current_index);
        }

        // Very low probability of the random moves resulting in a board that is already won, but just in case...
        if (total_lights_on <= 0) generate_random_initial_board(difficulty_level);

        // Reset the move counter since the initial random moves increment it
        current_move_count = 0;
    }

    public void reset_board() {
        // TODO: do this later by resetting board to the initial board and resetting move count
    }

    public void handle_click(int button_id) {

        /**
         * Set the appropriate board states after a move has been made
         */

        // Check if the game has been won.  If yes, then don't accept more changes to the board.
        if (game_won_condition) return;

        // Check if the row and column value are valid for the current board
        if (button_id >= board_columns * board_rows || button_id < 0) {
            return;
        }

        int row = button_id / board_columns;
        int column = button_id % board_columns;

        // This flag controls whether to flip the state.  Note that it will not flip the value at (row, column).  This must be flipped at the end
        boolean flip_state = false;

        for (int current_row = row-1; current_row <= row+1; current_row++) {
            for (int current_column = column-1; current_column <= column+1; current_column++) {
                // Check if the current column is inside the board (edges can be problematic)
                if (current_column < 0 || current_column >= board_columns || current_row < 0 || current_row >= board_columns) {
                    flip_state = !flip_state;
                    continue;
                }

                // Skip this entry if the flip_state flag is false
                if (!flip_state) {
                    flip_state = !flip_state;
                    continue;
                }

                switch(current_board_states.get(current_row).get(current_column)) {
                    case 0:
                        current_board_states.get(current_row).set(current_column, 1);
                        total_lights_on++;
                        break;
                    case 1:
                        current_board_states.get(current_row).set(current_column, 0);
                        total_lights_on--;
                        break;
                    default:
                        break;
                }

                // Flip the flip_state flag
                flip_state = !flip_state;
            }
        }

        // Flip the actual (row, column) entry, since the flip_state flag bypasses it
        switch(current_board_states.get(row).get(column)) {
            case 0:
                current_board_states.get(row).set(column, 1);
                total_lights_on++;
                break;
            case 1:
                current_board_states.get(row).set(column, 0);
                total_lights_on--;
                break;
            default:
                break;
        }

        // Increment the move counter
        current_move_count++;

        // Check if all the lights have gone out.  If so, implement the win condition.
        if (total_lights_on <= 0) userWonGame();
    }

    private void userWonGame() {

        /**
         * Do whatever we want to do when the user wins
         */

        game_won_condition = true;
    }

    /**
     * Getters
     *
     * @list: (int) getBoard_rows, (int) getBoard_columns, ArrayList<ArrayList<int>> getCurrent_board_states,
     *        (int) getMove_counter_id, (int) getTotal_move_count, (int) getGoal_number_moves_to_solve,
     *        (boolean) getGame_won_condition
     */

    public int getBoard_rows() { return board_rows; }

    public int getBoard_columns() {
        return board_columns;
    }

    public ArrayList<ArrayList<Integer>> getCurrent_board_states() {
        return current_board_states;
    }

    public int getMove_counter_id() {
        return move_counter_id;
    }

    public int getCurrent_move_count() {
        return current_move_count;
    }

    public int getGoal_number_moves_to_solve() {
        return goal_number_moves_to_solve;
    }

    public boolean getGame_won_condition() {
        return game_won_condition;
    }

    /**
     * Setters
     *
     * @list: (void) setBoard_rows(int), (void) setBoard_columns(int)
     */

    public void setBoard_rows(int new_rows) {
        board_rows = new_rows;
    }

    public void setBoard_columns(int new_columns) {
        board_columns = new_columns;
    }
    
}
