package org.example.Algorithms;

import org.example.Cubes.Coordinate;
import org.example.Cubes.Cubie;
import org.example.Enums.COLOR;
import org.example.Utils.AlgorithmUtils.Phase;

import java.util.Arrays;

import static org.example.Enums.PhaseNumber.*;

public class Kociemba {

    private static final byte[] moves = new byte[31];
    private static final byte[] solution = new byte[31];
    private static final Coordinate[] stack = new Coordinate[31];
    private static byte BOUND;

    private static final Phase PhaseOne = new Phase(FirstPhase);
    private static final Phase PhaseTwo = new Phase(SecondPhase);


    public static void main(String[] args){

        Coordinate coordinate = new Coordinate(new Cubie());
        coordinate.move(0);
        coordinate.move(6);
        coordinate.move(3);
        Search(coordinate);


    }

    public static void Search(Coordinate coordinate){

        Arrays.fill(moves, (byte) -1);
        Arrays.fill(stack, null);
        BOUND = 30;

        stack[0] = coordinate;
        int solutionLength = IDAStar((byte) 0, PhaseOne);
        for(int i = 1; i <= solutionLength; i++)
            System.out.println(translateToMove(solution[i]));

    }

    private static byte IDAStar(byte startDepth, Phase phase){

        byte depth = 0;
        byte solutionLength;
        while((solutionLength = AStar(startDepth, (byte) (startDepth + depth), phase)) == -1){
            depth++;
            if(depth > BOUND)
                return -1;
        }

        return solutionLength;

    }

    private static byte AStar(byte startDepth, byte depth, Phase phase){

        byte currentDepth = startDepth;

        while(currentDepth >= startDepth){

            if(currentDepth == depth){

                if(phase.getCubeChecker().isInGroup(stack[currentDepth])){

                    if(phase.getPhaseNumber() == SecondPhase){

                        System.arraycopy(moves, 0, solution, 0, moves.length);
                        return currentDepth;

                    }

                    byte phaseTwoLength = IDAStar(currentDepth, PhaseTwo);
                    if(phaseTwoLength != -1) {
                        BOUND = (byte) (currentDepth + phaseTwoLength);
                        return BOUND;
                    }
                    return -1;

                }

                if(moves[currentDepth] == 17){
                    stack[currentDepth] = null;
                    moves[currentDepth] = -1;
                }

                currentDepth--;

            }
            else if(currentDepth < depth){

                currentDepth++;
                do {

                    moves[currentDepth]++;

                }while(!isMoveValid(currentDepth,moves[currentDepth], phase));

                if(moves[currentDepth] >= 18) {
                    moves[currentDepth] = -1;
                    currentDepth -= 2;
                    continue;
                }

                stack[currentDepth] = new Coordinate(stack[currentDepth - 1]);
                stack[currentDepth].move(moves[currentDepth]);

            }

        }

        return -1;

    }

    private static boolean isMoveValid(int currentDepth, int move, Phase phase){

        /*
         * <p>
         *  1  is from the same side of the previous move
         *  2. is from the parallel side of the previous move and not in the correct order ( U -> D, F -> B
         *          and not the opposite to prevent duplicate this sequence )
         *  3. is from the same side of the 2nd previous move and previous is parallel side
         * </p>
         * */

        //move is not valid, but it reached too far, so I need to terminate it.

        if(phase.getPhaseNumber() == SecondPhase){
            if(move > 5)
                if((move - 6) % 3 != 1)
                    return false;
        }

        if(move >= 18)
            return true;

        if(currentDepth >= 2){

            int moveSide = move / 3;
            int preSide = moves[currentDepth-1] / 3;

            if(moveSide == preSide)
                return false;

            int parallelSide = (1-(moveSide % 2))*(moveSide + 1) + (moveSide % 2)*(moveSide - 1);
            if(parallelSide == preSide && preSide > moveSide)
                return false;

            if(currentDepth >= 3){

                return moveSide != (moves[currentDepth - 2] / 3) ||
                        preSide != parallelSide;

            }


        }

        Coordinate c = new Coordinate(stack[currentDepth-1]);
        c.move(move);
        return currentDepth + phase.getHeuristicValuer().getHeuristicValue(c) <= BOUND;

    }

    private static String translateToMove(int move){

        String side = String.valueOf(COLOR.values()[move/3]);
        String rotation = "";

        switch (move % 3) {
            case 0 -> rotation = " ";
            case 1 -> rotation = "2";
            case 2 -> rotation = "'";
        }

        return side + rotation;

    }

}
