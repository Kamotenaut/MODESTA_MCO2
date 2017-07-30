/**
 * Created by Brandon on 7/29/2017.
 */
public class Card {

    public final static int
            SPADES = 0,
            HEARTS = 1,
            DIAMONDS = 2,
            CLUBS = 3,
            ACE = 1,
            JACK = 10,
            QUEEN = 10,
            KING = 10;

    private final int suit; // spade, club, heart, diamonds
    private final int value;

    public Card(int value, int suit) {
        this.value = value;
        this.suit = suit;
    }

    public int getSuit() {
        return suit;
    }

    public int getValue() {
        return value;
    }

    public String getSuitAsString() {
        switch ( suit ) {
            case SPADES:   return "Spades";
            case HEARTS:   return "Hearts";
            case DIAMONDS: return "Diamonds";
            case CLUBS:    return "Clubs";
            default:       return "??";
        }
    }

    public String getValueAsString() {
        switch ( value ) {
            case 1:   return "Ace";
            case 2:   return "2";
            case 3:   return "3";
            case 4:   return "4";
            case 5:   return "5";
            case 6:   return "6";
            case 7:   return "7";
            case 8:   return "8";
            case 9:   return "9";
            case 10:  return "10";
            case 11:  return "Jack";
            case 12:  return "Queen";
            case 13:  return "King";
            default:  return "??";
        }
    }

    public String toString() {
        return getValueAsString() + " of " + getSuitAsString();
    }
}