// input.txt located at: https://github.com/UIUC-public/MP0/blob/master/input.txt

import java.io.*;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

public class MP0
{
    Random generator;
    String userName;

    String delimiters       = " \t,;.?!-:@[](){}_*/";
    String[] stopWordsArray = {"i", "me", "my", "myself", "we", "our", "ours", "ourselves", "you", "your", "yours",
            "yourself", "yourselves", "he", "him", "his", "himself", "she", "her", "hers", "herself", "it", "its",
            "itself", "they", "them", "their", "theirs", "themselves", "what", "which", "who", "whom", "this", "that",
            "these", "those", "am", "is", "are", "was", "were", "be", "been", "being", "have", "has", "had", "having",
            "do", "does", "did", "doing", "a", "an", "the", "and", "but", "if", "or", "because", "as", "until", "while",
            "of", "at", "by", "for", "with", "about", "against", "between", "into", "through", "during", "before",
            "after", "above", "below", "to", "from", "up", "down", "in", "out", "on", "off", "over", "under", "again",
            "further", "then", "once", "here", "there", "when", "where", "why", "how", "all", "any", "both", "each",
            "few", "more", "most", "other", "some", "such", "no", "nor", "not", "only", "own", "same", "so", "than",
            "too", "very", "s", "t", "can", "will", "just", "don", "should", "now"};

    public MP0(String userName)
    {
        this.userName = userName;
    }

    // Get the random indexes to focus on
    public Integer[] getIndexes() throws NoSuchAlgorithmException
    {
        Integer n               = 10_000;
        Integer number_of_lines = 50_000;
        Integer[] set           = new Integer[n];
        long longSeed           = Long.parseLong(this.userName);
        this.generator          = new Random(longSeed);

        for (int i = 0; i < n; i++)
        {
            set[i] = generator.nextInt(number_of_lines);
        }

        return set;
    }

    // Build String from all command line input
    public ArrayList<String> startList(Integer[] indexes) throws IOException
    {
        BufferedReader reader        = new BufferedReader(new InputStreamReader(System.in));
        ArrayList<String> readInput  = new ArrayList<>();
        ArrayList<String> wikipedia  = new ArrayList<>();
        String line                  = "";

        // Read in command line input and store as an array
        while((line = reader.readLine()) != null)
        {
            readInput.add(line);
        }

        // Create ArrayList of only the titles designated by the indexes
        for (int n = 0; n < indexes.length; n++)
        {
            wikipedia.add(readInput.get(indexes[n]));
        }

        return wikipedia;
    }

    // Tokenize the list of titles, remove spaces and underscores
    public ArrayList<String> getTokens(ArrayList<String> startList)
    {
        ArrayList<String> tokenList = new ArrayList<>(startList.size());

        StringTokenizer tokenizer;

        for (int i = 0; i < startList.size(); i++)
        {
            tokenizer = new StringTokenizer(startList.get(i), delimiters);

            while (tokenizer.hasMoreTokens())
            {
                tokenList.add(tokenizer.nextToken().trim().toLowerCase());
            }
        }

        return tokenList;
    }

    // Get frequencies of times each string appears in the command line input
    public Map<String, Integer> getFrequencies(ArrayList<String> tokenList)
    {
        Map<String, Integer> wordFrequency = new HashMap<>();

        for (int n = 0; n < tokenList.size(); n++)
        {
            wordFrequency.merge(tokenList.get(n), 1, Integer::sum);
        }

        return wordFrequency;
    }

    // Sort the frequency of each word's appearance within the command line input
    List<String> sortFrequencies(Map<String, Integer> wordFrequency)
    {
        /*
        Stack Exchange Network
        Question: https://stackoverflow.com/questions/28709769/how-to-sort-map-entries-by-values-first-then-by-key-and-put-the-ordered-keys-in
        Answer:   https://stackoverflow.com/a/28710694/6870302
        User:     harshtuna
        Profile:  https://stackoverflow.com/users/4568811/harshtuna
        */
        final Comparator<Map.Entry<String, Integer>> freq  = Comparator.comparing(Map.Entry::getValue, Comparator.reverseOrder());
        final Comparator<Map.Entry<String, Integer>> label = Comparator.comparing(Map.Entry::getKey);

        final List<String> sortedWords = wordFrequency
                .entrySet().stream()
                .sorted(freq.thenComparing(label))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        return sortedWords;
    }

    // Set the top 20 most frequently used words (lexigraphically if numbers are equal for words)
    String[] mostFrequent(final List<String> sortedWords)
    {
        String[] topItems = new String[20];

        for (int i = 0; i < topItems.length; i++)
        {
            topItems[i] = sortedWords.get(i);
        }

        return topItems;
    }

    // Process the top 20 most used words in wikipedia titles
    public String[] process() throws IOException
    {
        try
        {
            // Get the random indexes
            Integer[] indexes = getIndexes();

            // Create ArrayList of the selected wikipedia titles
            ArrayList<String> startList = startList(indexes);

            // Tokenize the list, remove spaces, and make lowercase
            ArrayList<String> tokenList = getTokens(startList);

            // Remove all words not counting toward the most popular words
            tokenList.removeAll(Arrays.asList(stopWordsArray));

            // Create map for each word and the frequency of its' appearance in the input
            Map<String, Integer> wordFrequency = getFrequencies(tokenList);

            // Sort the frequencies from most frequent to least frequent
            final List<String> sortedWords = sortFrequencies(wordFrequency);

            // Build Array of the top 20 most frequent words in wikipedia titles
            String[] topItems = mostFrequent(sortedWords);

            return topItems;
        }

        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    // Entry point into the application
    public static void main(String args[]) throws Exception
    {
        if (args.length < 1)
        {
            System.out.println("missing the argument");
        }

        else
        {
            String userName   = args[0];
            MP0 mp            = new MP0(userName);
            String[] topItems = mp.process();

            for (String item: topItems)
            {
                System.out.println(item);
            }
        }
    }
}