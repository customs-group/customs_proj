package util;

public final class LevensteinDistance {  
	   
    public LevensteinDistance () {  
    }  
      
// Compute Levenshtein distance:   
// see org.apache.commons.lang.StringUtils#getLevenshteinDistance(String, String)  
    
    public float getDistance (String target, String other) {
      char[] sa;
      int n;
      int p[];
      // 'previous' cost array, horizontally
      int d[];
      // cost array, horizontally
      int _d[];
      // placeholder to assist in swapping p and d
      sa = target.toCharArray();
      n = sa.length;
      p = new int[n+1];
      d = new int[n+1];
      
      final int m = other.length();
      if (n == 0 || m == 0) {
    	  if (n == m) {
    		  return 1;
    	  } else {
    		  return 0;
    	  }
      }

      // indexes into strings s and t
      int i;   
      // iterates through s
      int j;   
      // iterates through t
      char t_j;   
      // jth character of t
      int cost;   
      // cost
      for (i = 0; i<=n; i++) {
    	  p[i] = i;
      }
      
      for (j = 1; j<=m; j++) {
    	  t_j = other.charAt(j-1);
    	  d[0] = j;
    	  
    	  for (i=1; i<=n; i++) {
    		  cost = sa[i-1]==t_j ? 0 : 1;

    		  // minimum of cell to the left+1, to the top+1, diagonally left and up +cost
    		  d[i] = Math.min(Math.min(d[i-1]+1, p[i]+1),  p[i-1]+cost);
    	  }
    	  // copy current distance counts to 'previous row' distance counts
    	  _d = p;
    	  p = d;
    	  d = _d;
      }
      // our last action in the above loop was to switch d and p, so p now

      // actually has the most recent cost counts
      return 1.0f - ((float) p[n] / Math.max(other.length(), sa.length));
    }
}