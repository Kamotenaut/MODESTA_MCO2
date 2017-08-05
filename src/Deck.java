/**
 * Created by Brandon on 7/29/2017.
 */
public class Deck {

    private Card[] deck;
    private int cardsUsed;

    public Deck() {
        deck = new Card[52];
        int cardCount = 0;
        for ( int suit = 0; suit <= 3; suit++ ) {
            for ( int value = 1; value <= 13; value++ ) {
                deck[cardCount] = new Card(value,suit);
                cardCount++;
            }
        }
        cardsUsed = 0;
    }

    public void shuffle() { //shuffles all the cards in the deck
        for ( int i = 51; i > 0; i-- ) {
            int rand = (int)(Math.random()*(i+1));
            Card temp = deck[i];
            deck[i] = deck[rand];
            deck[rand] = temp;
        }
        cardsUsed = 0;
    }

    public int cardsLeft() {
        return 52 - cardsUsed;
    }

    public Card drawCard() { //without replacement
        if (cardsUsed == 52)
            shuffle();
        cardsUsed++;
//        System.out.println("[Repetition Deck]: " + deck[cardsUsed-1].toString());
        return deck[cardsUsed - 1];
    }

    public Card drawCardReplace() {
        Card temp = deck[0];
        shuffle();
//        System.out.println("[No Repetition Deck]: " +temp.toString());
        return temp;
    }

    public void addCard(Card card) {
        // shift the cards down, put card at the bottom
        for(int i=cardsUsed;i<52;i++)
        {
            deck[i-1] = deck[i];
        }
        deck[51] = card;
        cardsUsed--;
    }
}