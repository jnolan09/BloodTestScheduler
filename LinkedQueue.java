/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bloodtestscheduler;

/**
 *
 * @author joshu
 */
public class LinkedQueue<A> implements QueueInterface<A> {
    private class Node {
        private A data; 
        private Node next;
        
        public Node(A data) {
            this.data = data;
            this.next = null;
        }
    }
    
    private Node front;
    private Node rear;
    private int size;
    
    // Makes an empty queue
    public LinkedQueue() {
        front = null;
        rear = null;
        size = 0;
    }
    
    // Adds element to end of queue
    @Override
    public void enqueue(A element) {
        Node newNode = new Node(element);
        if(isEmpty()) {
            front = newNode;
        } else {
            rear.next = newNode;
        }
        rear = newNode;
        size++;
    }
    
    // Removes element at front of queue
    @Override
    public A dequeue() {
        if(isEmpty()) {
            return null;
        }
        
        A data = front.data;
        front = front.next;
        size--;
        if (front == null) {
            rear = null;
        }
        return data;
    }
    
    // Returns element at front of the queue
    @Override
    public A peek() {
        if (isEmpty()) {
            return null;
        }
        return front.data;
    }
    
    // Checks if queue is empty
    @Override
    public boolean isEmpty() {
        return size == 0;
    }
    
    // Size of the queue
    @Override
    public int size() {
        return size;
    }
    
    // Removes all elements from the queue
    @Override
    public void clear() {
        front = null;
        rear = null;
        size = 0;
    
    }
    
}
