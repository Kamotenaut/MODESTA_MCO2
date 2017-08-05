/**
 * Created by jasonsapdos on 05/08/2017.
 */
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JOptionPane;

public class CSVWriter extends Writer
{

    @Override
    public String format(Object[] tableRow)
    {
        String formattedData;
        final String attribDelimeter = ", ";

        StringBuilder sb = new StringBuilder();
        // numTrials
        sb.append(tableRow[0]).append(attribDelimeter);
        // numCards
        sb.append(tableRow[1]).append(attribDelimeter);
        // withReplace
        sb.append(tableRow[2]).append(attribDelimeter);
        // searchSum
        sb.append(tableRow[3]).append(attribDelimeter);
        // binom
        sb.append(tableRow[4]).append(attribDelimeter);
        // nbinom
        sb.append(tableRow[5]).append(attribDelimeter);
        // hyper
        sb.append(tableRow[6]).append(attribDelimeter);
        // multinom
        sb.append(tableRow[7]).append(attribDelimeter);
        // actual
        sb.append(tableRow[8]);

        formattedData = sb.toString();

        return formattedData;
    }

    @Override
    public void writeRow(String fileName, String formattedData)
    {
        FileWriter csvWriter = null;

        try
        {
            csvWriter = new FileWriter(fileName);

            csvWriter.append(formattedData);

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
                // Closes the CSVWriter
                if (csvWriter == null)
                    return;

                csvWriter.flush();
                csvWriter.close();
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
