
public class Command {
	String type; 			//Store/Release/Lookup
	String variableID;
	int variableValue;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getVariableID() {
		return variableID;
	}
	public void setVariableID(String variableID) {
		this.variableID = variableID;
	}
	public int getVariableValue() {
		return variableValue;
	}
	public void setVariableValue(int variableValue) {
		this.variableValue = variableValue;
	}
	
	//public void getNextCommand()
}
