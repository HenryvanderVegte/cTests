package de.unidue.ctest.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class CTestRead {

	
	public static CTest initializeCTestFromFile(File file) throws IOException{

		List<String> lines = FileUtils.readLines(file);
		String docName = file.getName();
		String firstLines = "";
		for(int i = 0; i < lines.size(); i++){
			firstLines += lines.get(i);
			if(i > 4)
				break;
		}
		
		List<CTestVal> ctestValues = new ArrayList<>();
		
		
		for(String line : lines){
			if(line.contains("\t")){
				String[] split =line.split("\t");
				String word = split[0];
				String lemma = split[2];
				String errorRate = split[3];
				
				if(split.length > 4){
					List<String> alternativeWords = new ArrayList<>();
					String[] altWords = split[4].split(",");
					for(int i = 0; i < altWords.length; i++){
						alternativeWords.add(altWords[i]);
					}
					ctestValues.add(new CTestVal(word, lemma, alternativeWords, errorRate));
				} else {
					ctestValues.add(new CTestVal(word, lemma, errorRate));
				}
				
			}
		}
		
		CTest ctest = new CTest("unknown", docName,  ctestValues);
		ctest.firstLines = firstLines;
		return ctest;
	}
}
