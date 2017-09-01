package de.unidue.ctest.util;

import java.util.List;

public class CTest {
	
	public String language;
	
	public String documentName;
	
	public String firstLines;
	
	public List<CTestVal> cTestValues;
	
	private boolean foundRawDocument = false;
	
	
	public CTest(String language, String documentName, List<CTestVal> cTestValues){
		this.language = language;
		this.documentName = documentName;
		this.cTestValues = cTestValues;
	}
	
	public void setFoundRawDocument(boolean foundRawDocument){
		this.foundRawDocument = foundRawDocument;
	}
	
	public boolean foundRawDocument(){
		return foundRawDocument;
	}
	
	
}
