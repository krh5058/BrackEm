package BrackEm;

import java.util.Arrays;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

// Algorithm behind calculating number of players in Winner's and Loser's brackets.
// No bys past second round.
// So many hours into figuring this out...

public class BracketCalc {
	
	final static int[] twoN = {2, 4, 8, 16};
	int totalPlayersW;
	int secondRoundPlayersW;
	int firstRoundPlayersW;
	int firstRoundByW;
	int totalPlayersL;
	int secondRoundPlayersL;
	int firstRoundPlayersL;
	int firstRoundByL;
	int totalRoundsW;
	int totalRoundsL;
	
	int initialRounds;
	int addRoundMultiplier;
	int firstIncoming = 0;
	int addRoundsLessOne;
	
	int initLoserPlacementType; // 1: (1/2 Base2 + 1 -> Base2), 2: (Base2 + 1) -> (1/2 Base2)
	boolean[] quadrupletIndices; // Only for Loser Placement type 1
	
	static int log2(int a){
		int result = 0;
		boolean valid = isBase2(a);
		
		if (valid){
			result = (int) (Math.log(a)/Math.log(2));
		}
		
		return result;
	}
	
	static boolean isBase2(int a){
		boolean valid = false;
		for( int i : getIntArray() )
			if (a == i){
				valid = true;
				break;
			}
		return valid;
	}
	
//	private int ceilLog2(int a){
//		int result = (int) Math.ceil(Math.log(a)/Math.log(2));
//		return result;
//	}
	
	static int[] getFirstOrder(int numOfPlayers){

		// Pre-determined numbers for logbase2((2)^(1->5))
		// Contact for Matlab recursive script.
		// Too much of a pain in the ass to manipulate matrices in a recursion function in Java
		// Don't judge me....
		
		int[] order = null;
		switch (numOfPlayers){
		case 2:
			order = new int[] {1,2};
			break;
		case 4:
			order = new int[] {1,2,3,4};
			break;
		case 8:
			order = new int[] {1,2,5,6,3,4,7,8};
			break;
		case 16:
			order = new int[] {1,2,9,10,5,6,13,14,3,4,11,12,7,8,15,16};
			break;
		case 32:
			order = new int[] {1,2,17,18,9,10,25,26,5,6,21,22,13,14,29,30,3,4,19,20,11,12,27,28,7,8,23,24,15,16,31,32};
			break;
		}
//		
//		int[] order = new int[numOfPlayers];
//		int[] array1 = new int[numOfPlayers/2];
//		int[] array2 = new int[numOfPlayers/2];
//
//		int j1 = 0;
//		int j2 = 0;
//		for (int i=0;i<numOfPlayers;i++){
//			if (i % 2 == 0){ // if even
//				array1[j1] = (i+1);
//				j1++;
//			} else {
//				array2[j2] = (i+1);
//				j2++;
//			}
//		}
//
//		int i2 = 0;
//		int i3 = 0;
//		while(i3<numOfPlayers/4){
//			order[i2] = array1[i3];
//			i2++;
//			order[i2] = array2[i3];
//			i2++;
//			order[i2] = array1[i3+numOfPlayers/4];
//			i2++;
//			order[i2] = array2[i3+numOfPlayers/4];
//			i2++;
//			i3++;
//		}
		
		return order;
	}
	
	static int [] getIntArray() {
		return twoN;
	}
	
	public BracketCalc(int tpIn){
	
		totalPlayersW = tpIn;
		
		int twoNIndex = 0;
		boolean twoNCheck = true;
		while (twoNCheck) {
			if (twoNIndex > twoN.length-1){ // If reached end of twoN, scale down to max index and break
				--twoNIndex;
				break;
			}
			twoNCheck = twoN[twoNIndex] < totalPlayersW; // Checking for iteration of log base 2 that is less than total players 
			if (twoNCheck){
				++twoNIndex;
			} else {
				--twoNIndex;
			}
		}
		
		int halfBase2 = twoN[twoNIndex] + (twoN[twoNIndex]/2); 
		
		if ((totalPlayersW > halfBase2) & (totalPlayersW <= twoN[twoNIndex]*2)){
			initLoserPlacementType = 1;
		} else {
			initLoserPlacementType = 2;
		}
		
		secondRoundPlayersW = twoN[twoNIndex];
		firstRoundPlayersW = 2*(totalPlayersW - secondRoundPlayersW);
		firstRoundByW = totalPlayersW - firstRoundPlayersW;
		totalPlayersL = (firstRoundPlayersW/2) + (secondRoundPlayersW/2);
		
		int twoNIndexL = 0;
		boolean twoNCheckL = true;
		while (twoNCheckL) {
			if (twoNIndexL > twoN.length-1){
				--twoNIndexL;
				break;
			}
			twoNCheckL = twoN[twoNIndexL] <= totalPlayersL; // Less than or equal (Don't scale down)
			if (twoNCheckL){
				++twoNIndexL;
			} else {
				--twoNIndexL;
			}
		}
		
		secondRoundPlayersL = twoN[twoNIndexL];
		firstRoundPlayersL = 2*(totalPlayersL - secondRoundPlayersL);
		firstRoundByL = totalPlayersL - firstRoundPlayersL;
		
		if (firstRoundPlayersL == 0) {
			firstRoundPlayersL = firstRoundByL;
			firstRoundByL = 0; // First round == second round
		}
		
		int addRounds = log2(secondRoundPlayersW); // Additional rounds outside of the first winner's round
		
		if (addRounds==0){
			System.out.println("BracketCalc: Error in BracketCalc.log2()");
		}
		
		totalRoundsW = 1 + addRounds;
		
		if (isBase2(totalPlayersL)) {
			initialRounds = 1;
		} else {
			initialRounds = 2;
		}
		
		if((addRounds-1) > (log2(secondRoundPlayersL)-1)) { // If there are more rounds of loser's entering than there are loser rounds left
			firstIncoming = 1; // Add an additional round to play (not multiplied by 2)
			addRoundMultiplier = 2;
			--addRounds; // Take away from multiplier
		} else if ((addRounds-1) == 0) { // If there are no more additional matches to be played (no more incoming losers) 
			addRoundMultiplier = 1;
		} else { // Multiply by 2
			addRoundMultiplier = 2;
		}
		
		addRoundsLessOne = (addRounds-1);
		
		totalRoundsL = initialRounds + firstIncoming + addRoundMultiplier*addRoundsLessOne;
		
		if (BrackEm.debug){
			System.out.println("BracketCalc: Total (Winner's): " + totalPlayersW);
			System.out.println("BracketCalc: Second Round (Winner's): " + secondRoundPlayersW);
			System.out.println("BracketCalc: First Round (Winner's): " + firstRoundPlayersW);
			System.out.println("BracketCalc: First Round By (Winner's): " + firstRoundByW);
			System.out.println("BracketCalc: Total Players (Loser's): " + totalPlayersL);
			System.out.println("BracketCalc: Second Round (Loser's): " + secondRoundPlayersL);
			System.out.println("BracketCalc: First Round (Loser's): " + firstRoundPlayersL);
			System.out.println("BracketCalc: First Round By (Loser's): " + firstRoundByL);		
			System.out.println("BracketCalc: Total Rounds (Winner's): " + totalRoundsW);
			System.out.println("BracketCalc: Total Rounds (Loser's): " + totalRoundsL);
			System.out.println("BracketCalc: Loser Placement Type: " + initLoserPlacementType);
		}
		
	}
	
	void loserPlacementCalc(){
		
		if (initLoserPlacementType==1){

			if (BrackEm.debug){
				System.out.println("BracketCalc (loserPlacementCalc): Loser placement type 1 calculations");
			}

			quadrupletIndices = getQuadruplets();

		} else if (initLoserPlacementType==2){
		}
	}
	
	int[] getSamePanelEffectedBrackets(int placement){
		int[] out = new int[2]; // Always 2 brackets effected
		
		int pairedPlacement = ((byte)(placement - 1) ^ 1) + 1;
		int nextPlacement =  ((byte)(placement - 1) >> 1) + 1;
		
		out[0] = pairedPlacement; // First is paired bracket
		out[1] = nextPlacement; // Second is next round bracket
		
		return out;
	}
	
	int[] getLoserPanelEffectedRounds(int placement, int value, int round){
		int[] loserPlacementOut = new int[2];
		int loserRound = 0;
		int loserPlacement = 0;

		if (initLoserPlacementType==1){
			
			if (round==0){ // First round loser placement

				if (quadrupletIndices[value-1]){ // Quad match (by value)
					int bytePlacement = (placement - 1);
					int bitMask = 1 << 1;
					if ((bytePlacement & bitMask)!=0){ // Check for order 1 bit on/off
						System.out.println("BracketCalc (getLoserPanelEffectedRounds): Order 1 bit on");
						loserPlacement = bytePlacement & ~bitMask; // Turn off order 1 
						loserPlacement = loserPlacement | 1; // Turn on order 0;
					} else {
						System.out.println("BracketCalc (getLoserPanelEffectedRounds): Order 1 bit off");
						loserPlacement = bytePlacement & ~1; // Turn off order 0;
					}
					
					loserPlacement++; // Return to i0 = 1 index
					loserRound = 0; // First init round
				} else { // Match 1st W to 2nd L 
					loserPlacement =  ((byte)(placement - 1) >> 1) + 1;
					loserRound = 1; // Second init round
				}
			} // TODO Need second round loser placement, CrossTree
			
		} else if (initLoserPlacementType==2){
			// TODO Cross Tree 2nd W > 2nd L, Slide blocked to Bot of 1st L, Match 1st W > 1st L
		}
		
		loserPlacementOut[0] = loserRound;
		loserPlacementOut[1] = loserPlacement;
		
		return loserPlacementOut;
	}
	
	boolean[] getQuadruplets(){ // Find quadruplets for quad match algorithm
		Integer[] array1 = new Integer[getFirstPlayersW()];

		// Get bitwise >> 2 placement numbers
		for (Entry<Bracket, Integer> mapEntry : BrackEm.winPanel.hashList.get(0).entrySet()) // Get all first round (0) brackets
		{
			Bracket key = mapEntry.getKey();
			int value = mapEntry.getValue();
			int placement = key.getPlacement();
			array1[value-1] = ((placement - 1) >> 2); // Enter by value
			
			if (BrackEm.debug){
				System.out.println("BracketCalc (getQuadruplets): getValue() : " + value);
				System.out.println("BracketCalc (getQuadruplets): getPlacement() >> 2: " + array1[value-1]);
			}
		}
		
		// Unique placement values
		Set<Integer> uniqKeys = new TreeSet<Integer>();
		uniqKeys.addAll(Arrays.asList(array1));
		if (BrackEm.debug){
			System.out.println("uniqKeys: " + uniqKeys);
		}
		
		int[] array2 = new int[uniqKeys.size()];
		boolean[] array3 = new boolean[uniqKeys.size()];
		
		// Iterate through placement numbers and count if any unique numbers reach 4 counts
		for (Integer i : array1){
			if (uniqKeys.contains(i)){
//				if (BrackEm.debug){
//					System.out.println("BracketCalc (getQuadruplets): Found " + i);
//				}
				array2[i]++;
				if (array2[i]==4){
					array3[i] = true;
					if (BrackEm.debug){
						System.out.println("BracketCalc (getQuadruplets): Value " + i + " is a quadruplet");
					}
				}
			}
		}
		
		boolean[] out = new boolean[array1.length];
		
		for (int j = 0;j<array1.length;j++){
			if (array3[array1[j]]){
//				if (BrackEm.debug){
//					System.out.println("BracketCalc (getQuadruplets): Placement value " + j + " is a quadruplet index");
//				}
				out[j] = true;
			}
		}
			
		return out;
	}
	
	int getTotalRoundsW(){
		return this.totalRoundsW;
	}
	
	int getTotalRoundsL(){
		return this.totalRoundsL;
	}
	
	int getFirstPlayersW(){
		return this.firstRoundPlayersW;
	}
	
	int getFirstPlayersL(){
		return this.firstRoundPlayersL;
	}
	
	int getSecondPlayersW(){
		return this.secondRoundPlayersW;
	}
	
	int getSecondPlayersL(){
		return this.secondRoundPlayersL;
	}
	
	int getFirstRoundByW(){
		return this.firstRoundByW;
	}
	
	int getFirstRoundByL(){
		return this.firstRoundByL;
	}
	
	int getInitialRounds(){
		return this.initialRounds;
	}
	
	int getFirstIncoming(){
		return this.firstIncoming;
	}
	
	int getAddRoundMultiplier(){
		return this.addRoundMultiplier;
	}
	
	int getAddRoundLessOne(){
		return this.addRoundsLessOne;
	}
}
