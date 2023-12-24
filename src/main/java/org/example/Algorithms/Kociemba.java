package org.example.Algorithms;

import org.example.Cubes.Coordinate;
import org.example.Cubes.Cubie;
import org.example.Enums.COLOR;

public class Kociemba {


    public static void main(String[] args){



    }


    public static boolean isG2(Coordinate coordinate){

        short UDSliceCombination = (short) (coordinate.getUDSlice() / 24);
        short EdgeOrientation = coordinate.getEdgeOrientation();
        short CornerOrientation = coordinate.getCornerOrientation();

         return (UDSliceCombination == 494) && (EdgeOrientation == 0) && (CornerOrientation == 0);

    }

    public static boolean isG3(Coordinate coordinate){

        Coordinate solvedCoordinate = new Coordinate(new Cubie());
        return isG2(coordinate) && coordinate.equals(solvedCoordinate);


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
