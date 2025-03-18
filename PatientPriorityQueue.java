/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bloodtestscheduler;

import java.util.NoSuchElementException;
/**
 *
 * @author joshu
 */

public class PatientPriorityQueue implements PriorityQueueInterface<Patient> {
    private Patient[] heap;
    private int size;
    private static final int DEFAULT_CAPACITY = 20;
    
    public PatientPriorityQueue() {
        this(DEFAULT_CAPACITY);
    }
    
    @SuppressWarnings("unchecked")
    public PatientPriorityQueue(int initialCapacity) {
        heap = new Patient[initialCapacity + 1];
        size = 0;
    }
    
    @Override
    public void add(Patient patient) {
        if (size >= heap.length - 1) {
            expandCapacity();
        }
        
        // Add element at the end
        size++;
        heap[size] = patient;
        // Restore heap property by swimming up
        swim(size);
    }
    
    @Override
    public Patient dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("Priority queue is empty");
        }
        
        Patient max = heap[1];
        heap[1] = heap[size];
        heap[size] = null;
        size--;
        // Restore heap property by sinking down
        if (size > 0) {
            sink(1);
        }
        
        return max;
    }
    
    public Patient remove() {
        if (isEmpty()) {
            throw new NoSuchElementException("Priority queue is empty");
        }
        
        Patient max = heap[1];
        heap[1] = heap[size];
        heap[size] = null;
        size--;
        // Restore heap property by sinking down
        if (size > 0) {
            sink(1);
        }
        
        return max;
    }
    
    @Override
    public Patient peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("Priority queue is empty");
        }
        return heap[1];
    }
    
    @Override
    public boolean isEmpty() {
        return size == 0;
    }
    
    @Override
    public int size() {
        return size;
    }
    
    @Override
    public void clear() {
        for (int i = 1; i <= size; i++) {
            heap[i] = null;
        }
        size = 0;
    }
    
    private void swim(int k) {
        while (k > 1 && compare(heap[k/2], heap[k]) < 0) {
            swap(k/2, k);
            k = k/2;
        }
    }
    
    private void sink(int k) {
        while (2*k <= size) {
            int j = 2*k;
            if (j < size && compare(heap[j], heap[j+1]) < 0) j++;
            if (compare(heap[k], heap[j]) >= 0) break;
            swap(k, j);
            k = j;
        }
    }
    
    private void swap(int i, int j) {
        Patient temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }
    
    private int compare(Patient p1, Patient p2) {
        // Compare by priority
        int priorityCompare = comparePriority(p1.getPriority(), p2.getPriority());
        if (priorityCompare != 0) {
            return priorityCompare;
        }
        
        // Then compare by hospital ward status
        if (p1.isFromHospitalWard() && !p2.isFromHospitalWard()) {
            return 1;
        } else if (!p1.isFromHospitalWard() && p2.isFromHospitalWard()) {
            return -1;
        }
        
        // Finally compare by age (older patients get priority)
        return Integer.compare(p1.getAge(), p2.getAge());
    }
    
    private int comparePriority(String p1, String p2) {
        int p1Value = getPriorityValue(p1);
        int p2Value = getPriorityValue(p2);
        return Integer.compare(p1Value, p2Value);
    }
    
    private int getPriorityValue(String priority) {
        return switch (priority.toLowerCase()) {
            case "urgent" -> 3;
            case "medium" -> 2;
            case "low" -> 1;
            default -> 0;
        };
    }
    
    @SuppressWarnings("unchecked")
    private void expandCapacity() {
        Patient[] newHeap = new Patient[heap.length * 2];
        for (int i = 0; i <= size; i++) {
            newHeap[i] = heap[i];
        }
        heap = newHeap;
    }
    
    // Finding a patient by name
    public Patient findPatientByName(String name) {
        return findPatientByNameRecursive(1, name);
    }
    
    // Recursive method to find a patient by name
    private Patient findPatientByNameRecursive(int index, String name) {
        if (index > size) {
            return null;
        }
        
        if (heap[index] != null && heap[index].getName().equalsIgnoreCase(name)) {
            return heap[index];
        }
        
        // Search left child
        Patient found = findPatientByNameRecursive(2 * index, name);
        if (found != null) {
            return found;
        }
        
        // Search right child
        return findPatientByNameRecursive(2 * index + 1, name);
    }
}