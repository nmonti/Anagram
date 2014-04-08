/* Nick Monti
 * Spire: 27288678
 * CS311
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.*;

public class Anagram {
	// stores longest word and amount of anagram classes
	private static int longestWord = 0;
	private static int anagramClasses = 0;
	
	public static void main(String[] args) throws IOException {
		// created the array list of SuperStrings(sorted, original, length)
		ArrayList<SuperString> list = new ArrayList<SuperString>();
		// buffered reader used to read input
		BufferedReader dict = null;
		System.out.print("Enter file: ");
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		String in = input.readLine();
		try {
			dict = new BufferedReader(new FileReader(in));
		} catch (IOException e) {
			System.out.println("Could not read file");
			System.exit(0);
		}	
		
		// read in dict line by line, creating a SuperString object and adding it to the list
		String word;
		System.out.println("Reading lines and getting keys via counting sort...");
		try {
			while (((word = dict.readLine()) != null) && (word != "")){
				if (word.length() > longestWord) longestWord = word.length();
				SuperString s = new SuperString(countingSort(word), word, word.length());
				list.add(s);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// call radix sort on list, and print the list
		radixSort(list);
		printAnagrams(list);
		System.out.println("Done! Amount of anagram classes: " + anagramClasses);
	}
	
	
	public static void printAnagrams(ArrayList<SuperString> list) throws IOException{
		/* printAnagrams takes in the list and prints it so long as the size isn't huge.
		 * To print this correctly I tried to keep a pointer to the previously visited word.
		 * If it was the same as the next one, we keep track until we find a different word.
		 * Then, using PrintWriter we print out everything to anagram1.
		 * Also, anagramClasses keeps track of the anagram classes
		 */
		String file = "anagram2";
		if(list.size() < 100000){
			System.out.println("Writing to anagram1.txt...");
			file = "anagram1";
		}
		PrintWriter writer = new PrintWriter(file);
		
		int counter = 1;
		int ptr = 0;

		for(int i = 0; i < list.size()-1; i++){
			if(list.get(i).getSorted().equals(list.get(i+1).getSorted())){
				counter++;
			} else{
				ptr = counter;
				counter = 1;
			}
			// if there are more than one matches, print on same line
			if(ptr > 1){
				while(ptr > 0){
					//anagramClasses++;
					if(list.size() < 100000)
						writer.print(list.get(i - ptr+1).getWord() + " ");
					ptr--;
				}
				if(list.size() < 100000) writer.println();
			}
			else {
				anagramClasses++;
				if(list.size() < 100000)
					writer.println(list.get(i).getWord());
			}
			
		}
		writer.println();
		writer.println("Anagram Classes: " + anagramClasses);
		writer.close();
	}
	
	public static String countingSort(String word){
		/* This rendition of countingSort takes in a word, and sorts
		 * on the characters within the word. Ex. cats->acst.
		 * This is used as the key (getSorted) for the algorithm.
		 */
		
		char[] charArray = new char[word.length()+1];
		int[] c = new int[26];
		for(int j = 0; j < word.length(); j++){
			int alpha = (word.charAt(j) - 'a'); // set ASCII values to 0-25

			c[alpha] = c[alpha] + 1;
		}
		for(int i = 1; i < c.length; i++){
			c[i] = c[i]+c[i-1];
		}
		for(int j = word.length() - 1; j >= 0; j--){
			int alpha = (word.charAt(j) - 'a');
			charArray[c[alpha]-1] = word.charAt(j);
			c[alpha] = c[alpha] - 1;
		}
		return new String(charArray);
	}	
	
	
	public static ArrayList<SuperString> radixSort(ArrayList<SuperString> list){
		/* This radix sort takes in the list of SuperStrings and sorts
		 * based on the key (getSorted). Temp acts as a "bucket" that captures the 
		 * least significant digit to most significant digit. So temp stores 
		 * lists of the words that's current place is at that value. Again,
		 * we adjust the ASCII values to 0-25 so, temp[0] would have all the words
		 * whose current placeholder contains that letter.
		 */
		System.out.println("Sorting keys via radix sort...");
		ArrayList<ArrayList<SuperString>> temp = new ArrayList<ArrayList<SuperString>>(27);
		int size = list.size();
		for(int i = 0; i < 27; i++){
			temp.add(new ArrayList<SuperString>());
		}
		// longestWord because we start from least significant digit
		for(int i = longestWord -1; i >= 0; i--){
			int placeholder = 0;
			
			for(int j = 0; j < size; j++){
				if(i >= list.get(j).getLength()){
					placeholder = 0;
				}
				else {
					placeholder = list.get(j).getSorted().charAt(i) - 97;	
				}	
				temp.get(placeholder).add(list.get(j));	
			}
			// clear list so we can rewrite sorted into it.
			list.clear();
			for(ArrayList<SuperString> g : temp){
				for(SuperString s : g){
					list.add(s);
				}
				// clear "bucket"
				g.clear();
			}
		}
		return list;				
	}	
}
