import org.example.Cubes.Coordinate;
import org.example.Cubes.Cubie;

import java.util.Arrays;

import static org.example.DataBases.MoveTable.MOVES_NUMBER;
public class TestMain {

    public static void main(String[] args) {

        Cubie cubie = new Cubie();
        Coordinate coordinate = new Coordinate(cubie);

        String[] toMove = new String[]{

                "U", "U2", "U'", "D", "D2", "D'", "F", "F2", "F'", "B", "B2", "B'", "R", "R2", "R'", "L", "L2", "L'"

        };

        byte[] oppMove = new byte[]{2, 1, 0, 5, 4, 3, 8, 7, 6, 11, 10, 9, 14, 13, 12, 17, 16, 15};

        byte[] currentMoves = new byte[]{17, 0, 5};

        for(int move = 0; move < currentMoves.length; move++){

            coordinate.move(currentMoves[move]);

        }
        for(int move = currentMoves.length - 1; move >= 0; move--){

            System.out.println(oppMove[currentMoves[move]]);

        }

//        System.out.println("solved: " + isSolved(coordinate));

    }

    public static boolean isSolved(Coordinate coordinate){

        Coordinate solvedCube = new Coordinate(new Cubie());
        return coordinate.equals(solvedCube);

    }

}