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
            System.out.println("[TESTING] The mean of given vector is=" + mean);
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

    public void graphValuesHist(ArrayList<Integer> values, String title){
        String code = values.get(0).toString();
        for(int i = 1; i < values.size(); i++){
            code += ("," + values.get(i).toString());
        }
        System.out.println();
        System.out.println("hist(c("+code+"),main='"+title+"');");
        try {
            connection = new RConnection();
            connection.eval("try(png('" + USER_DIR +"image" + Integer.toString(imageCount)+".png'))");
            connection.eval("hist(c("+code+"),main='"+title+"',ylab='Frequency', xlab='Possible Total Values')");
            connection.eval("dev.off()");
        } catch (RserveException e) {
            e.printStackTrace();
        }finally{
            connection.close();
        }
        imageCount++;
    }

    public void graphValuesScatterPlot(ArrayList<Integer> values, String title){
        connection = null;
        String code = values.get(0).toString();
        for(int i = 1; i < values.size(); i++){
            code += ("," + values.get(i).toString());
        }
        System.out.println();
        System.out.println("hist(c("+code+"),main='"+title+"');");
        try {
            connection = new RConnection();
            connection.eval("try(png('" + USER_DIR +"image" + Integer.toString(imageCount)+".png'))");
            connection.eval("plot(c("+code+"),main='"+title+"',ylab='Frequency', xlab='Possible Total Values')");
            connection.eval("dev.off()");
        } catch (RserveException e) {
            e.printStackTrace();
        }finally{
            connection.close();
        }
        imageCount++;
    }

    public void graphValuesLineGraph(ArrayList<Integer> values, String title){
        connection = null;
        String code = values.get(0).toString();
        for(int i = 1; i < values.size(); i++){
            code += ("," + values.get(i).toString());
        }
        System.out.println();
        System.out.println("plot(c("+code+"),main='"+title+"');");
        try {
            connection = new RConnection();
            connection.eval("try(png('" + USER_DIR +"image" + Integer.toString(imageCount)+".png'))");
            connection.eval("plot(c("+code+"),main='"+title+"',ylab='Frequency', xlab='Possible Total Values',type='o'))");
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
            sum = connection.eval("comb_with_replacement("+n+","+r+")").asInteger();
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
    public double doDBinom(int n, int size, double prob){
        connection = null;
        double result = 0;
        try {
            connection = new RConnection();
            result = connection.eval("dbinom("+n+",size="+size+",prob="+prob+")").asDouble();
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
            result = connection.eval("rbinom("+n+",size="+size+",prob="+prob+")").asList();
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

    public double doDNBinom(int n, int size, double prob) {
        connection = null;
        double result = 0;
        try {
            connection = new RConnection();
            result = connection.eval("dnbinom(" + n + ",size=" + size + ",prob=" + prob + ")").asDouble();
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

    public double doDHyper(int n, int success, int failure, int size){
        connection = null;
        double result = 0;
        try {
            connection = new RConnection();
            result = connection.eval("dhyper(" + n + ","+ success + ","+ failure +"," + size + ")").asDouble();
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

    public double doDMultinom(ArrayList<Integer> input, ArrayList<Double> prob){
        connection = null;
        double result = 0;
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
            result = connection.eval("dmultinom(" + inputCode + ","+probCode+")").asDouble();
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
}



