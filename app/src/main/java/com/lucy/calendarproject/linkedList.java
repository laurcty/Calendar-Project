// Java program to illustrate merge sorted
// of linkedList

package com.lucy.calendarproject;

import com.prolificinteractive.materialcalendarview.CalendarDay;

public class linkedList {
    node head;

    static class node {
        CalendarDay date;
        int val;
        node next;

        public node(CalendarDay date,int val)
        {
            this.date = date;
            this.val = val;
        }
    }

    node sortedMerge(node a, node b)
    {
        node result = null;

        // Goes into here if they're already sorted
        if (a == null)
            return b;
        if (b == null)
            return a;

        /* Pick either a or b, and recur */
        if (a.val <= b.val) {
            result = a;
            result.next = sortedMerge(a.next, b);
        }
        else {
            result = b;
            result.next = sortedMerge(a, b.next);
        }
        return result;
    }

    node mergeSort(node h)
    {

        System.out.println("I'M IN THE MERGE SORT!!");
        // Base case : if head is null
        if (h == null || h.next == null) {
            return h;
        }

        // get the middle of the list
        node middle = getMiddle(h);
        node nextofmiddle = middle.next;

        // set the next of middle node to null
        middle.next = null;

        // Apply mergeSort on left list
        node left = mergeSort(h);

        // Apply mergeSort on right list
        node right = mergeSort(nextofmiddle);

        // Merge the left and right lists
        node sortedlist = sortedMerge(left, right);

        return sortedlist;
    }

    // Utility function to get the middle of the linked list
    public static node getMiddle(node head)
    {
        if (head == null)
            return head;

        node slow = head, fast = head;

        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }

    void push(CalendarDay date, int new_data)
    {
        /* allocate node */
        node new_node = new node(date, new_data);

        /* link the old list off the new node */
        new_node.next = head;

        /* move the head to point to the new node */
        head = new_node;
    }

    // Outputs the sorted list
    void printList(node headref)
    {
        while (headref != null) {
            System.out.println(headref.date + ": " + headref.val + " ");
            headref = headref.next;
        }
    }

    // The below code was used for the testing of this merge sort algorithm, before it was used in ViewGroup.java

    /*
    public static void main(String[] args)
    {

        linkedList li = new linkedList();


        CalendarDay day = CalendarDay.today();

        li.push(day,15);
        li.push(day,10);
        li.push(day,5);
        li.push(day,20);
        li.push(day,3);
        li.push(day,2);

        // Apply merge Sort
        li.head = li.mergeSort(li.head);
        System.out.print("\n Sorted Linked List is: \n");
        li.printList(li.head);
    }
    */
}

// This code is contributed by Rishabh Mahrsee
