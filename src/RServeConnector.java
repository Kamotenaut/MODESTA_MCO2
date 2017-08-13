import com.sun.jmx.snmp.Enumerated;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Brandon on 8/5/2017.
 */
public class RServeConnector {

    //TXTWriter writer = new TXTWriter();
    String filename = "log.txt";

    private RConnection connection = null;
    private int imageCount = 0;
    private final String PERM_WITHO_REP = "perm_without_replacement <- function(n, r){return(factorial(n)/factorial(n - r))}";
    private final String COMBI_WITH_REP = "comb_with_replacement <- function(n, r){return( factorial(n + r - 1)/(factorial(r) * factorial(n - 1)))}";
    private String USER_DIR = System.getProperty("user.dir") + "//src//images//";

    public RServeConnector(){
        connection = null;
        try {
            connection = new RConnection();
            String vector = "c(1,2,3,4)";
            connection.eval("meanVal=mean(" + vector + ")");
            double mean = connection.eval("meanVal").asDouble();
            //System.out.println("[TESTING] The mean of given vector is=" + mean);
            //writer.write("[TESTING] The mean of given vector is=" + mean);
            connection.eval(COMBI_WITH_REP);
            connection.eval(PERM_WITHO_REP);
        } catch (RserveException e) {
            e.printStackTrace();
        } catch (REXPMismatchException e) {
            e.printStackTrace();
        }finally{
            connection.close();
        }
        USER_DIR = USER_DIR.replace("\\", "\\\\");
    }

    public int getPower(int n, int r){
        int ans =  1;
        connection = null;
        try {
            connection = new RConnection();
            ans = connection.eval(n + "^" + r).asInteger();
            if(r >= 5)
                ans -= 4;
        } catch (RserveException e) {
            e.printStackTrace();
        } catch (REXPMismatchException e) {
            e.printStackTrace();
        } finally{
            connection.close();
        }
        return ans;
    }

    public void graphValuesHist(ArrayList<Integer> values, String title, String fileName){
        String code = values.get(0).toString();
        for(int i = 1; i < values.size(); i++){
            code += ("," + values.get(i).toString());
        }
        System.out.println();
        System.out.println("hist(c("+code+"),main='"+title+"');");
        try {
            connection = new RConnection();
            connection.eval("try(png('" + USER_DIR + fileName +".png'))");
            connection.eval("hist(c("+code+"),main='"+title+"',ylab='Frequency', xlab='Possible Total Values', col='red')");
            connection.eval("dev.off()");
        } catch (RserveException e) {
            e.printStackTrace();
        }finally{
            connection.close();
        }
        imageCount++;
    }

    public void graphProbHist(ArrayList<Double> prob, String title, String fileName){
        String code = prob.get(0).toString();
        for(int i = 1; i < prob.size(); i++){
            code += ("," + prob.get(i).toString());
        }
        System.out.println();
        System.out.println("barplot(c("+code+"),main='"+title+"', ylab='Probability', xlab='Possible Total Values', col='red')");
        try {
            connection = new RConnection();
            connection.eval("try(png('" + USER_DIR + fileName +".png'))");
            connection.eval("barplot(c("+code+"),main='"+title+"',ylab='Probability', xlab='Possible Total Values', col='red')");
            connection.eval("dev.off()");
        } catch (RserveException e) {
            e.printStackTrace();
        }finally{
            connection.close();
        }
        imageCount++;
    }

    public void graphValuesScatterPlot(ArrayList<Integer> values, String title, String fileName){
        connection = null;
        String code = values.get(0).toString();
        for(int i = 1; i < values.size(); i++){
            code += ("," + values.get(i).toString());
        }
        System.out.println();
        System.out.println("hist(c("+code+"),main='"+title+"');");
        try {
            connection = new RConnection();
            connection.eval("try(png('" + USER_DIR + fileName +".png'))");
            connection.eval("plot(c("+code+"),main='"+title+"',ylab='Total Value', xlab='Trial Number', col='blue')");
            connection.eval("dev.off()");
        } catch (RserveException e) {
            e.printStackTrace();
        }finally{
            connection.close();
        }
        imageCount++;
    }

    public void graphProbScatterPlot(ArrayList<Double> values, String title, String fileName){
        connection = null;
        String code = values.get(0).toString();
        for(int i = 1; i < values.size(); i++){
            code += ("," + values.get(i).toString());
        }
        System.out.println();
        System.out.println("hist(c("+code+"),main='"+title+"');");
        try {
            connection = new RConnection();
            connection.eval("try(png('" + USER_DIR + fileName +".png'))");
            connection.eval("plot(c("+code+"),main='"+title+"',ylab='Probability', xlab='Frequency', col='green')");
            connection.eval("dev.off()");
        } catch (RserveException e) {
            e.printStackTrace();
        }finally{
            connection.close();
        }
        imageCount++;
    }


    public void graphValuesLineGraph(ArrayList<Integer> values, String title, String fileName){
        connection = null;
        String code = values.get(0).toString();
        for(int i = 1; i < values.size(); i++){
            code += ("," + values.get(i).toString());
        }
        System.out.println();
        System.out.println("plot(c("+code+"),main='"+title+"');");
        try {
            connection = new RConnection();
            connection.eval("try(png('" + USER_DIR + fileName +".png'))");
            connection.eval("plot(c("+code+"),main='"+title+"',ylab='Probability', xlab='Frequency',type='o'))");
            connection.eval("dev.off()");
        } catch (RserveException e) {
            e.printStackTrace();
        }finally{
            connection.close();
        }
        imageCount++;
    }

    public void graphProbLineGraph(ArrayList<Double> values, String title, String fileName){
        connection = null;
        String code = values.get(0).toString();
        for(int i = 1; i < values.size(); i++){
            code += ("," + values.get(i).toString());
        }
        System.out.println();
        System.out.println("plot(c("+code+"),main='"+title+"');");
        try {
            connection = new RConnection();
            connection.eval("try(png('" + USER_DIR + fileName +".png'))");
            connection.eval("plot(c("+code+"),main='"+title+"',ylab='Probability', xlab='Frequency',type='o'))");
            connection.eval("dev.off()");
        } catch (RserveException e) {
            e.printStackTrace();
        }finally{
            connection.close();
        }
        imageCount++;
    }

    public void graphDBinom(String n, String size, Double prob, String fileName){
        connection = null;

        String title = "Ideal Binomial Distribution";
        String graph = "barplot(dbinom("+n+","+size+","+prob+"), col=rainbow(10, 0.5), main=\""+title+"\")";

        System.out.println();
        System.out.println(graph);
        try {
            connection = new RConnection();
            connection.eval("try(png('" + USER_DIR + fileName +".png'))");
            connection.eval(graph);
            connection.eval("dev.off()");
        } catch (RserveException e) {
            e.printStackTrace();
        }finally{
            connection.close();
        }
        imageCount++;
    }

    public void graphDNBinom(String n, String size, Double prob, String fileName){
        connection = null;

        String title = "Ideal Neg. Binomial Distribution";
        String graph = "barplot(dnbinom("+n+","+size+","+prob+"), col=rainbow(10, 0.5), main=\""+title+"\")";

        System.out.println();
        System.out.println(graph);
        try {
            connection = new RConnection();
            connection.eval("try(png('" + USER_DIR + fileName +".png'))");
            connection.eval(graph);
            connection.eval("dev.off()");
        } catch (RserveException e) {
            e.printStackTrace();
        }finally{
            connection.close();
        }
        imageCount++;
    }

    public void graphDHyper(String x, String m, String n, String k, String fileName){
        connection = null;

        String title = "Ideal Hypergeometric Distribution";
        String graph = "barplot(dhyper("+x+","+m+","+n+","+k+"), col=rainbow(10, 0.5), main=\""+title+"\")";

        System.out.println();
        System.out.println(graph);
        try {
            connection = new RConnection();
            connection.eval("try(png('" + USER_DIR + fileName +".png'))");
            connection.eval(graph);
            connection.eval("dev.off()");
        } catch (RserveException e) {
            e.printStackTrace();
        }finally{
            connection.close();
        }
        imageCount++;
    }

    public void graphDMultinom(ArrayList<Integer> x, ArrayList<Double> prob, int numTrials, String fileName){
        connection = null;

        String xString = x.get(0).toString();
        String probString = prob.get(0).toString();
        for(int i = 1; i < x.size(); i++){
            xString += ("," + x.get(i).toString());
            probString += (", " + prob.get(i).toString());
        }

       // String graph = "barplot(dmultinom(c("+xString+"),prob=c("+probString+")), col=rainbow(10, 0.5))";
        String title = "Ideal Multinomial Distribution";
        String graph = "barplot(rmultinom(" + numTrials + ", size=1, prob=c(" + probString + "))[1,], col=rainbow(2, 0.2), main='"+title+"')";


        System.out.println();
        System.out.println(graph);
        try {
            connection = new RConnection();
            connection.eval("try(png('" + USER_DIR + fileName +".png'))");
            connection.eval(graph);
            connection.eval("dev.off()");
        } catch (RserveException e) {
            e.printStackTrace();
        }finally{
            connection.close();
        }
        imageCount++;
    }


    public int doCombinationsRep(int n, int r){
        int sum = 0;
        connection = null;
        try {
            connection = new RConnection();
            sum = connection.eval("choose("+(n+r-1)+","+r+")").asInteger();
            //sum = connection.eval("comb_with_replacement("+n+","+r+")").asInteger();
        } catch (RserveException e) {
            e.printStackTrace();
        } catch (REXPMismatchException e) {
            e.printStackTrace();
        } finally{
            connection.close();
        }
        imageCount++;
        return sum;
    }

    public int doCombinationsNoRep(int n, int r){
        int sum = 0;
        connection = null;
        try {
            connection = new RConnection();
            sum = connection.eval("choose("+n+","+r+")").asInteger();
        } catch (RserveException e) {
            e.printStackTrace();
        } catch (REXPMismatchException e) {
            e.printStackTrace();
        } finally{
            connection.close();
        }
        imageCount++;
        return sum;
    }

    public int doPermutationsNoRep(int n, int r){
        int sum = 0;
        connection = null;
        try {
            connection = new RConnection();
            sum = connection.eval("perm_without_replacement("+n+","+r+")").asInteger();
        } catch (RserveException e) {
            e.printStackTrace();
        } catch (REXPMismatchException e) {
            e.printStackTrace();
        } finally{
            connection.close();
        }
        imageCount++;
        return sum;
    }

    public int doPermutationsRep(int n, int r){
        int sum = 0;
        connection = null;
        try {
            connection = new RConnection();
            sum = connection.eval("n^r").asInteger();
        } catch (RserveException e) {
            e.printStackTrace();
        } catch (REXPMismatchException e) {
            e.printStackTrace();
        } finally{
            connection.close();
        }
        imageCount++;
        return sum;
    }


    public Stats doDBinom(String n, int size, double prob){
        connection = null;
        Stats stats = new Stats();
        double result = 0;
        try {
            connection = new RConnection();
            String code = "round(sum(dbinom("+n+",size="+size+",prob="+prob+")),4)";
            System.out.println(code);
            result = connection.eval(code).asDouble();
            stats.setProb(result);
            stats.setMean(connection.eval("round(("+n+"*"+prob+"),4)").asDouble());
            stats.setVariance(connection.eval("round(("+n+"*"+prob+"*"+(1-prob)+"),4)").asDouble());
            stats.setSd(connection.eval("round(sqrt("+n+"*"+prob+"*"+(1-prob)+"),4)").asDouble());

        } catch (RserveException e) {
            e.printStackTrace();
        } catch (REXPMismatchException e) {
            e.printStackTrace();
        } finally{
            connection.close();
        }
        imageCount++;
        return stats;
    }

    public double doDBinom(int n, int size, double prob){
        connection = null;
        double result = 0;
        try {
            connection = new RConnection();
            String code = "round(sum(dbinom("+n+",size="+size+",prob="+prob+")),4)";
            System.out.println(code);
            result = connection.eval(code).asDouble();
        } catch (RserveException e) {
            e.printStackTrace();
        } catch (REXPMismatchException e) {
            e.printStackTrace();
        } finally{
            connection.close();
        }
        imageCount++;
        return result;
    }

    public List<Integer> doRBinom(int n, int size, double prob){
        connection = null;
        List<Integer> result = new ArrayList<Integer>();
        try {
            connection = new RConnection();
            String code = "round(rbinom("+n+",size="+size+",prob="+prob+"),4";
            System.out.println(code);
            result = connection.eval(code).asList();
        } catch (RserveException e) {
            e.printStackTrace();
        } catch (REXPMismatchException e) {
            e.printStackTrace();
        } finally{
            connection.close();
        }
        imageCount++;
        return result;
    }

    public Stats doDNBinom(String n, String size, double prob) {
        connection = null;
        double result = 0;
        Stats stats = new Stats();
        try {
            connection = new RConnection();

            String code = "round(sum(dnbinom(" + n + ",size=" + size + ",prob=" + prob + ")),4)";
            System.out.println(code);
            result = connection.eval(code).asDouble();

            stats.setProb(result);
            stats.setMean(connection.eval("round(("+n+"/"+prob+"),4)").asDouble());
            stats.setVariance(connection.eval("round(("+n+"*"+(1-prob)+")/("+prob+"^2),4)").asDouble());
            stats.setSd(connection.eval("round(sqrt(("+n+"*"+(1-prob)+")/("+prob+"^2)),4)").asDouble());
        } catch (RserveException e) {
            e.printStackTrace();
        } catch (REXPMismatchException e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
        imageCount++;
        return stats;
    }


    public double doDNBinom(int n, int size, double prob) {
        connection = null;
        double result = 0;
        try {
            connection = new RConnection();
            String code = "round(sum(dnbinom(" + n + ",size=" + size + ",prob=" + prob + ")),4)";
            System.out.println(code);
            result = connection.eval(code).asDouble();
        } catch (RserveException e) {
            e.printStackTrace();
        } catch (REXPMismatchException e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
        imageCount++;
        return result;
    }


    public Stats doDHyper(String n, int success, int failure, int size){
        Stats stats = new Stats();
        connection = null;
        double result = 0;
        try {
            connection = new RConnection();
            String code = "round(sum(dhyper(" + n + ","+ success + ","+ failure +"," + size + ")),4)";
            System.out.println(code);
            result = connection.eval(code).asDouble();
            stats.setProb(result);
            stats.setMean(connection.eval("round(mean(dhyper(" + n + ","+ success + ","+ failure +"," + size + ")),4)").asDouble());
            stats.setVariance(connection.eval("round(var(dhyper(" + n + ","+ success + ","+ failure +"," + size + ")),4)").asDouble());
            stats.setSd(connection.eval("round(sd(dhyper(" + n + ","+ success + ","+ failure +"," + size + ")),4)").asDouble());
        } catch (RserveException e) {
            e.printStackTrace();
        } catch (REXPMismatchException e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
        imageCount++;
        return stats;
    }

    public double doDHyper(int n, int success, int failure, int size){
        connection = null;
        double result = 0;
        try {
            connection = new RConnection();
            String code = "round(sum(dhyper(" + n + ","+ success + ","+ failure +"," + size + ")),4)";
            System.out.println(code);
            result = connection.eval(code).asDouble();
        } catch (RserveException e) {
            e.printStackTrace();
        } catch (REXPMismatchException e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
        imageCount++;
        return result;
    }

    public Stats doDMultinom(ArrayList<Integer> input, ArrayList<Double> prob){
        connection = null;
        double result = 0;
        Stats stats = new Stats();
        String inputCode = "c(", probCode = "c(";
        for(int i = 0; i < input.size(); i++){
            inputCode += input.get(i);
            probCode += prob.get(i);
            if(i<input.size()-1) {
                inputCode += ",";
                probCode += ",";
            } else {
                inputCode += ")";
                probCode += ")";
            }
        }
        try {
            connection = new RConnection();
            String code = "round(dmultinom(" + inputCode + ", prob="+probCode+"),4)";
            System.out.println(code);
            result = connection.eval(code).asDouble();

            stats.setProb(result);
            stats.setMean(connection.eval("round(mean(dmultinom(" + inputCode + ", prob="+probCode+")),4)").asDouble());
            stats.setVariance(connection.eval("round(var(dmultinom(" + inputCode + ", prob="+probCode+")),4)").asDouble());
            stats.setSd(connection.eval("round(sd(dmultinom(" + inputCode + ", prob="+probCode+")),4)").asDouble());

        } catch (RserveException e) {
            e.printStackTrace();
        } catch (REXPMismatchException e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
        imageCount++;
        return stats;
    }
    public void graphActualVsIdeal(double actual, double ideal, String title, String ylab, String xlab, String fileName){
        String graph =  "barplot(c(" + actual + "," + ideal + "), main='" + title + "', beside = TRUE, ylim = c(0, 1), names.arg = c(\"Actual\", \"Ideal\"), col=rainbow(2, 0.5))";

        System.out.println();
        System.out.println(graph);
        try {
            connection = new RConnection();
            connection.eval("try(png('" + USER_DIR + fileName +".png'))");
            connection.eval(graph);
            connection.eval("dev.off()");
        } catch (RserveException e) {
            e.printStackTrace();
        }finally{
            connection.close();
        }
        imageCount++;
    }


}



