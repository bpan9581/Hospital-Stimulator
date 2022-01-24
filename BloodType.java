/*Brian Pan 112856241 Recitation 02*/

import java.io.Serializable;

/**
 * Bloodtype class that carries the blood type for a patient
 */
public class BloodType implements Serializable {
    public String bloodType;

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    /**
     * no args constructor
     */
    public BloodType(){}

    /**
     * args constructor
     * @param bloodType
     * returns string blood type
     */
    public BloodType(String bloodType){
        this.bloodType = bloodType;
    }

    /**
     * boolean method that compares blood type
     * @param recipient
     * Patient object
     * @param donor
     * Patient object
     * @return
     * boolean
     */
    public static boolean isCompatible(BloodType recipient, BloodType donor) {
        switch (donor.bloodType) {
            case "O":
                return true;
            case "A":
                return recipient.bloodType.equals("A") || recipient.bloodType.equals("AB");
            case "B":
                return recipient.bloodType.equals("B") || recipient.bloodType.equals("AB");
            case "AB":
                return recipient.bloodType.equals("AB");
            default:
                return false;
        }
    }

    /**
     * To string method for BloodType object
     * @return
     * String notation of object
     */
    public String toString(){
        return bloodType;
    }
}
