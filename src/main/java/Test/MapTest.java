package Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by edwardlol on 15/9/6.
 */
public class MapTest {
    public static void main(String[] args) {
        HashMap<String, HashMap<String, Integer>> brand_to_id = new HashMap<>();
        HashMap<String, Integer> id_map1 = new HashMap<>();
        HashMap<String, Integer> id_map2 = new HashMap<>();
        HashMap<String, Integer> id_map3 = new HashMap<>();
        id_map1.put("accord", 1);
        id_map1.put("civic", 2);
        id_map2.put("s3", 3);
        id_map2.put("m4", 4);
        id_map2.put("m6", 5);
        id_map3.put("amg c63", 6);
        id_map3.put("slk 200", 7);
        brand_to_id.put("honda", id_map1);
        brand_to_id.put("bmw", id_map2);
        brand_to_id.put("benz", id_map3);

        String max_brand = "";
        int max_count = 0;
/*
        int m_count = brand_to_id.entrySet().stream().mapToInt(brand_entry -> {
            String brand = brand_entry.getKey();
            HashMap<String, Integer> id_map = brand_entry.getValue();
            int count = id_map.entrySet().stream().mapToInt(HashMap.Entry::getValue).sum();
            return count;
        });
*/
        for (Map.Entry<String, HashMap<String, Integer>> brand_entry : brand_to_id.entrySet()) {
            String brand = brand_entry.getKey();
            HashMap<String, Integer> id_map = brand_entry.getValue();

            int count = id_map.entrySet().stream().mapToInt(HashMap.Entry::getValue).sum();
            if (count > max_count) {
                max_brand = brand;
                max_count = count;
            }
        }
        System.out.print(max_brand + ": " + max_count);

    }
}
