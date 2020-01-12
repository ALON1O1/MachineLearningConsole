package console;

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
		String network_path = "bin\\networks\\";
		
		boolean shouldContinue = true;
		
		while(shouldContinue) {
			try {
				String[] command = receiveCommand(">");
				switch(command[0].toLowerCase()) {
					case "quit": shouldContinue = false; break;
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
