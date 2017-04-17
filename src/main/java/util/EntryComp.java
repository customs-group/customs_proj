package util;

import com.google.common.collect.Lists;
import com.google.common.collect.Multiset;
import com.google.common.primitives.Ints;

import java.util.Comparator;
import java.util.List;

/**
 * Created by edwardlol on 17-4-17.
 */
public enum EntryComp implements Comparator<Multiset.Entry<?>> {
    DESCENDING {
        @Override
        public int compare(final Multiset.Entry<?> a, final Multiset.Entry<?> b) {
            return Ints.compare(b.getCount(), a.getCount());
        }
    },
    ASCENDING {
        @Override
        public int compare(final Multiset.Entry<?> a, final Multiset.Entry<?> b) {
            return Ints.compare(a.getCount(), b.getCount());
        }
    };

    public static <E> List<Multiset.Entry<E>> getEntriesSortedByFrequency(
            final Multiset<E> ms, final boolean ascending) {
        final List<Multiset.Entry<E>> entryList = Lists.newArrayList(ms.entrySet());
        entryList.sort(ascending
                ? EntryComp.ASCENDING
                : EntryComp.DESCENDING);
        return entryList;
    }

}