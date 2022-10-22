package checkers;

import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import checkers.UseNumberOfPawnAndKingsHeuristic.move;

public class UsePositionOnBoardHeuristic extends Heuristic{
	
    final int MAX_WIN = 10000, MAX_LOSE = -10000;
    
    int []fr = {1, 1, -1, -1, 2, 2, -2, -2};
    int []fc = {1, -1, -1, 1, 2, -2, -2, 2};
    

	public UsePositionOnBoardHeuristic() {
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
        int myCantMove = 0, opCantMove = 0;
        
        for(int i=0; i<8; i++){
            for(int j=0; j<8;j++){
                //player MAX
                if(cell[i*8+j].getIcon() == currentPlayer){
                    myPostionVal += cellValue(i, j);
                }
                
                //opponent player
                else if(cell[i*8+j].getIcon() != null){
                    opPositionVal += cellValue(i, j);
                }
            }
        }
        
        opCantMove = 12- moveGenerator(game, cell, game.board.black);
        myCantMove = 12 - moveGenerator(game, cell, game.board.red);
        
        //System.out.printf("%d %d %d\n", opPawns, opKings, opCantMove);
        if(opCantMove == 12) return MAX_WIN; // opponent has no move
        if(myCantMove == 12) return  MAX_LOSE; // MAX has no move
        
        //System.err.println("HERE");
        int heuristicVal =  (myPostionVal)
             - (myCantMove)
             - (opPositionVal)
             + (opCantMove);
        
        //if myCheckers is considerably greater than opponentCheckers, increase by movable checkers tendency (propotional to checker count difference)
        if( myPostionVal - opPositionVal > 4) return heuristicVal + (myPostionVal - opPositionVal) * 12 - myCantMove;
        return heuristicVal;
    }
	
    private int  moveGenerator(checkerGame game, JButton[] cell,  ImageIcon icon) {
    	ArrayList<move> answer = new ArrayList<>();
        for(int row=0; row<8; row++){
            for(int col=0; col<8; col++){
                if(cell[row*8 + col].getIcon() == null || cell[row*8 + col].getIcon() != icon) continue;
                
                for(int i=0; i<8; i++){
                    int nrow = row + fr[i], ncol = col + fc[i];
                    if(!validCell(nrow, ncol)) continue;
                    
                    if(game.board.validMove(cell, game.board.currentPlayer, nrow, ncol, row, col, false)){
                      move m = new move();
                      m.row = nrow;
                      m.col = ncol;
                      answer.add(m);
                    }
                }
            }
        }
    	return answer.size();
    }
    
    class move {
    	public int row, col;
    }
}	
