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
	
	static int [] getIntArray() {
		return twoN;
	}
	
	public BracketCalc(int tpIn){
	
		totalPlayersW = tpIn;
		
		int twoNIndex = 0;
		boolean twoNCheck = true;
		while (twoNCheck) {
			if (twoNIndex > twoN.length-1){
				--twoNIndex;
				break;
			}
			twoNCheck = twoN[twoNIndex] < totalPlayersW; // Less than only (Scale down)
			if (twoNCheck){
				++twoNIndex;
			} else {
				--twoNIndex;
			}
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
		
//		if (secondRoundPlayersL==2) { // If there are no more matches to be played****
//			addRoundMultiplier = 1;
//		} else {
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
			System.out.println("BracketCalc: 1/2 initial rounds: " + initialRounds);
			System.out.println("BracketCalc: 0/1 incoming play after initial: " + firstIncoming);
			System.out.println("BracketCalc: 1/2 Multiplier for (addRounds-1): " + addRoundMultiplier);
			System.out.println("BracketCalc: addRounds-1: " + addRoundsLessOne);
		}
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
