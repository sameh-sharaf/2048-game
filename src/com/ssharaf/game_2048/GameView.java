package com.ssharaf.game_2048;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.util.Log;
import android.view.View;

/**
 * 2048 game design class. Responsible for drawing
 * game's interface.
 * 
 * @author SAMEH SHARAF
 *
 */

public class GameView extends View
{	
	// Tag name for debugging.
	private static final String TAG = "2048 GameView";
	
	// Activity's context.
	private Context context;
	
	// Application's interface canvas.
	private Canvas canvas;
	
	// Paints used to draw background, board and tiles.
	private Paint background_paint;
	private Paint numbers_paint;
	private Paint board_text_paint;
	
	// Screen's width & height.
	private int width;
	private int height;
	
	// Game's core class.
	private Game2048 game2048;
	
	// Tile's margin
	private int margin;
	
	// Game tile dimension.
	private int tiles;
	
	// Tile size.
	private int tile_size;
	
	// Text scale to tile.
	private float text_scale;
	
	// Text scale to board.
	private float board_scale = 0.25f;
	
	// Board's left margin. 
	private int board_margin_x = 20;
	
	// Board's top margin.
	private int board_margin_y = 20;
	
	// Screen orientation.
	private int screen_orientation;
	
	/**
	 * GameView constructor
	 * @param context Application context
	 */
	public GameView(Context context)
	{
		super(context);
		
		// Set application context
		this.context = context;
		
		// Create puzzle grid
		game2048 = new Game2048();
		
		// Initialize parameters
		width = 0;
		height = 0;
		margin = 5;
		tiles = game2048.getTiles();
		tile_size = 0;
		text_scale = 0.55f;
		
		// Begin new game
		game2048.begin_game();
	}
	
	/**
	 * Get game object.
	 * @return Game object.
	 */
	public Game2048 getGame2048()
	{
		return game2048;
	}
	
	/**
	 * Set game object
	 * @param game2048 Game object to be set.
	 */
	public void setGame2048(Game2048 game2048)
	{
		this.game2048 = game2048;
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{		Log.i(TAG, "Size Changed");
		width = w;
		height = h;
		tile_size = (width < height ? width : height) / tiles;
		
		super.onSizeChanged(w, h, oldw, oldh);
	}
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		// Draw Background
		background_paint = new Paint();
		background_paint.setColor(getResources().getColor(R.color.background));
		canvas.drawRect(0.0f, 0.0f, width, height, background_paint);
		
		// Draw tiles
		for (int i = 0; i < tiles; i++)
			for (int j = 0; j < tiles; j++)
			{
				switch(game2048.get_tile(j, i))
				{
					case 8:
						background_paint.setColor(getResources().getColor(R.color.tile_8));
						break;
					case 16:
						background_paint.setColor(getResources().getColor(R.color.tile_16));
						break;
					case 32:
						background_paint.setColor(getResources().getColor(R.color.tile_32));
						break;
					case 64:
						background_paint.setColor(getResources().getColor(R.color.tile_64));
						break;
					case 128:
						background_paint.setColor(getResources().getColor(R.color.tile_128));
						break;
					case 256:
						background_paint.setColor(getResources().getColor(R.color.tile_256));
						break;
					case 512:
						background_paint.setColor(getResources().getColor(R.color.tile_512));
						break;
					case 1024:
						background_paint.setColor(getResources().getColor(R.color.tile_1024));
						break;
					case 2048:
						background_paint.setColor(getResources().getColor(R.color.tile_2048));
						break;
					default:
						background_paint.setColor(getResources().getColor(R.color.tile_normal));
						break;
				}
				
				canvas.drawRect(tile_size * i + margin, tile_size * j + margin, 
						tile_size * (i+1) - margin, tile_size * (j+1) - margin, background_paint);
			}
		
		/* 
		 * Let's draw the numbers!! 
		 * Define default color and style for numbers
		 */
		numbers_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		numbers_paint.setColor(getResources().getColor(R.color.text_normal));
		numbers_paint.setStyle(Style.FILL);
		numbers_paint.setTextSize(tile_size * text_scale);
		numbers_paint.setTextScaleX(tile_size / tile_size);
		numbers_paint.setTextAlign(Paint.Align.CENTER);

		// Set number in center of the tile
		FontMetrics fontMetrics = numbers_paint.getFontMetrics();
		
		// Centering in X: use alignment (and X at midpoint)
		float x = tile_size / 2;
		
		// Centering in Y: measure ascent/descent first
		float y = tile_size / 2 - (fontMetrics.ascent + fontMetrics.descent) / 2;
		
		// Let's display those numbers on screen
		for (int i = 0; i < tiles; i++)
			for (int j = 0; j < tiles; j++)
				if (game2048.get_tile(j, i) != 0)
				{					
					// Apply unique text color on new number added to game grid.
					if (j == game2048.get_last_num_row() && i == game2048.get_last_num_col())
						numbers_paint.setColor(getResources().getColor(R.color.new_number));
					else
						numbers_paint.setColor(getResources().getColor(R.color.text_normal));
					
					// Draw number on screen
					canvas.drawText(String.valueOf(game2048.get_tile(j, i)), i * tile_size + x, j * tile_size + y, numbers_paint);
				}
		
		// Display Score board
		float board_height = height - (tile_size * tiles + margin * (tiles + 1));
		float board_width = width - (tile_size * tiles + margin * (tiles + 1));
		
		// Define color and style for numbers
		board_text_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		board_text_paint.setColor(Color.BLACK); // #### Must be added to resources
		board_text_paint.setStyle(Style.FILL);
		board_text_paint.setTextSize(tile_size * board_scale);
		board_text_paint.setTextScaleX(1.0f);
		board_text_paint.setTextAlign(Paint.Align.CENTER);
		
		FontMetrics board_fontMetrics = board_text_paint.getFontMetrics();
		
		// Centering in X: use alignment (and X at midpoint)
		float board_x = (width < height ? (width / 2) 
				: ((width - board_width * 2 / 3) - (board_fontMetrics.ascent + board_fontMetrics.descent) / 2) );
		
		// Centering in Y: measure ascent/descent first
		float board_y = (width < height ? (height - board_height * 2 / 3) - (board_fontMetrics.ascent + board_fontMetrics.descent) / 2
				: (height / 3));
		
		// Draw Score Text
		canvas.drawText("Score: " + game2048.getScore(), 
				board_x, board_y, board_text_paint);
		
		// Draw Best Score Text
		board_y = (width < height ? (height - board_height / 3) - (board_fontMetrics.ascent + board_fontMetrics.descent) / 2
				: height * 2 / 3);
		canvas.drawText("Best Score: " + game2048.getBestScore(), board_x, board_y, board_text_paint);
	}
	
	/**
	 *	This function is called when re-drawing is required to update game screen. 
	 */
	public void call_invalidate()
	{
		invalidate();
	}
	
	/**
	 * Set screen orientation in order to re-draw game grid
	 * @param orientation Orientation of screen.
	 */
	public void setOrientation(int orientation)
	{
		this.screen_orientation = orientation;
	}
}