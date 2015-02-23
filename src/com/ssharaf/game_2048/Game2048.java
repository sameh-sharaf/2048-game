package com.ssharaf.game_2048;

import java.util.Random;

import android.util.Log;

public class Game2048 {

	private int[][] puzzle_grid;
	private int tiles;
	private int winning_number;
	private int score;
	
	// Object to read/write log file data including best score and saved game.
	private FileManager fileManager;
	
	private final static String TAG = "Game 2048";
	
	/**
	 * Default constructor.
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
		
		// Set log file
		fileManager = new FileManager();
		
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
		return puzzle_grid[i][j];
	}
	
	/**
	 * Set game grid tile value
	 * @param i Row number.
	 * @param j Column number.
	 * @param x New value to be set.
	 */
	public void set_tile(int i, int j, int x)
	{
		puzzle_grid[i][j] = x;
	}
	
	/**
	 *  Clear puzzle grid.
	 */
	public void clear_grid()
	{
		for (int i = 0; i < tiles; i++)
			for (int j = 0; j < tiles; j++)
				puzzle_grid[i][j] = 0;
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
		while (puzzle_grid[x][y] != 0);
		
		// Add new number to game grid
		puzzle_grid[x][y] = 2;
	}
	
	/**
	 * Print game grid contents into LogCat (for debugging purposes).
	 */
	public void print_grid()
	{
		for(int i = 0; i < tiles; i++)
			for(int j = 0; j < tiles; j++)
				Log.i(TAG,puzzle_grid[i][j] + " ");
	}
	
	/**
	 * Check if player wins the game by checking one of grid tiles reached target number.
	 * @return Flag indicates if player wins the game.
	 */
	public boolean isWin()
	{
		for (int i = 0; i < tiles; i++)
			for (int j = 0; j < tiles; j++)
				if (puzzle_grid[i][j] == winning_number)
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
				if (puzzle_grid[i][j] == 0)
					return false;
		
		// Next check: If there are possible combinations between neighbor tiles horizontally or vertically.
		return isMergable();
	}
	
	/**
	 * Merge each pair of tiles with same value and add to the left tile. 
	 * @param arr Array which its items to be merged
	 */
	public boolean merge_left(int[] arr)
	{
		boolean isChanged = false;
		concat_left(arr);
		
		for(int i = tiles - 2; i >= 0; i--)
		{
			if (arr[i] == arr[i+1])
			{
				arr[i] = arr[i] * 2;
				arr[i+1] = 0;
				
				// Add sum to score
				score += arr[i];
				
				// Flag change in array
				isChanged = true;
				
				// Skip next tile
				i--;
			}
		}
		
		return isChanged;
	}
	
	/**
	 * Merge each pair of tiles with same value and add to the right tile
	 * @param arr Array which its items to be merged
	 */
	public boolean merge_right(int[] arr)
	{
		boolean isChanged = false;
		concat_right(arr);
		
		for(int i = 1; i < tiles; i++)
		{
			if (arr[i] == arr[i-1])
			{
				arr[i] = arr[i] * 2;
				arr[i-1] = 0;
				
				// Add sum to score
				score += arr[i];
				
				// Flag change in array
				isChanged = true;
				
				// Skip next tile
				i++;
			}
		}
		
		return isChanged;
	}
	
	/**
	 * Concatenate tiles to right
	 * @param arr Grid tiles to be concatenated.
	 */
	public boolean concat_right(int[] arr)
	{
		boolean isChanged = false;
		
		for(int i = 0; i < tiles - 1; i++)
			for(int j = i; j < tiles - 1; j++)
				if (arr[j] != 0 && arr[j+1] == 0)
				{
					arr[j+1] = arr[j];
					arr[j] = 0;
					
					// Flag change in array
					isChanged = true;
				}
		return isChanged;
	}
	
	/**
	 * Concatenate tiles to left
	 * @param arr Grid tiles to be concatenated.
	 */
	public boolean concat_left(int[] arr)
	{
		boolean isChanged = false;
		
		for(int i = tiles - 2; i >= 0; i--)
			for(int j = i; j >= 0; j--)
				if (arr[j] == 0 && arr[j+1] != 0)
				{
					arr[j] = arr[j+1];
					arr[j+1] = 0;
					
					// Flag change in array
					isChanged = true;
				}
		
		return isChanged;
	}
	
	/**
	 * Begin new game
	 */
	public void begin_game()
	{
		// Create new puzzle grid
		puzzle_grid = new int[tiles][tiles];
		
		// Initialize grid by assigning 0s to tiles
		clear_grid();
		
		// Set score to zero
		score = 0;
		
		// Add two numbers to new game
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
		return puzzle_grid[i];
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
	 * Copy a one-dimensional array
	 * @param arr1 Source array.
	 * @param arr2 Destination array.
	 * @param size Size to be copied starting from first item.
	 */
	public void copyArray(int[] arr1, int[] arr2, int size)
	{
		for(int i=0; i<size; i++)
			arr2[i] = arr1[i];
	}
	
	/**
	 * Check if any combination of tiles is located in game grid. This to check if game has reached a dead-end.
	 * @return Flag whether there is possible combination.
	 */
	public boolean isMergable()
	{
		// Check rows if possible merge can be located.
		for(int i=0; i<tiles; i++)
		{
			int[] temp = new int[tiles];
			copyArray(puzzle_grid[i], temp, tiles);
			
			if (merge_right(temp) || merge_left(temp))
			{
				Log.i(TAG, "Row " + i + " has movement.");
				return true;
			}
		}
		
		// Check columns if possible merge can be located.
		for(int j=0; j<tiles; j++)
		{
			int[] temp = new int[tiles];
			for(int i=0; i<tiles; i++)
			{
				temp[i] = puzzle_grid[i][j];
			}
			
			if (merge_right(temp) || merge_left(temp))
			{
				Log.i(TAG, "Column " + j + " has movement.");
				return true;
			}
		}
		
		Log.i(TAG, "No movements available anymore...");
		return false;
	}
}