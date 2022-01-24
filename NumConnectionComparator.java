/*Brian Pan 112856241 Recitation 02*/

import java.util.Comparator;

/**
 * Comparator class comparing how many compatible organs donors/receivers of Patient
 */
public class NumConnectionComparator implements Comparator<Patient> {
    public int compare(Patient o1, Patient o2) {
        return o1.getDonorAmount() - o2.getDonorAmount();
    }
}
