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

    // JPANELS
    public JPanel contentPane;
    private JPanel menuPanel;
    private JPanel trialPanel;
    private JPanel valuePanel;
    private JPanel cardPanel;
    private JPanel runPanel;
    private JPanel probdistPanel;
    private JSplitPane splitPanel;

    // COMBO BOXES
    private JComboBox comboBoxTrial;
    private JComboBox comboBoxCard;
    private JComboBox comboBoxProbDist;

    // LABELS
    private JLabel labelNumTrial;
    private JLabel labelNumCard;
    private JLabel labelTotalValue;
    private JLabel labelProbDist;

    // BUTTONS
    private JButton buttonRun;

    // CHECKBOXES
    private JCheckBox cboxRep;

    // SPINNERS
    private JSpinner spinnerSearchValue;
    private JTabbedPane histPanel;
    private JPanel resultPanel;
    private JPanel rValueChartPanel;
    private JPanel actualVsIdealChartPanel;
    private JPanel actualChartPanel;
    private JPanel idealChartPanel;
    private JLabel labelAMean;
    private JLabel labelAVar;
    private JLabel labelASD;
    private JLabel labelACorr;
    private JPanel actualValuePanel;
    private JPanel idealValuePanel;
    private JLabel labelIMean;
    private JLabel labelIVar;
    private JLabel labelISD;
    private JLabel labelICorr;
    private JSplitPane resultSplitPanel;

    // INPUT ATTRIBUTES
    String[] arrayTrial = {"10", "100", "1000", "10000"};
    String[] arrayCard = {"1", "2", "3", "4", "5"};
    String[] arrayProbDist = {"Binomial",
            "Negative Binomial",
            "Hypergeometric",
            "Multinomial"};
    int numTrials, numCards, numSearchValue;
    String probDist;
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
        comboBoxProbDist.setModel(new DefaultComboBoxModel(arrayProbDist));

        // SPINNER
        spinnerSearchValue.setValue(1);
        ((SpinnerNumberModel)spinnerSearchValue.getModel()).setMinimum(1);
        ((SpinnerNumberModel)spinnerSearchValue.getModel()).setMaximum(13*5);
    }

    private void setupListeners() {

        // "Run" button is clicked
        buttonRun.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // Get input from combo boxes
                numTrials = Integer.parseInt((String)comboBoxTrial.getSelectedItem());
                numCards = Integer.parseInt((String)comboBoxCard.getSelectedItem());
                probDist = (String)comboBoxProbDist.getSelectedItem();

                // Get input from textfield - number to search
                if(spinnerSearchValue.getValue() != null)
                    numSearchValue = (int)spinnerSearchValue.getValue();

                // Get input from checkbox
                withReplace = cboxRep.isSelected();
                CardDrawSim sim = new CardDrawSim(numTrials,numCards,numSearchValue, withReplace);

                // WHERE THE COMPUTING BEGINS
                /*
                resultModel.computeProbabilities(numTrials, numCards, numSearchValue, withReplace);
                resultModel.setValueAt(sim.getIdealBinomProbElem(),0,0);
                resultModel.setValueAt(sim.getIdealNBinomProbElem(),0,1);
                resultModel.setValueAt(sim.getIdealHyperProbElem(),0,2);
                resultModel.setValueAt(sim.getIdealMultiProbElem(), 0, 3);
                resultModel.setValueAt(sim.getActualProbability(), 0, 4);
                */
            }
        });
    }
}