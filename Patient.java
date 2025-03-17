/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bloodtestscheduler;

/**
 *
 * @author joshu
 */
public class Patient {
    private String id;
    private String name;
    private String priority; // High, Medium, Low
    private String gpDetails;
    private int age;
    private boolean fromHospitalWard;

    public Patient(String id, String name, String priority, String gpDetails, int age, boolean fromHospitalWard) {
        this.id = id;
        this.name = name;
        this.priority = priority;
        this.gpDetails = gpDetails;
        this.age = age;
        this.fromHospitalWard = fromHospitalWard;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getGpDetails() {
        return gpDetails;
    }

    public void setGpDetails(String gpDetails) {
        this.gpDetails = gpDetails;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isFromHospitalWard() {
        return fromHospitalWard;
    }

    public void setFromHospitalWard(boolean fromHospitalWard) {
        this.fromHospitalWard = fromHospitalWard;
    }
    
    
    @Override
    public String toString() {
        return "Patient: " + "\nID: " + id + "\nName:" + name + "\nPriority: " + priority + "\ngpDetails: " + gpDetails + "\nAge: " + age + "fromHospitalWard: " + (fromHospitalWard ? "Yes" : "No") + ")";
    }
    
}
