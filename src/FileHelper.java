import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;

public class FileHelper 										//*This class is used to help parse the Input.txt file
{		
	Scanner scannerProcess;										//Using scanner to read input
	Scanner scannerCommands;
	Scanner scannerMemConfig;
	ArrayList<Process> waitingProcesses = new ArrayList<Process>();
	ArrayList<Command> commandList = new ArrayList<>();
	int memConfig;
	
	public FileHelper(String string)
	{
		try 
		{
			File file = new File(string);
			scannerProcess = new Scanner(file);						//Create scanner with appropriate file 
		}
		
		catch(FileNotFoundException e)
		{
			System.out.println("FILE NOT FOUND!");
		}
		
	}
	
	public FileHelper(String stringProcess, String stringCommand)
	{
		try 
		{
			File fileProcess = new File(stringProcess);
			File fileCommand = new File(stringCommand);
			scannerProcess = new Scanner(fileProcess);						//Create scanner with appropriate file 
			scannerCommands = new Scanner(fileCommand);						//Create scanner with appropriate file 
		}
		
		catch(FileNotFoundException e)
		{
			System.out.println("FILE NOT FOUND!");
		}
		
	}
	
	public FileHelper(String stringProcess, String stringCommand, String stringMemConfig)
	{
		try 
		{
			File fileProcess = new File(stringProcess);
			File fileCommand = new File(stringCommand);
			File fileMemConfig = new File(stringMemConfig);
			scannerProcess = new Scanner(fileProcess);						//Create scanner with appropriate file 
			scannerCommands = new Scanner(fileCommand);						//Create scanner with appropriate file 
			scannerMemConfig = new Scanner(fileMemConfig);					//Create scanner with appropriate file 
		}
		
		catch(FileNotFoundException e)
		{
			System.out.println("FILE NOT FOUND!");
		}
		
	}
	
	public void FillWaitingProcesses()
	{
		String numLinesString = scannerProcess.nextLine();
		int numLines = Integer.parseInt(numLinesString);
		
		while (scannerProcess.hasNextLine())						
		{
			Process process = new Process();
			String nextLine = scannerProcess.nextLine();
			String array[] = new String[2]; 					//Index 0 holds the arrival time, Index 1 holds the remaining time
			array = nextLine.split("\t");						//Split by a tab
			
			double arrivalTime = Double.parseDouble(array[0]);
			process.setArrivalTime(arrivalTime);
			
			double remainingTime = Double.parseDouble(array[1]);
			process.setRemainingTime(remainingTime);
			System.out.println("Process Created: " + process.ID + " " + process.getArrivalTime() + " " + process.getRemainingTime());
			
			waitingProcesses.add(process);
		}
		scannerProcess.close();	
	}
	
	public ArrayList<Process> getwaitingProcesses()				//Return the waiting process list
	{
		return waitingProcesses;
	}
	
	public void FillCommandList()
	{
		while (scannerCommands.hasNextLine())						
		{
			Command command = new Command();
			String nextLine = scannerCommands.nextLine();
			String array[] = new String[3]; 					//Index 0 holds the arrival time, Index 1 holds the remaining time
			array = nextLine.split("\t");						//Split by a tab
			
			if (array.length == 2)
			{
				String type = array[0];
				command.setType(type);
				
				String variableID = array[1];	
				command.setVariableID(variableID);
				
				System.out.println("Command Added to List: " + command.getType() + " " + command.getVariableID());
			}
			
			else if (array.length == 3)
			{
				String type = array[0];
				command.setType(type);
				
				String variableID = array[1];	
				command.setVariableID(variableID);
				
				int variableValue = Integer.parseInt(array[2]);	
				command.setVariableValue(variableValue);
				
				System.out.println("Command Added to List: " + command.getType() + " " + command.getVariableID() + " " + Integer.toString(command.getVariableValue()));
			}
			
			//System.out.println("Command Added to List: " + command.getType() + " " + command.getVariableID() + " " + Integer.toString(command.getVariableValue()));
			
			commandList.add(command);
		}
		scannerCommands.close();	
	}
	
	public void FillMemConfig()
	{
		String memConfig = scannerMemConfig.nextLine();
		this.memConfig = Integer.parseInt(memConfig);
		
		System.out.println("MemConfig: " + memConfig);
		
		scannerMemConfig.close();	
	}

	public int getMemConfig() {
		return memConfig;
	}

	public void setMemConfig(int memConfig) {
		this.memConfig = memConfig;
	}

	public ArrayList<Command> getCommandList() {
		return commandList;
	}

	public void setCommandList(ArrayList<Command> commandList) {
		this.commandList = commandList;
	}
	
	
}
