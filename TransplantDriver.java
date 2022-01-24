/*Brian Pan 112856241 Recitation 02*/

import java.io.*;
import java.util.*;

/**
 * Creates and serializes a file that contains logs of patients receiving and donating organs
 */
public class TransplantDriver{
    private final String DONOR_FILE = "donors.txt";
    private final String RECIPIENT_FILE = "recipient.txt";

    public static void main(String[] args) throws IOException {
        TransplantGraph transplantObj = TransplantGraph.buildFromFiles("donors.txt", "recipients.txt");
        try {
            FileInputStream file = new FileInputStream("transplant.obj");
            ObjectInputStream fin  = new ObjectInputStream(file);
            TransplantGraph obj = (TransplantGraph) fin.readObject();
            fin.close();}
        catch(IOException | ClassNotFoundException e) {
            System.out.println("transplant.obj not found. Creating new TransplantGraph object");
        }
        try {
            System.out.println("Loading data from 'donors.txt'");
            System.out.println("Loading data from 'recipients.txt'");
            Scanner stdin = new Scanner(System.in);
            String choice;
            Patient newPatient;
            boolean quit = false;
            while (!quit) {
                System.out.println("Menu:\n" +
                        "    (LR) - List all recipients\n" +
                        "    (LO) - List all donors\n" +
                        "    (AO) - Add new donor\n" +
                        "    (AR) - Add new recipient\n" +
                        "    (RO) - Remove donor\n" +
                        "    (RR) - Remove recipient\n" +
                        "    (SR) - Sort recipients\n" +
                        "    (SO) - Sort donors\n" +
                        "    (Q) - Quit\n" +
                        " \n" +
                        "Please select an option:");
                choice = stdin.nextLine().toUpperCase();
                String name, age, organ, bloodType;
                BloodType newBloodType;
                switch (choice) {
                    case "LR":
                        System.out.println("Index| Recipient Name     | Age | Organ Needed  | Blood Type | Donor ID\n" +
                                "========================================================================");
                        transplantObj.printAllRecipient();
                        break;
                    case "LO":
                        System.out.println("Index| Recipient Name     | Age | Organ Needed  | Blood Type | Donor IDs\n" +
                                "=========================================================================");
                        transplantObj.printAllDonor();
                        break;
                    case "AO":
                        System.out.println("Please enter the organ donor name:");
                        name = stdin.nextLine();
                        System.out.println("Please enter the organs " + name + " is donating:");
                        organ = stdin.nextLine();
                        System.out.println("Please enter the blood type of " + name + ":");
                        bloodType = stdin.nextLine().toUpperCase();
                        if(!(bloodType.compareToIgnoreCase("a") == 0 || bloodType.compareToIgnoreCase("b") == 0
                        ||bloodType.compareToIgnoreCase("ab") == 0|| bloodType.compareToIgnoreCase("o") == 0))
                            throw new InvalidBloodTypeException();
                        System.out.println("Please enter the age of " + name + ":");
                        age = stdin.nextLine();
                        newBloodType = new BloodType(bloodType);
                        newPatient = new Patient(name, newBloodType, Integer.parseInt(age), organ, true);
                        transplantObj.addDonor(newPatient);
                        System.out.println("The organ donor with ID " +
                                transplantObj.getDonors().get(transplantObj.getDonors().size() - 1).getId() +
                                " was successfully added to the donor list!");
                        break;
                    case "AR":
                        System.out.println("Please enter the new recipient's name:");
                        name = stdin.nextLine();
                        System.out.println("Please enter the recipient's blood type:");
                        bloodType = stdin.nextLine().toUpperCase();
                        System.out.println("Please enter the recipient's age:");
                        age = stdin.nextLine();
                        System.out.println("Please enter organ needed:");
                        organ = stdin.nextLine();
                        newBloodType = new BloodType(bloodType);
                        newPatient = new Patient(name, newBloodType, Integer.parseInt(age), organ, false);
                        transplantObj.addRecipient(newPatient);
                        System.out.println("The organ donor with ID " +
                                transplantObj.getRecipients().get(transplantObj.getRecipients().size() - 1).getName() +
                                " is now on the organ transplant waitlist!");
                        break;
                    case "RO":
                        System.out.println("Please enter the name of the organ donor to remove:");
                        name = stdin.nextLine();
                        transplantObj.removeDonor(name);
                        System.out.println(name + " was removed from the organ donor list");
                        break;
                    case "RR":
                        System.out.println("Please enter the name of the recipient to remove:");
                        name = stdin.nextLine();
                        transplantObj.removeRecipient(name);
                        System.out.println(name + " was removed from the organ transplant waitlist");
                        break;
                    case "SR":
                        transplantObj.sortRecipients();
                        break;
                    case "SO":
                        transplantObj.sortDonors();
                        break;
                    case "Q":
                        FileOutputStream f = new FileOutputStream("transplant.obj");
                        ObjectOutputStream fout = new ObjectOutputStream(f);

                        fout.writeObject(transplantObj);
                        fout.close();
                        quit = true;
                        break;
                    default:
                        System.out.println("Invalid input.");
                }
            }
        }catch (InvalidBloodTypeException ex){
            System.out.println("Blood type entered is invalid");
        }catch (InputMismatchException ex){
            System.out.println("Invalid value entered");
        }
    }
}
