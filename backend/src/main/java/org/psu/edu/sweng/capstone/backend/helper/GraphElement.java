package org.psu.edu.sweng.capstone.backend.helper;

public class GraphElement {

	private Node source;
	private Node destination;
	
	public GraphElement(Node source, Node destination) {
		this.source = source;
		this.destination = destination;
	}

	public Node getSource() {
		return source;
	}

	public void setSource(Node source) {
		this.source = source;
	}

	public Node getDestination() {
		return destination;
	}

	public void setDestination(Node destination) {
		this.destination = destination;
	}
}
