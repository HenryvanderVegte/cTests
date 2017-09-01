package de.unidue.ctest.compareUtil;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import de.unidue.ctest.comp.CompareObject;
import de.unidue.ctest.comp.Comparison;
import de.unidue.ctest.util.CTest;
import de.unidue.ctest.util.CTestVal;

public class CTestCompare {

	public static List<Comparison> compareAll(List<CTest> oldTests, List<CTest> newTests){
		List<Comparison> comparison = new ArrayList<>();
		int i = 0;
		boolean foundPair = true;
		while(foundPair){
			foundPair = false;
			CTest oldTest = null;
			CTest newTest = null;
			for(CTest currOld : oldTests){
				if(foundPair)
					break;
				for(CTest currNew : newTests){
					if(currOld.firstLines.equals(currNew.firstLines)){
						oldTest = currOld;
						newTest = currNew;
						foundPair = true;
						break;
					}
				}
			}
			
			if(foundPair){
				i++;
				oldTests.remove(oldTest);
				newTests.remove(newTest);
				System.out.println(i + ") Add:" + newTest.documentName);
				comparison.add(compareCTests(oldTest, newTest));
			}
			
			
		}
		for(CTest notFound : oldTests){
			System.out.println(notFound.documentName);
		}
		
		return comparison;
	}
	
	
	public static Comparison compareCTests(CTest oldTest, CTest newTest){
		if(oldTest.cTestValues.size() != newTest.cTestValues.size()){
			System.err.println("ERROR: Different sizes: " + 
								oldTest.documentName + "(" +  oldTest.cTestValues.size() + ")" +
								"  --  " +  
								newTest.documentName + "(" +  newTest.cTestValues.size() + ")" );
			
			return null;
		}
		List<CompareObject> compareObjects = new ArrayList<>();
		for(int i = 0; i < oldTest.cTestValues.size(); i++){
			CTestVal oldCTestVal = oldTest.cTestValues.get(i);
			CTestVal newCTestVal = newTest.cTestValues.get(i);
			if(!oldCTestVal.getWord().equals(newCTestVal.getWord())){
				System.out.println("ERROR: Different words: " + oldCTestVal.getWord() + " " + newCTestVal.getWord());
			}
			if(newCTestVal.hasAlternativeWords()){
				compareObjects.add(new CompareObject(oldCTestVal.getWord(), 
													oldCTestVal.getLemma(),
													oldCTestVal.getCount(),
													newCTestVal.getCount(),
													oldCTestVal.getErrorRate(), 
													newCTestVal.getErrorRate(), 
													newCTestVal.getAlternativeWords()));
				
			}else {
				compareObjects.add(new CompareObject(oldCTestVal.getWord(), 
													oldCTestVal.getLemma(),
													oldCTestVal.getCount(),
													newCTestVal.getCount(),
													oldCTestVal.getErrorRate(), 
													newCTestVal.getErrorRate()));
			}

		}
		
		return new Comparison(newTest.documentName, compareObjects);
	}
}
