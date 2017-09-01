package de.unidue.ctest.util;

import java.util.List;

public class CTestVal {

	private String word;
	
	private boolean hasLemma;
	
	private boolean hasAlternativeWords;
	
	private String lemma;
	
	private String errorRate = "0.0";
	
	private String count = "0";

	
	private List<String> alternativeWords;
	
	public CTestVal(String word){
		this.word = word;
		hasLemma = false;
	}
	
	public CTestVal(String word, String lemma){
		this.word = word;
		this.lemma = lemma;
		hasLemma = true;
	}
	
	public CTestVal(String word, String lemma, List<String> alternativeWords){
		this.word = word;
		this.lemma = lemma;
		this.alternativeWords = alternativeWords;
		hasLemma = true;
		hasAlternativeWords = true;
	}
	
	public CTestVal(String word, String lemma, List<String> alternativeWords, String errorRate){
		this(word, lemma, alternativeWords);
		this.errorRate = errorRate;
	}
	
	public CTestVal(String word, String lemma, String errorRate){
		this(word, lemma);
		this.errorRate = errorRate;
	}
	
	
	public boolean hasLemma(){
		return hasLemma;
	}
	
	public boolean hasAlternativeWords(){
		return hasAlternativeWords;
	}
	
	public String getWord(){
		return word;
	}
	
	public String getLemma(){
		return lemma;
	}
	
	public List<String> getAlternativeWords(){
		return alternativeWords;
	}
	
	public String getErrorRate(){
		return errorRate;
	}
	

	public void setErrorRate(String errorRate){
		this.errorRate = errorRate;
	}
	
	public String getCount(){
		return count;
	}
	
	public void setCount(String count){
		this.count = count;
	}

	
}
