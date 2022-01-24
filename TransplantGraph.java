/*Brian Pan 112856241 Recitation 02*/

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Transplant Graph object that adds and removes patients from a graph
 */
public class TransplantGraph implements Serializable {
    private ArrayList<Patient> donors = new ArrayList<>(), originalDonors = new ArrayList<>();
    private ArrayList<Patient> recipients = new ArrayList<>(), originalRecipients = new ArrayList<>();
    boolean[][] connections = new boolean[MAX_PATIENTS][MAX_PATIENTS];
    private static final int MAX_PATIENTS = 100;

    public ArrayList<Patient> getDonors() {
        return donors;
    }

    public void setDonors(ArrayList<Patient> donors) {
        this.donors = donors;
    }

    public ArrayList<Patient> getRecipients() {
        return recipients;
    }

    public void setRecipients(ArrayList<Patient> recipients) {
        this.recipients = recipients;
    }

    public boolean[][] getConnections() {
        return connections;
    }

    public void setConnections(boolean[][] connections) {
        this.connections = connections;
    }

    /**
     * No args constructor
     */
    public TransplantGraph() {
    }

    /**
     * Adds predetermined data from text files to the graph
     * @param donorFile
     * File of donors
     * @param recipientFIle
     * FIle of recipients
     * @return
     * returns a graph of patients
     * @throws IOException
     */
    public static TransplantGraph buildFromFiles(String donorFile, String recipientFIle) throws IOException {
        TransplantGraph premade = new TransplantGraph();
        FileInputStream fis = new FileInputStream(donorFile);
        InputStreamReader instream = new InputStreamReader(fis);
        BufferedReader reader = new BufferedReader(instream);
        String patient;
        String[] values;
        Patient newPatient;
        while ((patient = reader.readLine()) != null) {
            values = patient.split(", ");
            BloodType bloodType = new BloodType(values[4]);
            newPatient = new Patient(values[1], bloodType, Integer.parseInt(values[2]), values[3], true);
            premade.addDonor(newPatient);
        }
        fis = new FileInputStream(recipientFIle);
        instream = new InputStreamReader(fis);
        reader = new BufferedReader(instream);
        while ((patient = reader.readLine()) != null) {
            values = patient.split(", ");
            BloodType bloodType = new BloodType(values[4]);
            newPatient = new Patient(values[1], bloodType, Integer.parseInt(values[2]), values[3], false);
            premade.addRecipient(newPatient);
        }
        return premade;
    }

    /**
     * Add patients that needs organs
     * @param patient
     * Object of patient
     */
    public void addRecipient(Patient patient) {
        OrganComparator o = new OrganComparator();
        recipients.add(patient);
        if (donors.size() != 0) {
            for (int i = 0; i < donors.size(); i++) {
                connections[recipients.size() - 1][i] =
                        (BloodType.isCompatible(patient.getBloodType(),
                                donors.get(i).getBloodType()) && o.compare(patient, donors.get(i)) == 0);
                if (connections[recipients.size() - 1][i]) {
                    recipients.get(recipients.size() - 1).setDonorAmount(
                            recipients.get(recipients.size() - 1).getDonorAmount() + 1);
                }
            }
        }
    }

    /**\
     * Add donors that are donating organs
     * @param patient
     * Object of patient
     */
    public void addDonor(Patient patient) {
        OrganComparator o = new OrganComparator();
        donors.add(patient);
        if (recipients.size() != 0) {
            for (int i = 0; i < recipients.size(); i++) {
                connections[i][donors.size() - 1] =
                        (BloodType.isCompatible(recipients.get(i).getBloodType(), patient.getBloodType()) &&
                                o.compare(patient, recipients.get(i)) == 0);
                if (connections[i][donors.size() - 1]) {
                    recipients.get(i).setDonorAmount(recipients.get(i).getDonorAmount() + 1);
                }
            }
        }
    }

    /**
     * Removes a recipient from the graph
     * @param name
     * name of recipient
     */
    public void removeRecipient(String name) {
        OrganComparator o = new OrganComparator();
        boolean removed = false;
        int i = 0;
        while (!removed && !(i == recipients.size())) {
            if (recipients.get(i).getName().equalsIgnoreCase(name)) {
                Patient temp = recipients.get(i);
                for (Patient donor : donors) {
                    if (BloodType.isCompatible(donor.getBloodType(), temp.getBloodType())
                            && o.compare(temp, donor) == 0) {
                        donor.setDonorAmount(donor.getDonorAmount() - 1);
                    }
                }
                recipients.remove(i);
                for (int j = i + 1; j < recipients.size(); j++) {
                    recipients.set(i, recipients.get(j));
                    recipients.get(j).setId(recipients.get(j).getId() - 1);
                }
                removed = true;
            }
            i++;
        }
    }

    /**
     * Removes a donor from the graph
     * @param name
     * name of donor
     */
    public void removeDonor(String name) {
        OrganComparator o = new OrganComparator();
        boolean removed = false;
        int i = 0;
        while (!removed && !(i == donors.size())) {
            if (donors.get(i).getName().equalsIgnoreCase(name)) {
                Patient temp = donors.get(i);
                for (Patient recipient : recipients) {
                    if (BloodType.isCompatible(recipient.getBloodType(), temp.getBloodType())
                            && o.compare(temp, recipient) == 0) {
                        recipient.setDonorAmount(recipient.getDonorAmount() - 1);
                    }
                }
                donors.remove(i);
                for (int j = i + 1; j < donors.size(); j++) {
                    donors.set(i, donors.get(j));
                    donors.get(j).setId(donors.get(j).getId() - 1);
                }
                removed = true;
            }
            i++;
        }
    }

    /**
     * Prints a table of recipients
     */
    public void printAllRecipient() {
        String donor;
        OrganComparator o = new OrganComparator();
        for (int i = 0; i < recipients.size(); i++)
            for (int j = 0; j < donors.size(); j++)
                connections[i][j] =
                        (BloodType.isCompatible(recipients.get(i).getBloodType(),
                                donors.get(j).getBloodType()) && o.compare(recipients.get(i), donors.get(j)) == 0);
        for (int i = 0; i < recipients.size(); i++) {
            donor = "";
            for (int j = 0; j < donors.size(); j++) {
                if (connections[i][j])
                    donor += donors.get(j).getId() + " ";
            }
            System.out.println(recipients.get(i) + donor);
        }
    }

    /**
     * Prints a table of donors
     */
    public void printAllDonor() {
        String recipient;
        for (int i = 0; i < donors.size(); i++) {
            recipient = "";
            for (int j = 0; j < recipients.size(); j++) {
                if (connections[j][i])
                    recipient += donors.get(j).getId() + " ";
            }
            System.out.println(donors.get(i) + recipient);
        }
    }

    /**
     * Sorts recipients in certain orders
     */
    public void sortRecipients() {
        BloodTypeComparator b = new BloodTypeComparator();
        OrganComparator o = new OrganComparator();
        NumConnectionComparator n = new NumConnectionComparator();
        originalRecipients = recipients;
        originalDonors = donors;
        boolean quit = false;
        String choice;
        Scanner stdin = new Scanner(System.in);
        while (!quit) {
            System.out.println("(I) Sort by ID\n" +
                    "    (N) Sort by Number of Donors\n" +
                    "    (B) Sort by Blood Type\n" +
                    "    (O) Sort by Organ Needed\n" +
                    "    (Q) Back to Main Menu\n" +
                    "\n" +
                    "Please select an option:");
            choice = stdin.nextLine().toUpperCase();
            Patient temp;
            switch (choice) {
                case "I":
                    for (int i = 0; i < recipients.size() - 1; i++)
                        for (int j = i + 1; j < recipients.size(); j++) {
                            if (recipients.get(i).compareTo(recipients.get(j)) > 0) {
                                temp = recipients.get(i);
                                recipients.set(i, recipients.get(j));
                                recipients.set(j, temp);
                            }
                        }
                    for (int i = 0; i < recipients.size(); i++)
                        for (int j = 0; j < donors.size(); j++)
                            connections[i][j] =
                                    (BloodType.isCompatible(recipients.get(i).getBloodType(),
                                            donors.get(j).getBloodType()) && o.compare(recipients.get(i), donors.get(j)) == 0);
                    System.out.println("Index | Recipient Name   " +
                            "  | Age | Organ needed | Blood Type | Donor IDs\n" +
                            "=========================================================================");
                    printAllRecipient();
                    break;
                case "N":
                    for (int i = 0; i < recipients.size() - 1; i++)
                        for (int j = i + 1; j < recipients.size(); j++) {
                            if (n.compare(recipients.get(i), recipients.get(j)) > 0) {
                                temp = recipients.get(i);
                                recipients.set(i, recipients.get(j));
                                recipients.set(j, temp);
                            }
                        }
                    for (int i = 0; i < recipients.size(); i++)
                        for (int j = 0; j < donors.size(); j++)
                            connections[i][j] =
                                    (BloodType.isCompatible(recipients.get(i).getBloodType(),
                                            donors.get(j).getBloodType()) && o.compare(recipients.get(i), donors.get(j)) == 0);
                    System.out.println("Index | Recipient Name   " +
                            "  | Age | Organ needed | Blood Type | Donor IDs\n" +
                            "=========================================================================");
                    printAllRecipient();
                    break;
                case "B":
                    for (int i = 0; i < recipients.size() - 1; i++)
                        for (int j = i + 1; j < recipients.size(); j++) {
                            if (b.compare(recipients.get(i), recipients.get(j)) > 0) {
                                temp = recipients.get(i);
                                recipients.set(i, recipients.get(j));
                                recipients.set(j, temp);
                            }
                        }
                    for (int i = 0; i < recipients.size(); i++)
                        for (int j = 0; j < donors.size(); j++)
                            connections[i][j] =
                                    (BloodType.isCompatible(recipients.get(i).getBloodType(),
                                            donors.get(j).getBloodType()) && o.compare(recipients.get(i), donors.get(j)) == 0);
                    System.out.println("Index | Recipient Name   " +
                            "  | Age | Organ needed | Blood Type | Donor IDs\n" +
                            "=========================================================================");
                    printAllRecipient();
                    break;
                case "O":
                    for (int i = 0; i < recipients.size() - 1; i++)
                        for (int j = i + 1; j < recipients.size(); j++) {
                            if (o.compare(recipients.get(i), recipients.get(j)) > 0) {
                                temp = recipients.get(i);
                                recipients.set(i, recipients.get(j));
                                recipients.set(j, temp);
                            }
                        }
                    for (int i = 0; i < recipients.size(); i++)
                        for (int j = 0; j < donors.size(); j++)
                            connections[i][j] =
                                    (BloodType.isCompatible(recipients.get(i).getBloodType(),
                                            donors.get(j).getBloodType()) && o.compare(recipients.get(i), donors.get(j)) == 0);
                    System.out.println("Index | Recipient Name   " +
                            "  | Age | Organ needed | Blood Type | Donor IDs\n" +
                            "=========================================================================");
                    printAllRecipient();
                    break;
                case "Q":
                    System.out.println("Returning back to main menu");
                    for (int i = 0; i < recipients.size() - 1; i++)
                        for (int j = i + 1; j < recipients.size(); j++) {
                            if (recipients.get(i).compareTo(recipients.get(j)) > 0) {
                                temp = recipients.get(i);
                                recipients.set(i, recipients.get(j));
                                recipients.set(j, temp);
                            }
                        }
                    for (int i = 0; i < recipients.size(); i++)
                        for (int j = 0; j < donors.size(); j++)
                            connections[i][j] =
                                    (BloodType.isCompatible(recipients.get(i).getBloodType(),
                                            donors.get(j).getBloodType()) && o.compare(recipients.get(i), donors.get(j)) == 0);
                    quit = true;
                    break;
                default:
                    System.out.println("Invalid input");
            }
        }
    }

    /**
     * Sort donors in certain orders
     */
    public void sortDonors() {
        BloodTypeComparator b = new BloodTypeComparator();
        OrganComparator o = new OrganComparator();
        NumConnectionComparator n = new NumConnectionComparator();
        originalDonors = donors;
        originalRecipients = recipients;
        boolean quit = false;
        String choice;
        Scanner stdin = new Scanner(System.in);
        while (!quit) {
            System.out.println("(I) Sort by ID\n" +
                    "    (N) Sort by Number of Donors\n" +
                    "    (B) Sort by Blood Type\n" +
                    "    (O) Sort by Organ Needed\n" +
                    "    (Q) Back to Main Menu\n" +
                    "\n" +
                    "Please select an option:");
            choice = stdin.nextLine().toUpperCase();
            Patient temp;
            switch (choice) {
                case "I":
                    for (int i = 0; i < donors.size() - 1; i++)
                        for (int j = i + 1; j < donors.size(); j++) {
                            if (donors.get(i).compareTo(donors.get(j)) > 0) {
                                temp = donors.get(i);
                                donors.set(i, donors.get(j));
                                donors.set(j, temp);
                            }
                        }
                    for (int i = 0; i < recipients.size(); i++)
                        for (int j = 0; j < donors.size(); j++)
                            connections[i][j] =
                                    (BloodType.isCompatible(recipients.get(i).getBloodType(),
                                            donors.get(j).getBloodType()) && o.compare(recipients.get(i), donors.get(j)) == 0);
                    System.out.println("Index | Donor Name         " +
                            "| Age | Organ Donated | Blood Type | Recipient IDs\n" +
                            "=============================================================================");
                    printAllRecipient();
                    break;
                case "N":
                    for (int i = 0; i < donors.size() - 1; i++)
                        for (int j = i + 1; j < donors.size(); j++) {
                            if (n.compare(donors.get(i), donors.get(j)) > 0) {
                                temp = donors.get(i);
                                donors.set(i, donors.get(j));
                                donors.set(j, temp);
                            }
                        }
                    for (int i = 0; i < recipients.size(); i++)
                        for (int j = 0; j < donors.size(); j++)
                            connections[i][j] =
                                    (BloodType.isCompatible(recipients.get(i).getBloodType(),
                                            donors.get(j).getBloodType()) && o.compare(recipients.get(i), donors.get(j)) == 0);
                    System.out.println("Index | Donor Name         " +
                            "| Age | Organ Donated | Blood Type | Recipient IDs\n" +
                            "=============================================================================");
                    printAllRecipient();
                    break;
                case "B":
                    for (int i = 0; i < donors.size() - 1; i++)
                        for (int j = i + 1; j < donors.size(); j++) {
                            if (b.compare(donors.get(i), donors.get(j)) > 0) {
                                temp = donors.get(i);
                                donors.set(i, donors.get(j));
                                donors.set(j, temp);
                            }
                        }
                    for (int i = 0; i < recipients.size(); i++)
                        for (int j = 0; j < donors.size(); j++)
                            connections[i][j] =
                                    (BloodType.isCompatible(recipients.get(i).getBloodType(),
                                            donors.get(j).getBloodType()) && o.compare(recipients.get(i), donors.get(j)) == 0);
                    System.out.println("Index | Donor Name         " +
                            "| Age | Organ Donated | Blood Type | Recipient IDs\n" +
                            "=============================================================================");
                    printAllRecipient();
                    break;
                case "O":
                    for (int i = 0; i < donors.size() - 1; i++)
                        for (int j = i + 1; j < donors.size(); j++) {
                            if (o.compare(donors.get(i), donors.get(j)) > 0) {
                                temp = donors.get(i);
                                donors.set(i, donors.get(j));
                                donors.set(j, temp);
                            }
                        }
                    for (int i = 0; i < recipients.size(); i++)
                        for (int j = 0; j < donors.size(); j++)
                            connections[i][j] =
                                    (BloodType.isCompatible(recipients.get(i).getBloodType(),
                                            donors.get(j).getBloodType()) && o.compare(recipients.get(i), donors.get(j)) == 0);
                    System.out.println("Index | Donor Name         " +
                            "| Age | Organ Donated | Blood Type | Recipient IDs\n" +
                            "=============================================================================");
                    printAllRecipient();
                    break;
                case "Q":
                    for (int i = 0; i < donors.size() - 1; i++)
                        for (int j = i + 1; j < donors.size(); j++) {
                            if (donors.get(i).compareTo(donors.get(j)) > 0) {
                                temp = donors.get(i);
                                donors.set(i, donors.get(j));
                                donors.set(j, temp);
                            }
                        }
                    for (int i = 0; i < recipients.size(); i++)
                        for (int j = 0; j < donors.size(); j++)
                            connections[i][j] =
                                    (BloodType.isCompatible(recipients.get(i).getBloodType(),
                                            donors.get(j).getBloodType()) && o.compare(recipients.get(i), donors.get(j)) == 0);
                    System.out.println("Returning back to main menu");
                    quit = true;
                    break;
                default:
                    System.out.println("Invalid input");
                    break;
            }
        }
    }
}