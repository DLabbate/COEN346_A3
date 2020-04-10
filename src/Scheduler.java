import java.util.List;
import java.util.ArrayList;
import java.util.Timer;

public class Scheduler implements Runnable {
	
	public static long startTime = System.currentTimeMillis(); 			//CLOCK
	
	private List<Process> waitingProcesses; 							//List of processes in waiting queue
	private List<Process> readyProcesses; 								//List of processes in ready queue
	private List<Thread> threads = new ArrayList<Thread>();				//These threads are created when a process is ready (based on arrival time)
	
	private boolean signalNext = false;
	
	/*
	 * The following function gets the elapsed time
	 * since the scheduler began
	 */
	public static long getElapsedtime() {
		return System.currentTimeMillis() - startTime;
	}
	
	///////////////////////
	//GETTERS AND SETTERS//
	///////////////////////
	public List<Process> getWaitingProcesses() {
		return waitingProcesses;
	}
	
	public void setWaitingProcesses(List<Process> waitingProcesses) {
		this.waitingProcesses = waitingProcesses;
	}
	
	public List<Process> getReadyProcesses() {
		return readyProcesses;
	}
	
	public void setReadyProcesses(List<Process> readyProcesses) {
		this.readyProcesses = readyProcesses;
	}


	
	@Override
	public void run() {
		
		startTime = System.currentTimeMillis();  //Re-initialize start time
		while (!isCompleted())
		{
			
			checkWaitingList();
			
			if (!readyProcesses.isEmpty())
			{
				{
					int nextIndex = findNextProcess();
					Process currentProcess = readyProcesses.get(nextIndex);
					currentProcess.setHasCpu(true);
				
					
					if (currentProcess.isFinished())
					{
						System.out.println("(Time, ms: " + Scheduler.getElapsedtime() + ") " + "Process #" + readyProcesses.get(nextIndex).ID + " BEING REMOVED FROM READY QUEUE");
						readyProcesses.remove(nextIndex);
						threads.remove(nextIndex);
					}
					
				}
			}
			
		}
		System.out.println("(Time, ms: " + Scheduler.getElapsedtime() + ") " + "*********SCHEDULER FINISHED********");
	}
		
	
	//Checks if all processes have been completed
	private boolean isCompleted()
	{
		if (waitingProcesses.isEmpty() && readyProcesses.isEmpty())
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	
	/*
	 * This process is responsible for finding a process with
	 * the MINIMUM remaining execution time
	 * 
	 * Note the function returns the INDEX, not the actual process itself
	 */
	public int findNextProcess()
	{
		int minimum_index = 0; //Index of the process with minimum remaining time 
		for (int i=0; i < readyProcesses.size(); i++)
		{
			double temp = readyProcesses.get(i).getRemainingTime();
			if (readyProcesses.get(minimum_index).getRemainingTime() > temp)
			{
				minimum_index = i;
			}
			
		}
		
		return minimum_index;
	}
	
	private void checkWaitingList()
	{
		
		if (!waitingProcesses.isEmpty())
		{
			for (int i = 0 ; i <waitingProcesses.size(); i++)
			{
				Process currentProcess = waitingProcesses.get(i); 
				if (currentProcess.getArrivalTime() < Scheduler.getElapsedtime())
				{
					/*
					 * If a process is ready, we add it to the ready queue AND run it
					 */
					currentProcess.setHasCpu(true);
					Thread thread = new Thread(currentProcess);
					threads.add(thread);
					thread.start();
					
					//Now we want to move from the waiting queue to the ready queue
					waitingProcesses.remove(i);
					readyProcesses.add(currentProcess);
					
					/*
					 * Here we increment the waiting time
					 * based on how long it has been waiting to be put in the ready queue
					 * The waiting time is also incremented when the process does not have the CPU (done SEPARATELY in the Process class)
					 */
					currentProcess.incrementWaitingTime(Scheduler.getElapsedtime()); 
					
				}
			}
		}
		
	}
	
}
