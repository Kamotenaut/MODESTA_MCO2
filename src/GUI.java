import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
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
    private JSplitPane splitPanel;

    // COMBO BOXES
    private JComboBox comboBoxTrial;
    private JComboBox comboBoxCard;

    // LABELS
    private JLabel labelNumTrial;
    private JLabel labelNumCard;
    private JLabel labelTotalValue;

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
    private JPanel binomChartPanel;
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
    private JLabel actualvsidealGraph;
    private JLabel actualProbGraph;
    private JLabel binomGraph;
    private JPanel controlPanel;
    private JPanel inputInfoPanel;
    private JLabel labelTotalCombi;
    private JLabel labelTotalSuccess;
    private JLabel labelSuccessProb;
    private JPanel nbinomChartPanel;
    private JLabel nbinomGraph;
    private JPanel hyperChartPanel;
    private JLabel hyperGraph;


    public String selectedTab;

    // INPUT ATTRIBUTES
    String[] arrayTrial = {"10", "100", "1000", "10000"};
    String[] arrayCard = {"1", "2", "3", "4", "5"};
    int numTrials, numCards, numSearchValue;
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

        // SPINNER
        spinnerSearchValue.setValue(1);
        ((SpinnerNumberModel)spinnerSearchValue.getModel()).setMinimum(1);
        ((SpinnerNumberModel)spinnerSearchValue.getModel()).setMaximum(13*5);

        // ADD TABS
        histPanel.addTab("Actual Values", rValueChartPanel);
        histPanel.addTab("Actual Probabilities", actualChartPanel);
        histPanel.addTab("Actual vs Ideal", actualVsIdealChartPanel);
        histPanel.addTab("Binomial Distribution", binomChartPanel);
        histPanel.addTab("N. Binomial Distribution", nbinomChartPanel);
        histPanel.addTab("HyperGeometric Distbribution", hyperChartPanel);


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

                if(withReplace) {
                    labelTotalCombi.setText("Total Number of Combinations: " + sim.getOverAllProb().getTotalCombinationsWRep());
                    labelTotalSuccess.setText("Total Number of Success Combinations: " + sim.getOverAllProb().getCorrectCombinationsWRep());
                    labelSuccessProb.setText("Probability of Success: " + sim.getOverAllProb().getProbRep());
                }
                else {
                    labelTotalCombi.setText("Total Number of Combinations: " + sim.getOverAllProb().getTotalCombinationsWORep());
                    labelTotalSuccess.setText("Total Number of Success Combinations: " + sim.getOverAllProb().getCorrectCombinationsWORep());
                    labelSuccessProb.setText("Probability of Success: " + sim.getOverAllProb().getProbNoRep());
                }

                resultPanel.revalidate();
                resultPanel.repaint();
                setGraphs();
                contentPane.revalidate();
                contentPane.repaint();



            }
        });
    }

    private void setGraphs(){
        String USER_DIR = System.getProperty("user.dir") + "\\src\\images\\";

        //USER_DIR = "C:/Users/Ronnie Nieva/Documents/Dydy/Eclipse Projects/MODESTA_MCO2/src/images/";

        //File file = new File(USER_DIR + "actualres_hist.png");
        //System.out.println(file.exists());

        File file = new File(USER_DIR + "actualres_hist.png");
        System.out.println(USER_DIR);
        System.out.println(file.exists());

        try {
            valueGraph.setText("");
            valueGraph.setIcon(new ImageIcon(ImageIO.read( new File(USER_DIR + "actualres_hist.png") )));
            actualProbGraph.setText("");
            actualProbGraph.setIcon(new ImageIcon(ImageIO.read( new File(USER_DIR+"actualprob_hist.png"))));
            actualvsidealGraph.setText("");
            actualvsidealGraph.setIcon(new ImageIcon(ImageIO.read( new File(USER_DIR+"actualvsideal.png"))));

            if(!withReplace) {
                binomGraph.setText("Binomial Distribution is not possible for Drawing without Replacement");
                binomGraph.setIcon(null);
                nbinomGraph.setText("Neg. Binomial Distribution is not possible for Drawing without Replacement");
                nbinomGraph.setIcon(null);
                hyperGraph.setIcon(new ImageIcon(ImageIO.read( new File(USER_DIR+"hyper_graph.png"))));
                hyperGraph.setText("");
            }
            else {
                hyperGraph.setText("Hypergeometric Distribution is only possible for Drawing without Replacement");
                hyperGraph.setIcon(null);
                binomGraph.setIcon(new ImageIcon(ImageIO.read( new File(USER_DIR+"dbinom_graph.png"))));
                binomGraph.setText("");

                nbinomGraph.setIcon(new ImageIcon(ImageIO.read( new File(USER_DIR+"dnbinom_graph.png"))));
                nbinomGraph.setText("");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }








        valueGraph.revalidate();
        valueGraph.repaint();
        rValueChartPanel.revalidate();
        rValueChartPanel.repaint();
        actualProbGraph.revalidate();
        actualProbGraph.repaint();
        actualChartPanel.revalidate();
        actualChartPanel.repaint();
        actualvsidealGraph.revalidate();
        actualvsidealGraph.repaint();
        actualVsIdealChartPanel.revalidate();
        actualVsIdealChartPanel.repaint();
        binomGraph.revalidate();
        binomGraph.repaint();
        binomChartPanel.revalidate();
        binomChartPanel.repaint();
        nbinomGraph.revalidate();
        nbinomGraph.repaint();
        nbinomChartPanel.revalidate();
        nbinomChartPanel.repaint();
        hyperGraph.revalidate();
        hyperGraph.repaint();
        hyperChartPanel.revalidate();
        hyperChartPanel.repaint();

    }
}