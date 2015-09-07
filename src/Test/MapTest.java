package Test;

import str_rec.Pair;

import java.util.*;

/**
 * Created by edwardlol on 15/9/6.
 */
public class MapTest {
    public static void main(String[] args) {
        LinkedHashMap<String, HashSet<Pair>> map = new LinkedHashMap<>();
        Pair pair1 = new Pair("id1", "n1");
        Pair pair2 = new Pair("id2", "n2");
        Pair pair3 = new Pair("id3", "n3");
        Pair pair4 = new Pair("id4", "n4");
        HashSet<Pair> pairs1 = new HashSet<>();
        pairs1.add(pair1);
        pairs1.add(pair2);
        HashSet<Pair> pairs2 = new HashSet<>();
        pairs2.add(pair3);
        HashSet<Pair> pairs3 = new HashSet<>();
        pairs3.add(pair4);
        map.put("brand1", pairs1);
        //map.put("brand2", pairs2);
        //map.put("brand3", pairs3);

        String out = map.keySet().toString();
        List<String> l = new ArrayList<>(map.keySet());
        System.out.println(l.get(0));

        Iterator<Map.Entry<String, HashSet<Pair>>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, HashSet<Pair>> entry = it.next();
            System.out.print(entry.getKey() + ": ");
            Iterator<Pair> it2 = entry.getValue().iterator();
            while (it2.hasNext()) {
                System.out.print(it2.next().toString());
                if (it2.hasNext()) {
                    System.out.print(", ");
                }
            }
            if (it.hasNext()) {
                System.out.print(", ");
            }
        }
    }
}
