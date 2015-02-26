package com.ssharaf.game_2048;

import java.util.Random;
import android.util.Log;

/**
 * 2048 game core class which manages the game's logic. 
 * 
 * @author SAMEH SHARAF
 *
 */

public class Game2048 
{
	// Game grid.
	private int[][] game_grid;
	
	// Game number of tiles (Default: 4).
	private int tiles;
	
	// Game's target number to win (Default: 2048).
	private int winning_number;
	
	// User's score.
	private int score;
	
	// Game's best score.
	private static int best_score;
	
	// Last added number's row number.
	private int last_num_row;
	
	// Last added number's column number.
	private int last_num_col;
	
	// Tag name for debugging.
	private final static String TAG = "Game 2048";
	
	/**
	 * Default constructor.
	 * Here, we defined the game with 4 tiles and target number of 2048
	 * which is the original. You can modify it whatever you want!
	 */
	public Game2048()
	{
		this(4, 2048);
	}
	
	/**
	 * Constructor for Game 2048.
	 * @param tiles Number of tiles for game (Default: 4).
	 * @param winning_number Number which is the game target (Default: 2048).
	 */
	public Game2048(int tiles, int winning_number)
	{
		this.winning_number = winning_number;
		this.tiles = tiles;
		
		// Create new game.
		begin_game();
	}
	
	/**
	 * Get game grid tile.
	 * @param i Row number.
	 * @param j Column number
	 * @return
	 */
	public int get_tile(int i, int j)
	{
		return game_grid[i][j];
	}
	
	/**
	 * Set game grid tile value
	 * @param i Row number.
	 * @param j Column number.
	 * @param x New value to be set.
	 */
	public void set_tile(int i, int j, int x)
	{
		game_grid[i][j] = x;
	}
	
	/**
	 *  Clear puzzle grid.
	 */
	public void clear_grid()
	{
		for (int i = 0; i < tiles; i++)
			for (int j = 0; j < tiles; j++)
				game_grid[i][j] = 0;
	}
	
	/**
	 * Randomly inserting new number to puzzle grid, looking for an empty tile. 
	 */
	public void insert_new_number()
	{
		int x = 0;
		int y = 0;
		
		// Create Random object
		Random random = new Random();
		
		// Keep looking for an empty tile until you find one.
		do
		{
			x = random.nextInt(tiles);
			y = random.nextInt(tiles);
		} 
		while (game_grid[x][y] != 0);
		
		// Add new number to game grid
		game_grid[x][y] = 2;
		
		// Store coordinates of last added number to grid
		last_num_row = x;
		last_num_col = y;
	}
	
	public int get_last_num_row()
	{
		return last_num_row;
	}
	
	public int get_last_num_col()
	{
		return last_num_col;
	}
	
	/**
	 * Print game grid contents into LogCat (for debugging purposes).
	 */
	public void print_grid()
	{
		Log.i(TAG, "2048 Game Grid:");
		
		for(int i = 0; i < tiles; i++)
		{
			for(int j = 0; j < tiles; j++)
				System.out.print(game_grid[i][j] + " ");
			System.out.println();
		}
	}
	
	/**
	 * Check if player wins the game by checking one of grid tiles reached target number.
	 * @return Flag indicates if player wins the game.
	 */
	public boolean isWin()
	{
		for (int i = 0; i < tiles; i++)
			for (int j = 0; j < tiles; j++)
				if (game_grid[i][j] == winning_number)
					return true;
		
		return false;
	}
	
	/**
	 * Check if player lost the game by checking all tiles are filled and no movements available.
	 * @return Flag indicates if player lost the game.
	 */
	public boolean isLost()
	{
		// First check: If there are empty tiles left.
		for (int i = 0; i < tiles; i++)
			for (int j = 0; j < tiles; j++)
				if (game_grid[i][j] == 0)
					return false;
		
		// Next check: If there are possible combinations between neighbor tiles horizontally or vertically.
		return !isPlayable();
	}
	
	/**
	 * Combine each pair of tiles with same value and sum them into one tile.
	 * @param arr Array which its items to be merged
	 * @return Flag indicates if combination occurred on array. Useful for checking possible movements.
	 */
	public boolean combine_tiles(int[] arr)
	{
		// Define flag for changes in array
		boolean isChanged = false;
		
		// First, concatenate array tiles to combine equal neighbors later. 
		isChanged = concat_tiles(arr) || isChanged;
		
		for(int i = tiles - 1; i > 0; i--)
		{
			if (arr[i] != 0 && arr[i] == arr[i-1])
			{
				arr[i] = arr[i] * 2;
				arr[i-1] = 0;
				
				// Add sum to score
				score += arr[i];
				
				// Flag change in array
				isChanged = true;
				Log.i(TAG, "combination applied.");
				// Skip next tile
				i--;
			}
		}
		
		// Second concatenation after equal tiles combination.
		isChanged = concat_tiles(arr) || isChanged;
		
		return isChanged;
	}
	
	/**
	 * Concatenate tiles and remove spacing between numbers.
	 * @param arr Grid tiles to be concatenated.
	 * @return Flag indicates if concatenation occurred on array. Useful for checking possible movements.
	 */
	public boolean concat_tiles(int[] arr)
	{
		boolean isChanged = false;
		
		for(int i = 0; i < tiles - 1; i++)
			for(int j = tiles - 1; j > i; j--)
				if (arr[j] == 0 && arr[j-1] != 0)
				{
					arr[j] = arr[j-1];
					arr[j-1] = 0;
					
					// Flag change in array
					isChanged = true;
					Log.i(TAG, "concatenation applied.");
				}
		
		return isChanged;
	}
	
	/**
	 * Begin new game
	 */
	public void begin_game()
	{
		// Create new puzzle grid
		game_grid = new int[tiles][tiles];
		
		// Initialize grid by assigning 0s to tiles
		clear_grid();
		
		// Set score to zero
		score = 0;
		
		// Add two numbers to begin a new game!
		insert_new_number();
		insert_new_number();
	}
	
	/**
	 * Get game grid dimension (Default: 4).
	 * @return Grid dimension. 
	 */
	public int getTiles()
	{
		return tiles;
	}
	
	/**	
	 * Get game grid row
	 * @param Row number.
	 * @return Grid row.
	 */
	public int[] getGameGridRow(int i)
	{
		return game_grid[i];
	}
	
	/**
	 * Get game score
	 * @return game score.
	 */
	public int getScore()
	{
		return score;
	}
	
	/**
	 * Get game's best score.
	 * @return Best score value.
	 */
	public int getBestScore()
	{
		return best_score;
	}
	
	/**
	 * Set game's best score.
	 * @param best_score New best score to be set.
	 */
	public void setBestScore(int best_score)
	{
		Game2048.best_score = best_score;
	}
	
	/**
	 * Copy a one-dimensional array
	 * @param arr1 Source array.
	 * @param arr2 Destination array.
	 * @param size Size to be copied starting from first item.
	 * @param isReverseCopy Choose copy mode: Normal or reversed copy.
	 */
	public static void copyArray(int[] arr1, int[] arr2, boolean isReverseCopy)
	{
		for(int i=0; i<arr1.length; i++)
		{
			if (isReverseCopy)
				arr2[i] = arr1[arr1.length - i - 1];
			else
				arr2[i] = arr1[i];
		}
	}
	
	/**
	 * Copy specified column in game grid to array. 
	 * @param arr Destination array.
	 * @param j Column number.
	 * @param isReverseCopy Copy mode: Normal or reversed.
	 */
	public void copyFromColumn(int[] arr, int j, boolean isReverseCopy)
	{
		for(int i=0; i<tiles; i++)
		{
			if (isReverseCopy)
				arr[i] = game_grid[tiles - i - 1][j];
			else
				arr[i] = game_grid[i][j];
		}
	}
	
	/**
	 * Copy input array to specified grid column. 
	 * @param arr Source array.
	 * @param j Column number.
	 * @param isReverseCopy Copy mode: Normal or reversed.
	 */
	public void copyToColumn(int[] arr, int j, boolean isReverseCopy)
	{
		for(int i=0; i<tiles; i++)
		{
			if (isReverseCopy)
				game_grid[tiles - i - 1][j] = arr[i];
			else
				game_grid[i][j] = arr[i];
		}
	}
	
	/**
	 * Check if any moves on game is possible, to determine if game has reached a dead-end.
	 * @return Flag whether there is possible move.
	 */
	public boolean isPlayable()
	{
		// Check rows if possible moves can be located.
		for(int i = 0; i < tiles; i++)
		{
			int[] temp = new int[tiles];
			copyArray(game_grid[i], temp, false);
			
			if (combine_tiles(temp))
			{
				Log.i(TAG, "Row " + i + " has movement.");
				return true;
			}
		}
		
		// Check columns if possible combine can be located.
		for(int j = 0; j < tiles; j++)
		{
			int[] temp = new int[tiles];
			copyFromColumn(temp, j, false);
			
			if (combine_tiles(temp))
			{
				Log.i(TAG, "Column " + j + " has movement.");
				return true;
			}
		}
		
		Log.i(TAG, "No movements available anymore...");
		return false;
	}
}