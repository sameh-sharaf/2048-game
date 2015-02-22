package com.ssharaf.game_2048;

import com.ssharaf.game_2048.SimpleGestureFilter.SimpleGestureListener;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Toast;

public class Game_2048Activity extends Activity implements SimpleGestureListener
{    
	// Game view object
	private GameView gameView;
	
	// Gesture detector for swipe direction
	private SimpleGestureFilter detector;
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);

        // Display game interface view by creating an object and set content view.
        gameView = new GameView(getApplicationContext());
        setContentView(gameView);
        
        // Detect touched area 
        detector = new SimpleGestureFilter(this,this);
    }
    
    @Override
    public boolean dispatchTouchEvent(MotionEvent me)
    {
    	// Call onTouchEvent of SimpleGestureFilter class
        this.detector.onTouchEvent(me);
        return super.dispatchTouchEvent(me);
    }
    
    @Override
    /**
     * Called when swipe on screen is detected.
     */
	public void onSwipe(int direction) 
    {
    	// Get GamePuzzle object from GameView to process user's commands.
    	GamePuzzle gamePuzzle = gameView.getGamePuzzle();
	 
    	switch (direction) 
    	{
    		// When user swipes right on screen
    		case SimpleGestureFilter.SWIPE_RIGHT : 
    			// For each row in game grid:
    			for (int i = 0; i < gamePuzzle.getTiles(); i++)
				{
    				// First right concatenation: so to remove any spaces between numbers 
    				gamePuzzle.concat_right(gamePuzzle.getPuzzleArray(i));
    				
    				// Sum each identical tiles from left to right
    				gamePuzzle.merge_right(gamePuzzle.getPuzzleArray(i));
    				
    				// Second right concatenation: After merging identical tiles in row, re-concatenate to right
    				gamePuzzle.concat_right(gamePuzzle.getPuzzleArray(i));
				}
    			
    			break;
    		// When user swipes left on screen
    		case SimpleGestureFilter.SWIPE_LEFT :  
    			// For each row in game grid:
    			for (int i = 0; i < gamePuzzle.getTiles(); i++)
				{
    				// First left concatenation: so to remove any spaces between numbers 
    				gamePuzzle.concat_left(gamePuzzle.getPuzzleArray(i));
    				
    				// Sum each identical tiles from right to left
    				gamePuzzle.merge_left(gamePuzzle.getPuzzleArray(i));
    				
    				// Second left concatenation: After merging identical tiles in row, re-concatenate to left
    				gamePuzzle.concat_left(gamePuzzle.getPuzzleArray(i));
				}
    			
    			break;
    		// When user swipes up on screen
    		case SimpleGestureFilter.SWIPE_DOWN :  
    			// For each column in game grid:
    			for(int j = 0; j < gamePuzzle.getTiles(); j++)
				{
    				// First, let's move the column to individual array to process merging and concatenation
    				// (Much easier this way!)
					int[] temp = new int[gamePuzzle.getTiles()];
					for(int i = 0; i < gamePuzzle.getTiles(); i++)
					{
						temp[i] = gamePuzzle.get_tile(i, j); 
					}
					
					// Here, we treat up swipe as right swipe, so it passes the same path of merging and concatenation. 
					gamePuzzle.concat_right(temp);
					gamePuzzle.merge_right(temp);
					gamePuzzle.concat_right(temp);
					
					// After all the processing, time to move column to its place on game grid
					for(int i = 0; i < gamePuzzle.getTiles(); i++)
					{
						gamePuzzle.set_tile(i, j, temp[i]);
					}
				}
    			break;
    		// Finally, when user swipes down on screen
    		case SimpleGestureFilter.SWIPE_UP :
    			for(int j = 0; j < gamePuzzle.getTiles(); j++)
				{
    				// Same as down swipe, we move the column to an individual array.
					int[] temp = new int[gamePuzzle.getTiles()];
					for(int i = 0; i < gamePuzzle.getTiles(); i++)
					{
						temp[i] = gamePuzzle.get_tile(i, j);
					}
					
					gamePuzzle.concat_left(temp);
					gamePuzzle.merge_left(temp);
					gamePuzzle.concat_left(temp);
					
					for(int i = 0; i < gamePuzzle.getTiles(); i++)
					{
						gamePuzzle.set_tile(i, j, temp[i]);
					}
				}
    			break;
    	}
    	
    	gameView.call_invalidate();
    	
    	// Check if game ends either by winning or losing. If not, continue by adding a new number
    	if (!gamePuzzle.isLost() && !gamePuzzle.isWin())
    	{
    		gamePuzzle.insert_new_number();
    	}
    	else if(gamePuzzle.isLost())
    		Toast.makeText(getApplicationContext(), "You lost...", Toast.LENGTH_LONG).show();
    	else if(gamePuzzle.isWin())
    		Toast.makeText(getApplicationContext(), "You win!!", Toast.LENGTH_LONG).show();
    	
    	// Check grid view
    	gamePuzzle.print_grid();
    }
    	
	@Override
 	public void onDoubleTap() 
	{
		Toast.makeText(this, "Double Tap", Toast.LENGTH_SHORT).show();
 	}
}