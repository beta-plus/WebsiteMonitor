/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package PDFComLinInt;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;
import org.betaplus.testcases.Comparitor;

/**
 * Class providing command line interface for PDF comparisons.
 *
 * @author StephenJohnRussell
 */
public class PDFComLinInt extends Comparitor {

    public static void main(String[] args) {

        LinkedList<File> files = new LinkedList<File>();
        Scanner in = new Scanner(System.in);
        menu(in, files);
    }

    private static void menu(Scanner in, LinkedList<File> files) {
        System.out.println("Please provide paths to files you wish to compare\n"
                + "seporated by pressing enter.  If the path you provide is\n"
                + "invalid you will be asked to re-enter it.  A second menu\n"
                + "will appear once you type END and press enter\n");
        String input = "";
        while (!input.equals("END")) {
            input = in.nextLine();
            if (!input.equals("")){
                File f = new File(input);
                if (files.add(f)) {
                    System.out.println("File added sucessfully.");
                } else {
                    System.out.println("File not added, please try again.");
                }
            }
        }
        boolean menuOp = true;
        while(menuOp) {
            System.out.println("Please select an option:\n"
                             + "1.Compare Checksums\n"
                             + "2.Show Differences\n"
                             + "3.Percentage Changed\n"
                             + "0.Close & Exit");
            input = in.nextLine();
            int sel = -1;
            try {
                sel = Integer.parseInt(input);
            } catch(NumberFormatException ex) {
                System.out.println("Please enter a valid menu selection.");
            }            
            if (sel != -1) {
                doWork(sel, in, files);
            }
        }
    }
    
    private static void doWork(int sel, Scanner in, LinkedList<File> files) {
        if (sel == 0) {
            System.exit(0);
        } else if (sel == 1) {
            compCheck(in, files);
        } else if (sel == 2) {
            showDiff(in, files);
        } else if (sel == 3) {
            showPercentChanged(in, files);
        }
        
    }
    
    private static void compCheck(Scanner in, LinkedList<File> files) {
        int[] sels = showFiles(in, files);
        if (sels[0] != -1 || sels[1]  != -1) {
            if (compareChecksums(files.get(sels[0]), files.get(sels[1]), MD5)) {
                System.out.println("Files match");
            } else {
                System.out.println("File do not match");
            }
        }        
    }
    
    private static int[] showFiles(Scanner in, LinkedList<File> files) {
        for (File f : files) {
            System.out.println(files.indexOf(f) + "|" + f.getName());
        }
        System.out.println("Please enter 2 file numbers seporated by a space.");
        int a = -1, b = -1;
        String input = in.nextLine();
        try {
            a = Integer.parseInt(input.substring(0,1));
            b = Integer.parseInt(input.substring(2,3));
            System.out.println(a + "|" + b);
        } catch(NumberFormatException ex) {
            System.out.println("Please enter a valid menu selection.");
            showFiles(in, files);
        }  
        return new int[] {a, b};
    }
    
    private static void showDiff(Scanner in, LinkedList<File> files) {
        int[] sels = showFiles(in, files);
        if (sels[0] != -1 || sels[1]  != -1) {
            LinkedList<String> diffs = getDifference(files.get(sels[0]), files.get(sels[1]));
            for (String s : diffs) {
                System.out.println(s);
            }
        } 
    }
    
    private static void showPercentChanged(Scanner in, LinkedList<File> files) {
        int[] sels = showFiles(in, files);
        if (sels[0] != -1 || sels[1]  != -1) {
            double[] percs = percentageChanged(getDifference(files.get(sels[0]), files.get(sels[1])));
            System.out.println("Removed:\t" + percs[0]);            
            System.out.println("Added:\t" + percs[1]);            
            System.out.println("Equal:\t" + percs[2]);            
        }
    }
}
