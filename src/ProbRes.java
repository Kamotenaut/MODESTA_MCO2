public class ProbRes {

    private float probNoRep = 0;
    private float probRep = 0;
    private int correctCombinationsWRep = 0;
    private int correctCombinationsWORep = 0;
    private int totalCombinations = 0;
    private int desiredValue = 0;

    public ProbRes(int desiredValue){
        this.desiredValue = desiredValue;

    }

    public float getProbNoRep() {
        return probNoRep;
    }

    public void setProbNoRep(float probNoRep) {
        this.probNoRep = probNoRep;
    }

    public float getProbRep() {
        return probRep;
    }

    public void setProbRep(float probRep) {
        this.probRep = probRep;
    }

    public int getCorrectCombinationsWRep() {
        return correctCombinationsWRep;
    }

    public void setCorrectCombinationsWRep(int correctCombinationsWRep) {
        this.correctCombinationsWRep = correctCombinationsWRep;
    }

    public int getCorrectCombinationsWORep() {
        return correctCombinationsWORep;
    }

    public void setCorrectCombinationsWORep(int correctCombinationsWORep) {
        this.correctCombinationsWORep = correctCombinationsWORep;
    }

    public int getTotalCombinations() {
        return totalCombinations;
    }

    public void setTotalCombinations(int totalCombinations) {
        this.totalCombinations = totalCombinations;
    }

    public int getDesiredValue() {
        return desiredValue;
    }

    public void setDesiredValue(int desiredValue) {
        this.desiredValue = desiredValue;
    }
}
