import org.example.Cubes.Coordinate;
import org.example.Cubes.Cubie;

import java.util.Arrays;

import static org.example.DataBases.MoveTable.MOVES_NUMBER;
public class TestMain {

    public static void main(String[] args) {

        Cubie cubie = new Cubie();
        int val = cubie.getUBtoDF();
        System.out.println(val);
        System.out.println(Arrays.toString(cubie.getEdgePermutation()));

        System.out.println("\n\nAfter setting new ub to df: ");
        val = cubie.getUBtoDF();
        System.out.println(val);
        System.out.println(Arrays.toString(cubie.getEdgePermutation()));


    }


}