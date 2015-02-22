package com.ssharaf.game_2048;

import com.ssharaf.game_2048.SimpleGestureFilter.SimpleGestureListener;

import android.R.color;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class GameView extends View
{
	
	private Context context;
	private Canvas canvas;
	
	private Paint paint;
	private Paint numbers_paint;
	
	private int width;
	private int height;
	
	private GamePuzzle gamePuzzle;
	private int margin;
	private int tiles;
	private int tile_size;
	private float text_scale;
	
	public GameView(Context context)
	{
		super(context);
		
		// Set application context
		this.context = context;
		
		// Initialize parameters
		width = 0;
		height = 0;
		margin = 5;
		tiles = 4;
		tile_size = 0;
		text_scale = 0.55f;
		
		// Create puzzle grid
		gamePuzzle = new GamePuzzle();
		
		// Begin new game
		gamePuzzle.begin_game();
	}
	
	public GamePuzzle getGamePuzzle()
	{
		return gamePuzzle;
	}
	
	public void setGamePuzzle(GamePuzzle gamePuzzle)
	{
		this.gamePuzzle = gamePuzzle;
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{		
		width = w;
		height = h;
		tile_size = width / tiles;
		
		super.onSizeChanged(w, h, oldw, oldh);
	}
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		// Draw Background
		paint = new Paint();
		paint.setColor(getResources().getColor(R.color.background));
		canvas.drawRect(0.0f, 0.0f, width, height, paint);
		
		// Draw tiles
		paint.setColor(getResources().getColor(R.color.tile));
		for (int i = 0; i < tiles; i++)
			for (int j = 0; j < tiles; j++)
				canvas.drawRect(tile_size * i + margin, tile_size * j + margin, 
						tile_size * (i+1) - margin, tile_size * (j+1) - margin, paint);
		
		/* ##### Let's draw the numbers!! ##### */ 
		
		// Define color and style for numbers
		numbers_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		numbers_paint.setColor(getResources().getColor(R.color.text));
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
				if (gamePuzzle.get_tile(j, i) != 0)
					canvas.drawText(String.valueOf(gamePuzzle.get_tile(j, i)), i * tile_size + x, j * tile_size + y, numbers_paint);
	}
	
	public void call_invalidate()
	{
		invalidate();
	}
}