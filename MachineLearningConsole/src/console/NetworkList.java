package console;

import NeuralNetwork.Network;

public class NetworkList {
	
	private Node<Network> first;
	
	public NetworkList() {
		first = null;
	}
	
	public boolean addNetwork(Network network, String name) {
		if(first == null) first = new Node<Network>(network, name);
		else {
			Node<Network> iterator = first;
			while(!iterator.getName().equals(name) && iterator.hasNext()) iterator = iterator.getNext();
			if(iterator.getName().equals(name)) return false;
			iterator.setNext(new Node<Network>(network, name));
		}
		return true;
	}
	public boolean removeNetwork(String name) {
		if(first != null) {
			if(first.getName().equals(name)) {
				first = first.getNext();
				return true;
			}
			Node<Network> iterator = first;
			while(iterator.hasNext() && !iterator.getNext().getName().equals(name)) iterator = iterator.getNext();
			if(iterator.hasNext()) {
				iterator.setNext(iterator.getNext().getNext());
				return true;
			}
		}
		return false;
	}
	
	public Network getNetwork(String name) {
		Node<Network> iterator = first;
		while(iterator != null && !iterator.getName().equals(name)) iterator = iterator.getNext();
		if(iterator != null) return iterator.getInfo();
		return null;
	}
	public boolean renameNetwork(String oldName, String newName) {
		Node<Network> iterator = first;
		while(iterator != null && !iterator.getName().equals(oldName)) iterator = iterator.getNext();
		if(iterator == null) return false;
		iterator.setName(newName);
		return true;
	}
	public boolean networkExists(String name) {
		Node<Network> iterator = first;
		while(iterator != null && !iterator.getName().equals(name)) iterator = iterator.getNext();
		return iterator != null;
	}
	
	private class Node<T>{
		
		private String name;
		private T info;
		private Node<T> next;
		
		private Node(T info, String name) {
			this.info = info;
			this.name = name;
			this.next = null;
		}
		
		private String getName() {
			return this.name;
		}
		private T getInfo() {
			return this.info;
		}
		private Node<T> getNext() {
			return this.next;
		}
		
		private void setName(String Name) {
			this.name = Name;
		}
		private void setInfo(T info) {
			this.info = info;
		}
		private void setNext(Node<T> next) {
			this.next = next;
		}
		
		private boolean hasNext() {
			return next != null;
		}
		
		public String toString() {
			String s = name + ":" + info.toString() + ";";
			if(next == null) return s;
			return s + next.toString();
		}
	}
}
