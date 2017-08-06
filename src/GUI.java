import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

/**
 * Created by jasonsapdos on 04/08/2017.
 */

public class GUI {

    // GUI ELEMENTS

    public JPanel contentPane;
    private JPanel menuPanel;
    private JPanel trialPanel;
    private JScrollPane resultScrollPane;

    private JComboBox comboBoxTrial;
    private JComboBox comboBoxCard;

    private JLabel labelNumTrial;
    private JLabel labelNumCard;
    private JLabel labelTotalValue;

    private JFormattedTextField fTxtFieldTotalValue;

    private JButton buttonRun;

    private JTable tableRunResult;
    private JCheckBox cboxRep;

    // INPUT ATTRIBUTES
    String[] arrayTrial = {"10", "100", "1000", "10000", "100000"};
    String[] arrayCard = {"1", "2", "3", "4", "5"};
    String[] columnNames = {"Binomial",
            "Negative Binomial",
            "Hypergeometric",
            "Multinomial",
            "ACTUAL"};
    int numTrials, numCards, numSearchValue;
    boolean withReplace;
    RunResultModel resultModel = new RunResultModel();

    public GUI(){
        createUIComponents();
        setupListeners();
    }

    private void createUIComponents() {

        comboBoxTrial.setModel(new DefaultComboBoxModel(arrayTrial));
        comboBoxCard.setModel(new DefaultComboBoxModel(arrayCard));

        // FORMATTED TEXT FIELD
        NumberFormat format = NumberFormat.getInstance();
        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(0);
        formatter.setMaximum(Integer.MAX_VALUE);
        formatter.setAllowsInvalid(false);
        // If you want the value to be committed on each keystroke instead of focus lost
        formatter.setCommitsOnValidEdit(true);
        fTxtFieldTotalValue.setFormatterFactory(new DefaultFormatterFactory(formatter));

        // JTABLE
        tableRunResult.setModel(resultModel);
    }

    private void setupListeners() {

        // "Run" button is clicked
        buttonRun.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // Get input from combo boxes
                numTrials = Integer.parseInt((String)comboBoxTrial.getSelectedItem());
                numCards = Integer.parseInt((String)comboBoxCard.getSelectedItem());

                // Get input from textfield - number to search
                if(fTxtFieldTotalValue.getValue() != null)
                    numSearchValue = (int)fTxtFieldTotalValue.getValue();

                // Get input from checkbox
                withReplace = cboxRep.isSelected();

                // WHERE THE COMPUTING BEGINS
                resultModel.computeProbabilities(numTrials, numCards, numSearchValue, withReplace);
            }
        });
    }

    // Change the contents of the probability row in JTable
    private void changeProbTable(double dbinom, double dnbinom,
                                 double dhyper, double dmultinom,
                                 double dactual){

    }
}
