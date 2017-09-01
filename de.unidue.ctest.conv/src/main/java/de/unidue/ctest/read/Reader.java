package de.unidue.ctest.read;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import de.unidue.ctest.util.CTest;
import de.unidue.ctest.util.CTestVal;

public class Reader {
	
	private static final String[] SPLIT_AT = {"?","!",",",".",";","\"","-","—",":","”","…",
												"(",")","(","“", "”","„"};
	
	private Path resourcePath;
	private Path inputTextsPath;
	private Path inputRawPath;

	
	public static void main(String[] args) throws IOException{
		Reader reader = new Reader();
		reader.getAllCTests();
	}
	
	public Reader() throws IOException{
		resourcePath = Paths.get("target/resources");
		inputTextsPath = resourcePath.resolve("input/texts_all");
		inputRawPath = resourcePath.resolve("input/raw");
	}
	
	public List<CTest> getAllCTests() throws IOException{
		File[] textFiles = inputTextsPath.toFile().listFiles();
		List<CTest> allCTests = new ArrayList<>();
		
		for(File f : textFiles){
			allCTests.addAll(exportCTestsOfFile(f));
		}
		addErrorRate(allCTests);
		return allCTests;
	}
	
	private List<CTest> addErrorRate(List<CTest> cTests) throws IOException{
		for(CTest cTest : cTests){
			//System.out.println("Document: " + cTest.documentName + " - " + cTest.language);
			File[] rawFiles = inputRawPath.resolve(cTest.language).toFile().listFiles();
			for(File f : rawFiles){
				List<String> lines = FileUtils.readLines(f);
				if(lines.get(0).equals(" \"C-Text: " + cTest.documentName +"\"")){
					cTest.setFoundRawDocument(true);
					int currentRawPosition = 2;
					for(CTestVal cTestVal : cTest.cTestValues){
						if(cTestVal.hasLemma()){

							String rawLine = lines.get(currentRawPosition);
							String[] rawSplit = rawLine.split(";");
							String errorRate = rawSplit[3];
							String count = rawSplit[1];
							String convertedErrorRate = "0.0";
							if(!count.equals("0")){
								convertedErrorRate = convertPercentage(errorRate);
							}
							cTestVal.setErrorRate(convertedErrorRate);
							cTestVal.setCount(count);
							currentRawPosition++;
						};
					}
					if(lines.size() != currentRawPosition){
						//System.out.println("WRONGSIZE " + cTest.documentName + ":" + lines.size() + " - " + currentRawPosition);
					} else  {
						//System.out.println("CORRECTSIZE");
					}
				} else {
					String first = lines.get(0).substring(0,17);
					String second = " \"C-Text: " + cTest.documentName +"\"";
					second = second.substring(0, 17);
					if(first.equals(second)){
						System.out.print("Similar title:");
						System.out.print(lines.get(0) + " ---" + cTest.documentName);
						System.out.println("");
					}
				}
			}							
		}		
		return null;
	}
	
	private String convertPercentage(String prevPercentage){
		Double value = Double.parseDouble(prevPercentage);
		value = value / 100d;
		value = 1d - value;
		return Math.round(value * Math.pow(10, 2)) / Math.pow(10, 2) + "";
	}
	
	
	private List<CTest> exportCTestsOfFile(File file) throws IOException{
		List<CTest> cTests = new ArrayList<>();
		
		List<String> lines = FileUtils.readLines(file);
		for(int i = 0; i < lines.size(); i++){
			String docName = lines.get(i);
			String docText = lines.get(i + 1);
		
			List<CTestVal> cTestValues = analyzeDocText(docText);
			String language = FilenameUtils.removeExtension(file.getName());
			CTest cTest = new CTest(language,docName, cTestValues);
			cTests.add(cTest);
			i = i + 3;
		}
		
		return cTests;
	}
	
	private List<CTestVal> analyzeDocText(String docText){
		ArrayList<CTestVal> cTestValues = new ArrayList<>();
		
		String[] words = docText.split(" ");
		boolean lastLetterSplit = false;
		String lastLetter= "";
		for(String word : words){
			lastLetterSplit = false;
			for(String splitAt : SPLIT_AT){
				if(word.startsWith(splitAt) && word.length() != 1){
					String firstLetter = word.substring(0, 1);
					word = word.substring(1, word.length());
					cTestValues.add(new CTestVal(firstLetter));
				}
				
				if(word.endsWith(splitAt) && word.length() != 1){
					lastLetterSplit = true;
					lastLetter = word.substring(word.length() - 1, word.length());
					word = word.substring(0, word.length() - 1);
				}
			}
			
			if(word.contains("{") && word.contains("}")){
				
				
				String[] split = word.split("\\{");
				String lemma = split[0];
				String ending = split[1].substring(0, split[1].length() - 1);
				if(ending.contains(",")){
					String[] endings = ending.split(",");
					String currentWord = lemma + endings[0];
					List<String> alternativeWords = new ArrayList<>();
					for(int i = 1; i < endings.length; i++){
						alternativeWords.add(lemma + endings[i]);
					}
					CTestVal cTestVal = new CTestVal(currentWord, lemma, alternativeWords);
					cTestValues.add(cTestVal);
				} else {
					CTestVal cTestVal = new CTestVal(lemma + ending, lemma);
					cTestValues.add(cTestVal);
				}
			} else {
				cTestValues.add(new CTestVal(word));
			}
			if(lastLetterSplit){
				cTestValues.add(new CTestVal(lastLetter));
			}
		}
		return cTestValues;
	}

}
