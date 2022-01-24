/*Brian Pan 112856241 Recitation 02*/

import java.util.Comparator;

/**
 * Comparator class comparing organ type of Patient
 */
public class OrganComparator implements Comparator<Patient> {
    public int compare(Patient o1, Patient o2) {
        return o1.getOrgan().compareToIgnoreCase(o2.getOrgan());
    }
}
