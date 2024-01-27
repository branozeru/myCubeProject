import org.example.Algorithms.Kociemba;
import org.example.Algorithms.KociembaV2;
import org.example.Cubes.Coordinate;
import org.example.Cubes.Cubie;
import org.example.DataBases.PruningTable;
import org.example.Enums.COLOR;
import org.example.Utils.CubieUtils.Encoder;

import java.util.Arrays;

import static org.example.Algorithms.KociembaV2.Search;

public class TestMain {

    public static void main(String[] args) {

//        int[] combination = Encoder.decodeCombination(0,12,3);
//        System.out.println(Arrays.toString(combination));

//        Coordinate coordinate = new Coordinate(new Cubie());
        solveFor(7);

//        short URtoUL = 9;
//        short UBtoDF = 240;
//
//        int[] URtoULComb = Encoder.decodeCombination(URtoUL / 6, 8, 3);
//        int[] UBtoDFComb = Encoder.decodeCombination(UBtoDF / 6, 8, 3);
//
//        System.out.println(printColored(URtoULComb));
//        System.out.println(printColored(UBtoDFComb));
//

//
//        Cubie cubie = new Cubie();
//        System.out.println(": " + cubie.getURtoDF(URtoUL, UBtoDF));


    }

    private static void createArr(){

        short[] arr = new short[336];

        System.out.println("P8\t\t\t\t\t\t\t         P12");

        for(int i = 0; i < 336/6; i++){

            System.out.print(printColored(Encoder.decodeCombination(i, 8, 3)));
            System.out.println(" -> " + printColored(Encoder.decodeCombination(i, 12,3)));

        }

    }

    private static void solveFor(int shuffleMoves){

        int slice = PruningTable.UDSliceAndCoPruningTable[0];

        String[] moves = new String[3* COLOR.values().length];

        String currentMove = "";
        for(int i = 0; i < moves.length; i++){

            currentMove = String.valueOf(COLOR.values()[i/3]);
            switch (i % 3) {
                case 0 -> currentMove += "";
                case 1 -> currentMove += "2";
                case 2 -> currentMove += "'";
            }
            moves[i] = currentMove;

        }


        byte[] shuffle = new byte[shuffleMoves];
        for (byte i = 0; i < shuffleMoves; i++) {

            do {
                shuffle[i] = (byte) (Math.random() * 18);
            }while(!isMoveValid(shuffle,i,shuffle[i]));

        }


        Coordinate coordinate = new Coordinate(new Cubie());
        for(int i = 0; i < shuffleMoves; i++) {
            coordinate.move(shuffle[i]);
        }


        String[] shuffleMoveToString = new String[shuffleMoves];
        for(int i = 0; i < shuffleMoves; i++)
            shuffleMoveToString[i] = moves[shuffle[i]];

        System.out.println("Random moves: " + Arrays.toString(shuffleMoveToString));

        System.out.println("Solution: ");

        long start = System.currentTimeMillis();

        //Add your code here
        Kociemba.Search(coordinate);

        long end = System.currentTimeMillis();
        long duration = end - start;

        System.out.println("\n\nRuntime: " + duration*0.001+"s");

    }
    private static boolean isMoveValid(byte[] moves, byte index, byte move){

        /*
         * <p>
         *  1  is from the same side of the previous move
         *  2. is from the parallel side of the previous move and not in the correct order ( U -> D, F -> B
         *          and not the opposite to prevent duplicate this sequence )
         *  3. is from the same side of the 2nd previous move and previous is parallel side
         * </p>
         * */

        //move is not valid, but it reached too far, so I need to terminate it.


        if(index == 0)
            return true;

        int moveSide = move / 3;
        int preMoveSide = moves[index - 1] / 3;

        if(moveSide == preMoveSide)
            return false;

        int parallelSide = (1-(moveSide % 2))*(moveSide+1) + (moveSide % 2)*(moveSide - 1);
            //int parallelSide = (1-(moveSide % 2))*(moveSide + 1) + (moveSide % 2)*(moveSide - 1);
        if(preMoveSide == parallelSide){

            if(preMoveSide > moveSide)
                return false;

            if(index >= 2){

                return moveSide != moves[index - 2] / 3;

            }

        }

        return true;


    }

    private static String printColored(int[] arr){

        // ANSI escape code for red color
        String redColorCode = "\u001B[31m";

        // ANSI escape code to reset color
        String resetColorCode = "\u001B[0m";

        StringBuilder string = new StringBuilder();
        string.append('[');
        String currentColor;

        int i;
        for(i = 0; i < arr.length-1; i++){

            currentColor = resetColorCode;
            if(arr[i] == 1){

                currentColor = redColorCode;

            }

            string.append(currentColor).append(arr[i]).append(", ");

        }
        if(arr[i] == 1){
            currentColor = redColorCode;
        }
        else{
            currentColor = resetColorCode;
        }
        string.append(currentColor).append(arr[i]);
        string.append(resetColorCode).append(']');


        return string.toString();
    }

}