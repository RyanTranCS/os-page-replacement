/**
 *
 * Name: Tran, Ryan
 * Project: PA-2 (Page Replacement Algorithm)
 * File: PFCircularLinkedList.java
 * Instructor: Feng Chen
 * Class: cs4103-sp21
 * LogonID: cs410378
 *
 * */

public class PFCircularLinkedList {

    class PFNode {
        int pageNumber;
        boolean isAllocated;
        boolean hasBeenModified;
        int refBit;
        PFNode nextPFNode;
    }

    private PFNode head = null;
    private PFNode tail = null;

    public void addNode() {
        PFNode newPFNode = new PFNode();

        if (head == null) {
            head = newPFNode;
        }
        else {
            tail.nextPFNode = newPFNode;
        }

        tail = newPFNode;
        tail.nextPFNode = head;
    }

    public PFNode getHead() {
        return head;
    }

    public PFNode getTail() {
        return tail;
    }

    public boolean isPageInMemory(int pageNumber) {
        PFNode currentPFNode = head;

        if (head != null) {
            do {
                if (currentPFNode.pageNumber == pageNumber && currentPFNode.isAllocated) {
                    return true;
                }
                currentPFNode = currentPFNode.nextPFNode;
            } while (currentPFNode != head);
        }
        return false;
    }

    public PFNode find(int pageNumber) {
        PFNode currentPFNode = head;

        if (head != null) {
            do {
                if (currentPFNode.pageNumber == pageNumber && currentPFNode.isAllocated) {
                    return currentPFNode;
                }
                currentPFNode = currentPFNode.nextPFNode;
            } while (currentPFNode != head);
        }
        return null;
    }

    public boolean isFreeFrameAvailable() {
        PFNode currentPFNode = head;

        if (head != null) {
            do {
                if (!currentPFNode.isAllocated) {
                    return true;
                }
                currentPFNode = currentPFNode.nextPFNode;
            } while (currentPFNode != head);
        }
        return false;
    }

    public PFNode getFreeFrame() {
        PFNode currentPFNode = head;

        if (head != null) {
            do {
                if (!currentPFNode.isAllocated) {
                    return currentPFNode;
                }
                currentPFNode = currentPFNode.nextPFNode;
            } while (currentPFNode != head);
        }
        return null;
    }

}
