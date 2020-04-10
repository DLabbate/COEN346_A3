import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;

public class FileHelper 										//*This class is used to help parse the Input.txt file
{		
	Scanner scanner;											//Using scanner to read input
	ArrayList<Process> waitingProcesses = new ArrayList<Process>();
	
	public FileHelper(String string)
	{
		try 
		{
			File file = new File(string);
			scanner = new Scanner(file);						//Create scanner with appropriate file 
		}
		
		catch(FileNotFoundException e)
		{
			System.out.println("FILE NOT FOUND!");
		}
		
	}
	
	public void FillWaitingProcesses()
	{
		String numLinesString = scanner.nextLine();
		int numLines = Integer.parseInt(numLinesString);
		
		while (scanner.hasNextLine())						
		{
			Process process = new Process();
			String nextLine = scanner.nextLine();
			String array[] = new String[2]; 					//Index 0 holds the arrival time, Index 1 holds the remaining time
			array = nextLine.split("\t");						//Split by a tab
			
			double arrivalTime = Double.parseDouble(array[0]);
			process.setArrivalTime(arrivalTime);
			
			double remainingTime = Double.parseDouble(array[1]);
			process.setRemainingTime(remainingTime);
			System.out.println("Process Created: " + process.ID + " " + process.getArrivalTime() + " " + process.getRemainingTime());
			
			waitingProcesses.add(process);
		}
		scanner.close();	
	}
	
	public ArrayList<Process> getwaitingProcesses()				//Return the waiting process list
	{
		return waitingProcesses;
	}
}
