package console;

import java.util.ArrayList;

public class DatasetList {
	
	ArrayList<Dataset> datasets;
	ArrayList<String> names;
	int length;
	
	public DatasetList() {
		datasets = new ArrayList<Dataset>();
		names = new ArrayList<String>();
		length = 0;
	}
	
	public boolean addDataset(Dataset dataset, String name) {
		if(!names.contains(name)) {
			datasets.add(dataset);
			names.add(name);
			return true;
		}
		return false;
	}
	
}
