/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package bloodtestscheduler;

/**
 *
 * @author joshu
 */
public interface QueueInterface<A> {
    
    void enqueue(A  element); // Adds a new element to the end of the queue
    A dequeue(); // Removes the element at the front of the queue
    A peek(); // Shows the element at the front of the queue
    boolean isEmpty(); // Checks if queue is empty
    int size(); // The number of elements in the queue
    void clear(); // Removes all elements from the queue
    
    
}
