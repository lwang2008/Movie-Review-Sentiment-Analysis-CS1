import java.io.*;
import java.util.*;

public class MovieReviewSentimentAnalysis {
    private static final String REVIEW_FILE = "movieReviews.txt";
    private static Scanner keyboard = new Scanner(System.in);

    //calculate average score for single word
    private static double getWordScore(String word) throws FileNotFoundException {
        int totalScore = 0;
        int count = 0;

        Scanner reviewScanner = new Scanner(new File(REVIEW_FILE));
        while (reviewScanner.hasNext()) {
            int reviewScore = reviewScanner.nextInt();
            String reviewText = reviewScanner.nextLine().toLowerCase();

            if (reviewText.contains(word)) {
                totalScore += reviewScore;
                count++;
            }
        }
        reviewScanner.close();
        return count > 0 ? (double) totalScore / count : -1;
    }

    //calculate average score for a single word
    private static void scoreWord() throws FileNotFoundException {
        System.out.print("Enter a word: ");
        String word = keyboard.nextLine().toLowerCase();

        double averageScore = getWordScore(word);
        
        if (averageScore != -1) {
            //count occurrences of the word
            int count = 0;
            Scanner reviewScanner = new Scanner(new File(REVIEW_FILE));
            while (reviewScanner.hasNext()) {
                reviewScanner.nextInt();
                String reviewText = reviewScanner.nextLine().toLowerCase();
                if (reviewText.contains(word)) {
                    count++;
                }
            }
            reviewScanner.close();
            System.out.println();
            System.out.println(word + " appears " + count + " times.");
            System.out.println("The average score for reviews containing the word " + word + " is " + averageScore);
        } else {
            System.out.println("This word does not appear in any reviews.");
        }
    }

    //calculate average score of words in file
    private static void scoreFile() throws FileNotFoundException {
        System.out.print("Enter the name of the file with words you want to find the average score for: ");
        String fileName = keyboard.nextLine();

        List<String> words = readWordsFromFile(fileName);
        double totalScore = 0;
        int wordCount = 0;

        //calculate score for each word in the file
        for (String word : words) {
            double wordScore = getWordScore(word); //uses helper function getWordScore
            if (wordScore != -1) {  //-1 means word is not found in any reviews
                totalScore += wordScore;
                wordCount++;
            }
        }

        //display results
        double averageScore = totalScore / wordCount;
        System.out.println();
        System.out.println("The average score of words in " + fileName + " is " + averageScore);
        if (averageScore > 2.01) {
            System.out.println("The overall sentiment of " + fileName + " is positive.");
        } else if (averageScore < 1.99) {
            System.out.println("The overall sentiment of " + fileName + " is negative.");
        } else {
            System.out.println("The overall sentiment of " + fileName + " is neutral.");
        }
    }

    //finds the most positive and most negative words from a file
    private static void highLowWord() throws FileNotFoundException {
        System.out.print("Enter the name of the file with words you want to score: ");
        String fileName = keyboard.nextLine();

        List<String> words = readWordsFromFile(fileName);
        String mostPositive = "";
        String mostNegative = "";
        double highestScore = Double.NEGATIVE_INFINITY;
        double lowestScore = Double.POSITIVE_INFINITY;

        //find the words with the highest and lowest scores
        for (String word : words) {
            double wordScore = getWordScore(word);
            if (wordScore > highestScore) {
                highestScore = wordScore;
                mostPositive = word;
            }
            if (wordScore < lowestScore) {
                lowestScore = wordScore;
                mostNegative = word;
            }
        }
        
        //display results
        System.out.println();
        System.out.println("The most positive word, with a score of " + String.format("%.2f", highestScore) + ", is '" + mostPositive + "'");
        System.out.println("The most negative word, with a score of " + String.format("%.2f", lowestScore) + ", is '" + mostNegative + "'");
        System.out.println();
    }

    //sorts words from a file into positive and negative and writes separate files
    private static void sortWords() throws FileNotFoundException {
        System.out.print("Enter the name of the file containing words: ");
        String fileName = keyboard.nextLine();

        List<String> words = readWordsFromFile(fileName);
        List<String> positiveWords = new ArrayList<>();
        List<String> negativeWords = new ArrayList<>();

        //sort words based on their scores, neutral wor
        for (String word : words) {
            double wordScore = getWordScore(word);
            if (wordScore > 2.1) {
                positiveWords.add(word);
            } else if (wordScore < 1.9) {
                negativeWords.add(word);
            }
        }

        //write the sorted words to their respective files
        writeWordsToFile(positiveWords, "positive.txt");
        writeWordsToFile(negativeWords, "negative.txt");
        System.out.println();
        System.out.println("Words have been sorted into 'positive.txt' and 'negative.txt'.");
    }

    //reads words from a file and returns them as a list
    private static List<String> readWordsFromFile(String fileName) throws FileNotFoundException {
        List<String> words = new ArrayList<>();
        Scanner fileScanner = new Scanner(new File(fileName));
        while (fileScanner.hasNextLine()) {
            words.add(fileScanner.nextLine().toLowerCase());
        }
        fileScanner.close();
        return words;
    }

    //writes a list of words to a file
    private static void writeWordsToFile(List<String> words, String fileName) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(new File(fileName));
        for (String word : words) {
            writer.println(word);
        }
        writer.close();
    }

    //main method that displays a menu and handles user choices
    public static void main(String[] args) throws FileNotFoundException {
        int choice;
        do {
            displayMenu();
            choice = keyboard.nextInt();
            keyboard.nextLine(); 

            switch (choice) {
                case 1:
                    scoreWord();
                    break;
                case 2:
                    scoreFile();
                    break;
                case 3:
                    highLowWord();
                    break;
                case 4:
                    sortWords();
                    break;
                case 5:
                    System.out.println("Exited Program.");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        } while (choice != 5);
    }

    //displays the options
    private static void displayMenu() {
        System.out.println("\n What would you like to do?");
        System.out.println("1. Get the score of a word");
        System.out.println("2. Get the average score of words in a file (one word per line)");
        System.out.println("3. Find the highest/lowest scoring words in a file");
        System.out.println("4. Sort words from a file into positive.txt and negative.txt");
        System.out.println("5. Exit the program");
        System.out.print("Enter a number 1-5: ");
    }
}
