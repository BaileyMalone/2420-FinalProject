package comprehensive;

import java.io.File;



import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;


/**
 * A class representing an "input grammar file".
 * Input grammar files are files specified using 
 * Stephen Ward's format for the comprehensive final
 * project in CS 2420, Summer 2012 -- they are part of
 * a framework for generating random phrases following
 * a specified sentence structure. 
 * 
 * @author Bailey malone && Jeongyoun Chae
 *
 */
public class GrammarFile 
{
	// Data Members
		ArrayList<String> sentenceReps;
		HashMap<String, ArrayList<String> > nonTerminals;
		
		MersenneTwisterFast twister;
		//XORSRandom xrand;
		//Random rand;


		// Constructor
		//public GrammarFile(File file)
		public GrammarFile(String filename)
		{
			twister = new MersenneTwisterFast();
			twister.setSeed(System.currentTimeMillis());
			//xrand = new XORSRandom(System.currentTimeMillis());
			//rand = new Random();
			
			Object[] grammarData = TextProcessor.ProcessGrammarFile(filename);
			sentenceReps = ((ArrayList<String>) grammarData[0]);
			nonTerminals = ((HashMap<String, ArrayList<String>>) grammarData[1]);
			
			/* DEBUG */
//			System.out.println("<Sentence Formats>: " + grammarData[0]);
//			System.out.println("<GrammarFile Constructor, before cast>: "+ grammarData[1] + "\n");
		}
		
		// Method Members
		public ArrayList<String> sentenceFormats()
		{	return this.sentenceReps;	}
		
		/* DEBUG METHOD */
		protected void outputNonTerminalLists()
		{
			Iterator it = this.nonTerminals.entrySet().iterator();
			while (it.hasNext())
			{
				Map.Entry pair = (Map.Entry) it.next();
				System.out.println("["+pair.getKey()+", "+pair.getValue()+"]");
			}
		}
		
		private int randomIndex(int highest)
		{
			return twister.nextInt(highest+1);
			//return Math.abs(this.xrand.nextInt()) % ((highest > 0) ? highest : 1);
			//return rand.nextInt(highest+1);
		}
		
		public String getRandomProductionRule()
		{	return sentenceReps.get(randomIndex(sentenceReps.size()-1));	}
		
		/*
		 * NOTE:
		 * RandomPhraseGenerator should loop, calling this method,
		 * until all Non-Terminals are replaced with terminals!
		 */
		/**
		 * Randomly indexes an ArrayList of Terminals associated with 
		 * the Non-Terminal parameter as a key to our map that associates
		 * Non-Terminals and Terminals of a grammar input file.
		 * 
		 * PUNCTUATION NOTE: Must/does handle any punctuation attached to an
		 * argument. For example, if this method got passed "<plea>," , it would
		 * trim off the ',' and hold onto it, replace "<plea>", and then
		 * concatenate the ',' back onto the replacement.
		 * 
		 * @param nonTerminalKey
		 * @return
		 */
		public String getRandomReplacement(String nonTerminalKey)
		{
			// Check for punctuation
			//String punctuation = nonTerminalPunctuation(nonTerminalKey);
			// Remove the punctuation from the Non-Terminal (~ map key)
			nonTerminalKey = nonTerminalKey.substring(0, nonTerminalKey.indexOf('>')+1);
			
			// Get the list of terminals for the Non-Terminal passed/found
			ArrayList<String> values = this.nonTerminals.get(nonTerminalKey);
			
			/* DEBUG */
			//System.out.println("<NTK>: " + nonTerminalKey);
			
			// Generate a random index within the bounds of that list
			int randomIndex = 0;
			
			// Is the Production-Rule being referenced?
			if (nonTerminalKey.compareTo("<start>") == 0)
				return this.getRandomProductionRule();
			else
				randomIndex = this.randomIndex(values.size()-1);
			
			// Return the randomly-indexed replacement
			return values.get(randomIndex);
		}
		
		/**
		 * Pulls off any end to the Non-Terminal tag that is
		 * punctuation.
		 * 
		 * @param nt
		 * @return
		 */
		protected String nonTerminalPunctuation(String nt)
		{
			// Find the closing '>'
			int closeIndex = nt.indexOf('>');
			
			// Substring the end from the close
			String str = nt.substring(closeIndex).trim();
			
			return str;
		}
}
