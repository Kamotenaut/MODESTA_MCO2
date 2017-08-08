import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

/**
 * Created by Brandon on 7/30/2017.
 */
public class CardDrawSim {
    private TXTWriter writer = new TXTWriter();

    private int numCards, numTrials, currTrial, userValue, corGuess;
    private ArrayList<Card> cardHandRep, cardHandNoRep;
    private ArrayList<Integer> cardHandRepValues, cardHandNoRepValues;
    private Deck deckRep, deckNoRep;
    private Logger logger = Logger.getLogger("MyLog");
    private FileHandler fh;
    private RServeConnector rServeConnector;
    private boolean withReplacement;
    private double idealBinomProbElem, idealNBinomProbElem, idealHyperProbElem, idealMultiProbElem;

    private double actualProbability = 0;

    private ProbRes overAllProb;
    private ArrayList<ProbRes> perTriWRepProb;
    private ArrayList<ProbRes> perTriWORepProb;

    private Stats binomStats = new Stats();
    private Stats nbinomStats = new Stats();
    private Stats hyperStats = new Stats();
    private Stats multiStats = new Stats();


    public CardDrawSim(int numTrials, int numCards, int userValue, boolean withReplacement){
        this.withReplacement = withReplacement;
        cardHandRep = new ArrayList<Card>();
        cardHandNoRep = new ArrayList<Card>();
        cardHandRepValues = new ArrayList<Integer>();
        cardHandNoRepValues = new ArrayList<Integer>();
        deckRep = new Deck();
        deckRep.shuffle();
        deckNoRep = new Deck();
        deckNoRep.shuffle();
        currTrial = 0;

        this.numTrials = numTrials;
        this.numCards = numCards;
        this.userValue = userValue;

        overAllProb = new ProbRes(userValue);
        perTriWRepProb = new ArrayList<>();
        perTriWORepProb = new ArrayList<>();

        rServeConnector = new RServeConnector();

        // Print all combinations & get all correct combinations out of all of them.

        printCombination(13, numCards, overAllProb);

        //Get Probability of Getting any correct values in a deck
        getProbabilityOfValue(overAllProb);

        run();

        writer.writeResult("Ideal Probability: \n ");
        writer.writeResult("Probability of any of the correct combination with Replacement: " + overAllProb.getProbRep());
        writer.writeResult("Probability of any of the correct combination without Replacement: " + overAllProb.getProbNoRep());
        writer.writeResult("Binomial Distribution: " + getIdealBinomProbElem());
        writer.writeResult("          Mean: " + binomStats.getMean() + " || Variance: " + binomStats.getVariance() + " || SD: " + binomStats.getSd());
        writer.writeResult("Neg. Binomial Distribution: " + getIdealNBinomProbElem());
        writer.writeResult("          Mean: " + nbinomStats.getMean() + " || Variance: " + nbinomStats.getVariance() + " || SD: " + nbinomStats.getSd());
        writer.writeResult("Hypergeometric Distribution: " + getIdealHyperProbElem());
        writer.writeResult("          Mean: " + hyperStats.getMean() + " || Variance: " + hyperStats.getVariance() + " || SD: " + hyperStats.getSd());
        writer.writeResult("Multinomial Distribution: " + getIdealMultiProbElem());
        writer.writeResult("          Mean: " + multiStats.getMean() + " || Variance: " + multiStats.getVariance() + " || SD: " + multiStats.getSd());


        graphActualVsIdeal();

        if(withReplacement) {
            graphAllRep();
            graphActualProbabilitiesRep();
            graphIdealProbabilitiesWRep();
        }
        else{
            graphAllNoRep();
            graphActualProbabilitiesNoRep();
            graphIdealProbabilitiesWORep();
        }

        System.out.println("End of Process");
    }

    public void run(){
        if(withReplacement)
            doAllReplacement();
        else
            doAllNoReplacement();

        while(currTrial<numTrials){
            writer.writeValue("Trial # " + (currTrial+1)+"\n");
            //System.out.println("Trial #" + (currTrial+1));
            DrawCards();
            currTrial++;
        }

        writer.writeResult("Total Possible Combinations with Replacement: " + overAllProb.getTotalCombinationsWRep());
        writer.writeResult("Total Possible Combinations without Replacement: " + overAllProb.getTotalCombinationsWORep());
        writer.writeResult("Num of Correct Combinations of Value with Replacement: " + overAllProb.getCorrectCombinationsWRep());
        writer.writeResult("Num of Correct Combinations of Value without Replacement: " + overAllProb.getCorrectCombinationsWORep());
        writer.writeResult("Num of correct trials: " + corGuess+"\n");
        //System.out.println("Num of correct trials: " + corGuess);
        actualProbability = (float)corGuess / (float)numTrials;
        //System.out.println("Actual Probability: " + corGuess + " / " + numTrials + " = " + (float)actualProbability);
        writer.writeResult("Actual Probability of Experiment: " + actualProbability+"\n");



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
        writer.writeValue("Total value of Drawing without Rep: " + sumNRoRep +"\n");

        writer.writeValue("Total value of Drawing with Rep: " + sumRep +"\n");
        //System.out.println("Total value of Drawing without Rep: " + sumNRoRep);
        //System.out.println("Total value of Drawing with Rep: " + sumRep);
        if(sumNRoRep==userValue && !withReplacement)
            corGuess++;
        else if(sumRep==userValue && withReplacement)
            corGuess++;
        cardHandNoRepValues.add(sumNRoRep);
        cardHandRepValues.add(sumRep);

        //System.out.println("Actual Probability: ");
        //System.out.println("Without Rep: " + sumNRoRep);
        perTriWORepProb.add(new ProbRes(sumNRoRep));
        printCombination(13, numCards, perTriWORepProb.get(perTriWORepProb.size()-1));
        getProbabilityOfValue(perTriWORepProb.get(perTriWORepProb.size()-1));
        //System.out.println("With Rep: " + sumRep);
        perTriWRepProb.add(new ProbRes(sumRep));
        printCombination(13, numCards, perTriWRepProb.get(perTriWRepProb.size()-1));
        getProbabilityOfValue(perTriWRepProb.get(perTriWRepProb.size()-1));

        writer.writeValue("Probability of Value wo Rep: " +  perTriWORepProb.get(perTriWORepProb.size()-1).getProbNoRep());
        writer.writeValue("Probability of Value w Rep: " +  perTriWORepProb.get(perTriWORepProb.size()-1).getProbRep());


    }

    private void doAllNoReplacement(){
        ProbRes prob = overAllProb;
        setBinomStats(rServeConnector.doDBinom("1:"+prob.getCorrectCombinationsWORep(), numTrials, prob.getProbNoRep()));
        setNbinomStats(rServeConnector.doDNBinom("1:"+(numTrials-1),
                1+"",
                prob.getProbNoRep()));
        setHyperStats(rServeConnector.doDHyper("1:"+prob.getCorrectCombinationsWORep(),
                prob.getCorrectCombinationsWORep(),
                prob.getTotalCombinationsWORep() - prob.getCorrectCombinationsWORep(),
                numTrials));


        ArrayList<Integer> n = new ArrayList<>();
        ArrayList<Double> p = new ArrayList<>();
        n.add(1);
        n.add(numTrials-1);
        p.add((double)prob.getProbNoRep());
        p.add((double)1-p.get(0));

        setMultiStats(rServeConnector.doDMultinom(n, p));

        idealBinomProbElem = getBinomStats().getProb();
        idealNBinomProbElem = getNbinomStats().getProb();
        idealHyperProbElem = getHyperStats().getProb();
        idealMultiProbElem = getMultiStats().getProb();
    }

    private void doAllReplacement(){
        ProbRes prob = overAllProb;

        setBinomStats(rServeConnector.doDBinom("1:"+prob.getCorrectCombinationsWRep(), numTrials, prob.getProbRep()));
        setNbinomStats(rServeConnector.doDNBinom("1:"+(numTrials-1),
                1+"",
                prob.getProbRep()));
        setHyperStats(rServeConnector.doDHyper("1:"+prob.getCorrectCombinationsWRep(),
                prob.getCorrectCombinationsWRep(),
                prob.getTotalCombinationsWRep() - prob.getCorrectCombinationsWRep(),
                numTrials));

        ArrayList<Integer> n = new ArrayList<>();
        ArrayList<Double> p = new ArrayList<>();
        n.add(1);
        n.add(numTrials-1);
        p.add((double)prob.getProbRep());
        p.add((double)1-p.get(0));

        setMultiStats(rServeConnector.doDMultinom(n, p));

        idealBinomProbElem = getBinomStats().getProb();
        idealNBinomProbElem = getNbinomStats().getProb();
        idealHyperProbElem = getHyperStats().getProb();
        idealMultiProbElem = getMultiStats().getProb();


    }

    private void graphIdealProbabilitiesWRep(){
        ProbRes prob = overAllProb;
        rServeConnector.graphDBinom("0:"+numTrials, ""+numTrials, (double)prob.getProbRep());
        rServeConnector.graphDNBinom("1:"+(numTrials-1), 1+"", (double)prob.getProbRep());
        rServeConnector.graphDHyper("1:"+prob.getCorrectCombinationsWRep(),
                                    ""+prob.getCorrectCombinationsWRep(),
                                    ""+ (prob.getTotalCombinationsWRep()-prob.getCorrectCombinationsWRep()),
                                    numTrials+"");
        ArrayList<Integer> n = new ArrayList<>();
        ArrayList<Double> p = new ArrayList<>();
        n.add(1);
        n.add(0);
        p.add((double)prob.getProbRep());
        p.add((double)1-p.get(0));
        rServeConnector.graphDMultinom(n, p, numTrials);
    }

    private void graphIdealProbabilitiesWORep(){
        ProbRes prob = overAllProb;
        rServeConnector.graphDBinom("0:"+numTrials, ""+numTrials, (double)prob.getProbNoRep());
        rServeConnector.graphDNBinom("1:"+(numTrials-1), 1+"", (double)prob.getProbNoRep());
        rServeConnector.graphDHyper("1:"+prob.getCorrectCombinationsWORep(),
                ""+prob.getCorrectCombinationsWORep(),
                ""+ (prob.getTotalCombinationsWORep()-prob.getCorrectCombinationsWORep()),
                numTrials+"");
        ArrayList<Integer> n = new ArrayList<>();
        ArrayList<Double> p = new ArrayList<>();
        n.add(1);
        n.add(0);
        p.add((double)prob.getProbNoRep());
        p.add((double)1-p.get(0));
        rServeConnector.graphDMultinom(n, p, numTrials);
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

    private void graphActualProbabilitiesNoRep(){
        ArrayList<Double> probsWORep = new ArrayList<Double>();
        for(int i=0; i<perTriWORepProb.size(); i++){
            probsWORep.add((double)perTriWORepProb.get(i).getProbNoRep());
        }
        rServeConnector.graphProbHist(probsWORep,"Histogram of Actual Probabilities (No Repetitions)");

    }

    private void graphActualProbabilitiesRep(){
        ArrayList<Double> probsWRep = new ArrayList<Double>();
        for(int i=0; i<perTriWRepProb.size(); i++){
            probsWRep.add((double)perTriWRepProb.get(i).getProbRep());
        }
        rServeConnector.graphProbHist(probsWRep,"Histogram of Actual Probabilities (Repetitions)");
    }

    private void graphActualVsIdeal(){
        rServeConnector.graphActualVsIdeal(actualProbability, overAllProb.getProbNoRep(), "Actual vs Ideal Probability of Success WO Repetition", "Probability", "");

        rServeConnector.graphActualVsIdeal(actualProbability, overAllProb.getProbRep(), "Actual vs Ideal Probability of Success W Repetition", "Probability", "");

        rServeConnector.graphActualVsIdeal(actualProbability, idealBinomProbElem, "Actual Probability vs Ideal Binomial Distribution",
                                            "Probability", "");
        rServeConnector.graphActualVsIdeal(actualProbability, idealNBinomProbElem, "Actual Probability vs Ideal Neg. Binomial Distribution",
                "Probability", "");
        rServeConnector.graphActualVsIdeal(actualProbability, idealHyperProbElem, "Actual Probability vs Ideal HyperGeometric Distribution",
                "Probability", "");
        rServeConnector.graphActualVsIdeal(actualProbability, idealMultiProbElem, "Actual Probability vs Ideal Multinomial Distribution",
                "Probability", "");
    }






    public void getProbabilityOfValue(ProbRes prob){
        //System.out.println("Combinations of value " + prob.getDesiredValue() + " wo replacement : " + prob.getCorrectCombinationsWORep());
        //System.out.println("Combination of value " + prob.getDesiredValue() + " w replacement : " + prob.getCorrectCombinationsWRep());
        prob.setTotalCombinationsWORep(rServeConnector.doCombinationsNoRep(13, numCards));
        //System.out.println("Total WO Rep: " + prob.getTotalCombinationsWORep());
        prob.setTotalCombinationsWRep(rServeConnector.doCombinationsRep(13, numCards));
        //System.out.println("Total W Rep: " + prob.getTotalCombinationsWRep());
        prob.setProbNoRep( ((float)1/(float)(prob.getTotalCombinationsWORep())) * (float)prob.getCorrectCombinationsWORep() );
        prob.setProbRep( ((float)1/(float)(prob.getTotalCombinationsWRep())) * (float) prob.getCorrectCombinationsWRep());
        //System.out.println("Ideal Probability of Hand Without Rep: " + String.format("%.5f", (float) prob.getProbNoRep()));
        //System.out.println("Ideal Probability of Hand With Rep: " + String.format("%.5f", (float) prob.getProbRep()));
    }


    //Obtained from http://www.geeksforgeeks.org/print-all-possible-combinations-of-r-elements-in-a-given-array-of-size-n/
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


            int sameNumCtr = 0;
            int sum = 0;
            for (int j=0; j<r; j++){
                sum += data[j];
            }


            if(sum == prob.getDesiredValue()){
                //System.out.print("$$$$ Correct Value : " );
                for(int i=0; i<r; i++)
                    //System.out.print(data[i] + " ");
                //System.out.println();
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

    public double getActualProbability(){
        return actualProbability;
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

    public Stats getBinomStats() {
        return binomStats;
    }

    public void setBinomStats(Stats binomStats) {
        this.binomStats = binomStats;
    }

    public Stats getNbinomStats() {
        return nbinomStats;
    }

    public void setNbinomStats(Stats nbinomStats) {
        this.nbinomStats = nbinomStats;
    }

    public Stats getHyperStats() {
        return hyperStats;
    }

    public void setHyperStats(Stats hyperStats) {
        this.hyperStats = hyperStats;
    }

    public Stats getMultiStats() {
        return multiStats;
    }

    public void setMultiStats(Stats multiStats) {
        this.multiStats = multiStats;
    }
}
