package checkers;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class UsePawnsKingsMyPostionValHeuristic extends Heuristic{
	
    final int MAX_WIN = 10000, MAX_LOSE = -10000;
     
    int []fr = {1, 1, -1, -1, 2, 2, -2, -2};
    int []fc = {1, -1, -1, 1, 2, -2, -2, 2};
    

	public UsePawnsKingsMyPostionValHeuristic() {
		// TODO Auto-generated constructor stub
	}
	
	private int cellValue(int row, int col){
        if(row == 0 || row == 7 || col == 0 || col == 7) return 4;
        if(row == 1 || row == 6 || col == 1 || col == 6) return 3;
        if(row == 2 || row == 5 || col == 2 || col == 5) return 2;
        return 1;
    }
    
    private boolean validCell(int row, int col){
        return !(row < 0 || row > 7 || col < 0 || col > 7);
    }

	@Override
	public int HeuristicValue(checkerGame game, JButton[] cell, ImageIcon currentPlayer) {
        ImageIcon opponentPlayer = null;
        if(currentPlayer == game.board.red) opponentPlayer = game.board.black;
        else opponentPlayer = game.board.black;
        
        int myPostionVal = 0, opPositionVal = 0;
        int myPawns = 0, opPawns = 0;
        int myKings = 0, opKings = 0;
        int myAttackers = 0, opAttackers = 0;
        int myCantMove = 0, opCantMove = 0;
        
        for(int i=0; i<8; i++){
            for(int j=0; j<8;j++){
                //player MAX
                if(cell[i*8+j].getIcon() == currentPlayer){
                    myPostionVal += cellValue(i, j);
                    myPawns++;
                    if("K".equals(cell[i*8+j].getText())) myKings++;
                    
                    boolean CantMove = true;
                    for(int k=0; k<8; k++){
                        int ni = i+fr[i], nj = j+fr[j];
                        if(!validCell(ni, nj)) continue;
                        if(game.board.validMove(cell, currentPlayer, ni, nj, i, j, false)){
                            CantMove = false;
                            if(k>3) myAttackers++;
                        }
                    }
                    if(CantMove) myCantMove++;
                }
                
                //opponent player
                else if(cell[i*8+j].getIcon() != null){
                    opPositionVal += cellValue(i, j);
                    opPawns++;
                    if("K".equals(cell[i*8+j].getText())) opKings++;
                    
                    boolean CantMove = true;
                    for(int k=0; k<8; k++){
                        int ni = i+fr[i], nj = j+fr[j];
                        if(!validCell(ni, nj)) continue;
                        if(game.board.validMove(cell, opponentPlayer, ni, nj, i, j, false)){
                            CantMove = false;
                            if(k>3) opAttackers++;
                        }
                    }
                    if(CantMove) opCantMove++;
                }
            }
        }
        
        //System.out.printf("%d %d %d\n", opPawns, opKings, opCantMove);
        if(opPawns + opKings - opCantMove == 0) return MAX_WIN; // opponent has no move
        if(myPawns + myKings - myCantMove == 0) return  MAX_LOSE; // MAX has no move
        
        int myCheckers = myPawns + myKings, opCheckers = opPawns + opKings; 
        
        //System.err.println("HERE");
        int heuristicVal =  (myPawns + 2*myKings + myPostionVal)
             - (myCantMove)
             - (opPawns + 2*opKings + opAttackers + opPositionVal)
             + (opCantMove);
        
        //if myCheckers is considerably greater than opponentCheckers, increase attacking tendency (propotional to checker count difference)
        if( myCheckers - opCheckers > 4) return heuristicVal + (myCheckers - opCheckers) * myAttackers;
        return heuristicVal;
    }
}	
