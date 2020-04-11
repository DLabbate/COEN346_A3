import java.util.ArrayList;

//COEN 346 - ASSIGNMENT 3
//Written by:
//Domenic Labbate (40063296)
//Gianluca Lepore (40064389)

public class Driver {

	public static void main(String[] args) {
			
		FileHelper fileHelper = new FileHelper("src/input.txt","src/commands.txt","src/memconfig.txt"); 			//We use this to help parse input.txt
		fileHelper.FillWaitingProcesses();																		//We fill an ArrayList of type Process
		fileHelper.FillCommandList();
		fileHelper.FillMemConfig();
		
		VMM vmm = new VMM(fileHelper.getMemConfig());
		
		Scheduler scheduler = new Scheduler();
		ArrayList<Process> waitingList = fileHelper.getwaitingProcesses();
		ArrayList<Process> readyList = new ArrayList<>();
		
		scheduler.setWaitingProcesses(waitingList);							//Give the processes to the scheduler
		scheduler.setReadyProcesses(readyList);
		
		Thread schedulerThread = new Thread(scheduler);					
		schedulerThread.start();											//Start the round robin scheduler
		
	}

}
