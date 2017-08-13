import javax.swing.table.AbstractTableModel;

/**
 * Created by jasonsapdos on 06/08/2017.
 */
public class RunResultModel extends AbstractTableModel {

    public RunResultModel(){
    }

    private String[] columnNames = {"Binomial",
            "Negative Binomial",
            "Hypergeometric",
            "Multinomial",
            "Actual"};
    private Object[][] data = {
            {" ", " ", " ", " ", " "}
    };

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return data.length;
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
        return data[row][col];
    }

    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    /*
     * Don't need to implement this method unless your table's
     * editable.
     */
    public boolean isCellEditable(int row, int col) {
        //Note that the data/cell address is constant,
        //no matter where the cell appears onscreen.
        if (col < 2) {
            return false;
        } else {
            return true;
        }
    }

    /*
     * Don't need to implement this method unless your table's
     * data can change.
     */
    public void setValueAt(Object value, int row, int col) {
        data[row][col] = value;
        fireTableCellUpdated(row, col);
    }

    // Compute for the probabilities
    public void computeProbabilities(int numTrials, int numCards, int numSearchValue, boolean withReplace){

        int success, allCombis;
        double successProb;



        // Changing content of table
        // DBINOM - resultModel.setValueAt(<int result>, 0, 0);
        // NBINOM - resultModel.setValueAt(<int result>, 0, 1);
        // DHYPER - resultModel.setValueAt(<int result>, 0, 2);
        // DMULTINOM - resultModel.setValueAt(<int result>, 0, 3);
        // ACTUAL - resultModel.setValueAt(<int result>, 0, 4);

        // Print results to CSVWriter
    }

}