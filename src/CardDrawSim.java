import org.apache.commons.math3.distribution.BinomialDistribution;
import org.apache.commons.math3.distribution.HypergeometricDistribution;
import umontreal.ssj.probdist.BinomialDist;
import umontreal.ssj.probdist.HypergeometricDist;
import umontreal.ssj.probdist.NegativeBinomialDist;
import umontreal.ssj.probdistmulti.MultinomialDist;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Brandon on 7/30/2017.
 */
public class CardDrawSim {
    private int numCards, numTrials, currTrial;
    private ArrayList<Card> cardHandRep, cardHandNoRep;
    private Deck deckRep, deckNoRep;

    public CardDrawSim(){
        Scanner sc = new Scanner(System.in);
        cardHandRep = new ArrayList<Card>();
        cardHandNoRep = new ArrayList<Card>();
        deckRep = new Deck();
        deckRep.shuffle();
        deckNoRep = new Deck();
        deckNoRep.shuffle();
        currTrial = 0;

        System.out.println("Input number of cards on hand: ");
        numCards = sc.nextInt();

        System.out.println("Input number of trials: ");
        numTrials = sc.nextInt();

        sc.close();
        run();
    }

    public void run(){
        while(currTrial<numTrials){
            System.out.println("Trial #" + (currTrial+1));
            DrawCards();
            currTrial++;
        }
    }
    public void DrawCards(){
        for(int i = 0; i<numCards; i++ ){
            //cardHandNoRep.add(deckNoRep.drawCard());
            cardHandRep.add(deckRep.drawCardReplace());
        }
    }

    //HypergeometricDist or HypergeometricDistribution (Apache)
    //BinomialDist or BinomialDistribution (Apache)
    //MultinomialDist
    //NegativeBinomialDist
}
