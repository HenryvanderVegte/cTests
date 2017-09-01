package de.unidue.ctest.comp;

import java.util.List;

public class Comparison {
	public String docName;
	public List<CompareObject> objects;
	
	public Comparison(String docName, List<CompareObject> objects){
		this.docName = docName;
		this.objects = objects;
	}
}
