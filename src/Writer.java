/**
 * Created by jasonsapdos on 05/08/2017.
 */

public abstract class Writer
{

    protected abstract void write(String fileName, String printString);

    // getWriter creates the TXT Writer object
    public static Writer getWriter(String fileName)
    {

        return new TXTWriter();

    }
}
