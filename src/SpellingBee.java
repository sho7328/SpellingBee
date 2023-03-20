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
public class SpellingBee {

    private String letters;
    private ArrayList<String> words;
    public static final int DICTIONARY_SIZE = 143091;
    public static final String[] DICTIONARY = new String[DICTIONARY_SIZE];

    public SpellingBee(String letters) {
        this.letters = letters;
        words = new ArrayList<String>();
    }

    // TODO: generate all possible substrings and permutations of the letters.
    //  Store them all in the ArrayList words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void generate() {
        makeWords("", letters);
    }

    public void makeWords(String possibles, String originals)
    {
        if (originals.length() == 0)
        {
            words.add(possibles);
            return;
        }
        words.add(possibles);
        for (int i = 0; i < originals.length(); i++) {
            String newPossible = possibles.substring(0, i);
            String newOriginals = originals.substring(0, i) + originals.substring(i + 1);
            makeWords(newPossible, newOriginals);
        }
    }

    // TODO: Apply mergesort to sort all words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void sort()
    {
        mergeSort(words, 0, words.size());
    }

    public ArrayList<String> mergeSort(ArrayList<String> arr, int low, int high)
    {
        if (high - low == 0) {
            ArrayList<String> sorted = new ArrayList<String>();
            sorted.add(arr.get(low));
            return sorted;
        }
        int med = (high + low) / 2;
        ArrayList<String> arr1 = new ArrayList<String>();
        arr1 = mergeSort(arr, low, med);
        ArrayList<String> arr2 = new ArrayList<String>();
        arr2 = mergeSort(arr, med + 1, high);
        merge(arr1, arr2);

    }

    public void merge(ArrayList<String> arr1, ArrayList<String> arr2)
    {
        ArrayList<String> merged = new ArrayList<String>();
        int i = 0, j = 0;
        while(i < arr1.size() && j < arr2.size())
        {
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
        while(j < arr2.size()) {
            merged.add(i + j, arr2.get(j));
            j++;
        }
        while(i < arr1.size()) {
            merged.add(i + j, arr1.get(i));
            i++;
        }
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

    // TODO: For each word in words, use binary search to see if it is in the dictionary.
    //  If it is not in the dictionary, remove it from words.
    public void checkWords() {
        for (int i = 0; i < words.size(); i++)
        {
            if(!found(words.get(i)))
            {
                words.remove(i);
                i--;
            }
        }
    }

    public boolean found(String n) {
        return search(n, DICTIONARY, 0, DICTIONARY_SIZE - 1);
    }

    public boolean search(String target, String[] DICTIONARY, int low, int high)
    {
        if (low > high)
        {
            return false;
        }
        int med = (high + low) / 2;
        if (DICTIONARY[med] == target)
        {
            return true;
        }
        if (DICTIONARY[med].compareTo(target) < 0)
        {
            low = med + 1;
        }
        else
        {
            high = med - 1;
        }
        return search(target, DICTIONARY, low, high);
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
