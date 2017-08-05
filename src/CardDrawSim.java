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
        run();
    }

    public void run(){
        while(currTrial<numTrials){
            System.out.println("Trial #" + (currTrial+1));
            DrawCards();
            currTrial++;
        }
        rServeConnector.graphValuesHist(cardHandNoRepValues,"No Repetitions");
        rServeConnector.graphValuesHist(cardHandRepValues, "Repetitions");
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
