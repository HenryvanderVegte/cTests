package de.unidue.ctest.comp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import de.unidue.ctest.compareUtil.CTestCompare;
import de.unidue.ctest.util.CTest;
import de.unidue.ctest.util.CTestRead;

public class Compare {

	public static void main(String[] args) throws Exception{
		Path newFiles = Paths.get("target/compare/new");
		Path oldFiles = Paths.get("target/compare/old");
		Path outputPath = Paths.get("target/compare/comparison");		
		Compare compare = new Compare();
		List<Comparison> comparison = compare.compareAll(oldFiles, newFiles);
		compare.writeComparisonToFiles(comparison, outputPath);
	}
	
	public List<Comparison> compareAll(Path oldpath, Path newpath) throws IOException{
		List<CTest> oldCTests = getAllCTests(oldpath);
		List<CTest> newCTests = getAllCTests(newpath);
		return CTestCompare.compareAll(oldCTests, newCTests);
	}
	
	private List<CTest> getAllCTests(Path path) throws IOException{
		List<CTest> ctests = new ArrayList<>();
		File folder = path.toFile();
		File[] listOfFiles = folder.listFiles();
		for(File f : listOfFiles){
			CTest cTest = CTestRead.initializeCTestFromFile(f);
			ctests.add(cTest);
		}
		return ctests;
	}
	
	private void writeComparisonToFiles(List<Comparison> comparison, Path outputPath) throws IOException{

		for(Comparison comp : comparison){
			int i = 0;
			float totalDiff = 0f;
			if(comp == null)
				continue;
			List<String> lines = new ArrayList<>();
			lines.add("Wort;Stamm;Andere Möglichkeiten;Fehlerrate geschätzt;Fehlerrate tatsächlich;Fehlerrate Diff"); 
			for(CompareObject compObj : comp.objects){
				String line = compObj.word;
				line += ";" + compObj.lemma;
				if(compObj.hasAlternativeWords){
					String altWords = "";
					for(String alt :compObj.alternativeWords){
						altWords += alt + " ";
					}
					altWords = altWords.substring(0, altWords.length() - 1);
					line += ";" + altWords;
				} else {
					line += "; ";
				}
				line += ";" + roundErrorRate(compObj.oldErrorRate);
				line += ";" + roundErrorRate(compObj.newErrorRate);		
				line += ";" + calculateDifference(compObj.oldErrorRate, compObj.newErrorRate);
				lines.add(line);
				totalDiff += Float.parseFloat(calculateDifference(compObj.oldErrorRate, compObj.newErrorRate));
				i++;
			}
			//String meanDiff = roundErrorRate((totalDiff / (float)i) +"");
			//lines.add(";;;;;");
			//lines.add(";;;;Diff-Durchschnitt:;" + meanDiff);
			String fileName = comp.docName;
			fileName = fileName.substring(0,fileName.length() -8);
			fileName+= "." + "csv";
			
			File outputFile = outputPath.resolve(fileName).toFile();
			if(!outputFile.exists()){
				outputFile.getParentFile().mkdirs(); 
				outputFile.createNewFile();
			}
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
		   		for(String line : lines){
		   			writer.write(line);
		   			writer.newLine();
		   		}
			}
		}
	}
	
	private String roundErrorRate(String errorRate){
		Double value = Double.parseDouble(errorRate);
		return Math.round(value * Math.pow(10, 2)) / Math.pow(10, 2) + "";
	}
	
	private String calculateDifference(String errorRate1, String errorRate2){
		Double val1 = Double.parseDouble(errorRate1);
		Double val2 = Double.parseDouble(errorRate2);
		return roundErrorRate((val1 - val2) + "");
	}
	

}
