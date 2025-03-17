/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package bloodtestscheduler;

/**
 *
 * @author joshu
 */
public interface PriorityQueueInterface<A> {
    
    void add(A element); // Adds a new element to the priority queue
    A dequeue(); // Removes the highest priority element
    A peek(); // Shows the highest priority element
    boolean isEmpty(); // Checks if queue is empty
    int size(); // The number of elements in the queue
    void clear(); // Clears the queue
             
}
