package comprehensive;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * A utility class for processing .g file text and
 * building a Grammar File representing a randomizable
 * set of defined sentences.
 * 
 * @author Bailey Malone && Jeongyoun Chae
 *
 */
public class TextProcessor 
{
	public static Object[] ProcessGrammarFile(String filename)
	{
		// We'll be returning these to GrammarFile...
		Object[] objects = new Object[2];
		ArrayList<String> format = new ArrayList<String>();
		HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();

		/* Start Scanning the file */
		try 
		{
			ArrayList<String> values = new ArrayList<String>();
			
			//File file = new File(filename);
			//Scanner scan = new Scanner(file);
			FileReader fr = new FileReader(filename);
			BufferedReader reader = new BufferedReader(fr);
			
			String next = "";
			while ((next = reader.readLine()) != null)
			//while (scan.hasNext())
			{
				//next = scan.nextLine();

				if (next.equals("{"))
				{
					//next = scan.nextLine();
					next = reader.readLine();

					if (next.equals("<start>"))	/* Phrase Structure */
					{
						while (!next.equals("}"))
						{
							//next = scan.nextLine();
							next = reader.readLine();
							
							if (!next.equals("}"))	format.add(next); // *** Trim punctuation?
						}
					}
					else	/* Set of Non-Terminals */
					{
						String key = "";	
						while (!next.equals("}"))
						{
							// Non-Terminal?
							if (next.contains("<") && key.equals(""))
								key = next;
							else // Terminal
							{
								values.add(next); // *** Trim punctuation?
							}
							
							//next = scan.nextLine();
							next = reader.readLine();
						}
						map.put(key, values);
						values = new ArrayList<String>();
					} // END Else-block
				} // END IF ("{")
			} // END_WHILE
			
			
			objects[0] = format;
			objects[1] = map;
			

			// Read the information in?
			if (format.isEmpty())	
			{
				System.err.println("Sentence Representation Empty!");
				System.exit(1);
			}
			if (map.isEmpty())	
			{
				System.err.println("Non-Terminals List Empty!");
				System.exit(1);
			}
		} 
		catch (FileNotFoundException e) 
		{	System.err.println("***File Not Found!***");	e.printStackTrace();	} 
		catch (IOException e)
		{	e.printStackTrace();	}


		return objects;
	}
}
