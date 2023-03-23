import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Spelling Bee
 *
 * This program accepts an input of letters. It prints to an output file
 * all English words that can be generated from those letters.
 *
 * For example: if the user inputs the letters "doggo" the program will generate:
 * do
 * dog
 * doggo
 * go
 * god
 * gog
 * gogo
 * goo
 * good
 *
 * It utilizes recursion to generate the strings, mergesort to sort them, and
 * binary search to find them in a dictionary.
 *
 * @author Zach Blick, [ADD YOUR NAME HERE]
 *
 * Written on March 5, 2023 for CS2 @ Menlo School
 *
 * DO NOT MODIFY MAIN OR ANY OF THE METHOD HEADERS.
 */

// SOPHIE HO 3/23/23
// GENERATE, SORT, AND CHECK WORDS METHODS CODED BY SOPHIE HO
// The recursive methods used in the above methods coded by Sophie Ho
public class SpellingBee {
    private String letters;
    private ArrayList<String> words;
    public static final int DICTIONARY_SIZE = 143091;
    public static final String[] DICTIONARY = new String[DICTIONARY_SIZE];

    // Constructor: letters is the word inputted by the user
    // ArrayList words is the ArrayList that will be filled with the possible word combinations made from letters
    public SpellingBee(String letters) {
        this.letters = letters;
        words = new ArrayList<String>();
    }

    // Generates all possible substrings and permutations of the letters.
    // Stores them all in the ArrayList words
    public void generate() {
        // Call the recursive method makeWords
        makeWords("", letters);
    }

    // A recursive method that finds the substrings of letters recursively.
    // String possibles is the String used to make all possible combos
    // String originals is the word that is passed in to make the possible combos from.
    public void makeWords(String possibles, String originals)
    {
        // Base case: if all the possibles have been made, add the possible word and return/exit
        if (originals.length() == 0)
        {
            words.add(possibles);
            return;
        }
        // Add the possible word to the ArrayList of words
        words.add(possibles);
        // Go through originals and make all possible combinations of originals
        for (int i = 0; i < originals.length(); i++) {
            // Create a new possible combo by adding a new letter from originals
            String newPossible = possibles + originals.substring(i, i + 1);
            // Update originals by removing the letter just added to possibles
            String newOriginals = originals.substring(0, i) + originals.substring(i + 1);
            // Recurse.
            makeWords(newPossible, newOriginals);
        }
    }

    // Applies mergesort to sort all words by calling the recursive method mergeSort
    public void sort()
    {
        // Use the recursive method mergeSort to sort words
        words = mergeSort(words, 0, words.size() - 1);
    }

    // Mergesort sorts the words recursively by continuously splitting up the ArrayList until it reaches the smallest case
    // Then compares them and merges them together using the recursive method merge()
    public ArrayList<String> mergeSort(ArrayList<String> arr, int low, int high)
    {
        // If the passed in ArrayList is broken down to the smallest piece
        if (high - low == 0) {
            ArrayList<String> sorted = new ArrayList<String>();
            sorted.add(arr.get(low));
            // Return the ArrayList
            return sorted;
        }
        // Split the passed in ArrayList into two halves, and run merge on them so sort them.
        int med = (high + low) / 2;
        ArrayList<String> arr1 = new ArrayList<String>();
        arr1 = mergeSort(arr, low, med);
        ArrayList<String> arr2 = new ArrayList<String>();
        arr2 = mergeSort(arr, med + 1, high);
        // Return the sorted ArrayList
        return merge(arr1, arr2);
    }

    // Recursively merges the two arraylists together in order of String size
    public ArrayList<String> merge(ArrayList<String> arr1, ArrayList<String> arr2)
    {
        ArrayList<String> merged = new ArrayList<String>();
        int i = 0, j = 0;
        // While the ArrayLists' sizes are still comparable
        while(i < arr1.size() && j < arr2.size())
        {
            // Compare the Strings inside to determine which one is bigger, then add the smaller own first.
            if(arr1.get(i).compareTo(arr2.get(j)) < 0)
            {
                merged.add(i + j, arr1.get(i));
                i++;
            }
            else
            {
                merged.add(i + j, arr2.get(j));
                j++;
            }
        }
        // Add the rest of the sorted indexes to the merged ArrayList
        while(j < arr2.size()) {
            merged.add(i + j, arr2.get(j));
            j++;
        }
        // Add the rest of the sorted indexes to the merged ArrayList
        while(i < arr1.size()) {
            merged.add(i + j, arr1.get(i));
            i++;
        }
        // Return the sorted ArrayList
        return merged;
    }

    // Removes duplicates from the sorted list.
    public void removeDuplicates() {
        int i = 0;
        while (i < words.size() - 1) {
            String word = words.get(i);
            if (word.equals(words.get(i + 1)))
                words.remove(i + 1);
            else
                i++;
        }
    }

    // For each word in words, checkWord uses binary search to see if it is in the dictionary.
    // Calls method found that calls a recursive binary search method
    public void checkWords() {
        for (int i = 0; i < words.size(); i++)
        {
            // If the word is not in the dictionary, remove it from words.
            if(!found(words.get(i)))
            {
                words.remove(i);
                i--;
            }
        }
    }

    // This method uses the recursive method search (utilizing binary search) to make sure a possible word is a word
    // in the dictionary.
    public boolean found(String n) {
        return search(n, 0, DICTIONARY_SIZE - 1);
    }

    // Uses binary search to search for a given target string
    public boolean search(String target,  int low, int high)
    {
        // Check if the parameters are valid, and if the target word is a word in the dictionary.
        if (low > high)
        {
            return false;
        }
        int med = (high + low) / 2;
        // If the target word is in Dictionary, return true
        if (DICTIONARY[med].equals(target))
        {
            return true;
        }
        // If the target word is bigger than the Dictionary word, change the value of low
        if (DICTIONARY[med].compareTo(target) < 0)
        {
            low = med + 1;
        }
        // If the target word is smaller than the Dictionary word, change the value of high
        else
        {
            high = med - 1;
        }
        // Recurse
        return search(target, low, high);
    }

    // Prints all valid words to wordList.txt
    public void printWords() throws IOException {
        File wordFile = new File("Resources/wordList.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(wordFile, false));
        for (String word : words) {
            writer.append(word);
            writer.newLine();
        }
        writer.close();
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }

    public SpellingBee getBee() {
        return this;
    }

    public static void loadDictionary() {
        Scanner s;
        File dictionaryFile = new File("Resources/dictionary.txt");
        try {
            s = new Scanner(dictionaryFile);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open dictionary file.");
            return;
        }
        int i = 0;
        while(s.hasNextLine()) {
            DICTIONARY[i++] = s.nextLine();
        }
    }

    public static void main(String[] args) {

        // Prompt for letters until given only letters.
        Scanner s = new Scanner(System.in);
        String letters;
        do {
            System.out.print("Enter your letters: ");
            letters = s.nextLine();
        }
        while (!letters.matches("[a-zA-Z]+"));

        // Load the dictionary
        SpellingBee.loadDictionary();

        // Generate and print all valid words from those letters.
        SpellingBee sb = new SpellingBee(letters);
        sb.generate();
        sb.sort();
        sb.removeDuplicates();
        sb.checkWords();
        try {
            sb.printWords();
        } catch (IOException e) {
            System.out.println("Could not write to output file.");
        }
        s.close();
    }
}
