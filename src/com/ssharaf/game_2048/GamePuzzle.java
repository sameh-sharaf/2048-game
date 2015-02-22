package com.ssharaf.game_2048;

import java.util.Random;
import java.util.Scanner;

public class GamePuzzle {

	private int[][] puzzle_grid;
	private int tiles;
	private int winning_number;
	
	public GamePuzzle()
	{
		this(4, 2048);
	}
	
	/**
	 * Constructor for 2048 game puzzle
	 * @param tiles Number of tiles for game
	 * @param winnint_number Number which is the game target
	 */
	public GamePuzzle(int tiles, int winning_number)
	{
		this.winning_number = winning_number;
		this.tiles = tiles;

		// Create new puzzle grid
		puzzle_grid = new int[tiles][tiles];
		
		// Initialize grid by assigning 0s to tiles
		clear_grid();
	}
	
	public int get_tile(int i, int j)
	{
		return puzzle_grid[i][j];
	}
	
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
		Random random = new Random();
		int x = 0;
		int y = 0;
		
		// Keep looking for an empty tile until you find it.
		do
		{
			x = random.nextInt(tiles);
			y = random.nextInt(tiles);
		} 
		while (puzzle_grid[x][y] != 0);
		
		puzzle_grid[x][y] = 2;
		
		//System.out.println(x + " " + y);
	}
	
	public void print_grid()
	{
		for(int i = 0; i < tiles; i++)
		{
			for(int j = 0; j < tiles; j++)
				System.out.print(puzzle_grid[i][j] + " ");
			System.out.println();
		}
	}
	
	/**
	 * Check if player won the game by checking one of grid tiles reached target number.
	 * @return Flag indicates if player won the game.
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
	 * Check if player lost the game by checking all tiles are filled.
	 * @return Flag indicates if player lost the game.
	 */
	public boolean isLost()
	{
		for (int i = 0; i < tiles; i++)
			for (int j = 0; j < tiles; j++)
				if (puzzle_grid[i][j] == 0)
					return false;
		
		return true;
	}
	
	public void merge_left(int[] arr)
	{
		for(int i = tiles - 2; i >= 0; i--)
		{
			if (arr[i] == arr[i+1])
			{
				arr[i] = arr[i] * 2;
				arr[i+1] = 0;
				i--;
			}
		}
	}
	
	public void merge_right(int[] arr)
	{
		for(int i = 1; i < tiles; i++)
		{
			if (arr[i] == arr[i-1])
			{
				arr[i] = arr[i] * 2;
				arr[i-1] = 0;
				i++;
			}
		}
	}
	
	public void concat_right(int[] arr)
	{
		for(int i = 0; i < tiles - 1; i++)
			for(int j = i; j < tiles - 1; j++)
				if (arr[j] != 0 && arr[j+1] == 0)
				{
					arr[j+1] = arr[j];
					arr[j] = 0;
				}
	}
	
	public void concat_left(int[] arr)
	{
		for(int i = tiles - 2; i >= 0; i--)
			for(int j = i; j >= 0; j--)
				if (arr[j] == 0 && arr[j+1] != 0)
				{
					arr[j] = arr[j+1];
					arr[j+1] = 0;
				}
	}
	
	public void begin_game()
	{
		// Add to numbers to new game
		insert_new_number();
		insert_new_number();
	}
	
	public int getTiles()
	{
		return tiles;
	}
	
	public int[] getPuzzleArray(int i)
	{
		return puzzle_grid[i];
	}
	
	/*
	
	public static void main(String[] args) {

		GamePuzzle gp = new GamePuzzle();
		
		System.out.println("hello world");
		
		// Add to numbers to new game
		gp.insert_new_number();
		gp.insert_new_number();
		
		System.out.println("Let the game begin!");
		
		int x = 1;
		
		while(!gp.isLost() && !gp.isWin() && x != 0)
		{
			gp.print_grid();
			
			System.out.println("Which direction?");
			
			Scanner reader = new Scanner(System.in);
			
			//get user input for a
			x = reader.nextInt();
			
			switch (x) {
				case 1: // right
					for (int i = 0; i < gp.tiles; i++)
					{
						gp.concat_right(gp.puzzle_grid[i]);
						gp.merge_right(gp.puzzle_grid[i]);
						gp.concat_right(gp.puzzle_grid[i]);
					}
					
					break;
				case 2: // down
					for(int i = 0; i < gp.tiles; i++)
					{
						int[] temp = new int[gp.tiles];
						
						for(int j = 0; j < gp.tiles; j++)
						{
							temp[j] = gp.puzzle_grid[j][i]; 
						}
						
						gp.concat_right(temp);
						gp.merge_right(temp);
						gp.concat_right(temp);
						
						for(int j = 0; j < gp.tiles; j++)
						{
							gp.puzzle_grid[j][i] = temp[j];
						}
					}
					break;
				case 3: // left
					for (int i = 0; i < gp.tiles; i++)
					{
						gp.concat_left(gp.puzzle_grid[i]);
						gp.merge_left(gp.puzzle_grid[i]);
						gp.concat_left(gp.puzzle_grid[i]);
					}
					
					break;
				case 4: // up
					for(int i = 0; i < gp.tiles; i++)
					{
						int[] temp = new int[gp.tiles];
						
						for(int j = 0; j < gp.tiles; j++)
						{
							temp[j] = gp.puzzle_grid[j][i]; 
						}
						
						gp.concat_left(temp);
						gp.merge_left(temp);
						gp.concat_left(temp);
						
						for(int j = 0; j < gp.tiles; j++)
						{
							gp.puzzle_grid[j][i] = temp[j];
						}
					}
					break;
				default:
					break;
			}
			
			gp.insert_new_number();
		}
		
		if (gp.isLost())
			System.out.println("Sorry.. You lost!");
		else if (gp.isWin())
			System.out.println("Congrats.. You win!");
		else
			System.out.println("Goodbye!");
	}*/
}