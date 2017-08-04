import org.apache.commons.math3.distribution.BinomialDistribution;
import org.apache.commons.math3.distribution.HypergeometricDistribution;

import javax.swing.*;

/**
 * Created by Brandon on 7/29/2017.
 */
public class Driver {
    public static void main(String[] args) {
        // CardDrawSim sim = new CardDrawSim();
        JFrame frame = new JFrame("MODESTA Card Drawing Simulator");
        frame.setContentPane(new GUI().contentPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
