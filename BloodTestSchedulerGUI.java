/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package bloodtestscheduler;

import java.util.ArrayList;
import java.io.*;
import javax.swing.JOptionPane;

/**
 *
 * @author joshu
 */
public class BloodTestSchedulerGUI extends javax.swing.JFrame {
    
    private LinkedQueue<Patient> regularQueue;
    private PatientPriorityQueue priorityQueue;
    private NoShowTracker noShowTracker;
    private ArrayList<Patient> processedPatients;
    private final String FILE_PATH = "patients.dat";

    public BloodTestSchedulerGUI() {
        initComponents();
        regularQueue = new LinkedQueue<>();
        priorityQueue = new PatientPriorityQueue();
        noShowTracker = new NoShowTracker();
        processedPatients = new ArrayList<>();
        loadFromFile();
        updateStatistics();
        
    }
    
    private void loadFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            ArrayList<Patient> allPatients = (ArrayList<Patient>) ois.readObject();
            
            // Clear existing queues
            regularQueue.clear();
            priorityQueue.clear();
            
            // Add patients to appropriate queues
            for (Patient p : allPatients) {
                if (p.getPriority().equalsIgnoreCase("urgent") || p.isFromHospitalWard()) {
                    priorityQueue.add(p);
                } else {
                    regularQueue.enqueue(p);
                }
            }
            
            // Load processed patients
            processedPatients = (ArrayList<Patient>) ois.readObject();
            
            updateQueueDisplays();
            updateStatistics();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error " + e);
        }
    }

    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            // Collect all patients in queues
            ArrayList<Patient> allPatients = new ArrayList<>();
            
            // Get patients from regular queue
            LinkedQueue<Patient> tempRegularQueue = new LinkedQueue<>();
            while (!regularQueue.isEmpty()) {
                Patient p = regularQueue.dequeue();
                allPatients.add(p);
                tempRegularQueue.enqueue(p);
            }
            while (!tempRegularQueue.isEmpty()) {
                regularQueue.enqueue(tempRegularQueue.dequeue());
            }
            
            // Get patients from priority queue
            PatientPriorityQueue tempPriorityQueue = new PatientPriorityQueue();
            while (!priorityQueue.isEmpty()) {
                Patient p = priorityQueue.dequeue();
                allPatients.add(p);
                tempPriorityQueue.add(p);
            }
            while (!tempPriorityQueue.isEmpty()) {
                priorityQueue.add(tempPriorityQueue.dequeue());
            }
            
            // Save patients and processed patients
            oos.writeObject(allPatients);
            oos.writeObject(processedPatients);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error " + e);
        }
    }
    
    public void addPatientToQueue(Patient patient) {
        if (patient.getPriority().equalsIgnoreCase("urgent") || patient.isFromHospitalWard()) {
            priorityQueue.add(patient);
        } else {
            regularQueue.enqueue(patient);
        }
        
        updateQueueDisplays();
        updateStatistics();
    }
    
    private void updateQueueDisplays() {
    // Clear text areas
    regTA.setText("");
    prioTA.setText("");
    
    // Display regular queue
    LinkedQueue<Patient> tempRegularQueue = new LinkedQueue<>();
    StringBuilder regularText = new StringBuilder();
    
    while (!regularQueue.isEmpty()) {
        Patient p = regularQueue.dequeue();
        regularText.append("ID: ").append(p.getId())
                  .append(", Name: ").append(p.getName())
                  .append(", Priority: ").append(p.getPriority())
                  .append(", Age: ").append(p.getAge())
                  .append("\n");
        tempRegularQueue.enqueue(p);
    }
    
    // Restore regular queue
    while (!tempRegularQueue.isEmpty()) {
        regularQueue.enqueue(tempRegularQueue.dequeue());
    }
    
    // Display priority queue with identical formatting
    PatientPriorityQueue tempPriorityQueue = new PatientPriorityQueue();
    StringBuilder priorityText = new StringBuilder();
    
    while (!priorityQueue.isEmpty()) {
        Patient p = priorityQueue.dequeue();
        priorityText.append("ID: ").append(p.getId())
                   .append(", Name: ").append(p.getName())
                   .append(", Priority: ").append(p.getPriority())
                   .append(", Age: ").append(p.getAge())
                   .append("\n");
        tempPriorityQueue.add(p);
    }
    
    // Restore priority queue
    while (!tempPriorityQueue.isEmpty()) {
        priorityQueue.add(tempPriorityQueue.dequeue());
    }
    
    // Set text to text areas
    regTA.setText(regularText.toString());
    prioTA.setText(priorityText.toString());
    }

    // Method to update statistics fields
    private void updateStatistics() {
    regTF.setText(String.valueOf(regularQueue.size()));
    priostatTF.setText(String.valueOf(priorityQueue.size()));
    processedTF.setText(String.valueOf(processedPatients.size()));
    didntTF.setText(String.valueOf(noShowTracker.getNoShowCount()));
    }

    // Find and remove a patient by ID
    private Patient findAndRemovePatientById(String id) {
    // Try priority queue first
    Patient noShowPatient = findPatientInPriorityQueue(id);
    if (noShowPatient != null) {
        return noShowPatient;
    }
    
    // Then try regular queue
    return findPatientInRegularQueue(id);
    }

    // Find and remove patient from priority queue using recursion
    private Patient findPatientInPriorityQueue(String id) {
    PatientPriorityQueue tempQueue = new PatientPriorityQueue();
    Patient found = null;
    
    return findPatientInPriorityQueueRecursive(id, tempQueue, found);
    }

    // Recursive helper method
    private Patient findPatientInPriorityQueueRecursive(String id, PatientPriorityQueue tempQueue, Patient found) {
    // Base case: queue is empty
    if (priorityQueue.isEmpty()) {
        // Restore priority queue without the found patient
        while (!tempQueue.isEmpty()) {
            priorityQueue.add(tempQueue.dequeue());
        }
        return found;
    }
    
    // Get next patient
    Patient p = priorityQueue.dequeue();
    
    // Check if this is the patient we're looking for
    if (p.getId().equals(id) && found == null) {
        found = p;
    } else {
        tempQueue.add(p);
    }
    
    // Recursive call to process next patient in queue
    return findPatientInPriorityQueueRecursive(id, tempQueue, found);
    }

    // Find and remove patient from regular queue (iterative approach)
    private Patient findPatientInRegularQueue(String id) {
    LinkedQueue<Patient> tempQueue = new LinkedQueue<>();
    Patient found = null;
    
    while (!regularQueue.isEmpty()) {
        Patient p = regularQueue.dequeue();
        if (p.getId().equals(id) && found == null) {
            found = p;
        } else {
            tempQueue.enqueue(p);
        }
    }
    
    // Restore regular queue without the found patient
    while (!tempQueue.isEmpty()) {
        regularQueue.enqueue(tempQueue.dequeue());
    }
    
    return found;
    } 

    // Recursive method to append processed patients to StringBuilder
    private void appendProcessedPatients(StringBuilder sb, int index) {
    if (index >= processedPatients.size()) {
        return; // Base case: end of list
    }
    
    Patient p = processedPatients.get(index);
    sb.append("ID: ").append(p.getId())
      .append(", Name: ").append(p.getName())
      .append(", Priority: ").append(p.getPriority())
      .append(", Age: ").append(p.getAge())
      .append(", Hospital: ").append(p.isFromHospitalWard() ? "Yes" : "No")
      .append("\n");
    
    // Recursive call to process next patient
    appendProcessedPatients(sb, index + 1);
    }
   
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        addBTN = new javax.swing.JButton();
        prioTF = new javax.swing.JTextField();
        idTF = new javax.swing.JTextField();
        ageTF = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        didntTF = new javax.swing.JTextField();
        processedTF = new javax.swing.JTextField();
        regTF = new javax.swing.JTextField();
        priostatTF = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        processBTN = new javax.swing.JButton();
        didntBTN = new javax.swing.JButton();
        viewBTN = new javax.swing.JButton();
        nameTF = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        regTA = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        prioTA = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(600, 400));
        setSize(new java.awt.Dimension(400, 400));

        jLabel1.setText("Patient Information:");

        jLabel2.setText("ID: ");

        jLabel3.setText("Priority: ");

        jLabel4.setText("Name:");

        jLabel5.setText("Age: ");

        addBTN.setText("ADD");
        addBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addBTNActionPerformed(evt);
            }
        });

        jLabel6.setText("Statistics: ");

        jLabel7.setText("Regular:");

        jLabel8.setText("Processed:");

        jLabel9.setText("Priority: ");

        jLabel10.setText("Didn't Show: ");

        jLabel11.setText("Regular Queue");

        jLabel12.setText("Priority Queue");

        processBTN.setText("Process");
        processBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                processBTNActionPerformed(evt);
            }
        });

        didntBTN.setText("Didn't Show");
        didntBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                didntBTNActionPerformed(evt);
            }
        });

        viewBTN.setText("View Processed");
        viewBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewBTNActionPerformed(evt);
            }
        });

        regTA.setColumns(20);
        regTA.setRows(5);
        jScrollPane2.setViewportView(regTA);

        prioTA.setColumns(20);
        prioTA.setRows(5);
        jScrollPane3.setViewportView(prioTA);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel2)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(idTF, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabel4)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(nameTF, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabel5)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(ageTF, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabel3)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(prioTF, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(58, 58, 58)
                                    .addComponent(addBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(124, 124, 124))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                    .addGap(12, 12, 12)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(63, 63, 63)))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addComponent(processBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(didntBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(viewBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(jLabel7)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(regTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(jLabel9))
                                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(priostatTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jLabel8)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(processedTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jLabel10)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(didntTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel5)
                    .addComponent(jLabel4)
                    .addComponent(addBTN)
                    .addComponent(ageTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(idTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(prioTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nameTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE)
                    .addComponent(jScrollPane2))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(processBTN)
                    .addComponent(didntBTN)
                    .addComponent(viewBTN))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(regTF, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel9)
                        .addComponent(priostatTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel8)
                        .addComponent(processedTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel10)
                        .addComponent(didntTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(14, 14, 14))
        );

        getAccessibleContext().setAccessibleDescription("");

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    /**
     * Set up file menu for load/save operations
     */
    
    private void addBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addBTNActionPerformed
    String id = idTF.getText().trim();
    String name = nameTF.getText().trim();
    String priority = prioTF.getText().trim();
    String ageText = ageTF.getText().trim();
    String gpDetails = "GP Details";
    
    // Validate input
    if (id.isEmpty() || name.isEmpty() || priority.isEmpty() || ageText.isEmpty()) {
        JOptionPane.showMessageDialog(this, "All fields are required", "Input Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    // Validate priority
    if (!priority.equalsIgnoreCase("urgent") && 
        !priority.equalsIgnoreCase("medium") && 
        !priority.equalsIgnoreCase("low")) {
        JOptionPane.showMessageDialog(this, "Priority must be 'urgent', 'medium', or 'low'", "Input Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    // Validate age
    int age;
    try {
        age = Integer.parseInt(ageText);
        if (age <= 0 || age > 120) {
            JOptionPane.showMessageDialog(this, "Age must be between 1 and 120", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Age must be a valid number", "Input Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    // Ask if patient is from hospital ward
    int response = JOptionPane.showConfirmDialog(this, "Is the patient from a hospital ward?", "Hospital Ward", JOptionPane.YES_NO_OPTION);
    boolean fromHospitalWard = (response == JOptionPane.YES_OPTION);
    
    // Create patient
    Patient newPatient = new Patient(id, name, priority, gpDetails, age, fromHospitalWard);
    
    // Add to appropriate queue
    if (priority.equalsIgnoreCase("urgent") || fromHospitalWard) {
        priorityQueue.add(newPatient);
    } else {
        regularQueue.enqueue(newPatient);
    }
    
    // Clear input fields
    idTF.setText("");
    nameTF.setText("");
    ageTF.setText("");
    prioTF.setText("");
    
    // Update UI
    updateQueueDisplays();
    updateStatistics();
    
    // Save to file
    saveToFile();
    
    JOptionPane.showMessageDialog(this, "Patient added successfully");   
    }//GEN-LAST:event_addBTNActionPerformed

    private void processBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_processBTNActionPerformed
    Patient patientToProcess = null;
    
    // Process from priority queue first if not empty
    if (!priorityQueue.isEmpty()) {
        patientToProcess = priorityQueue.dequeue();
    }
    // Then check regular queue
    else if (!regularQueue.isEmpty()) {
        patientToProcess = regularQueue.dequeue();
    }
    
    if (patientToProcess != null) {
        // Add to processed patients
        processedPatients.add(patientToProcess);
        
        // Update UI
        updateQueueDisplays();
        updateStatistics();
        
        // Save changes
        saveToFile();
        
        JOptionPane.showMessageDialog(this, 
            "Processed Patient:" + 
            "\nID: " + patientToProcess.getId() + 
            "\nName: " + patientToProcess.getName() + 
            "\nPriority: " + patientToProcess.getPriority(), 
            "Patient Processed", 
            JOptionPane.INFORMATION_MESSAGE);
    } else {
        JOptionPane.showMessageDialog(this, "No patients in queue to process", "Empty Queue", JOptionPane.WARNING_MESSAGE);
    }
    }//GEN-LAST:event_processBTNActionPerformed

    private void didntBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_didntBTNActionPerformed
        if (priorityQueue.isEmpty() && regularQueue.isEmpty()) {
        JOptionPane.showMessageDialog(this, "No patients in queue to mark as no-show", "Empty Queue", JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    // Get ID of patient who didn't show
    String patientId = JOptionPane.showInputDialog(this, "Enter ID of patient who didn't show:");
    if (patientId == null || patientId.trim().isEmpty()) {
        return; // User cancelled or entered empty string
    }
    
    // Search in priority queue and regular queue
    Patient noShowPatient = findAndRemovePatientById(patientId);
    
    if (noShowPatient != null) {
        // Add to no-show tracker
        noShowTracker.addNoShow(noShowPatient);
        
        // Update UI
        updateQueueDisplays();
        updateStatistics();
        
        // Save changes
        saveToFile();
        
        JOptionPane.showMessageDialog(this, 
            "Patient marked as no-show:" + 
            "\nID: " + noShowPatient.getId() + 
            "\nName: " + noShowPatient.getName(), 
            "No-Show Recorded", 
            JOptionPane.INFORMATION_MESSAGE);
    } else {
        JOptionPane.showMessageDialog(this, "Patient with ID " + patientId + " not found in queue", "Patient Not Found", JOptionPane.WARNING_MESSAGE);
    }
    }//GEN-LAST:event_didntBTNActionPerformed

    private void viewBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewBTNActionPerformed
        if (processedPatients.isEmpty()) {
        JOptionPane.showMessageDialog(this, "No patients have been processed yet", "No Processed Patients", JOptionPane.INFORMATION_MESSAGE);
        return;
    }
    
    StringBuilder sb = new StringBuilder("Processed Patients:\n\n");
    
    // Use recursion to show processed patients (as required)
    appendProcessedPatients(sb, 0);
    
    JOptionPane.showMessageDialog(this, sb.toString(), "Processed Patients", JOptionPane.INFORMATION_MESSAGE);

    }//GEN-LAST:event_viewBTNActionPerformed

    /** 
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(BloodTestSchedulerGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BloodTestSchedulerGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BloodTestSchedulerGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BloodTestSchedulerGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new BloodTestSchedulerGUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addBTN;
    private javax.swing.JTextField ageTF;
    private javax.swing.JButton didntBTN;
    private javax.swing.JTextField didntTF;
    private javax.swing.JTextField idTF;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextField nameTF;
    private javax.swing.JTextArea prioTA;
    private javax.swing.JTextField prioTF;
    private javax.swing.JTextField priostatTF;
    private javax.swing.JButton processBTN;
    private javax.swing.JTextField processedTF;
    private javax.swing.JTextArea regTA;
    private javax.swing.JTextField regTF;
    private javax.swing.JButton viewBTN;
    // End of variables declaration//GEN-END:variables
}
