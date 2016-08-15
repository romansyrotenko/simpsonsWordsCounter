import java.io.*;
import java.text.BreakIterator;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class WordsFromFilesCounter {

    static String DEFAULT_PATH_TO_FILES = "/home/sroman/Projects/IdeaProjects/jsonParseHtmlApp/";
    static int totalWordsCount = 0;
    static int totalTreatedFiles = 0;
    static HashMap<String, Integer> allUniqWords = new HashMap<String, Integer>();


    public static void main(String args[]) throws IOException {

        String pathToFiles = getPathToFiles();
        readAndCalcFiles(pathToFiles);
        outputResults();
    }

    private static String getPathToFiles() {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to Simpson's Words Counter!" +
                            "\n---\nPlease Enter Path to Directory with files: " +
                            "[Enter] is Default Path: \"" + DEFAULT_PATH_TO_FILES +"\"");

        String pathToFiles = scanner.nextLine();
        if (pathToFiles.equals("")) {
            pathToFiles = DEFAULT_PATH_TO_FILES;
        }
        scanner.close();
        return pathToFiles;
    }

    private static void readAndCalcFiles(String pathToFiles) throws IOException {

        File folder = new File(pathToFiles);
        File[] listOfFiles = folder.listFiles();
        BufferedReader inputStream;
        String sCurrentLine;

        for (int i = 0; i < listOfFiles.length; i++) {

            File file = listOfFiles[i];
            if (file.isFile() && file.getName().endsWith(".txt") && !(file.length() == 0)) {
                String text = null;
                inputStream = new BufferedReader(new FileReader(file));
                while ((sCurrentLine = inputStream.readLine()) != null) {
                    text = text + sCurrentLine;
                }
                calcAllWords(text);
                totalTreatedFiles++;
            }
        }
    }

    private static void calcAllWords(String text) {

        BreakIterator wordIterator = BreakIterator.getWordInstance();

        wordIterator.setText(text);
        int start = wordIterator.first();
        int end = wordIterator.next();

        while (end != BreakIterator.DONE) {
            String word = text.substring(start,end);
            if (Character.isLetterOrDigit(word.charAt(0))) {
                totalWordsCount++;
                addWordtoTable(word.toLowerCase());
            }
            start = end;
            end = wordIterator.next();
        }

    }

    private static void addWordtoTable(String word) {

        if (!allUniqWords.containsKey(word)) {
            allUniqWords.put(word, 1);
        } else {
            int tempValue = allUniqWords.get(word);
            allUniqWords.put(word, tempValue + 1);
        }
    }

    private static void outputResults() {

        TreeMap<String, Integer> sortedMap;

        sortedMap = sortHashMapByValue(allUniqWords);
        System.out.println("Total treated files are:     " + totalTreatedFiles);
        System.out.println("Main count all words are:    " + totalWordsCount);
        System.out.println("All count of uniq words are: " + allUniqWords.size());
        System.out.println("\nTOP 100 Most frequency words:");

        int count = 0;
        for (Map.Entry entry: sortedMap.entrySet()) {
            if (count >= 100) {
                break;
            } else {
                System.out.println("\"" + entry.getKey() + "\"" + " present " + entry.getValue() + " times");
                count++;
            }
        }
    }

    private static TreeMap<String, Integer> sortHashMapByValue (HashMap<String, Integer> inputHashMap) {

        ValueComparator bvc = new ValueComparator(inputHashMap);
        TreeMap<String, Integer> sorted_map = new TreeMap<String, Integer>(bvc);
        sorted_map.putAll(inputHashMap);
        return sorted_map;
    }

}
