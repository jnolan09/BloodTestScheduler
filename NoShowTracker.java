/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bloodtestscheduler;

/**
 *
 * @author joshu
 */
public class NoShowTracker {
    private FixedSizeStack<Patient> noShowStack;
    private static final int MAX_SIZE = 5;
    
    public NoShowTracker() {
        noShowStack = new FixedSizeStack<>(MAX_SIZE);
    }
    
    // Adds a patient to no-show tracker
    public void addNoShow(Patient patient) {
        noShowStack.push(patient);
    }
    
    public Patient[] getNoShows() {
        return noShowStack.getElements();
    }
    
    public int getNoShowCount() {
        return noShowStack.size();
    }
    
    public Patient findPatientById(String id) {
        return findPatientById(noShowStack.getElements(), 0, id);
    }
    
    // Searches for a patient by ID
    private Patient findPatientById(Patient[] patients, int index, String id) {
        if (index >= patients.length || patients[index] == null) {
            return null;
        }
        
        if (patients[index].getId().equals(id)) {
            return patients[index];
        }
        
        return findPatientById(patients, index + 1, id);
    }
    
    public void clear() {
        noShowStack.clear();
    }
}