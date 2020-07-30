package org.psu.edu.sweng.capstone.backend.helper;

public class GraphElement {

	private String source;
	private GraphElement destination;
	
	public GraphElement(String source) {
		this.source = source;
	}
	
	public GraphElement(String source, GraphElement destination) {
		this.source = source;
		this.destination = destination;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public GraphElement getDestination() {
		return destination;
	}

	public void setDestination(GraphElement destination) {
		this.destination = destination;
	}
}
