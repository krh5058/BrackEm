// Algorithm behind calculating number of players in Winner's and Loser's brackets.
// No bys past second round.
// So many hours into figuring this out...

public class BracketCalc {
	
	int[] twoN = {2, 4, 8, 16};
	int totalPlayersW;
	int secondRoundPlayersW;
	int firstRoundPlayersW;
	int firstRoundByW;
	int totalPlayersL;
	int secondRoundPlayersL;
	int firstRoundPlayersL;
	int firstRoundByL;
	
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
			firstRoundByL = 0;
		}
		
		if (BrackEm.debug){
			System.out.println("Total (Winner's): " + totalPlayersW);
			System.out.println("Second Round (Winner's): " + secondRoundPlayersW);
			System.out.println("First Round (Winner's): " + firstRoundPlayersW);
			System.out.println("First Round By (Winner's): " + firstRoundByW);
			System.out.println("Total Players (Loser's): " + totalPlayersL);
			System.out.println("Second Round (Loser's): " + secondRoundPlayersL);
			System.out.println("First Round (Loser's): " + firstRoundPlayersL);
			System.out.println("First Round By (Loser's): " + firstRoundByL);
		}
	}
}
