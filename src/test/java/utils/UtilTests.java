package utils;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import org.junit.Test;
import util.EntryComp;

import java.util.List;

/**
 * Created by edwardlol on 17-4-17.
 */
public class UtilTests {
    @Test
    public void multisetSortTest() {
        Multiset<Integer> multiset = HashMultiset.create();

        multiset.add(1, 3);
        multiset.add(2, 2);

        List<Multiset.Entry<Integer>> sortedAcc = EntryComp.getEntriesSortedByFrequency(multiset, true);
        List<Multiset.Entry<Integer>> sortedDec = EntryComp.getEntriesSortedByFrequency(multiset, false);
        System.out.println(sortedAcc);
        System.out.println(sortedDec);
    }
}
