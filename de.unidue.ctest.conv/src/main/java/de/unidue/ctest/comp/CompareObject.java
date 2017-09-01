package de.unidue.ctest.comp;

import java.util.List;

import de.unidue.ctest.util.CTestVal;

public class CompareObject {
		
	public String word;
	public String lemma;
	public String oldCount;
	public String newCount;
	public String oldErrorRate;
	public String newErrorRate;
	public boolean hasAlternativeWords;
	public List<String> alternativeWords;
	
	public CompareObject(String word, String lemma, String oldCount, String newCount, String oldErrorRate, String newErrorRate){
		this.word = word;
		this.lemma = lemma;
		this.oldCount = oldCount;
		this.newCount = newCount;
		this.oldErrorRate = oldErrorRate;
		this.newErrorRate = newErrorRate;
		hasAlternativeWords = false;
	}
	public CompareObject(String word, String lemma, String oldCount, String newCount, String oldErrorRate, String newErrorRate, List<String> alternativeWords){
		this.word = word;
		this.lemma = lemma;
		this.oldCount = oldCount;
		this.newCount = newCount;
		this.oldErrorRate = oldErrorRate;
		this.newErrorRate = newErrorRate;
		this.alternativeWords = alternativeWords;
		hasAlternativeWords = true;
	}
	
}
