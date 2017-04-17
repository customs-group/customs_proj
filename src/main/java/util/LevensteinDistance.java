package util;

/**
 * Compute Levenshtein distance:
 * see org.apache.commons.lang.StringUtils#getLevenshteinDistance(String, String)
 */
public final class LevensteinDistance {

    public float getDistance (String string1, String string2) {
        char[] s1_array = string1.toCharArray();
        final int s1_len = string1.length();
        final int s2_len = string2.length();

        // 'previous' cost array, horizontally
        int p[] = new int[s1_len + 1];

        // cost array, horizontally
        int d[] = new int[s1_len + 1];

        int s1_added = 0, s2_added = 0;

        // check if len == 0
        if (s1_len == 0 || s2_len == 0) {
            if (s1_len == s2_len) {
                return 1;
            } else {
                return 0;
            }
        }

        for (int i = 0; i <= s1_len; i++) {
            p[i] = i;
        }

        for (int j = 1; j <= s2_len; j++) {
            char s2_j = string2.charAt(j - 1);
            d[0] = j;

            for (int i = 1; i <= s1_len; i++) {
                int cost = s1_array[i - 1] == s2_j ? 0 : 1;
                // minimum of cell to the left+1, to the top+1, diagonally left and up +cost
                d[i] = Math.min(Math.min(d[i - 1] + 1, p[i] + 1), p[i - 1] + cost);

                if (cost == 0
                        && (p[i - 1] + cost <= Math.min(d[i - 1] + 1, p[i] + 1))) {
                    if (i > j) {
                        s1_added++;
                    } else if (i < j) {
                        s2_added++;
                    }
                }
            }
            // copy current distance counts to 'previous row' distance counts
            int _d[] = p;
            p = d;
            d = _d;
        }
        // our last action in the above loop was to switch d and p, so p now
        // actually has the most recent cost counts
//        return 1.0f - ((float) p[s1_len] / Math.max(s2_len, s1_len));

        /// output
        double temp;
        if (s1_added == 0) {
            temp = s2_added;
        } else if (s2_added == 0) {
            temp = s1_added;
        } else {
            temp = Math.min(s1_added, s2_added);
        }
        double result = p[s1_len] - temp * 0.5;
        // debug info
        System.out.println("s1_added = " + s1_added + "; s2_added = " + s2_added + "; result = " + result);
        return 1.0f - ((float) result / Math.max(s2_len, s1_len));
    }
}