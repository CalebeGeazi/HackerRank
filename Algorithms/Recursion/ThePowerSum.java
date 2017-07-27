import java.util.Scanner;

/*
 * https://www.hackerrank.com/challenges/the-power-sum/problem
 */

public class ThePowerSum {
	// class variables
	static int result = 0;
	static int x = 0;
	static int n = 0;

    // recursive function to calculate power sums
    static void thePowerSum(int sum, int index) {
    	// loop thru possible values
        for (int i = index; i < x; i++) {
        	// get the value of i^n
        	double pow = Math.pow(i, n);
        	
        	// check the sum is less than x
    		if (sum + pow < x) {
    			// add to the sum
    			sum += pow;
    			
    			// call the recursive function again with an
    			// update sum and index
    			thePowerSum(sum, i + 1);
    			
    			// on return, subtract the previously added number
    			// and try adding the next number
    			sum -= pow;
    			continue;
    		}
    		
    		// check if we came across a power sum
    		else if (sum + pow == x) {
    			// increment result
				result++;
				break;
			} 
    		
    		// no need to go any further
    		else {
    			break;
    		}
    	}
    }
    
    // get all the passed in values
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        x = in.nextInt();
        n = in.nextInt();
        thePowerSum(0, 1);
        System.out.println(result);
        in.close();
    }
}
