package org.betaplus.pdfutility;

import java.io.File;
import java.util.LinkedList;
import org.betaplus.pdfutility.fileimport.Comparitor;

/**
 *
 * @author Stephen John Russell
 * @date 07-Feb-2013
 * @version 0.1
 */
public class App {

    public static void main(String[] args) {
        
        Comparitor c = new Comparitor();
        File f1 = new File("data/ukpga_20020030_en.pdf");
        File f2 = new File("data/ukpga_20110013_en.pdf");
        File f3 = new File("data/AssignmentTwo.pdf");
        File f4 = new File("data/AssignmentTwoDraft.pdf");

        System.out.println("PDF Comparison Test Case\n***********************");        
        System.out.println("Comparing :\nFile 1:" + f3.toString() + "\nFile 2: "
                + f4.toString() + "\nResult :" + c.compareChecksums(f3, f4, 0));

        LinkedList<String> diffs = c.getDifference(f3, f4);
        
        double[] changes = c.percentageChanged(diffs);
        
        System.out.print("\nContent deleted: " 
                + String.format("%2.2f", changes[0]));
        System.out.print("%");
        System.out.print("\nContent added: " 
                + String.format("%2.2f", changes[1]));
        System.out.print("%");
        System.out.print("\nContent equal: " 
                + String.format("%2.2f", changes[2]));
        System.out.print("%");
        
        for (String s : diffs) {
            if (s.contains("Diff(DELETE,") || s.contains("Diff(INSERT,")) {
                s = s.replace("?", "");
                s = s.replace(" ? ", "");                
                System.out.print("\n\n\n");
                for (int i = 0; i < s.length(); i++) {
                    System.out.print(s.charAt(i));
                    if (s.charAt(i) == '.' && s.charAt(i + 1) == ' ') {
                        i++;
                        System.out.print("\n");
                    }
                }
            }
        }
    }
}
