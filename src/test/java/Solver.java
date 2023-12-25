import org.example.Cubes.Coordinate;
import org.example.Cubes.Cubie;
import org.example.DataBases.PruningTable;
import org.example.Enums.COLOR;

import java.util.Arrays;

public class Solver {

    private static final byte[] moves = new byte[31];
    private static final Coordinate[] stack = new Coordinate[31];
    private static final int BOUND = 30;


    public static void main(String[] args) {

        Coordinate coordinate = new Coordinate(new Cubie());

//        String[] moves = new String[]{  "U", "U2", "U'",
//                                        "D", "D2", "D'",
//                                        "F", "F2", "F'",
//                                        "B", "B2", "B'",
//                                        "R", "R2", "R'",
//                                        "L", "L2", "L'"  };

        byte[] currentMoves = new byte[]{17, 0, 5};
        for(int move = 0; move < currentMoves.length; move++)
            coordinate.move(currentMoves[move]);


        long start = System.currentTimeMillis();

        //Add your code here
        Search(coordinate);

        long end = System.currentTimeMillis();
        long duration = end - start;

        System.out.println("Runtime: " + duration*0.001+"s");


    }

    public static void Search(Coordinate coordinate){

        Arrays.fill(moves, (byte) -1);
        Arrays.fill(stack, null);
        int depth = 0;


        stack[0] = coordinate;

        int currentDepth;
        boolean found = false;

        while(depth < BOUND) {

            currentDepth = 0;
            while (currentDepth != -1) {

                if (currentDepth >= depth) {

                    if (isG3(stack[currentDepth])) {

                        for(int i = 1; i <= currentDepth; i++)
                            System.out.println(translateToMove(moves[i]) + ", ");

                        found = true;
                        break;
                    }

                    currentDepth--;

                }
                else if(moves[currentDepth+1] == 17) {

                    moves[currentDepth+1] = -1;
                    currentDepth--;

                }
                else{

                    currentDepth++;
                    moves[currentDepth]++;

                    while(!isMoveValid(currentDepth, moves[currentDepth])){
                        moves[currentDepth]++;
                    }

                    if(moves[currentDepth] >= 18) {

                        moves[currentDepth] = -1;
                        currentDepth--;

                    }
                    else {
                        stack[currentDepth] = new Coordinate(stack[currentDepth - 1]);
                        stack[currentDepth].move(moves[currentDepth]);
                    }


                }


            }

            if(found)
                break;

            depth++;
        }


    }

    private static boolean isMoveValid(int currentDepth, int move){

        /**
         *
         *  1  is from the same side of the previous move
         *  2. is from the parallel side of the previous move and not in the correct order ( U -> D, F -> B
         *          and not the opposite to prevent duplicate this sequence )
         *  3. is from the same side of the 2nd previous move and previous is parallel side
         *
         *
         * */

        if(currentDepth >= 2){

            int moveSide = move / 3;
            int preSide = moves[currentDepth-1] / 3;

            if(moveSide == preSide)
                return false;

            int parallelSide = (1-(moveSide % 2))*(moveSide + 1) + (moveSide % 2)*(moveSide - 1);
            if(parallelSide == preSide && preSide > moveSide)
                return false;

            if(currentDepth >= 3){

                if(moveSide == (moves[currentDepth-2] / 3) &&
                  preSide == parallelSide){
                    return false;
                }

            }


        }

//        if(moves[currentDepth] < 18) {
//            Coordinate c = new Coordinate(stack[currentDepth - 1]);
//            c.move(moves[currentDepth]);
//
//            if (currentDepth + getPhaseOneHeuristicValue(c) > BOUND) {
//                return false;
//            }
//        }



        return true;

    }
    private static byte getPhaseOneHeuristicValue(Coordinate coordinate){

        int index;

        int NewEdgeOrientation = coordinate.getEdgeOrientation();
        int UDSLICE_COMBINATION_NUMBER = 495;
        int NewUDSliceCombination = coordinate.getUDSlice() / 24;
        index = NewEdgeOrientation * UDSLICE_COMBINATION_NUMBER + NewUDSliceCombination;

        byte value1 = PruningTable.getFromTable(PruningTable.UDSliceAndEoPruningTable, index);
        int NewCornerOrientation = coordinate.getCornerOrientation();
        index = NewCornerOrientation * UDSLICE_COMBINATION_NUMBER + NewUDSliceCombination;
        byte value2 = PruningTable.getFromTable(PruningTable.UDSliceAndCoPruningTable, index);

        return (byte) Math.max(value1, value2);

    }

    private static boolean isG2(Coordinate coordinate){

        short UDSliceCombination = (short) (coordinate.getUDSlice() / 24);
        short EdgeOrientation = coordinate.getEdgeOrientation();
        short CornerOrientation = coordinate.getCornerOrientation();

        return (UDSliceCombination == 494) && (EdgeOrientation == 0) && (CornerOrientation == 0);

    }

    private static boolean isG3(Coordinate coordinate){

        Coordinate solvedCoordinate = new Coordinate(new Cubie());
        return coordinate.equals(solvedCoordinate);


    }

    public static String translateToMove(int move){

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
