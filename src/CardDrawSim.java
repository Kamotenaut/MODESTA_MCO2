import java.util.ArrayList;
import java.util.logging.FileHandler;

/**
 * Created by Brandon on 7/30/2017.
 */
public class CardDrawSim {
    private int numCards, numTrials, currTrial, userValue, corGuess;
    private ArrayList<Card> cardHandRep, cardHandNoRep;
    private ArrayList<Integer> cardHandRepValues, cardHandNoRepValues;
    private Deck deckRep, deckNoRep;
    private FileHandler fh;
    private RServeConnector rServeConnector;

    public CardDrawSim(int numTrials, int numCards, int userValue){
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

        rServeConnector = new RServeConnector();
        run();
    }

    public void run() {
        while (currTrial < numTrials) {
            System.out.println("Trial #" + (currTrial + 1));
            DrawCards();
            currTrial++;
        }
        int[] used = {0,0,0,0,0,0,0,0,0,0,0,0,0};
        /*rServeConnector.graphValuesHist(cardHandNoRepValues,"Histogram of Actual Results (No Repetitions)");
        rServeConnector.graphValuesHist(cardHandRepValues, "Histogram of Actual Results Repetitions");
        rServeConnector.graphValuesScatterPlot(cardHandNoRepValues,"Scatterplot of Actual Results (No Repetitions)" );
        rServeConnector.graphValuesScatterPlot(cardHandRepValues,"Scatterplot of Actual Results (Repetitions)" );*/
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
}
