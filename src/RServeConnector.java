import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Brandon on 8/5/2017.
 */
public class RServeConnector {
    private RConnection connection = null;
    private int imageCount = 0;
    private  String COMBI_WITH_REP = "comb_with_replacement <- function(n, r){return( factorial(n + r - 1)/(factorial(r) * factorial(n - 1)))}";
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
            connection.eval("hist(c("+code+"),main='"+title+"',ylab='Total', xlab='Possible hotdog')");
            connection.eval("dev.off()");
        } catch (RserveException e) {
            e.printStackTrace();
        }finally{
            connection.close();
        }
        imageCount++;
    }
}



