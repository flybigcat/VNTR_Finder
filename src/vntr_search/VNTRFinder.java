package vntr_search;

import java.util.*;

public class VNTRFinder {

	static final int WIN = 5;
	static final int TUP = 3;
	
	static final char[] vocabulary = {'A', 'C','G','T'};
	static final int nvocab = 4;
	static final int comb = 4*4*4;
	
	static String[] vobal_tab = new String[comb];	// all 3 (tup) combi 4^3 = 64 : initializeThreeComb
	static HashMap<String, Integer> hash_tab = new HashMap<String, Integer>(); // 3comb String ~ int (1~64)
	
	
	static long[] index; // unique identifier for each continuous 5 length string	
	static int[] preOccur; 
	
	static int[] vote;
	static int[] post;
	static int[] ppl;
	static int[] st;
	
	// initialize vobal_tab
	public static void initializeThreeComb() {
		int index = 0;
		
		for(int i = 0; i < nvocab; i++) {
			for(int j = 0; j < nvocab; j++) {
				for(int k = 0; k < nvocab; k++) {
					vobal_tab[index] = String.valueOf(vocabulary[i]) + String.valueOf(vocabulary[j]) +  String.valueOf(vocabulary[k]);
					index++;
				}
			}
		}
	}
	
	// initialize hash_tab hash: pair: 3comb String ~ int (1~64)	
	public static void initializeNvocab() {
		for(int i = 0; i < comb; i++) {
			hash_tab.put(vobal_tab[i], i + 1);	
		}
	}
	
	// calculate index[i] as unique identifier for each continuous 5 length string
	// use formula
	public static void initializeIndex(String str) {
		
		int n = str.length();
		index = new long[n - WIN + 1];
		
		for(int i = 0; i < n - WIN + 1; i++) {
			index[i] = 0;
			int ntup = 1;
			for(int j = i + 1; j < i + WIN; j++) {
				for(int k = j + 1; k < i + WIN; k++) {
					index[i] = index[i] + hash_tab.get(String.valueOf(str.charAt(i)) + String.valueOf(str.charAt(j)) +  String.valueOf(str.charAt(k))) * (long)Math.pow(comb, ntup);
					ntup++;
				}
			}
		}
		
	}
	
	// step1: calculate unique id for each 5 length string
	public static void calculateIdentifier(String str) {
		initializeThreeComb();
		initializeNvocab();
		initializeIndex(str); 
	}
	
	public static void populatePreOccur(String str) {
		int n = str.length();
		
		preOccur = new int[n - WIN + 1];
		
		HashMap<Long, Integer> g_hash = new HashMap<Long, Integer>(); // the first time happen or not
		HashMap<Long, Integer> f_hash = new HashMap<Long, Integer>();
		
		// 0 never happen before
		for(int i = 0 ; i < n-WIN+1; i++) {
			g_hash.put(index[i], 0);
		}
		
		for(int i = 0; i < n-WIN +1; i++) {
			if(g_hash.get(index[i]) == 0) {
				f_hash.put(index[i], i);
				g_hash.put(index[i], 1);
				preOccur[i] = -1; // beginning
			}
			else {
				preOccur[i] = f_hash.get(index[i]);
				f_hash.put(index[i], i);
			}
		}
		
	}
	
	public static void calculateVotes(String str) {
		int n = str.length();
		vote = new int[n - WIN + 1];
		post = new int[n - WIN + 1];
		ppl = new int[n - WIN + 1];
		st = new int[n - WIN + 1];
		
		int pl = 0, strr = 0;
		
		for(int i = 0; i < n - WIN + 1; i++) {
			int u = i;
			int br = 1;
			
			while(preOccur[u] != -1 && br == 1 && u >= strr) {
				
				if(pl == 0) {
					strr = preOccur[u];
					pl = u- preOccur[u];
					
					u = preOccur[u];
					vote[u]++;
					
					post[u] = i;
					ppl[u] = pl;
					st[u] = strr;
			
				}
				else if(u - preOccur[u] == pl) {
					
					u = preOccur[u];
					vote[u]++;
					post[u] = i;
					ppl[u] = pl;
					st[u] = strr;
					
				}
				else {
					if(preOccur[u] > strr) {
						pl = u -  preOccur[u];
						strr = preOccur[u];
						
						u = preOccur[u];
						vote[u]++;
						post[u] = i;
						ppl[u] = pl;
						st[u] = strr;	

					}
					br = 0;
				}
			}
			
		}
		
	}
	
	public static List<VNTR> getVNTR(String str) {
		int u = 0, v = 0, w = 0;
		int n = str.length();
		List<VNTR> res = new ArrayList<VNTR>();
		
		for(int i = 0; i < n - WIN + 1; i++) {
			
			int l = 0;
			if(ppl[i] > 4) {
				for(int j = 0; j <= ppl[i] - 5; j++ ){
					if(vote[i + j] < 3) {	// make sure at least 4 repeat
						l = 1;
					}
				}
				
				if(l == 0) {
					
					if(st[i] > u) {
	
						if( u > 0) {
							res.add(new VNTR(u, v, w));
						}
						u = st[i];
						v = vote[i];
						w = ppl[i];
					}
					else if(vote[i] > v && u == st[i] && w == ppl[i]){
						v = vote[i];
					}
				}
			}
		}
		res.add(new VNTR(u, v, w));
		return res;
	}
	
	// search 
	public static List<VNTR> vntrSearch(String str) {
		
		calculateIdentifier(str);
		populatePreOccur(str);
		calculateVotes(str);
		
		return getVNTR(str);
	}
	
	public static void main(String[] args) {
		String str = "GTTAAGTTACGAGTTACAGTTACAGTTACAGTTACACGTAGTTACAGTTACAGTTACAGTTACAGTTACACGTAGTTACAGTTACAGTTACAGTTACAGTTACACGTACAGTTAACAGTTAACAGTTAACAGTTAACGT";
		
		if(str == null || str.length() < WIN) {
			System.out.println("No VNTR with length >= 5");
			return;
		}
		
		List<VNTR> res = VNTRFinder.vntrSearch(str);
		
		for(VNTR s: res) 
			System.out.println(s.toString());
		
	}
}
