package console;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

import NeuralNetwork.ActivationFunction;
import NeuralNetwork.LayerType;
import NeuralNetwork.LossFunction;
import NeuralNetwork.Network;

public class Console {

	public static final Scanner IN = new Scanner(System.in);
	
	public static String[] receiveCommand(String msg) {
		System.out.print(msg);
		return IN.nextLine().split(" ");
	}
	
	public static void main(String[] args) {
		NetworkList networks = new NetworkList();
		DatasetList datasets = new DatasetList();
		String networks_path = "bin\\resources\\networks\\";
		String dataset_path = "bin\\resources\\datasets\\";
		
		boolean shouldContinue = true;
		
		while(shouldContinue) {
			try {
				String[] command = receiveCommand(">");
				switch(command[0].toLowerCase()) {
					case "quit": shouldContinue = false; break;
					case "import_dataset":
						if(command.length < 3) System.out.println("import_dataset mush be followed by the path to the dataset's folder followed by the dataset's name");
						else {
							File file = new File(command[1] + "\\" + command[2] + ".cvs");
							if(!file.exists()) System.out.println("there is no dataset with the name '" + command[2] + "' in the given path");
							else {
								BufferedReader reader = new BufferedReader(new FileReader(file));
								Dataset dataset = new Dataset();
								while(reader.ready()) {
									String[] data = reader.readLine().split(",");
									float inputs[] = new float[Integer.parseInt(data[0])];
									float results[] = new float[Integer.parseInt(data[inputs.length + 1])];
									int i = 0;
									while(i < inputs.length && i < results.length) {
										if(i < inputs.length) inputs[i] = Float.parseFloat(data[i + 1]);
										if(i < results.length) results[i] = Float.parseFloat(data[i + inputs.length + 2]);
									}
									dataset.addData(inputs, results);
								}
								reader.close();
								if(datasets.addDataset(dataset, command[2])) System.out.println("dataset '" + command[2] + "' was successfully imported");
								else System.out.println("there already is a dataset with the name '" + command[2] + "' in the given path");
							}
						}
						break;
					case "export_dataset":
						break;
					case "load_dataset":
						if(command.length == 1) System.out.println("import_dataset mush be followed by the dataset's name");
						else {
							File file = new File(dataset_path + command[1] + ".cvs");
							if(!file.exists()) System.out.println("there is no dataset with the name '" + command[1] + "'");
							else {
								BufferedReader reader = new BufferedReader(new FileReader(file));
								Dataset dataset = new Dataset();
								while(reader.ready()) {
									String[] data = reader.readLine().split(",");
									float inputs[] = new float[Integer.parseInt(data[0])];
									float results[] = new float[Integer.parseInt(data[inputs.length + 1])];
									int i = 0;
									while(i < inputs.length && i < results.length) {
										if(i < inputs.length) inputs[i] = Float.parseFloat(data[i + 1]);
										if(i < results.length) results[i] = Float.parseFloat(data[i + inputs.length + 2]);
									}
									dataset.addData(inputs, results);
								}
								reader.close();
								if(datasets.addDataset(dataset, command[1])) System.out.println("dataset '" + command[1] + "' was successfully loaded");
								else System.out.println("there already is a dataset with the name '" + command[1] + "'");
							}
						}
						break;
					case "save_dataset":
						break;
					case "import_network":
						if(command.length < 3) System.out.println("import_network command must be followed by the path to the network's folder followed by the network's name");
						else {
							if(networks.networkExists(command[2])) System.out.print("there already is a network named '" + command[2] + "'");
							else {
								File file = new File(command[1] + "\\" + command[2] + ".cvs");
								if(file.exists()) {
									BufferedReader br = new BufferedReader(new FileReader(file));
									networks.addNetwork(new Network(br), command[2]);
									System.out.println("successfully imported network '" + command[2] + "'");
								}
								else System.out.println("no network with the name '" + command[2] + "' was found");
							}
						}
						break;
					case "export_network":
						if(command.length == 1)System.out.println("export_network command must be followed by the path to the save folder followed by the network's name");
						else {
							Network n = networks.getNetwork(command[2]);
							if(n == null)System.out.println("the is no network with the name '" + command[2] + "' in the given path");
							else {
								String s = n.saveString();
								File file = new File(command[1] + "\\" + command[2] + ".cvs");
								file.createNewFile();
								BufferedWriter bw = new BufferedWriter(new FileWriter(file));
								bw.write(s);
								bw.flush();
								bw.close();
								System.out.println("successfully exported network '" + command[2] + "'");
							}
						}
						break;
					case "load_network":
						if(command.length == 1) System.out.println("load_network command must be followed by the name of the network which is to be loaded");
						else {
							File file = new File(networks_path + command[1] + ".cvs");
							if(file.exists()) {
								BufferedReader br = new BufferedReader(new FileReader(file));
								networks.addNetwork(new Network(br), command[1]);
								System.out.println("successfully loaded network '" + command[1] + "'");
							}
							else System.out.println("no network with the name '" + command[1] + "' was found");
						}
						break;
					case "save_network":
						if(command.length == 1)System.out.println("save_network command must be followed by the name of the network which is to be saved");
						else {
							Network n = networks.getNetwork(command[1]);
							if(n == null)System.out.println("the is no network with the name '" + command[1] + "'");
							else {
								String s = n.saveString();
								File file = new File(networks_path + command[1] + ".cvs");
								file.createNewFile();
								BufferedWriter bw = new BufferedWriter(new FileWriter(file));
								bw.write(s);
								bw.flush();
								bw.close();
								System.out.println("successfully saved network '" + command[1] + "'");
							}
						}
						break;
					case "show_network_list":
						String[] list = networks.names();
						for(String s : list) {
							System.out.println(s);
						}
						break;
					case "show_network":
						if(command.length == 1)System.out.println("show_network command must be followed by network name");
						else {
							Network n = networks.getNetwork(command[1]);
							if(n == null) System.out.println("there is no network with the name '" + command[1] + "'");
							else {
								System.out.println("name: " + command[1]);
								n.printNetwork();
							}
						}
						break;
					case "create":
						boolean cancel = false;
						String network_name = "";
						int inputs_num = 0;
						int num_of_layers = 0;
						ArrayList<LayerType> types = new ArrayList<LayerType>();
						ArrayList<int[]> sizes = new ArrayList<int[]>();
						ArrayList<ActivationFunction> functions = new ArrayList<ActivationFunction>();
						LossFunction function = null;
						while(!cancel) {
							try {
								String[] create_command = receiveCommand(">create>");
								switch(create_command[0].toLowerCase()) {
									case "cancel": cancel = true; break;
									case "name":
										if(create_command.length == 1) System.out.println("create name command must be followed by a name for the network");
										else if(networks.networkExists(create_command[1]))System.out.println("ther already is a network with the name '" + create_command[1] + "', try another one");
										else network_name = create_command[1];
										break;
									case "number_of_inputs":
										if(create_command.length == 1) System.out.println("create number_of_inputs command must be followed by the network's number of inputs");
										else{
											try {
												int temp = Integer.parseInt(create_command[1]);
												if(temp <= 0) throw new IllegalArgumentException();
												inputs_num = temp;
											}catch(Exception e) {
												System.out.println("create number_of_inputs command must be followed by an integer bigger than zero");
											}
										}
										break;
									case "show_layer_data":
										if(create_command.length == 1) System.out.println("create show_layer_data command must be followed by the index of the layer");
										else{
											try {
												int index = Integer.parseInt(create_command[1]);
												if(index < 0 || index >= num_of_layers) throw new IllegalArgumentException();
												System.out.println(types.get(index) + ", " + functions.get(index));
												int[] size = sizes.get(index);
												for(int i = 0 ; i < size.length ; i++) {
													System.out.print(size[i] + " ");
												}
												System.out.println();
											}catch(Exception e) {
												System.out.println("create show_layer_data command must be followed by an integer between 0 and the number of layers");
											}
										}
										break;
									case "show_data":
										System.out.println("name:" + network_name);
										System.out.println("number of inputs: " + inputs_num);
										System.out.println("loss function: " + function);
										for(int i = 0 ; i < num_of_layers ; i++) {
											System.out.println(types.get(i) + ", " + functions.get(i));
											int[] size = sizes.get(i);
											for(int j = 0 ; j < size.length ; j++) {
												System.out.print(size[j] + " ");
											}
											System.out.println();
										}
										break;
									case "remove_layer":
										if(create_command.length == 1) System.out.println("create remove_layer command must be followed by the index of the layer");
										else{
											try {
												int index = Integer.parseInt(create_command[1]);
												if(index < 0 || index >= num_of_layers) throw new IllegalArgumentException();
												types.remove(index);
												sizes.remove(index);
												functions.remove(index);
												num_of_layers--;
											}catch(Exception e) {
												System.out.println("create remove_layer command must be followed by an integer between 0 and the number of layers");
											}
										}
										break;
									case "loss_function":
										if(create_command.length == 1) System.out.println("create loss_function must be followed by the name of a loss function");
										else {
											switch(create_command[1]) {
												case "quadratic": function = LossFunction.Quadratic;
													break;
												default:System.out.println(create_command[1] + " is not recognized as a loss function");
											}
										}
										break;
									case "finish":
										if(inputs_num == 0 || function == null || num_of_layers == 0 || network_name.length() == 0)System.out.println("not all values have been filled");
										else cancel = networks.addNetwork(new Network(sizes.toArray(new int[num_of_layers][]), types.toArray(new LayerType[num_of_layers]), functions.toArray(new ActivationFunction[num_of_layers]), inputs_num, function), network_name);
										break;
									case "add_layer":
										if(create_command.length < 2) System.out.println("create add_layer command must be followed by a layer type");
										else {
											switch(create_command[1].toUpperCase()) {
												case "ANN":
													boolean innerCancel = false;
													int size = 0;
													ActivationFunction aFunction = null;
													while(!innerCancel) {
														try {
															String[] ann_command = receiveCommand(">create>add_layer_ANN>");
															switch(ann_command[0].toLowerCase()) {
																case "cancel": innerCancel = true; break;
																case "size":
																	try {
																		size = Integer.parseInt(ann_command[1]);
																	}catch(Exception e){
																		System.out.println("create_add_layer_ann_size command must be followed by an integer bigger than zero");
																	}
																	break;
																case "activation_function":
																	if(ann_command.length == 1) System.out.println("activation_function command must be followed by an activation function");
																	switch(ann_command[1]) {
																		case "identity": aFunction = ActivationFunction.identity; break;
																		case "sigmoid": aFunction = ActivationFunction.sigmoid; break;
																		default:System.out.println(ann_command[1] + " is not a recognized activation function");
																	}
																	break;
																case "finish":
																	if(size != 0 && aFunction != null) {
																		sizes.add(new int[] {size});
																		functions.add(aFunction);
																		types.add(LayerType.ANN);
																		num_of_layers++;
																		innerCancel = true;
																	}else System.out.println("not all values have been filled");
																	break;
																default:System.out.println(ann_command[0] + " is not a recognized command");
															}
														}catch(Exception e) {
															e.printStackTrace();
														}
													}
													break;
												default:
													System.out.println(create_command[1] + " is not a recognized layer type");
													break;
											}
										}
										break;
									case "edit_layer":
										if(create_command.length < 2) System.out.println("create edit_layer command must be followed by a layer type");
										else {
											switch(create_command[1].toUpperCase()) {
												case "ANN":
													boolean innerCancel = false;
													int size = 0;
													ActivationFunction aFunction = null;
													int location = -1;
													while(!innerCancel) {
														try {
															String[] ann_command = receiveCommand(">create>edit_layer_ANN>");
															switch(ann_command[0].toLowerCase()) {
																case "cancel": innerCancel = true; break;
																case "size":
																	try {
																		size = Integer.parseInt(ann_command[1]);
																		if(size <= 0) throw(new IllegalArgumentException());
																	}catch(Exception e){
																		System.out.println("create_edit_layer_ann_size command must be followed by an integer bigger than zero");
																	}
																	break;
																case "location":
																	try {
																		location = Integer.parseInt(ann_command[1]);
																		if(location < 0 || location > num_of_layers) throw(new IllegalArgumentException());
																	}catch(Exception e){
																		System.out.println("create_edit_layer_ann_size command must be followed by an integer bigger than zero");
																	}
																	break;
																case "activation_function":
																	if(ann_command.length == 1) System.out.println("activation_function command must be followed by an activation function");
																	switch(ann_command[1]) {
																		case "identity": aFunction = ActivationFunction.identity; break;
																		case "sigmoid": aFunction = ActivationFunction.sigmoid; break;
																		default:System.out.println(ann_command[1] + " is not a recognized activation function");
																	}
																	break;
																case "finish":
																	if(size != 0 && aFunction != null && location != -1) {
																		sizes.remove(location);
																		sizes.add(location, new int[] {size});
																		functions.remove(location);
																		functions.add(location, aFunction);
																		types.remove(location);
																		types.add(location, LayerType.ANN);
																		innerCancel = true;
																	}else System.out.println("not all values have been filled");
																	break;
																default:System.out.println(ann_command[0] + " is not a recognized command");
															}
														}catch(Exception e) {
															e.printStackTrace();
														}
													}
													break;
												default:
													System.out.println(create_command[1] + " is not a recognized layer type");
													break;
											}
										}
										break;
									default: System.out.println(create_command[0] + " is not a recognized command"); break;
								}
							}catch(Exception e) {
								e.printStackTrace();
							}
						}
						break;
						
					default: System.out.println(command[0] + " is not a recognized command");break;
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

}
