import java.util.ArrayList;

public class ProbRes {

    private float probNoRep = 0;
    private float probRep = 0;
    private int correctCombinationsWRep = 0;
    private int correctCombinationsWORep = 0;
    private int totalCombinationsWORep = 0;
    private int totalCombinationsWRep = 0;
    private int desiredValue = 0;

    private int allCombinationValuesWRep = 0;
    private int allCombinationValuesWORep = 0;
    private int correctCombinationValuesWRep = 0;
    private int correctCombinationValuesWORep = 0;


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

    public int getDesiredValue() {
        return desiredValue;
    }

    public void setDesiredValue(int desiredValue) {
        this.desiredValue = desiredValue;
    }

    public int getTotalCombinationsWORep() {
        return totalCombinationsWORep;
    }

    public void setTotalCombinationsWORep(int totalCombinationsWORep) {
        this.totalCombinationsWORep = totalCombinationsWORep;
    }

    public int getTotalCombinationsWRep() {
        return totalCombinationsWRep;
    }

    public void setTotalCombinationsWRep(int totalCombinationsWRep) {
        this.totalCombinationsWRep = totalCombinationsWRep;
    }

    public int getAllCombinationValuesWRep() {
        return allCombinationValuesWRep;
    }

    public void setAllCombinationValuesWRep(int allCombinationValuesWRep) {
        this.allCombinationValuesWRep = allCombinationValuesWRep;
    }

    public int getAllCombinationValuesWORep() {
        return allCombinationValuesWORep;
    }

    public void setAllCombinationValuesWORep(int allCombinationValuesWORep) {
        this.allCombinationValuesWORep = allCombinationValuesWORep;
    }

    public int getCorrectCombinationValuesWRep() {
        return correctCombinationValuesWRep;
    }

    public void setCorrectCombinationValuesWRep(int correctCombinationValuesWRep) {
        this.correctCombinationValuesWRep = correctCombinationValuesWRep;
    }

    public int getCorrectCombinationValuesWORep() {
        return correctCombinationValuesWORep;
    }

    public void setCorrectCombinationValuesWORep(int correctCombinationValuesWORep) {
        this.correctCombinationValuesWORep = correctCombinationValuesWORep;
    }
}
