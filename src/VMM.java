import java.util.List;
import java.util.Scanner;
import java.io.*;
import java.util.ArrayList;
import java.util.Timer;
import java.sql.Timestamp;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

public class VMM implements Runnable{

	private int size;						//Indicated by memconfig file
	private Variable mainMemory[];			//Main Memory Array
	
	protected boolean isRunning = true;		//boolean to indicate whether vmm is running or not

	//VMM runnable
	@Override
	public void run() {
		System.out.println("\n[VMM thread starting]");
		
		
		while (isRunning())
		{
			//Processes calling Store/Release/Lookup (API)
		}
		
		System.out.println("[VMM thread ending]");
	}
	
	//Constructor
	public VMM(int size)
	{
		this.size = size;
		mainMemory = new Variable[size];
	}
	
	
	//------------------------------------------------------API-------------------------------------------------------//
	
	public void Store (String variableId,int value)
	{
		for (int i = 0; i<size; i++)
		{
			if (mainMemory[i] == null)
			{
				Variable variable = new Variable();
				variable.setId(variableId);
				variable.setValue(value);
				
				//Update timestamp
				variable.setLastAccessTime(new Timestamp(System.currentTimeMillis()));
				
				mainMemory[i] = variable;
				System.out.println("STORE (Found a Spot in Main Memory):" + " Variable: " + variableId + ", Value: " + value);
				return;
			}
		}
		
		//If we reach this point, main memory is full. Need to store in storage
		
		try
		{
			File file = new File ("src/storage.txt");
			String fileName="src/storage.txt";
			FileWriter fw = new FileWriter(fileName,true);
			
			fw.append(variableId + "\t" +Integer.toString(value));
			fw.append("\n");
			fw.close(); 
			System.out.println("STORE (Did not Find a Spot in Main Memory): " + "Variable: " + variableId + ", Value: " + value);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void Release (String variableId)
	{
		for (int i = 0; i<size; i++)
		{
			if (mainMemory[i].getId() == variableId)
			{
				System.out.println("RELEASE (From Main Memory)" + " Variable: " + variableId + ", Value: " + String.valueOf(mainMemory[i].getValue()));
				mainMemory[i] = null; //RELEASE
				return;
			}
		}
		
		//We need to check that it is in storage
		File fileStorage = new File("src/storage.txt");
		try (Scanner scannerStorage = new Scanner(fileStorage))
		{
			int lineNumber = 0;
			while (scannerStorage.hasNextLine())
			{
				String array[] = scannerStorage.nextLine().split("\t");
				
				if ((array[0] != null) && (array[0] != ""))
				{
					if (array[0].equals(variableId)) //We found what we were looking for
					{
						
						//Release Spot in Storage
						Path path = Paths.get("src/storage.txt");
					    List<String> lines = Files.readAllLines(path);
					    String data = "-1\t-1";
					    lines.set(lineNumber,data);
					    Files.write(path, lines);
					    System.out.println("RELEASE (Released from Storage): " + "Variable " + array[0] + ", Value: " +  array[1] );
						
						return;
					}
				}
				lineNumber++;
			}
			scannerStorage.close();
			//fileWriterStorage.close();
			
			//If we reach here, it means its not in storage
			return;
			
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	
	
	
	
	public int Lookup(String variableId) 
	{
		for (int i = 0; i<size; i++)
		{
			if (mainMemory[i].getId() == variableId)
			{
				System.out.println("LOOKUP (Found Variable in Main Memory): Variable " + variableId + ", Value: " + mainMemory[i].getValue());
				mainMemory[i].setLastAccessTime(new Timestamp(System.currentTimeMillis()));
				return mainMemory[i].getValue();
			}
		}
		
		//If we reach here, check storage
		
		File fileStorage = new File("src/storage.txt");
		//Scanner scannerStorage = null;
		FileWriter fileWriterStorage = null;
		
		//Open Files
		try (Scanner scannerStorage = new Scanner(fileStorage))
		{
			//fileWriterStorage = new FileWriter(fileStorage);
			int lineNumber = 0;
			while (scannerStorage.hasNextLine())
			{
				String array[] = scannerStorage.nextLine().split("\t");
				
				if ((array[0] != null) && (array[0] != ""))
				{
					
					if (array[0].equals(variableId)) //We found what we were looking for
					{
						Variable variable = new Variable();
						variable.setId(variableId);
						variable.setValue(Integer.parseInt(array[1]));
						variable.setLastAccessTime(new Timestamp(System.currentTimeMillis()));
						
						//Swap OR Put in Main Memory
						int index = checkFreeMainMemory();
						
						if (index != -1) //THERE IS SPACE IN MAIN MEMORY
						{
							mainMemory[index] = variable;
							
							//Free Spot in Storage
							Path path = Paths.get("src/storage.txt");
						    List<String> lines = Files.readAllLines(path);
						    String data = "-1\t-1";
						    lines.set(lineNumber,data);
						    Files.write(path, lines);
						    System.out.println("LOOKUP (Moved to a FREE spot in Main Memory): " + "Variable " + variable.getId() + ", Value: " +  variable.getValue() );
						    
						}
						
						else //NO SPACE IN MAIN MEMORY
						{
							int swapIndex = findLeastAccessTime();
							Variable temp = mainMemory[swapIndex];
							mainMemory[swapIndex] = variable;
							System.out.println("LOOKUP: " + "Variable " + variable.getId() + " , Value: " +  variable.getValue() );
							System.out.println("Memory Manager --> SWAP: " + "Variable " + variable.getId() + " With " +  temp.getId() );
							
							
							//Free Spot in Storage
							Path path = Paths.get("src/storage.txt");
						    List<String> lines = Files.readAllLines(path);
						    String data = temp.getId() + "\t" + String.valueOf(temp.getValue()) ;
						    lines.set(lineNumber,data);
						    Files.write(path, lines);
						   
						}
						
					
						return Integer.parseInt(array[1]); //Return value
					}
				}
				lineNumber++;
			}
			scannerStorage.close();
			//fileWriterStorage.close();
			
			//If we reach here, it means its not in storage
			return -1;
			
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	
		
		
		return -1;
	}
	
	public int checkFreeMainMemory()
	{
		int index = -1;
		
		for (int i = 0; i<size; i++)
		{
			if (mainMemory[i] == null)
			{
				return i;
			}
		}
		
		return index; //It is full
	}
	
	public int findLeastAccessTime()
	{
		int index = 0;
		
		for (int i = 0; i<size; i++)
		{
			long l1 = mainMemory[i].getLastAccessTime().getTime();
			long l2 = mainMemory[index].getLastAccessTime().getTime();
			if (l1 < l2)
			{
				index = i;
			}
		}
		
		return index;
	}

	public synchronized boolean isRunning() {
		return isRunning;
	}

	public synchronized void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

	
}
