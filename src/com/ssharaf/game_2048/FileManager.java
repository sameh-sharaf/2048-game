package com.ssharaf.game_2048;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import android.os.Environment;
import android.util.Log;

/**
 * This class is responsible for reading from and writing 
 * into application's log file.
 * 
 * @author SAMEH SHARAF
 *
 */

public class FileManager {
	
	// Application folder path.
	private String folderPath;
	
	// Application log file path. 
	private String logFilePath;
	
	// Constants for folder and file names for application.
	private static final String FOLDER_NAME = "Game 2048";
	private static final String FILE_NAME = "game.log";
	
	// Tag name for debugging purposes.
	private static final String TAG = "2048 Game File Manager";
	
	/**
	 * Default constructor
	 */
	public FileManager()
	{
		// Read default SD card root path
		String sd_root = Environment.getExternalStorageDirectory().toString();
		folderPath = sd_root + "/" + FOLDER_NAME;
		Log.i(TAG, "SD Card Root Path: " + folderPath);
		
		File file = new File(folderPath);
		
		// If folder for application is not found on SD card, create new folder.
		if (!file.exists())
		{
			Log.e(TAG, "Folder Not Found");
			file.mkdir();
		}
		
		logFilePath = folderPath + "/" + FILE_NAME;
		Log.i(TAG, logFilePath);
		file = new File(logFilePath);
		
		// If log file is not found, create new log file.
		if (!file.exists())
		{
			Log.e(TAG, "Log File Not Found");
			try
			{
				Log.i(TAG, "Create log file");
				
				// Initialize best score.
				writeFile("0", false);
			} 
			catch(Exception e)
			{
				Log.e(TAG, e.getMessage());
			}
		}
	}

	/**
	 * Read text file
	 * @return Text file content.
	 */
	public ArrayList<String> readFile()
	{
		File file = new File(logFilePath);
		ArrayList<String> file_lines = new ArrayList<String>();
		
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(file));
		    String line;
		    
		    while((line = br.readLine()) != null)
		    {
		    	file_lines.add(line);
		    }
		}
		catch(Exception e)
		{
			Log.e(TAG, e.getMessage());
		}
		
		return file_lines;
	}
	
	/**
	 * Write input text to specified text file.
	 * @param text Input text to be written in text file.
	 */
	public void writeFile(String text, boolean isAppend)
	{
		File file = new File(logFilePath);
		
		try
		{
			FileWriter writer = new FileWriter(file, isAppend);
	        
			writer.append(text);
			writer.append("\r\n");
			writer.flush();
	        writer.close();
		}
		catch(Exception e)
		{
			Log.e(TAG, e.getMessage());
		}
	}
}
