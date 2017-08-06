import java.util.ArrayList;

public class Probability {

    public void printCombinations(){
        int[] used = new int[11];
        System.out.println(getProbTest(21, 1, used));
    }

    public static long factorial(long number) {
        if (number <= 1) return 1;
        else return number * factorial(number - 1);
    }

    public float bcoef(int n, int k){
        return factorial(n) / (factorial(k) * factorial(n - k));
    }

    public int getProbTest(int limit, int start, int[] used){
        int res = 0;

        if (limit == 0) {
            ArrayList<Integer> combs = new ArrayList<>();
            for(int i=1; i<used.length; i++){
                combs.add((int)bcoef(i != 10 ? 4 : 16, used[i]));
                res = combs.get(0);
                for(int j=1; j<combs.size(); j++){
                    res *= combs.get(j);
                }

            }
            return res;
        }

        //Start iteration from lowest card to picked so far
        //So that we're only going to pick cards 3&7 order 3,7

        int index;
        int available;
        for(int i=start; i< Math.min(12, limit+1); i++){
            index = i != 11 ? i : 1;

            // There are 16 cards with value of 10 (T, J, Q, K) and 4 with every
            // other value
            available = index == 10 ? 16 : 4;
            if (used[index] < available) {
                // Mark the card used and go through combinations starting from
                // current card and limit lowered by the value
                used[index] += 1;
                res += getProbTest(limit - i, i, used);
                used[index] -= 1;
            }
        }
        return res;

    }

}
