import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class RepetitiveKSums {
	static int k = 0;
	static int n = 0;
	static ArrayList<Long> result;
	static ArrayList<Long> seq;
	static ArrayList<Long> seqCopy;
	
	static long startTime = System.currentTimeMillis();
		
	static void repetitiveKSums() {
		// System.out.println(result.toString());
		//System.out.println(seq.toString());
		if (n == 0 || seq.isEmpty()) {
			printAnswer();
		}
		
		else if (result.isEmpty()) {
			long a1 = seq.remove(0) / k;
			//System.out.println(seq.toString());
			result.add(a1);	
			n--;
			repetitiveKSums();
		} else if (result.size() == 1) {
			long si = seq.get(0);
			long ai = si - (result.get(0) * (k-1));
			
			ArrayList<Long> copy = new ArrayList<>(result);
		    copy.add(ai);
		    findAllPermutationsWith(copy, 1, ai, 0, true, true);
		    result.add(ai);
			n--;
			repetitiveKSums();
		} else if (result.size() == 2) {
			long ai = seq.get(seq.size() - 1) / k;
			
			ArrayList<Long> copy = new ArrayList<>(result);
		    copy.add(ai);
		    findAllPermutationsWith(copy, 1, ai, 0, true, true);
		    result.add(ai);
			n--;
			repetitiveKSums();
		} else if (result.size() == 3) {
			long si = seq.get(seq.size() - 1);
			long ai = si - (result.get(result.size() - 1) * (k-1));
			
			ArrayList<Long> copy = new ArrayList<>(result);
		    copy.add(ai);
		    findAllPermutationsWith(copy, 1, ai, 0, true, true);
		    result.add(ai);
			n--;
			repetitiveKSums();
		}
		else {
			final long endTime = System.currentTimeMillis();
            System.out.println("Total execution time: " + (endTime - startTime) );
			long si = seq.get(0);
			long ai = -1;
			for (int i = 0; i < result.size(); i++) {
				ai = si - (result.get(result.size() - i - 1) * (k-1));
			    if (ai >= 0 && seq.contains(ai * k)) {
				    ArrayList<Long> copy = new ArrayList<>(result);
				    seqCopy = new ArrayList<>(seq);
				    copy.add(ai);
				    boolean isValid = findAllPermutationsWith(copy, 1, ai, 0, true, false);
				    if (isValid) {
				    	seq = new ArrayList<>(seqCopy);
				    	break;
				    }
			    }
			}
			if (ai >= 0) {
				result.add(ai);
				n--;
			}
			repetitiveKSums();
		}
	}

		
	static boolean findAllPermutationsWith(ArrayList<Long> copy, int n, long x, int i, boolean isValid, boolean removeAll) {
		
		if (n >= k) {
			if (removeAll) {
				seq.remove(seq.indexOf(x));
			}
			else {
				isValid = seqCopy.contains(x);
				if (isValid) {
					seqCopy.remove(seqCopy.indexOf(x));
				};
			}
		}
		
		else {
			for (int j = i; j < copy.size(); j++) {
				x += copy.get(j);
				isValid = findAllPermutationsWith(copy, n + 1, x, j, isValid, removeAll);
				if (!isValid) {
					break;
				}
				x -= copy.get(j);
			}
		}

		return isValid;
	}
	
	
	
	static void printAnswer() {
		Collections.sort(result);
		String answer = "";
		for (Long i : result) {
			answer += i + " ";
		}
		answer.trim();
		System.out.println(answer);
	}
	
	// get all the passed in values
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int testCases = in.nextInt();
        System.out.println();

        for (int i = 0; i < testCases; i++) {
        	in.reset();
            n = in.nextInt();
            k = in.nextInt();
            in.useDelimiter("\n");
            String line = in.next();
            Scanner lineIn = new Scanner(line);
            seq = new ArrayList<>();
            result = new ArrayList<>();
            while(lineIn.hasNext()) {
            	long l = lineIn.nextLong();
            	seq.add(l);
            }
            //System.out.println("=================================");
            startTime = System.currentTimeMillis();
            repetitiveKSums();
            final long endTime = System.currentTimeMillis();
            System.out.println("Total execution time: " + (endTime - startTime) );
            //System.out.println("=================================");
            if (in.hasNextLine()) {
            	in.nextLine();
            }
            lineIn.close();
        }
        in.close();
    }	
}
