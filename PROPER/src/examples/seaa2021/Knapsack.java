package seaa2021;

public class Knapsack {
	/**
	 * Remove the commended out display() method before synthesising the model
	 * Change the property values @energy, @cost as necessary and/or define new properties
	 * @return
	 */
	public int knapsackDP(int[] w, int[] v, int n, int W) {
        if (n <= 0 || W <= 0) {
            return 0;
        }

        int[][] m = new int[n + 1][W + 1];
        
        int j = 0;
        while (j <= W) {
            m[0][j] = 0;
            j++;
        }
        
        int i = 1;
        while (i <= n) {
        	int k = 1;
            while (k <= W) {
                if (w[i - 1] > k) {
                    m[i][k] = m[i - 1][k];
                } else {
                    m[i][k] = Math.max(m[i - 1][k], m[i - 1][k - w[i - 1]] + v[i - 1]); //@time=2
                    //display(); // @energy:67
                }
                k++;
            }
            i++;
        }
        return m[n][W];
    }
}
