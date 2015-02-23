package com.ssharaf.game_2048;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import android.os.Environment;
import android.util.Log;

public class FileManager {
	
	private String folderPath;
	private String logFilePath;
	
	private static final String GAME_FOLDER_NAME = "Game 2048";
	private static final String GAME_LOG_FILE_NAME = "game.log";
	private static final String TAG = "Game 2048 File Manager";
	
	public FileManager()
	{
		String sd_root = Environment.getExternalStorageDirectory().toString();
		folderPath = sd_root + "/" + GAME_FOLDER_NAME;
		Log.i(TAG, folderPath);
		
		File file = new File(folderPath);
		if (!file.exists())
		{
			Log.e(TAG, "Folder Not Found");
			file.mkdir();
		}
		
		logFilePath = "/mnt/sdcard/" + GAME_LOG_FILE_NAME;
		Log.i(TAG, logFilePath);
		file = new File(logFilePath);
		if (!file.exists())
		{
			Log.e(TAG, "Log File Not Found");
			
			try
			{
				PrintWriter file_logger = new PrintWriter(GAME_LOG_FILE_NAME, "UTF-8");
				file_logger.println("Best score=0");
				file_logger.println("Last saved game=");
				file_logger.close();
			} 
			catch(Exception e)
			{
				Log.e(TAG, e.getMessage());
			}
		}
	}

	public void readBestScore()
	{
		
	}
	
	public void writeBestScore(int bestScore)
	{
		
	}
	
	public void saveGame(int[][] arr)
	{
		
	}
	
	public int[][] loadGame()
	{
		return null;
	}
}
