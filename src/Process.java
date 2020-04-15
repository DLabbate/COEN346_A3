import java.util.concurrent.locks.ReentrantLock;
import java.util.Random;
import java.util.ArrayList;
import java.util.concurrent.*;

public class Process implements Runnable {
	
	public static int nextID = 1;
	int ID; 						//Process ID
	double quantumTime = 1000;
	
	/*
	 * Note that times are in milliseconds!
	 */
	private double arrivalTime;		//When the process is ready
	private double serviceTime; 	//How long the process needs to execute
	private double remainingTime; 	//Initialized to serviceTime
	private boolean hasCpu; 		//Pause or Resume
	private boolean isFinished;		//Flag that indicates if the process has finished
	private double waitingTime;
	
	public boolean jobInProgress = false;
	
	public static final int NUM_CPU = 2; 								//The number of CPU
	public static final Semaphore semaphore = new Semaphore(NUM_CPU); 	//This mutex lock determines the amount of processes that are running
	
	//Command List
	public static ArrayList<Command> commandList;
	public static int counter = 0;
	
	//VMM reference
	public static VMM vmm;
	//*******************************************************************************************************************************************
	
	Process() //Hardcoded values for testing
	{
		ID = nextID++;
		
		this.arrivalTime = 1000; // (ms)
		this.serviceTime = 5000;
		this.remainingTime = this.serviceTime;
		this.hasCpu = false;
		this.isFinished = false;
		this.waitingTime = 0;
	}
	
	@Override
	public void run() {
		
		long enterTime = System.currentTimeMillis(); //Time at which we enter the run() function
		
		try
		{
			System.out.println("(Time, ms: " + Scheduler.getElapsedtime() + ") " + "Process #" + ID + " Started - calling run() ");
			
			
			while (!isFinished)
			{
				semaphore.acquire();
				jobInProgress = true;
				try
				{
					if (hasCpu)
					{
						//Update delta time (since process had the CPU)
						double delta = System.currentTimeMillis() - enterTime;
						incrementWaitingTime(delta);
						
						System.out.println("(Time, ms: " + Scheduler.getElapsedtime() + ") " + "Process #" + ID + " Resumed -" + " Remaining Time: " + remainingTime);
						updateQuantumTime();
						decrementTime();
						System.out.println("(Time, ms: " + Scheduler.getElapsedtime() + ") " + "Process #" + ID + " Paused -" + " Remaining Time: " + remainingTime);
					
						enterTime = System.currentTimeMillis();
					}
				}
				finally
				{
					jobInProgress = false;
					semaphore.release();			//Release semaphore
				}
				
			}
		}
		
		catch(Exception e)
		{
			System.out.println(e);
		}
		
		finally
		{
			System.out.println("(Time, ms: " + Scheduler.getElapsedtime() + ") " + "Process #" + ID + " Exiting run()");
		}
		
	}
	
	
	public synchronized void decrementTime() 
	{
		long EnterTime = System.currentTimeMillis();

		double previousRemainingTime = this.remainingTime;
		
		double runTime = Math.min(this.quantumTime,this.remainingTime);
		Random rand = new Random();
		int delay = rand.nextInt(500);
		while ( ((System.currentTimeMillis() - EnterTime) <= runTime) && (!isFinished()))
		{
			if ( (System.currentTimeMillis() - EnterTime) == delay )
			{
				doNextCommand();
				System.out.println("(Time, ms: " + Scheduler.getElapsedtime() + ") " + "Process #" + ID + " Running -" + " Remaining Time: " + remainingTime);
			}

			//Running!
		}
		
		//remainingTime -= quantumTime;
		remainingTime -= (System.currentTimeMillis() - EnterTime);
		
		if (remainingTime < 0.1)
		{
			finish();
		}
		hasCpu = false; //After we decrement the time, the next process should have the CPU
		
		
	}

	private void finish() 
	{
		System.out.println("-------------------------------------------------------------------------------------------------------");
		System.out.println("(Time, ms: " + Scheduler.getElapsedtime() + ") " + "Process #" + ID + " *FINISHED*");
		System.out.println("(Time, ms: " + Scheduler.getElapsedtime() + ") " + "Process #" + ID +" Waiting Time: " + waitingTime);
		System.out.println("-------------------------------------------------------------------------------------------------------");
		isFinished = true;
		hasCpu = false;
	}
	
	
	///////////////////////
	//GETTERS AND SETTERS//
	///////////////////////
	public double getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(double arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public double getServiceTime() {
		return serviceTime;
	}

	public void setServiceTime(double serviceTime) {
		this.serviceTime = serviceTime;
	}

	public double getRemainingTime() {
		return remainingTime;
	}

	public void setRemainingTime(double remainingTime) {
		this.remainingTime = remainingTime;
	}

	public boolean getHasCpu() {
		return hasCpu;
	}

	public void setHasCpu(boolean hasCpu) {
		this.hasCpu = hasCpu;
	}

	public boolean isFinished() {
		//return isFinished;
		if (remainingTime <= 0.1)
		{
			this.isFinished = true;
			return true;
		}
		else 
		{
			this.isFinished = false;
			return false;
		}
	}

	public void setFinished(boolean isFinished) {
		this.isFinished = isFinished;
	}
	
	public void updateQuantumTime()
	{ 
		//double scale = 0.10;
		//this.quantumTime = scale * remainingTime;
		this.quantumTime = 1000;
	}
	
	public void incrementWaitingTime(double delta)
	{
		waitingTime += delta;
	}
	
	public double getWaitingTime()
	{
		return waitingTime;
	}
	
	public void doNextCommand()
	{	
		if (counter < commandList.size())
		{
			Command currentCommand = commandList.get(counter);
			String currentID = currentCommand.getVariableID();
			int currentValue = currentCommand.getVariableValue();
			System.out.print("(Time, ms: " + Scheduler.getElapsedtime() + ") " + "Process #" + ID + " ");
			if (currentCommand.getType().equals("Store"))
			{
				vmm.Store(currentID, currentValue);
			}
			
			else if (currentCommand.getType().equals("Lookup"))
			{
				vmm.Lookup(currentID);
			}
			
			else if (currentCommand.getType().equals("Release"))
			{
				vmm.Release(currentID);
			}
			
			counter++;
		}
	}
}
