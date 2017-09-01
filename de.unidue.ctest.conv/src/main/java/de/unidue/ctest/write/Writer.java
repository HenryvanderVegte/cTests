package de.unidue.ctest.write;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import de.unidue.ctest.util.CTest;
import de.unidue.ctest.util.CTestVal;

public class Writer {
	
	private Path outputPath;
	private Path resourcePath;
	
	public Writer() throws IOException{
		resourcePath = Paths.get("target/resources");
		outputPath = resourcePath.resolve("output/texts_all");
	}

	public void writeCTests(List<CTest> cTests) throws IOException{
		FileUtils.cleanDirectory(outputPath.toFile()); 
		for(CTest cTest : cTests){
			int i = 0;
			List<String> lines = new ArrayList<>();
			//lines.add(cTest.documentName);
			//lines.add(cTest.language);
			//lines.add("FoundRawDoc: " + cTest.foundRawDocument());
			boolean hasValues = false;
			
			for(CTestVal cTestVal : cTest.cTestValues){
				
				if(cTestVal.getWord().contains("{") || cTestVal.getWord().contains("}") ){
					System.out.println(cTest.documentName+ " " + cTest.language);
					System.out.println(cTestVal.getWord());
				}
				
				if(!cTestVal.hasLemma()){
					lines.add(cTestVal.getWord());
					if(cTestVal.getWord().equals(".") ||
							cTestVal.getWord().equals("?") ||
							cTestVal.getWord().equals("!")
							){
						lines.add("----");
					}
				} else {
					

					String line = cTestVal.getWord();
					line += "\t";
					line += i;
					line += "\t";
					line += cTestVal.getLemma();
					line += "\t";
					line += cTestVal.getErrorRate();
					if(!cTestVal.getCount().equals("0")){
						hasValues = true;
					}
					
					if(cTestVal.hasAlternativeWords()){
						String words = "";
						for(String word : cTestVal.getAlternativeWords()){
							words += word + ",";
						}
						words = words.substring(0, words.length() - 1);
						line += "\t";
						line += words;
					}
					lines.add(line);
					i++;
				}
			}
			
			String docName = cTest.documentName;
			if(docName.length() > 7){
				docName = docName.substring(0, 6);
			}
			docName.replace(" ", "_");
			docName.replace(".", "");
			
			int boolVal = cTest.foundRawDocument() ? 1 : 0;
			int hasValuesVal = hasValues ? 1 : 0;

			String fileName = cTest.language + "_" + docName + "_" + boolVal + "_" + hasValuesVal ;
			writeToFile(fileName, lines, hasValues);
			for(String line : lines){
				//System.out.println(line);
			}
			
		}
	}
	
	private void writeToFile(String fileName, List<String> content, boolean isTrain) throws IOException{
		String add = "";
		if(isTrain){
			add = "train/";
		} else {
			add = "test/";
		}
		File outputFile = outputPath.resolve(add + fileName + ".txt").toFile();
		if(!outputFile.exists()){
			outputFile.getParentFile().mkdirs(); 
			outputFile.createNewFile();
		}
		
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
	   		for(String line : content){
	   			writer.write(line);
	   			writer.newLine();
	   		}
		}
	}
	
}
