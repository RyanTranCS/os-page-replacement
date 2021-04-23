/**
 * This program implements the second-chance (or clock) page replacement algorithm
 * used by the memory management systems contained within operating systems.
 * 
 * It accepts as input the number of initially available page frams, a sequence of page references
 * contained within a text file, the time cost for page access, the time cost for swapping in a page,
 * the time cost for swapping out a page, and the name of the text file where the algorithm's trace is to be stored.
 * 
 *
 * Name: Tran, Ryan
 * Project: PA-2 (Page Replacement Algorithm)
 * File: PageReplacement.java
 * Instructor: Feng Chen
 * Class: cs4103-sp21
 * LogonID: cs410378
 *
 * */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class PageReplacement {
    public static void main(String[] args) throws FileNotFoundException {

        int numPageFrames = Integer.parseInt(args[0]);
        String inFileName = args[1];
        int accessCost = Integer.parseInt(args[2]);
        int swapInCost = Integer.parseInt(args[3]);
        int swapOutCost = Integer.parseInt(args[4]);
        String outFileName = args[5];

        File inFile = new File(inFileName);
        Scanner in = new Scanner(inFile);
        File outFile = new File(outFileName);
        PrintWriter out = new PrintWriter(outFile);

        int numPageRefs = 0;
        int numPageFaultsRead = 0;
        int numPageFaultsWrite = 0;
        int timeAccessing = 0;
        int timeSwappingIn = 0;
        int timeSwappingOut = 0;

        // creation of page frames
        PFCircularLinkedList list = new PFCircularLinkedList();
        for (int i = 0; i < numPageFrames; i++) {
            list.addNode();
        }

        PFCircularLinkedList.PFNode clockHand = list.getHead();

        while (in.hasNext()) {
            char operation = in.next().charAt(0);   // read ('R') or write ('W')
            int requiredPage = in.nextInt();        // page number to be read/written
            char pageHitOrMiss = 'H';               // hit ('H') or miss/fault ('F')
            int victimPageNumber = -1;              // page to be evicted (-1 == None)
            int accessTime = accessCost;
            int swapInTime = 0;
            int swapOutTime = 0;
            numPageRefs++;

            boolean hasRequestBeenServiced = false;
            boolean isFirstLoop = true;

            while (!hasRequestBeenServiced) {
                // if page is in memory (page hit), then set page's reference bit to 1
                if (list.isPageInMemory(requiredPage)) {
                    PFCircularLinkedList.PFNode node = list.find(requiredPage);
                    if (operation == 'W') { node.hasBeenModified = true; }
                    node.refBit = 1;
                    hasRequestBeenServiced = true;
                }
                // if page is not in memory (page miss), then check for free page frame
                else {
                    if (isFirstLoop) {
                        pageHitOrMiss = 'F';
                        swapInTime = swapInCost;

                        if (operation == 'R') {
                            numPageFaultsRead++;
                        } else {
                            numPageFaultsWrite++;
                        }
                    }

                    // if free frame is available, then populate it with page to be referenced
                    if (list.isFreeFrameAvailable()) {
                        PFCircularLinkedList.PFNode node = list.getFreeFrame();
                        node.pageNumber = requiredPage;
                        node.isAllocated = true;
                    }
                    // if no free page frames available, then identify victim page
                    else {
                        while (victimPageNumber == -1) {        // while victim page hasn't been identified
                            if (clockHand.refBit == 1) {                    // if page's reference bit is 1
                                clockHand.refBit = 0;                       // then give page second chance
                            }
                            else {                                          // if page's reference bit is 0
                                victimPageNumber = clockHand.pageNumber;    // then choose page as victim and evict it
                                clockHand.isAllocated = false;              // reset allocation status
                                if (clockHand.hasBeenModified) { swapOutTime = swapOutCost; }
                                clockHand.hasBeenModified = false;          // reset modification status
                            }
                            clockHand = clockHand.nextPFNode;
                        }
                    }
                }
                isFirstLoop = false;
            }

            out.printf("%-2c %-3d %-2c %-3d %-2d %-3d %-3d\n", operation, requiredPage, pageHitOrMiss, victimPageNumber,
                    accessTime, swapInTime, swapOutTime);

            timeAccessing = timeAccessing + accessTime;
            timeSwappingIn = timeSwappingIn + swapInTime;
            timeSwappingOut = timeSwappingOut + swapOutTime;
        }

        in.close();
        out.close();

        System.out.println("Page References: " + numPageRefs);
        System.out.println("\nPage Faults (Read): " + numPageFaultsRead);
        System.out.println("Page Faults (Write): " + numPageFaultsWrite);
        System.out.println("\nAccess Time Units: " + timeAccessing);
        System.out.println("Swap In Time Units: " + timeSwappingIn);
        System.out.println("Swap Out Time Units: " + timeSwappingOut);

    }
}
