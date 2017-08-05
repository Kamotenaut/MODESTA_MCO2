/**
 * Created by jasonsapdos on 05/08/2017.
 */

public abstract class Writer
{
    public void saveColumnNamesToDataset(String filename){
        writeRow(filename, "numTrials," +
                "numCards," +
                "withReplace," +
                "searchSum," +
                "binom," +
                "nbinom," +
                "hyper," +
                "multinom," +
                "actual");
    }

    public void saveRowToDataset(String fileName, Object[] tableRow)
    {
        writeRow(fileName, format(tableRow));
    }

    protected abstract String format(Object[] tableRow);

    protected abstract void writeRow(String fileName, String formattedData);

    // getWriter creates the CSV Writer object
    public static Writer getWriter(String fileName)
    {

        return new CSVWriter();

    }
}
