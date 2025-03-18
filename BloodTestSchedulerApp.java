/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package bloodtestscheduler;

/**
 *
 * @author joshu
 */
public class BloodTestSchedulerApp {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        BloodTestSchedulerGUI myGUI = new BloodTestSchedulerGUI();
        addHardcodedPatients(myGUI);
        myGUI.setVisible(true);
    }
    
     private static void addHardcodedPatients(BloodTestSchedulerGUI gui) {
        // Create sample patients
        
        // Urgent patients (will go to priority queue)
        Patient p1 = new Patient("001", "John Smith", "urgent", "Dr. Wilson", 45, false);
        Patient p2 = new Patient("002", "Mary Jones", "urgent", "Dr. Adams", 62, false);
        Patient p3 = new Patient("003", "Robert Lee", "urgent", "Dr. Wilson", 28, false);
        
        // Hospital ward patients (will go to priority queue)
        Patient p4 = new Patient("004", "James Brown", "medium", "Dr. Johnson", 55, true);
        Patient p5 = new Patient("005", "Sarah Miller", "low", "Dr. Adams", 36, true);
        
        // Regular patients
        Patient p6 = new Patient("006", "Emily Davis", "medium", "Dr. Wilson", 41, false);
        Patient p7 = new Patient("007", "Michael Wang", "medium", "Dr. Johnson", 33, false);
        Patient p8 = new Patient("008", "David Wilson", "medium", "Dr. Adams", 59, false);
        Patient p9 = new Patient("009", "Lisa Garcia", "low", "Dr. Johnson", 27, false);
        Patient p10 = new Patient("010", "Kevin Brown", "low", "Dr. Wilson", 19, false);
        
        // Add patients to the GUI
        gui.addPatientToQueue(p1);
        gui.addPatientToQueue(p2);
        gui.addPatientToQueue(p3);
        gui.addPatientToQueue(p4);
        gui.addPatientToQueue(p5);
        gui.addPatientToQueue(p6);
        gui.addPatientToQueue(p7);
        gui.addPatientToQueue(p8);
        gui.addPatientToQueue(p9);
        gui.addPatientToQueue(p10);
    }
}
