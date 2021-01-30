/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package put.ai.games.naiveplayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import put.ai.games.game.Board;
import put.ai.games.game.Move;
import put.ai.games.game.Player;
import put.ai.games.linesofaction.LinesOfActionMove;

public class NaivePlayer extends Player {

    private boolean shareBorder(int x, int y, Board b){
        for(int i=x-1; i>x+1; i++){
            for(int j=y-1; j>y+1; j++){
                if(i<0 || i>=b.getSize())
                    continue;
                if(j<0 || j>=b.getSize())
                    continue;
                if(i == x && j == y)
                    continue;
                if (this.getColor() == b.getState(i, j))
                    return true;
            }
        }
        return false;
    }

    private List<List<Integer>> allCoords(Color c, Board b){
        List<List<Integer>> coords = new ArrayList<>();
        for(int i=0; i<b.getSize(); i++){
            for(int j=0; j<b.getSize(); j++){
                if(c == b.getState(i, j))
                    coords.add(Arrays.asList(i, j));
            }
        }
        return coords;
    }

    private int distance(Color c, Board b) {
        int sum = 0;
        for(List<Integer> coords1 : allCoords(c, b)){
            for(List<Integer> coords2 : allCoords(c, b)){
                if (coords1 == coords2)
                    continue;
                int dist = (int) Math.sqrt(Math.pow((coords1.get(0) - coords2.get(0)), 2)
                        + Math.pow((coords1.get(1) - coords2.get(1)), 2));
                sum += dist;
            }
        }
        return sum;
    }

    private int moveValue(Move m, Board b){
        Board board = b.clone();
        LinesOfActionMove move = (LinesOfActionMove) m;
        int value;

        board.doMove(move);
        if(shareBorder(move.getDstX(), move.getDstY(), board)) {
            value = Integer.MAX_VALUE;
        }
        else {
            int opponent = distance(getOpponent(getColor()), board);
            int me = distance(this.getColor(), board);
            value = opponent - me;
        }
        
        return value;
    }

    @Override
    public String getName() {
        return "Kacper Trzeciak 141330 Ernest Cichowski 141200";
    }


    @Override
    public Move nextMove(Board b) {
        List<Move> moves = b.getMovesFor(getColor());
        int bestValue = Integer.MIN_VALUE;

        Move bestMove = null;
        for(Move move : moves){
            int temp = moveValue(move, b);
            if (temp > bestValue) {
                bestValue = temp;
                bestMove = move;
            }
        }
        return bestMove;
    }
}
