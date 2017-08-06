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
    private JSpinner spinnerSearchValue;

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

        // COMBOBOX
        comboBoxTrial.setModel(new DefaultComboBoxModel(arrayTrial));
        comboBoxCard.setModel(new DefaultComboBoxModel(arrayCard));

        // SPINNER
        // ((SpinnerNumberModel)spinnerSearchValue.getModel()).setMaximum();

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
                if(spinnerSearchValue.getValue() != null)
                    numSearchValue = (int)spinnerSearchValue.getValue();

                // Get input from checkbox
                withReplace = cboxRep.isSelected();
                CardDrawSim sim = new CardDrawSim(numTrials,numCards,numSearchValue, withReplace);

                // WHERE THE COMPUTING BEGINS
                resultModel.computeProbabilities(numTrials, numCards, numSearchValue, withReplace);
                resultModel.setValueAt(sim.getIdealBinomProbElem(),0,0);
                resultModel.setValueAt(sim.getIdealNBinomProbElem(),0,1);
                resultModel.setValueAt(sim.getIdealHyperProbElem(),0,2);
                resultModel.setValueAt(sim.getIdealMultiProbElem(), 0, 3);
                resultModel.setValueAt(0.01, 0, 4);
            }
        });
    }

    // Change the contents of the probability row in JTable
    private void changeProbTable(double dbinom, double dnbinom,
                                 double dhyper, double dmultinom,
                                 double dactual){

    }
}