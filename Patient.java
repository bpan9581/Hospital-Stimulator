/*Brian Pan 112856241 Recitation 02*/

import java.io.Serializable;

/**
 * Patient class that tells us what organ the patient donates, their blood type, and puts an index on them
 */
public class Patient implements Serializable, Comparable {
    private String name, organ;
    private int age, id = 0;
    private BloodType bloodType;
    private boolean isDonor;
    private int donorAmount= 0;
    private static int patientId = -1, donorId= -1;

    public int getDonorAmount() {
        return donorAmount;
    }

    public void setDonorAmount(int donorAmount) {
        this.donorAmount = donorAmount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrgan() {
        return organ;
    }

    public void setOrgan(String organ) {
        this.organ = organ;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BloodType getBloodType() {
        return bloodType;
    }

    public void setBloodType(BloodType bloodType) {
        this.bloodType = bloodType;
    }

    public boolean isDonor() {
        return isDonor;
    }

    public void setDonor(boolean donor) {
        isDonor = donor;
    }

    /**
     * no args constructor
     */
    public Patient(){}

    /**
     * args constructor
     * @param name
     * name of patient
     * @param bloodType
     * blood type of patient
     * @param age
     * age of patient
     * @param organ
     * organ needed or donated
     * @param isDonor
     * checks to see if patient is donor or not
     */
    public Patient(String name, BloodType bloodType, int age, String organ, boolean isDonor) {
        this.name = name;
        this.bloodType = bloodType;
        this.age = age;
        this.organ = organ;
        if (isDonor) {
            donorId++;
            id = donorId;
        } else {
            patientId++;
            id = patientId;
        }
    }

    /**
     * compares patient id
     * @param o
     * Object class
     * @return
     * returns whether the two Patients are identical
     */
    public int compareTo(Object o){
        Patient otherPatient = (Patient) o;
        return Integer.compare(this.id, otherPatient.id);
    }

    /**
     * To string method
     * @return
     * String notation of Patient object
     */
    public String toString(){
        return String.format("%4d%3s%-19s%s%-4d%s%-14s%s%-7s%s", id, "| ", name, "| ", age, "| ", organ,
                "|     ", bloodType, "| ");
    }
}
