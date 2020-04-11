import java.util.List;
import java.util.ArrayList;
import java.util.Timer;

public class VMM implements Runnable{

	private int size;
	private Variable mainMemory[];
	private ArrayList<Command> commandList;
	
	@Override
	public void run() {
		
	}
	
	public VMM(int size)
	{
		this.size = size;
		mainMemory = new Variable[size];
	}
	
	public void Store (String variableId,int value)
	{
		
	}
	
	public void Release (String variableId)
	{
		
	}
	
	public int Lookup(String variableId) 
	{
		return 0;
	}

}
