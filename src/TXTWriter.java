/**
 * Created by jasonsapdos on 05/08/2017.
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JOptionPane;

public class TXTWriter
{

    public void write(String printString)
    {
        String fileName = "log.txt";
        FileWriter fileWriter = null;

        try
        {
            fileWriter = new FileWriter(fileName, true);

            BufferedReader br = null;
            br = new BufferedReader(new FileReader(fileName));
            String line;

            while((line = br.readLine()) != null) {
                System.out.println("Checking CSV line...");
            }

            // Act like println
            printString += "\n";

            fileWriter.append(printString);

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