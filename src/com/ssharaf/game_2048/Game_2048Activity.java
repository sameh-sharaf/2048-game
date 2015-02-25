package com.ssharaf.game_2048;

import com.ssharaf.game_2048.SimpleGestureFilter.SimpleGestureListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;

public class Game_2048Activity extends Activity implements SimpleGestureListener
{
	// Activity Debugging Tag
	private static final String TAG = "Game 2048 Activity";
	
	// Game view object
	private GameView gameView;
	
	// Gesture detector for swipe direction
	private SimpleGestureFilter detector;
	
	// File manager object responsible for reading/writing log file. 
	private FileManager fileManager;
	
	// User's best score
	private int best_score;
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);

        // Display game interface view by creating an object and set content view.
        setView();
        
        // Detect touched area 
        detector = new SimpleGestureFilter(this,this);
        
        // Read best score from log file.
        fileManager = new FileManager();
        best_score = Integer.parseInt(fileManager.readFile().get(0));
        
        Log.i(TAG, "Best Score: " + best_score);
    }
    
    /**
     * Set content view for game activity
     */
    public void setView()
    {
    	gameView = new GameView(getApplicationContext());
        setContentView(gameView);
    }
    
    @Override
    public boolean dispatchTouchEvent(MotionEvent me)
    {
    	// Call onTouchEvent of SimpleGestureFilter class
        this.detector.onTouchEvent(me);
        return super.dispatchTouchEvent(me);
    }
    
    
    /**
     * Called when swipe on screen is detected.
     */
    @Override
	public void onSwipe(int direction) 
    {
    	// Get Game2048 object from GameView to process user's commands.
    	Game2048 game2048 = gameView.getGame2048();
    	
    	// Flat indicates change in game grid.
    	boolean isChanged = false;
	 
    	// Check user's swipe direction to modify game grid.
    	switch (direction)
    	{
    		// When user swipes right on screen
    		case SimpleGestureFilter.SWIPE_RIGHT :
    			Log.i(TAG, "User swipes right.");
    			
    			// For each row in game grid:
    			for (int i = 0; i < game2048.getTiles(); i++)
				{
    				// Sum each identical tiles from left to right
    				isChanged = game2048.combine_tiles(game2048.getGameGridRow(i)) || isChanged; 
				}
    			
    			break;
    		// When user swipes left on screen
    		case SimpleGestureFilter.SWIPE_LEFT :  
    			Log.i(TAG, "User swipes left.");
    			
    			// For each row in game grid:
    			for (int i = 0; i < game2048.getTiles(); i++)
				{
    				int[] temp = new int[game2048.getTiles()];
    				Game2048.copyArray(game2048.getGameGridRow(i), temp, true);
    				
    				// Sum each identical tiles from right to left
    				isChanged = game2048.combine_tiles(temp) || isChanged;
    				
					// After all the processing, time to move column to its place on game grid
    				Game2048.copyArray(temp, game2048.getGameGridRow(i), true);
				}
    			
    			break;
    		// When user swipes up on screen
    		case SimpleGestureFilter.SWIPE_DOWN :  
    			Log.i(TAG, "User swipes down.");
    			
    			// For each column in game grid:
    			for(int j = 0; j < game2048.getTiles(); j++)
				{
    				// First, let's move the column to individual array to process combination and concatenation
    				// (Much easier this way!)
					int[] temp = new int[game2048.getTiles()];
					game2048.copyFromColumn(temp, j, false);
					
					// Here, we treat up swipe as right swipe, so it passes the same path of merging and concatenation. 
					isChanged = game2048.combine_tiles(temp) || isChanged;
					
					// After all the processing, time to move column to its place on game grid
					game2048.copyToColumn(temp, j, false);
				}
    			break;
    		// Finally, when user swipes down on screen
    		case SimpleGestureFilter.SWIPE_UP :
    			Log.i(TAG, "User swipes up.");
    			
    			for(int j = 0; j < game2048.getTiles(); j++)
				{
    				// Same as down swipe, we move the column to individual array, but in reverse way
					int[] temp = new int[game2048.getTiles()];
					game2048.copyFromColumn(temp, j, true);
					
					// Then calling for combination.
					isChanged = game2048.combine_tiles(temp) || isChanged;
					
					// Finally move it back to game grid
					game2048.copyToColumn(temp, j, true);
				}
    			break;
    	}
    	
    	// Check if user breaks best score
    	if (game2048.getScore() > game2048.getBestScore())
    	{
    		Log.i(TAG, "New best score! New score is: " + game2048.getScore());
    		
    		// Raise new best score.
    		game2048.setBestScore(game2048.getScore());
    		
    		// Write new best score to log file.
    		fileManager.writeFile(String.valueOf(game2048.getScore()), false);
    	}
    	
    	// Refresh game screen by re-drawing the changes made.
    	gameView.call_invalidate();
    	
    	// Check if user lost the game
    	if (game2048.isLost())
    	{
    		showDialog(this, "Dead End..", "Sorry.. You lost. Try again?", "Another round!", "Nah.. I'm done");
    	}	
    	// Didn't lose yet? Then let's check if he wins
    	else if (game2048.isWin())
    	{
    		showDialog(this, "Bravo!", "Congrats.. You win!", "Another round!", "Nah.. I'm done");
    	}
    	// OK then, game still on. Insert a new number to the game grid!
    	else if (isChanged)
    	{
    		Log.i(TAG, "Change detected.");
    		game2048.insert_new_number();
    	}
    	
    	gameView.call_invalidate();
    	game2048.print_grid();
    }
    
    public void showDialog(Activity activity, String title, CharSequence message, String ok_caption, String cancel_caption) 
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setTitle(title);

        builder.setMessage(message);
        builder.setPositiveButton(ok_caption, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int whichButton)
            {
            	// Begin new game by setting new content view
            	setView();
            }
        });
        builder.setNegativeButton(cancel_caption, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int whichButton)
            {
            	// Quit game
            	finish();
            }
        });
        builder.show();
    }
    	
	@Override
 	public void onDoubleTap() 
	{
		//Toast.makeText(this, "Double Tap", Toast.LENGTH_SHORT).show();
 	}
}