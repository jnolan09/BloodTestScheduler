/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package bloodtestscheduler;

/**
 *
 * @author joshu
 */
public interface StackInterface<A> {
    
    void push(A element); // Adds a new element to the top of the stack
    A pop(); // Removes the element at the top of the stack
    A peek(); // Shows the element at the top of the stack
    boolean isEmpty(); // Checks if stack is empty
    boolean isFull(); // Cjecks if stack is full
    int size(); // The number of elements in the stack
    void clear(); // Removes all elements from the stack
    
}
