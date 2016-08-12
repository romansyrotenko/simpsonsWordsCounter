import java.io.File;
import java.io.FileNotFoundException;
import java.text.BreakIterator;
import java.util.*;

public class WordsFromFilesCounter {

    static String DEFAULT_PATH_TO_FILES = "/home/sroman/Projects/IdeaProjects/jsonParseHtmlApp/";
    static int totalWordsCount = 0;
    static int totalTreatedFiles = 0;
    static long timeout = 0;
    static HashMap<String, Integer> allUnicWords = new HashMap<String, Integer>();


    public static void main(String args[]) throws FileNotFoundException {

        timeout = System.currentTimeMillis();
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
        return pathToFiles;
    }

    private static void readAndCalcFiles(String pathToFiles) throws FileNotFoundException {

        File folder = new File(pathToFiles);
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {

            File file = listOfFiles[i];
            if (file.isFile() && file.getName().endsWith(".txt")) {
                Scanner scanner = new Scanner( new File(file.toURI()) );
                String text = scanner.useDelimiter("\\A").next();
                scanner.close();
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

        if (!allUnicWords.containsKey(word)) {
            allUnicWords.put(word,1);
        } else {
            for (Map.Entry entry : allUnicWords.entrySet()) {
                if (entry.getKey().equals(word)) {
                    entry.setValue((Integer) entry.getValue() + 1);
                    break;
                }
            }
        }
    }

    private static void outputResults() {

        TreeMap<String, Integer> sortedMap;

        sortedMap = sortHashMapByValue(allUnicWords);
        timeout = System.currentTimeMillis() - timeout;
        System.out.println("Main working time is:        " + timeout/1000 + " sec");
        System.out.println("Total treated files are:     " + totalTreatedFiles);
        System.out.println("Main count all words are:    " + totalWordsCount);
        System.out.println("All count of unic aords are: " + allUnicWords.size());
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
