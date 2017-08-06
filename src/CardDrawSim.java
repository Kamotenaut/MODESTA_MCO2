import java.util.ArrayList;
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
    private ArrayList<Double> binomProb, nbinomProb, hyperProb, multiProb;
    private float idealProbabilityNoRep = 0;
    private float idealProbabilityRep = 0;
    private int correctCombinationsWRep = 0;
    private int correctCombinationsWORep = 0;
    private int totalCombinations = 0;
    private boolean withReplacement;
    private double idealBinomProbElem, idealNBinomProbElem, idealHyperProbElem, idealMultiProbElem;

    private double actualProbability = 0;
    private float perTrialProbabilityWORep = 0;
    private float perTrialProbabilityWRep = 0;

    private ProbRes overAllProb;
    private ArrayList<ProbRes> perTriWRepProb;
    private ArrayList<ProbRes> perTriWORepProb;

    public CardDrawSim(int numTrials, int numCards, int userValue, boolean withReplacement){
        this.withReplacement = withReplacement;
        cardHandRep = new ArrayList<Card>();
        cardHandNoRep = new ArrayList<Card>();
        cardHandRepValues = new ArrayList<Integer>();
        cardHandNoRepValues = new ArrayList<Integer>();
        binomProb = new ArrayList<Double>();
        nbinomProb = new ArrayList<Double>();
        hyperProb = new ArrayList<Double>();
        multiProb = new ArrayList<Double>();
        deckRep = new Deck();
        deckRep.shuffle();
        deckNoRep = new Deck();
        deckNoRep.shuffle();
        currTrial = 0;

        this.numTrials = numTrials;
        this.numCards = numCards;
        this.userValue = userValue;

        overAllProb = new ProbRes(userValue);
        perTriWRepProb = new ArrayList<ProbRes>();
        perTriWORepProb = new ArrayList<ProbRes>();

        rServeConnector = new RServeConnector();

        // Print all combinations & get all correct combinations out of all of them.

        printCombination(13, numCards, overAllProb);

        //Get Probability of Getting either or of all correct values in a deck
        getProbabilityOfCorrectValue();

        //System.out.println(rServeConnector.doDMultinom());
        run();
    }

    public void run(){
        if(withReplacement)
            doAllReplacement();
        else
            doAllNoReplacement();
        while(currTrial<numTrials){
            System.out.println("Trial #" + (currTrial+1));
            DrawCards();
            currTrial++;
            //rServeConnector.doDBinom(1, )
        }

        if(withReplacement)
            graphAllRep();
        else graphAllNoRep();
        System.out.println("Num of correct trials: " + corGuess);
        actualProbability = (float)corGuess / (float)numTrials;
        System.out.println("Actual Probability: " + corGuess + " / " + numTrials + " = " + (float)actualProbability);
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


        System.out.println("Actual Probability: ");
        System.out.println("Without Rep: " + sumNRoRep);
        perTriWORepProb.add(new ProbRes(sumNRoRep));
        printCombination(13, numCards, perTriWORepProb.get(perTriWORepProb.size()-1));
        getProbabilityOfValue(perTriWORepProb.get(perTriWORepProb.size()-1));
        System.out.println("With Rep: " + sumRep);
        perTriWRepProb.add(new ProbRes(sumRep));
        printCombination(13, numCards, perTriWRepProb.get(perTriWRepProb.size()-1));
        getProbabilityOfValue(perTriWRepProb.get(perTriWRepProb.size()-1));


    }

    private void doAllNoReplacement(){
        //idealBinomProbElem = rServeConnector.doDBinom(1, numTrials, idealProbabilityNoRep);
        //idealNBinomProbElem = rServeConnector.doDNBinom(0, 1, idealProbabilityNoRep);
        //idealHyperProbElem = rServeConnector.doDHyper(1,correctCombinationsWORep,totalCombinations-correctCombinationsWORep,1);

        ProbRes prob = overAllProb;
        idealBinomProbElem = rServeConnector.doDBinom(1, numTrials, prob.getProbRep());
        idealNBinomProbElem = rServeConnector.doDNBinom(0, 1, prob.getProbNoRep());
        idealHyperProbElem = rServeConnector.doDHyper(1, prob.getCorrectCombinationsWORep(),
                prob.getTotalCombinations() - prob.getCorrectCombinationsWORep(),
                1);

        ArrayList<Integer> n = new ArrayList<>();
        n.add(prob.getCorrectCombinationsWORep());
        n.add(prob.getTotalCombinations()-prob.getCorrectCombinationsWORep());
        ArrayList<Double> p = new ArrayList<>();
        p.add((double)prob.getProbNoRep());
        p.add((double)1-prob.getProbNoRep());
        idealMultiProbElem = rServeConnector.doDMultinom(n, p);


    }

    private void doAllReplacement(){
        ProbRes prob = overAllProb;
        idealBinomProbElem = rServeConnector.doDBinom(1, numTrials, prob.getProbRep());
        // idealNBinomProbElem = rServeConnector.doDNBinom(totalCombinations-correctCombinationsWORep, correctCombinationsWRep, idealProbabilityRep);
         idealNBinomProbElem = rServeConnector.doDNBinom(prob.getTotalCombinations() - prob.getCorrectCombinationsWORep(),
                                                            prob.getCorrectCombinationsWORep(),
                                                            prob.getProbRep());
        //idealHyperProbElem = rServeConnector.doDHyper(1,correctCombinationsWRep,totalCombinations-correctCombinationsWRep,1);
        idealHyperProbElem = rServeConnector.doDHyper(1, prob.getCorrectCombinationsWRep(),
                                                    prob.getTotalCombinations() - prob.getCorrectCombinationsWRep(),
                                                    1);

        ArrayList<Integer> n = new ArrayList<>();
        n.add(prob.getCorrectCombinationsWRep());
        n.add(prob.getTotalCombinations()-prob.getCorrectCombinationsWRep());
        ArrayList<Double> p = new ArrayList<>();
        p.add((double)prob.getProbRep());
        p.add((double)1-prob.getProbRep());
        idealMultiProbElem = rServeConnector.doDMultinom(n, p);
    }

    private void graphAllNoRep(){
        rServeConnector.graphValuesHist(cardHandNoRepValues,"Histogram of Actual Results (No Repetitions)");
        rServeConnector.graphValuesScatterPlot(cardHandNoRepValues,"Scatterplot of Actual Results (No Repetitions)" );
        //rServeConnector.graphValuesLineGraph(cardHandNoRepValues,"Line Graph of Actual Results (No Repetitions)" );
    }

    private void graphAllRep(){
        rServeConnector.graphValuesHist(cardHandRepValues,"Histogram of Actual Results (Repetitions)");
        rServeConnector.graphValuesScatterPlot(cardHandRepValues,"Scatterplot of Actual Results (Repetitions)" );
     //   rServeConnector.graphValuesLineGraph(cardHandRepValues,"Line Graph of Actual Results (Repetitions)" );
    }

    public void getProbabilityOfValue(ProbRes prob){
        float correct = 0;
        System.out.println("Combinations of value " + prob.getDesiredValue() + " wo replacement : " + prob.getCorrectCombinationsWORep());
        System.out.println("Combination of value " + prob.getDesiredValue() + " w replacement : " + prob.getCorrectCombinationsWRep());
        System.out.println("Total WO Rep: " + rServeConnector.doCombinationsNoRep(13, numCards));
        System.out.println("Total W Rep: " + rServeConnector.doCombinationsRep(13, numCards));
        prob.setProbNoRep( ((float)1/(float)(rServeConnector.doCombinationsNoRep(13, numCards))) * (float)prob.getCorrectCombinationsWORep() );
        prob.setProbRep( ((float)1/(float)(rServeConnector.doCombinationsRep(13, numCards))) * (float) prob.getCorrectCombinationsWRep());
        System.out.println("Ideal Probability of Hand Without Rep: " + String.format("%.5f", (float) prob.getProbNoRep()));
        System.out.println("Ideal Probability of Hand With Rep: " + String.format("%.5f", (float) prob.getProbRep()));
    }

    public void getProbabilityOfCorrectValue(){
        // TODO: Please migrate this to a separate file / class / chuchu. Thank you -Dyan
        /*
        * This is code for getting probability of correct hand in ONE trial. Thanks.
        *
         */
        float correct = 0;
        System.out.println("Correct Combinations wo replacement : " + overAllProb.getCorrectCombinationsWORep());
        System.out.println("Correct Combinations w replacement : " + overAllProb.getCorrectCombinationsWRep());
        overAllProb.setProbNoRep( ((float)1/(float)(rServeConnector.doCombinationsNoRep(13, numCards))) * (float)overAllProb.getCorrectCombinationsWORep() );
        overAllProb.setProbRep( ((float)1/(float)(rServeConnector.doCombinationsRep(13, numCards))) *(float) overAllProb.getCorrectCombinationsWRep() );
        System.out.println("Ideal Probability of Correct Hand Without Rep: " + String.format("%.5f", (float) overAllProb.getProbNoRep()));
        System.out.println("Ideal Probability of Correct Hand With Rep: " + String.format("%.5f", (float) overAllProb.getProbRep()));
    }

    //TODO: Obtained from http://www.geeksforgeeks.org/print-all-possible-combinations-of-r-elements-in-a-given-array-of-size-n/
    // The main function that prints all combinations of size r
    // in arr[] of size n. This function mainly uses combinationUtil()
    public void printCombination(int n, int r, ProbRes prob)
    {
        // A temporary array to store all combination one by one
        int[] data = new int[r];
        int[] arr = new int[n];

        for(int i=0; i<n; i++){
            arr[i] = (i+1);
        }

        // Print all combination using te2mprary array 'data[]'
        combinationUtil(arr, data, 0, n+r-3, 0, r, prob);
    }

    /* arr[]  ---> Input Array
       data[] ---> Temporary array to store current combination
       start & end ---> Staring and Ending indexes in arr[]
       index  ---> Current index in data[]
       r ---> Size of a combination to be printed */
    public void combinationUtil(int arr[], int data[], int start, int end,
                                int index, int r, ProbRes prob)
    {

        // Current combination is ready to be printed, print it
        if (index >= r)
        {
            prob.setTotalCombinations(prob.getTotalCombinations() + 1);
            //totalCombinations++;
            int sameNumCtr = 0;
            int sum = 0;
            for (int j=0; j<r; j++){
                sum += data[j];
            }


            if(sum == prob.getDesiredValue()){
                System.out.print("$$$$ Correct Value : " );
                for(int i=0; i<r; i++)
                    System.out.print(data[i] + " ");
                System.out.println();
                for(int j=0; j<r; j++){
                    if(j+1 < r)
                        if(data[j] == data[j+1]){
                            sameNumCtr ++;
                        }
                }
                //System.out.println("Same Num Ctr : " + sameNumCtr);
                prob.setCorrectCombinationsWRep(prob.getCorrectCombinationsWRep() + 1);
                //correctCombinationsWRep ++;
                if(sameNumCtr <= 3) {
                    //correctCombinationsWORep++;
                    prob.setCorrectCombinationsWORep(prob.getCorrectCombinationsWORep() + 1);
                }
            }

            return;
        }
        // replace index with all possible elements. The condition
        // "end-i+1 >= r-index" makes sure that including one element
        // at index will make a combination with remaining elements
        // at remaining positions
        try {
            for (int i = start; i <= end && end - i + 1 >= r - index; i++) {
                data[index] = arr[i];
                combinationUtil(arr, data, i, end, index + 1, r, prob);
            }
        }catch(Exception e){

        }
    }

    public double getIdealBinomProbElem() {
        return idealBinomProbElem;
    }

    public double getIdealNBinomProbElem() {
        return idealNBinomProbElem;
    }

    public double getIdealHyperProbElem() {
        return idealHyperProbElem;
    }

    public double getIdealMultiProbElem() {
        return idealMultiProbElem;
    }
}
