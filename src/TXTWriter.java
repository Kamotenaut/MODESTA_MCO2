/**
 * Created by jasonsapdos on 05/08/2017.
 */
import java.io.*;

import javax.swing.JOptionPane;

public class TXTWriter
{
    private static String VALUE_FILE = "value_log.txt";
    private static String RESULT_FILE = "result_log.txt";

    private static String ALLCOMB_FILE = "all_combi.txt";
    private static String CORRECT_FILE = "corr_combi.txt";


    public void writeCorCombi(String printString, boolean restart){
        write(printString, CORRECT_FILE, restart);
    }

    public void writeAllCombi(String printString, boolean restart){
        write(printString, ALLCOMB_FILE,  restart);
    }

    public void writeResult(String printString, boolean restart){
        write(printString, RESULT_FILE, restart);
    }

    public void writeValue(String printString, boolean restart){
        write(printString, VALUE_FILE, restart);
    }

    private void write(String printString, String fileName, boolean restart)
    {
        //FileWriter fileWriter = null;
        BufferedWriter fileWriter = null;

        try
        {
            fileWriter = new BufferedWriter(new FileWriter(fileName, true));
            //fileWriter = new FileWriter(fileName, true);

            BufferedReader br = null;
            br = new BufferedReader(new FileReader(fileName));
            String line;

            while((line = br.readLine()) != null) {
                //System.out.println("Checking CSV line...");
            }

            if(!restart) {
                fileWriter.append(printString);
                fileWriter.newLine();
            }
            else{
                fileWriter.write("");
            }

        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(null, "There is an error in saving.");
            e.printStackTrace();
        }
        finally
        {
            try
            {
                // Closes the TXTWriter
                if (fileWriter == null)
                    return;

                fileWriter.flush();
                fileWriter.close();
            }
            catch (IOException e)
            {
                JOptionPane.showMessageDialog(null,
                        "There is an error in closing CSV Writer.");
                e.printStackTrace();
            }
        }
    }
}