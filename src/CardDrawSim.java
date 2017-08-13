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


    private int combiforNoDuplicates = 0;
    private int combiforDuplicates = 0;

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

        setOverAllProb(new ProbRes(userValue));
        perTriWRepProb = new ArrayList<>();
        perTriWORepProb = new ArrayList<>();

        rServeConnector = new RServeConnector();

        combiforNoDuplicates = rServeConnector.getPower(4, numCards);
        combiforDuplicates = rServeConnector.doCombinationsNoRep(4, numCards);
        if(numCards < 5)
            combiforDuplicates = rServeConnector.doCombinationsNoRep(4, numCards);
        else
            combiforDuplicates = rServeConnector.doCombinationsRep(4, numCards);

        // Print all combinations & get all correct combinations out of all of them.
        printCombination(13, numCards, getOverAllProb());

        //Get Probability of Getting any correct values in a deck
        getProbabilityOfValue(getOverAllProb());

        writer.writeResult("", true);
        writer.writeValue("", true);
        writer.writeAllCombi("", true);
        writer.writeCorCombi("", true);

        run();




        System.out.println("End of Process");
    }

    public void run(){
        while(currTrial<numTrials){
            writer.writeValue("Trial # " + (currTrial+1)+"\n", false);
            DrawCards();
            currTrial++;
        }

        writer.writeResult("Total Possible Combinations with Replacement: " + getOverAllProb().getTotalCombinationsWRep(), false);
        writer.writeResult("Total Possible Combinations without Replacement: " + getOverAllProb().getTotalCombinationsWORep(), false);
        writer.writeResult("Num of Correct Combinations of Value with Replacement: " + getOverAllProb().getCorrectCombinationsWRep(), false);
        writer.writeResult("Num of Correct Combinations of Value without Replacement: " + getOverAllProb().getCorrectCombinationsWORep(), false);
        writer.writeResult("Num of correct trials: " + corGuess+"\n", false);
        actualProbability = (float)corGuess / (float)numTrials;
        writer.writeResult("Actual Probability of Experiment: " + actualProbability+"\n", false);


        if(withReplacement) {
            doAllReplacement();
            graphAllRep();
            graphActualProbabilitiesRep();
            graphIdealProbabilitiesWRep();
        }
        else {
            doAllNoReplacement();
            graphAllNoRep();
            graphActualProbabilitiesNoRep();
            graphIdealProbabilitiesWORep();
        }
        graphActualVsIdeal();
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
        writer.writeValue("Total value of Drawing without Rep: " + sumNRoRep +"\n", false);
        writer.writeValue("Total value of Drawing with Rep: " + sumRep +"\n", false);
        if(sumNRoRep==userValue && !withReplacement)
            corGuess++;
        else if(sumRep==userValue && withReplacement)
            corGuess++;
        cardHandNoRepValues.add(sumNRoRep);
        cardHandRepValues.add(sumRep);

        if(withReplacement){
            perTriWRepProb.add(new ProbRes(sumRep));
            printCombination(13, numCards, perTriWRepProb.get(perTriWRepProb.size()-1));
            getProbabilityOfValue(perTriWRepProb.get(perTriWRepProb.size()-1));
            writer.writeValue("Probability of Value w Rep: " +  perTriWRepProb.get(perTriWRepProb.size()-1).getProbRep(), false);
        }else{
            perTriWORepProb.add(new ProbRes(sumNRoRep));
            printCombination(13, numCards, perTriWORepProb.get(perTriWORepProb.size()-1));
            getProbabilityOfValue(perTriWORepProb.get(perTriWORepProb.size()-1));
            writer.writeValue("Probability of Value wo Rep: " +  perTriWORepProb.get(perTriWORepProb.size()-1).getProbNoRep(), false);
        }

    }

    private void doAllNoReplacement(){
        ProbRes prob = getOverAllProb();
        setHyperStats(rServeConnector.doDHyper("1:"+prob.getCorrectCombinationsWORep(),
                prob.getCorrectCombinationsWORep(),
                prob.getTotalCombinationsWORep() - prob.getCorrectCombinationsWORep(),
                numTrials));

        idealHyperProbElem = getHyperStats().getProb();

        writer.writeResult("Ideal Probability: \n ", false);
        writer.writeResult("Probability of any of the correct combination without Replacement: " + getOverAllProb().getProbNoRep(), false);
        writer.writeResult("Hypergeometric Distribution: " + getIdealHyperProbElem(), false);
        writer.writeResult("          Mean: " + hyperStats.getMean() + " || Variance: " + hyperStats.getVariance() + " || SD: " + hyperStats.getSd(), false);

    }

    private void doAllReplacement(){
        ProbRes prob = getOverAllProb();

        setBinomStats(rServeConnector.doDBinom("1:"+prob.getCorrectCombinationsWRep(), numTrials, prob.getProbRep()));
        setNbinomStats(rServeConnector.doDNBinom("1:"+(numTrials-1),
                1+"",
                prob.getProbRep()));

        idealBinomProbElem = getBinomStats().getProb();
        idealNBinomProbElem = getNbinomStats().getProb();

        writer.writeResult("Ideal Probability: \n ", false);
        writer.writeResult("Probability of any of the correct combination with Replacement: " + getOverAllProb().getProbRep(), false);
        writer.writeResult("Binomial Distribution: " + getIdealBinomProbElem(), false);
        writer.writeResult("          Mean: " + binomStats.getMean() + " || Variance: " + binomStats.getVariance() + " || SD: " + binomStats.getSd(), false);
        writer.writeResult("Neg. Binomial Distribution: " + getIdealNBinomProbElem(), false);
        writer.writeResult("          Mean: " + nbinomStats.getMean() + " || Variance: " + nbinomStats.getVariance() + " || SD: " + nbinomStats.getSd(), false);

    }

    private void graphIdealProbabilitiesWRep(){
        ProbRes prob = getOverAllProb();
        rServeConnector.graphDBinom("0:"+numTrials, ""+numTrials, (double)prob.getProbRep(), "dbinom_graph");
        rServeConnector.graphDNBinom("1:"+(numTrials-1), 1+"", (double)prob.getProbRep(), "dnbinom_graph");

    }

    private void graphIdealProbabilitiesWORep(){
        ProbRes prob = getOverAllProb();
        rServeConnector.graphDHyper("1:"+prob.getCorrectCombinationsWORep(),
                ""+prob.getCorrectCombinationsWORep(),
                ""+ (prob.getTotalCombinationsWORep()-prob.getCorrectCombinationsWORep()),
                numTrials+"",
                "hyper_graph");
    }

    private void graphAllNoRep(){
        rServeConnector.graphValuesHist(cardHandNoRepValues,"Histogram of Actual Results (No Repetitions)", "actualres_hist");
        rServeConnector.graphValuesScatterPlot(cardHandNoRepValues,"Scatterplot of Actual Results (No Repetitions)", "actualres_plot");
        //rServeConnector.graphValuesLineGraph(cardHandNoRepValues,"Line Graph of Actual Results (No Repetitions)" );
    }

    private void graphAllRep(){
        rServeConnector.graphValuesHist(cardHandRepValues,"Histogram of Actual Results (Repetitions)", "actualres_hist");
        rServeConnector.graphValuesScatterPlot(cardHandRepValues,"Scatterplot of Actual Results (Repetitions)", "actualres_plot");
     //   rServeConnector.graphValuesLineGraph(cardHandRepValues,"Line Graph of Actual Results (Repetitions)" );
    }

    private void graphActualProbabilitiesNoRep(){
        ArrayList<Double> probsWORep = new ArrayList<Double>();
        for(int i=0; i<perTriWORepProb.size(); i++){
            probsWORep.add((double)perTriWORepProb.get(i).getProbNoRep());
        }
        System.out.println("Actual Probability of Actual Values: " + probsWORep);

        rServeConnector.graphProbHist(probsWORep,"Histogram of Actual Probabilities (No Repetitions)", "actualprob_hist");

    }

    private void graphActualProbabilitiesRep(){
        ArrayList<Double> probsWRep = new ArrayList<Double>();
        for(int i=0; i<perTriWRepProb.size(); i++){
            probsWRep.add((double)perTriWRepProb.get(i).getProbRep());
        }

        System.out.println("Actual Probability of Actual Values: " + probsWRep);
        rServeConnector.graphProbHist(probsWRep,"Histogram of Actual Probabilities (Repetitions)", "actualprob_hist");
    }

    private void graphActualVsIdeal() {
        if (withReplacement){
            rServeConnector.graphActualVsIdeal(actualProbability, getOverAllProb().getProbRep(), "Actual vs Ideal Probability of Success W Repetition",
                    "Probability", "","actualvsideal");
            rServeConnector.graphActualVsIdeal(actualProbability, idealBinomProbElem, "Actual Probability vs Ideal Binomial Distribution",
                    "Probability", "", "actualvsideal_binom");
            rServeConnector.graphActualVsIdeal(actualProbability, idealNBinomProbElem, "Actual Probability vs Ideal Neg. Binomial Distribution",
                    "Probability", "","actualvsideal_nbinom");
        }else {
            rServeConnector.graphActualVsIdeal(actualProbability, getOverAllProb().getProbNoRep(), "Actual vs Ideal Probability of Success WO Repetition",
                    "Probability", "", "actualvsideal");
            rServeConnector.graphActualVsIdeal(actualProbability, idealHyperProbElem, "Actual Probability vs Ideal HyperGeometric Distribution",
                    "Probability", "", "actualvsideal_hyper");
        }
    }


    public void getProbabilityOfValue(ProbRes prob){
        prob.setProbNoRep( ((float)1/(float)(prob.getTotalCombinationsWORep())) * (float)prob.getCorrectCombinationsWORep() );
        prob.setProbRep( ((float)1/(float)(prob.getTotalCombinationsWRep())) * (float)prob.getCorrectCombinationsWRep() );

        if(prob != getOverAllProb())
            return;

        System.out.println("Correct Combination wo Rep: " + prob.getCorrectCombinationsWORep());
        System.out.println("Correct Combination w Rep: " + prob.getCorrectCombinationsWRep());
        System.out.println("Total Combination wo Rep: " + prob.getTotalCombinationsWORep());
        System.out.println("Total Combination w Rep: " + prob.getTotalCombinationsWRep());
        System.out.println("Prob wo Rep: " + prob.getProbNoRep());
        System.out.println("Prob w Rep: " + prob.getProbRep());
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
        combinationUtil(arr, data, 0, n+r-2, 0, r, prob);
    }

    /* arr[]  ---> Input Array
       data[] ---> Temporary array to store current combination
       start & end ---> Staring and Ending indexes in arr[]
       index  ---> Current index in data[]
       r ---> Size of a combination to be printed */
    public void combinationUtil(int arr[], int data[], int start, int end,
                                int index, int r, ProbRes prob)
    {
        boolean woRepValid = true;
        int hasSameSuitCtr = 0;
        // Current combination is ready to be printed, print it
        if (index >= r)
        {
            int[] temp = data;
            int sameNumCtr = 0;
            int sum = 0;
            String combi = "";
            for (int j=0; j<r; j++){
                combi += data[j] + "    ";
                sum += temp[j];
            }

            boolean hasSameSuit = false;
            for(int j=0; j<r; j++){
                if(j+1 < r)
                    if(data[j] == data[j+1]){
                        sameNumCtr ++;
                        hasSameSuit = true;
                    }
            }

            //combiforNoDuplicates = rServeConnector.getPower(4, numCards);
            //combiforDuplicates = rServeConnector.doCombinationsNoRep(4, numCards);

            if(!hasSameSuit){
                prob.setTotalCombinationsWRep(prob.getTotalCombinationsWRep() + combiforNoDuplicates);
                if(sameNumCtr <= 3) //is the card combination valid for without replacement
                    prob.setTotalCombinationsWORep(prob.getTotalCombinationsWORep() + combiforNoDuplicates);
            }

            else if(hasSameSuit){
                prob.setTotalCombinationsWRep(prob.getTotalCombinationsWRep() +  combiforDuplicates);
                if(sameNumCtr <= 3)  //is the card combination valid for without replacement
                    prob.setTotalCombinationsWORep(prob.getTotalCombinationsWORep() + combiforDuplicates);
            }


            if(sum == prob.getDesiredValue()){
                if(prob == getOverAllProb())
                System.out.println(combi + " | " + hasSameSuit + " | " + sameNumCtr);
                if(!hasSameSuit){
                    prob.setCorrectCombinationsWRep(prob.getCorrectCombinationsWRep() + combiforNoDuplicates);
                    if(sameNumCtr <= 3) //is the card combination valid for without replacement
                        prob.setCorrectCombinationsWORep(prob.getCorrectCombinationsWORep() + combiforNoDuplicates);
                }

                else if(hasSameSuit){
                    prob.setCorrectCombinationsWRep(prob.getCorrectCombinationsWRep() +  combiforDuplicates);
                    if(sameNumCtr <= 3)  //is the card combination valid for without replacement
                        prob.setCorrectCombinationsWORep(prob.getCorrectCombinationsWORep() + combiforDuplicates);
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

    public int getFactorial(int n, int r){
        int ans = 1;
        for(int i=0; i<r; i++){
            ans *= n;
        }
        if(r == 5)
            ans -= 4;
        return ans;
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

    public ProbRes getOverAllProb() {
        return overAllProb;
    }

    public void setOverAllProb(ProbRes overAllProb) {
        this.overAllProb = overAllProb;
    }
}
