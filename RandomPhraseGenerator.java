package comprehensive;

import java.io.File;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Generates pseudo-random phrases $
 * (1) phrase structure and elements determined by a
 * 		specifically formatted input file, and 
 * (2) randomness is determined by java.util.Random's
 * 		nextInt([int x])
 * 
 * @author Bailey Malone && Jeongyoun Chae
 *
 */
public class RandomPhraseGenerator 
{
	/**
	 * <VERSION 2.0>
	 * File reading speed optimized.
	 * 
	 * 
	 * args[0] ~ The path and name of the input grammar file
	 * args[1] ~ # of random phrases to generate
	 * 
	 * <Example Run from Cmd-Line>: 
	 * "java comprehensive.RandomPhraseGenerator poetic_sentence.g 5"
	 * 
	 * INPUT: grammar file (.g)
	 * OUTPUT: the randomly generated phrases, one per line
	 * 
	 * @param args
	 */
	public static void main(String[] args) 
	{
		// Build the Grammar File
		GrammarFile grammarFile = new GrammarFile(args[0]);

		// Generate the random phrases!
		ArrayList<String> phrases = new ArrayList<String>();
		ArrayList<String> sentences = grammarFile.sentenceFormats();


		for (int i = 0; i < Integer.parseInt(args[1]); i++)
		{
			String phrase = "";

			/* Start building the phrase */
			// Choose a random <start> production rule 
			// Tokenize the sentence-format on whitespace
			// Parse each token, replacing when needed, until we have all terminals
			String punc = "";
			String[] sentence = (grammarFile.getRandomProductionRule()).split("\\s");

			int sentencePieceIndex = 0;
			for (String s : sentence)
			{
				if (sentencePieceIndex == (sentence.length-1))
					punc = grabPuncutation(s);

				// If the last character is not something useful...
				if (s.charAt(s.length()-1) == '.' || s.charAt(s.length()-1) == '!' || s.charAt(s.length()-1) == '?')
					s.substring(0, s.length()-2);

				int replacementCount = 0;
				while (s.contains("<"))
				{
					// Replace Non-Terminal with a Terminal
					if (replacementCount == 0)
						s = grammarFile.getRandomReplacement(s);
					else	// Replace the outstanding Non-Terminal
					{
						// Tokenize s by whitespace
						String[] tokens = s.split("\\s");

						// Replace the Non-Terminal(s) with a Terminal
						s = "";
						int tokenCount = 0;
						for (String str : tokens)
						{
							if (str.contains("<"))
								str = grammarFile.getRandomReplacement(str);

							// Don't add an extra space at the end!
							if (tokenCount < (tokens.length-1))
								s += str + " ";
							else
								s += str;
							tokenCount++;
						}
					}
					replacementCount++;
				}

				phrase += s;
				if (sentencePieceIndex < (sentence.length-1))
					phrase += " ";
				sentencePieceIndex++;
			}
			// Add ending punctuation back on the phrase
			if (!phrase.contains("."))
				phrase += punc;

			// Add phrase to list-of-phrases
			phrases.add(i, phrase);
		}

		// Output the phrases!
		for (int j = 0; j < phrases.size(); j++)
		{
			System.out.println(phrases.get(j));
		}
	}

	private static String grabPuncutation(String s) 
	{
		// Find the last non-word character
		String[] split = s.split("\\w");

		String end = "";
		if (split[split.length-1].charAt(0) == '>')
			end = split[split.length-1].substring(1);
		else if (split.length >= 1)
			end = split[split.length-1];

		return end;
	}
}
