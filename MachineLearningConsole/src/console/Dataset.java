package console;

import java.util.ArrayList;

public class Dataset {
	
	ArrayList<float[]> inputs;
	ArrayList<float[]> results;
	int length;
	
	public Dataset() {
		inputs = new ArrayList<float[]>();
		results = new ArrayList<float[]>();
		length = 0;
	}
	public void addData(float[] inputs, float[] results) {
		this.inputs.add(inputs);
		this.results.add(results);
		length++;
	}
	public void getData(float[][] inputs, float[][] results) {
		float[][] returnInputs = this.inputs.toArray(new float[][] {new float[] {0}});
		float[][] returnResults = this.results.toArray(new float[][] {new float[] {0}});
		
		for(int i = 0 ; i < inputs.length && i < returnInputs.length ; i++) {
			inputs[i] = returnInputs[i];
			results[i] = returnResults[i];
		}
	}
	public void removeData(int index) {
		inputs.remove(index);
		results.remove(index);
		length--;
	}
}
