/**
 * AnagramTest.java -- Anagram
 * 
 * This program gets a string from the user (of length 3-8), generates
 * all permutations of the string and looks up each permutation in a
 * Dictionary. The Dictionary is the OPTED public domain English word
 * list dictionary, which is based on the public domain portion of 
 * "The Project Gutenberg Etext of Webster's Unabridged Dictionary",
 * which, in turn, based on the 1913 US Webster's Unabridged Dictionary.
 * 
 * See Project Gutenburg.
 *             http://msowww.anu.edu.au/~ralph/OPTED/
 * 
 * Words of length 3 to 8 were extracted from this dictionary into a  
 * file named opted3to8.txt. There are no plurals in the dictionary.
 * 
 * The entire dictionary is read into a Java Collection object and then
 * the user is allowed to enter a list of 3-8 characters, from which all
 * valid anagrams are extracted. 
 * 
 * @author rdb. Spring 2008
 * 
 * Edited
 * 02/13/13 rdb -- added a constructor that runs the code without timing
 *                 and from batch input -- much easier for grading.
 *                 It is called from the Tester.java class
 *     
 */

//////////////////////////////////////////////////////////////////////
// In early versions of the lab, students entered performance values as
//  comments. We now use a spreadsheet, but I left the values here for
//  historical curiosity.
///////////////////////////////////////////////////////////////////////
/* 
 * ------------------------- 7 character test ------------------------
 * Record 3 execution times for each of 3 options for 3 different words
 *                           message      escaper       relapse
 *                        +-------------+------------+-------------+
 * 1. Vector:             |    5.466    |  4.829     |    4.676    |
 *                        +-------------+------------+-------------+
 * 2. Hashset:            |    0.007    |  0.012     |    0.005    |
 *                        +-------------+------------+-------------+
 * 3. Binary search tree: |    0.027    |  0.02      |    0.005    |
 *                        +-------------+------------+-------------+
 *
 * ------------------------- 8 character test -----------------------
 * Record 1 execution time for Vector and 3 execution times for others.
 * Use the same word:        strainer
 *                        +-------------+
 * 1. Vector:             |   38,789    |  strainer     strainer
 *                        +-------------+------------+-------------+
 * 2. Hashset:            |   0.054     |    0.037   |   0.041     |
 *                        +-------------+------------+-------------+
 * 3. Binary search tree: |   0.063     |    0.045   |   0.044     |
 *                        +-------------+------------+-------------+
 *
 * ------------------------- 8 character worst case test --------------
 * Record 1 execution time for Vector and 3 execution times for others.
 * Use the string:           qwertiop
 *                        +-------------+
 * 1. Vector:             |   43.168    |  qwertiop     qwertiop
 *                        +-------------+------------+-------------+
 * 2. Hashset:            |    0.038    |   0.041    |    0.04     |
 *                        +-------------+------------+-------------+
 * 3. Binary search tree: |    0.041    |   0.043    |    0.045    |
 *                        +-------------+------------+-------------+ 
 */
import javax.swing.*;

import java.util.*;
import java.io.*;

public class AnagramNoGUI // extends JFrame
{
	// ---------------------- class variables -------------------------
	private static String smallDictionary = "opted3.txt";
	private static String bigDictionary = "opted3to8.txt";
	private static String dictionaryName = smallDictionary;

	// ---------------------- instance variables ----------------------
	Collection<String> _dictionary;
	private char _option;

	// --------------------------- constructor -----------------------

	public AnagramNoGUI() {

	}

	/**
	 * Test the anagram algorithm using different search strategies
	 */
	public void findAnagramByOptions(String fileName) {
		Collection<String> validWords = new Vector<String>();

		// Read The input file--------------------------
		File file = new File(fileName);

		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Properties props = new Properties();
		try {
			props.load(fis);
			fis.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		// ---------------------------------------------------------------------------------------------
		Object[] ekeys = props.keySet().toArray();
		String dictionaryChosen = null;
		String optionChosen = null;
		String anagramFileChosen = null;
		String wordsFile = null;

		// -----Among the read contents, get the required information----------
		for (int i = 0; i < ekeys.length; i++) {
			String key = (String) ekeys[i];
			switch (key) {
			case "dictionary":
				dictionaryChosen = (String) props.get(key);
				break;

			case "dataStructure":
				optionChosen = (String) props.get(key);
				break;

			case "inputFile":
				anagramFileChosen = (String) props.get(key);
				break;
			case "wordsFile":
				wordsFile = (String) props.get(key);
				break;
			}
		}

		// ------------Find out what dictionary is chosen-----------
		if (dictionaryChosen == null)
			return; // no dictionary change
		if (dictionaryChosen.equalsIgnoreCase("s"))
			dictionaryName = smallDictionary;
		else if (dictionaryChosen.equalsIgnoreCase("b"))
			dictionaryName = bigDictionary;

		// --------Find out what data structure is chosen-------------
		String option = null;
		if (optionChosen == null || optionChosen.length() == 0)
			option = optionChosen;
		else {
			_option = optionChosen.toLowerCase().charAt(0);
			switch (_option) {
			default:
				System.err.println("Invalid choice: using Hashset!");
				_option = 'h';
			case 'h':
				_dictionary = new HashSet<String>();
				option = "Hashset ";
				break;
			case 'v':
				option = "Vector ";
				_dictionary = new Vector<String>();
				break;

			case 'b':
				option = "Binary Tree ";
				_dictionary = new Vector<String>();
				break;
			}
		}

		if (option != null && option.length() > 0) {

			// ---------------Read the anagrams- to be tested-------------
			Scanner scanner = null;
			try {
				scanner = new Scanner(new File(anagramFileChosen));
			} catch (IOException ioe) {
				System.err.println("***Error -- can't open " + dictionaryName);
				System.exit(-1);
			}

			// -----------Add them to dictionary------------
			while (scanner.hasNext()) {
				_dictionary.add(scanner.next());
			}

			// ---Read the input file------------------------------
			try {
				scanner = new Scanner(new File(wordsFile));

				while (scanner.hasNextLine()) {
					String input = scanner.nextLine();
					String outMessage;

					if (input != null && input.length() > 0) {
						if (input.length() < 3 || input.length() > 8)
							outMessage = "Program limited to 3-8 letter words. "
									+ " Try another";
						else {
							long start = System.currentTimeMillis();
							lookup(validWords, "", input);
							if (validWords.size() == 0)
								outMessage = input + ": leads to no words";
							else
								outMessage = makeMessage(input, validWords);

							validWords.clear();

							long time = System.currentTimeMillis() - start;
							float seconds = time / 1000.0f;

							String timeMsg = option + ": " + input + " -> "
									+ seconds + " secs\n";
							System.out.print(timeMsg);
							outMessage += "\n\n" + timeMsg;
						}
						System.out.println(outMessage);

					}
				}

			} catch (IOException ioe) {
				System.err.println("***Error -- can't open " + wordsFile);
				System.exit(-1);
			}

		}
	}

	// --------------------- search( String, int, int ) ----------------
	/**
	 * Do a binary search looking for the word.
	 */
	private boolean search(String word, // word to look up
			Vector<String> dict,// Dict as array
			int lo, // low index to search
			int hi) // high index to search
	{
		// return false;
		if (lo > hi)
			return false;
		int mid = (hi + lo) / 2;

		int comp = word.compareTo(dict.get(mid));

		if (comp < 0) // word belongs between lo and mid
			return search(word, dict, lo, mid - 1);
		else if (comp > 0) // word is between mid and hi
			return search(word, dict, mid + 1, hi);
		else
			return true;
	}

	// ---------------- lookup( Collection, String, String ------------
	/**
	 * This is the key recursive call for permutation generation
	 * 
	 * @param wordsFound
	 *            Collection<String> - Permutations representing valid words are
	 *            added to wordsFound.
	 * @param head
	 *            String - The fixed portion of the current permutation, which
	 *            is the starting portion of the string
	 * @param tail
	 *            String - The remaining set of characters that have not yet
	 *            been used in the head; we need all permutations of these to be
	 *            appended to the this head.
	 */
	private void lookup(Collection<String> wordsFound, String head, String tail) {
		String newHead, newTail;

		// the "Base case" is when there is nothing left in the tail.

		if (tail.length() == 0) // no tail means we have possible word
		{
			if (ifWord(head) && !wordsFound.contains(head))
				wordsFound.add(head);
		} else { // for each character in "tail" add it to the head and
					// generate permutations for the remainder of the tail.
			for (int i = 0; i < tail.length(); i++) {
				// add next character of the tail to the head
				newHead = head + tail.substring(i, i + 1);

				// remove that character from the tail
				newTail = tail.substring(0, i) + tail.substring(i + 1);

				// and recurse
				lookup(wordsFound, newHead, newTail);
			}
		}
	}

	// --------------------- ifWord( String ) -------------------------
	/**
	 * check if the argument string is in the English word dictionary.
	 */
	private boolean ifWord(String word) {
		if (_option == 'h' || _option == 'v')
			return _dictionary.contains(word);
		else {
			return search(word, (Vector<String>) _dictionary, 0,
					_dictionary.size() - 1);
		}
	}

	// ----------------- makeMessage( String, Collection ) ------------
	/**
	 * create the message that reports the results
	 */
	private String makeMessage(String start, Collection<String> validWords) {
		String message = start + " => " + validWords.size() + " words";
		int i = 0;

		for (Iterator iter = validWords.iterator(); iter.hasNext();) {
			if (i++ % 4 == 0)
				message = message + "\n" + iter.next();
			else
				message = message + "    " + iter.next();
		}
		return message;
	}

	// ----------------------- main -----------------------------------
	public static void main(String[] args) {
		AnagramNoGUI a1 = new AnagramNoGUI();
		a1.findAnagramByOptions("anagram1.properties");
	}
}
