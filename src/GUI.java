import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

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
    private JPanel actualValuePanel;
    private JPanel idealValuePanel;
    private JLabel labelIMean;
    private JLabel labelIVar;
    private JLabel labelISD;
    private JSplitPane resultSplitPanel;
    //GRAPHS
    private JLabel valueGraph;
    private JLabel probGraph;
    private JLabel actualvsidealGraph;
    private JLabel actualProbGraph;
    private JLabel idealProbGraph;
    private JPanel controlPanel;
    private JPanel inputInfoPanel;
    private JLabel labelTotalCombi;
    private JLabel labelTotalSuccess;
    private JLabel labelSuccessProb;


    public String selectedTab;

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

    protected JComponent makeTextPanel(String text) {
        JPanel panel = new JPanel(false);
        JLabel filler = new JLabel(text);
        filler.setHorizontalAlignment(JLabel.CENTER);
        panel.setLayout(new GridLayout(1, 1));
        panel.add(filler);
        return panel;
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

        // ADD TABS
        histPanel.addTab("Result Values", rValueChartPanel);
        histPanel.addTab("Actual Vs. Ideal", actualVsIdealChartPanel);
        histPanel.addTab("Actual Only", actualChartPanel);
        histPanel.addTab("Ideal Only", idealChartPanel);

    }

    private void setupListeners() {

        histPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                String USER_DIR = System.getProperty("user.dir") + "//src//images//";
                String dist;

                int selectedTab = histPanel.getSelectedIndex();
                switch(selectedTab){
                    case 0:
                        valueGraph.setIcon(new ImageIcon(getClass().getResource("images/image52.png")));
                        valueGraph.setText("");
                        probGraph.setIcon(new ImageIcon(getClass().getResource("images/image54.png")));
                        probGraph.setText("");
                        break; //results
                    case 1:
                        dist = (String)comboBoxProbDist.getSelectedItem();
                        switch(dist){
                            case "Binomial":
                                actualvsidealGraph.setIcon(new ImageIcon(getClass().getResource("images/image48.png")));
                                actualvsidealGraph.setText("");
                                break;
                            case "Negative Binomial":
                                actualvsidealGraph.setIcon(new ImageIcon(getClass().getResource("images/image49.png")));
                                actualvsidealGraph.setText("");
                                break;
                            case "Hypergeometric":
                                actualvsidealGraph.setIcon(new ImageIcon(getClass().getResource("images/image50.png")));
                                actualvsidealGraph.setText("");
                                break;
                            case "Multinomial":
                                actualvsidealGraph.setIcon(new ImageIcon(getClass().getResource("images/image51.png")));
                                actualvsidealGraph.setText("");
                                break;
                        }



                        break; //actual vs ideal
                    case 2:
                        actualProbGraph.setIcon(new ImageIcon(getClass().getResource("images/image54.png")));
                        actualProbGraph.setText("");
                        break; //actual only

                    case 3:

                        dist = (String)comboBoxProbDist.getSelectedItem();
                        System.out.println(dist);
                        switch(dist){
                            case "Binomial":
                                idealProbGraph.setIcon(new ImageIcon(getClass().getResource("images/image55.png")));
                                idealProbGraph.setText("");
                                break;
                            case "Negative Binomial":
                                idealProbGraph.setIcon(new ImageIcon(getClass().getResource("images/image56.png")));
                                idealProbGraph.setText("");
                                break;
                            case "Hypergeometric":
                                idealProbGraph.setIcon(new ImageIcon(getClass().getResource("images/image57.png")));
                                idealProbGraph.setText("");
                                break;
                            case "Multinomial":
                                idealProbGraph.setIcon(new ImageIcon(getClass().getResource("images/image58.png")));
                                idealProbGraph.setText("");
                                break;
                        }
                        break; //ideal only
                    default: break;
                }
            }
        });

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