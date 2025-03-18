/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bloodtestscheduler;

import java.util.Arrays;

/**
 *
 * @author joshu
 */
public class FixedSizeStack<A> implements StackInterface<A> {
    private A[] stack; // Array to store stack elements
    private int top; // Index of top element
    private int capacity; // Max capacity
    
    // The max amount of elements the stack can have
    @SuppressWarnings("unchecked")
    public FixedSizeStack(int capacity) {
        this.capacity = capacity;
        stack = (A[]) new Object[capacity];
        top = -1;
    }
    
    // Push onto the stack
    @Override
    public void push(A element) {
        if (isFull()) {
            shiftElements();
        }
        stack[++top] = element;
    }
    
    // Shifts all elements to the left when the stack is full
    private void shiftElements() {
        for (int i = 0; i < capacity - 1; i++) {
            stack[i] = stack[i + 1];
        }
        top = capacity - 1;
    }
    
    // Removes and returns the top element from the stack
    @Override
    public A pop() {
        if (isEmpty()) {
            return null;
        }
        A element = stack[top];
        stack[top--] = null;
        return element;
    }
    
    // Returns the top element of the stack
    @Override
    public A peek() {
        if (isEmpty()) {
            return null;
        }
        return stack[top];
    }
    
    // Returns if stack is empty or not
    @Override
    public boolean isEmpty() {
        return top == -1;
    }
    
    // Returns if the stack is full or not
    @Override
    public boolean isFull() {
        return top == capacity - 1;
    }
    
    // Size of the stack
    @Override
    public int size() {
        return top + 1;
    }
    
    // Removes all elements
    @Override
    public void clear() {
        Arrays.fill(stack, null);
        top = -1;
    }
    
    // Returns all elements in the stack
    public A[] getElements() {
        @SuppressWarnings("unchecked")
        A[] elements = (A[]) new Object[size()];
        for (int i = 0; i <= top; i++) {
            elements[i] = stack[i];
        }
        return elements;
    }
}