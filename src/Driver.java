import java.util.ArrayList;
import java.io.IOException;
import java.io.PrintWriter;

//COEN 346 - ASSIGNMENT 3
//Written by:
//Domenic Labbate (40063296)
//Gianluca Lepore (40064389)

public class Driver {

	public static void main(String[] args) {
			
		FileHelper fileHelper = new FileHelper("src/input.txt","src/commands.txt","src/memconfig.txt"); 			//We use this to help parse input.txt
		fileHelper.FillWaitingProcesses();																			//We fill an ArrayList of type Process
		fileHelper.FillCommandList();
		fileHelper.FillMemConfig();
		
		Process.commandList = fileHelper.getCommandList(); 					//Set the list of commands
		VMM vmm = new VMM(fileHelper.getMemConfig());
		Process.vmm = vmm;
		
		//CLEAR STORAGE
		//********************************************************************************************************************************************************
		try 
		{
			PrintWriter writer = new PrintWriter("src/storage.txt");
			writer.print("");
			writer.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		//********************************************************************************************************************************************************
		
		
		Scheduler scheduler = new Scheduler();
		ArrayList<Process> waitingList = fileHelper.getwaitingProcesses();
		ArrayList<Process> readyList = new ArrayList<>();
		
		scheduler.setWaitingProcesses(waitingList);							//Give the processes to the scheduler
		scheduler.setReadyProcesses(readyList);
		
		Thread schedulerThread = new Thread(scheduler);					
		schedulerThread.start();											//Start the round robin scheduler
		
	}

}
