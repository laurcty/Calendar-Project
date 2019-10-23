package com.lucy.calendarproject;
import com.prolificinteractive.materialcalendarview.CalendarDay;

// created with the help of tutorial https://www.youtube.com/watch?v=jOyYaBHyN28&t=82s

class linkedList {
    node head;

    static class node {
        CalendarDay date;
        int val;
        node next;

        private node(CalendarDay date,int val){
            this.date = date;
            this.val = val;
        }
    }

    private node sortedMerge(node a, node b){
        node result = null;

        // Goes into here if they're already sorted
        if (a == null)
            return b;
        if (b == null)
            return a;

        // Recursively call sortedMerge for either a or b depending on which is larger
        if (a.val <= b.val){
            result = a;
            result.next = sortedMerge(a.next, b);
        }else{
            result = b;
            result.next = sortedMerge(a, b.next);
        }
        return result;
    }

    node mergeSort(node h) {
        // Base case : if head is null
        if (h == null || h.next == null) {
            return h;
        }

        // Get the middle of the list
        node middle = getMiddle(h);
        node nextofmiddle = middle.next;

        // Set the next of middle node to null
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
    private static node getMiddle(node head) {
        if (head == null)
            return head;

        node slow = head, fast = head;

        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }

    void push(CalendarDay date, int new_data){
        // Create new node and pass data into it
        node new_node = new node(date, new_data);

        // Link to old list
        new_node.next = head;

        // Move head pointer to new node
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

