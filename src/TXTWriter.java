/**
 * Created by jasonsapdos on 05/08/2017.
 */
import java.io.*;

import javax.swing.JOptionPane;

public class TXTWriter
{
    private static String VALUE_FILE = "value_log.txt";
    private static String RESULT_FILE = "result_log.txt";

    public void writeResult(String printString){
        write(printString, RESULT_FILE);
    }

    public void writeValue(String printString){
        write(printString, VALUE_FILE);
    }

    private void write(String printString, String fileName)
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

            fileWriter.append(printString);
            fileWriter.newLine();


            // JOptionPane.showMessageDialog(null, "Saving successful!");
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