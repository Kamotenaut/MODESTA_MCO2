import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

/**
 * Created by Brandon on 7/30/2017.
 */
public class CardDrawSim {
    private int numCards, numTrials, currTrial, userValue, corGuess;
    private ArrayList<Card> cardHandRep, cardHandNoRep;
    private ArrayList<Integer> cardHandRepValues, cardHandNoRepValues;
    private Deck deckRep, deckNoRep;
    private Logger logger = Logger.getLogger("MyLog");
    private FileHandler fh;
    private RServeConnector rServeConnector;

    private int correctCombinations = 0;

    public CardDrawSim(){
        Scanner sc = new Scanner(System.in);
        cardHandRep = new ArrayList<Card>();
        cardHandNoRep = new ArrayList<Card>();
        cardHandRepValues = new ArrayList<Integer>();
        cardHandNoRepValues = new ArrayList<Integer>();
        deckRep = new Deck();
        deckRep.shuffle();
        deckNoRep = new Deck();
        deckNoRep.shuffle();
        currTrial = 0;

        System.out.println("Input number of trials: ");
        numTrials = sc.nextInt();

        System.out.println("Input number of cards on hand: ");
        numCards = sc.nextInt();

        System.out.println("Input total value: ");
        userValue = sc.nextInt();
        sc.close();
        rServeConnector = new RServeConnector();

        // Print all combinations & get all correct combinations out of all of them.
        printCombination(13, numCards);

        //Get Probability of Getting either or of all correct values in a deck
        getProbabilityOfCorrectValue();

        run();
    }

    public void run(){
        while(currTrial<numTrials){
            System.out.println("Trial #" + (currTrial+1));
            DrawCards();
            currTrial++;
        }
        rServeConnector.graphValuesHist(cardHandNoRepValues,"Histogram of Actual Results (No Repetitions)");
        rServeConnector.graphValuesHist(cardHandRepValues, "Histogram of Actual Results Repetitions");
        rServeConnector.graphValuesScatterPlot(cardHandNoRepValues,"Scatterplot of Actual Results (No Repetitions)" );
        rServeConnector.graphValuesScatterPlot(cardHandRepValues,"Scatterplot of Actual Results (Repetitions)" );
        System.out.println("Num of correct trials: " + corGuess);
    }

    public void DrawCards(){
        int sumRep = 0, sumNRoRep = 0;
        Card cardRep, cardNoRep;

        for(int i = 0; i<numCards; i++ ){
            cardNoRep = deckNoRep.drawCard();
            cardRep = deckRep.drawCardReplace();
            cardHandNoRep.add(cardNoRep);
            cardHandRep.add(cardRep);
            sumNRoRep += cardNoRep.getValue();
            sumRep += cardRep.getValue();
        }
        System.out.println("Total value of Drawing without Rep: " + sumNRoRep);
        System.out.println("Total value of Drawing with Rep: " + sumRep);
        if(sumNRoRep==userValue)
            corGuess++;
        else if(sumRep==userValue)
            corGuess++;
        cardHandNoRepValues.add(sumNRoRep);
        cardHandRepValues.add(sumRep);
    }


    public void getProbabilityOfCorrectValue(){
        // TODO: Please migrate this to a separate file / class / chuchu. Thank you -Dyan
        /*
        * This is code for getting probability of correct hand in ONE trial. Thanks.
        *
         */
        float correct = 0;
        float actualProbabilityNoRep = 0;
        float actualProbabilityRep = 0;

        System.out.println("Correct Combinations: " + correctCombinations);
        System.out.println("Combinations: " + rServeConnector.doCombinationsNoRep(13, numCards));
        actualProbabilityNoRep = ((float)1/(float)(rServeConnector.doCombinationsNoRep(13, numCards))) * correctCombinations;
        actualProbabilityRep = ((float)1/(float)(rServeConnector.doCombinationsRep(13, numCards))) * correctCombinations;

        System.out.println("Actual Probability of Correct Hand Without Rep: " + String.format("%.5f", (float)actualProbabilityNoRep));
        System.out.println("Actual Probability of Correct Hand With Rep: " + String.format("%.5f", (float)actualProbabilityRep));
    }


    //TODO: Obtained from http://www.geeksforgeeks.org/print-all-possible-combinations-of-r-elements-in-a-given-array-of-size-n/
    // The main function that prints all combinations of size r
    // in arr[] of size n. This function mainly uses combinationUtil()
    public void printCombination(int n, int r)
    {
        // A temporary array to store all combination one by one
        int[] data = new int[r];
        int[] arr = new int[n];

        for(int i=0; i<n; i++){
            arr[i] = (i+1);
        }

        // Print all combination using te2mprary array 'data[]'
        combinationUtil(arr, data, 0, n+r-3, 0, r);
    }

    /* arr[]  ---> Input Array
       data[] ---> Temporary array to store current combination
       start & end ---> Staring and Ending indexes in arr[]
       index  ---> Current index in data[]
       r ---> Size of a combination to be printed */
    public void combinationUtil(int arr[], int data[], int start, int end,
                                int index, int r)
    {
        // Current combination is ready to be printed, print it
        if (index == r)
        {
            int sum = 0;
            for (int j=0; j<r; j++){
                sum += data[j];
            }
            if(sum == userValue){
                System.out.print("$$$$ Correct Value : " );
                for(int i=0; i<r; i++)
                    System.out.print(data[i] + " ");
                System.out.println();
                correctCombinations ++;
            }

            return;
        }

        // replace index with all possible elements. The condition
        // "end-i+1 >= r-index" makes sure that including one element
        // at index will make a combination with remaining elements
        // at remaining positions
        for (int i=start; i<=end && end-i+1 >= r-index; i++)
        {
            data[index] = arr[i];
            combinationUtil(arr, data, i, end, index+1, r);
        }
    }
}
