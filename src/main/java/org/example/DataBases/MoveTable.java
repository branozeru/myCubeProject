package org.example.DataBases;

import org.example.Cubes.Cubie;
import org.example.Enums.CORNER;
import org.example.Enums.EDGE;
import org.example.Utils.CubieUtils.NumberFinder;

import java.util.Arrays;

import static org.example.Enums.CORNER.*;
import static org.example.Enums.EDGE.*;

public class MoveTable {

    //The number of possible moves is 18 because there are 6 sides for the cube
    //and each side can be rotated clockwise, counter-clockwise or twice. 6 * 3 = 18
    public static final byte MOVES_NUMBER = 18;

    //The number of possible edge orientations are 2048 because there are 12 edges and
    //each have 2 orientation form, 0 or 1, so it's 2 * 2 * 2.... which lead to 2^12
    //but by the laws of the Rubik's cube because the sum of the orientation always has to be even
    //the last orientation is determined by the rest, so this number should be divided by 2 => 2^12 / 2 = 2^11 = 2048
    public static final short EDGE_ORIENTATIONS_NUMBER = 2048 ;

    //The number of possible corner orientation are 2187 because there are 8 corners and
    //each one have 3 orientation forms, 0/1/2, so it's 3 * 3 * 3.... which lead to 3^8
    //but by the laws of the Rubik's cube the last orientation is determined by the rest so this number
    //should be divided by 3 => 3^8 / 3 = 3^7 = 2187
    public static final short CORNER_ORIENTATIONS_NUMBER = 2187 ;

    //The number of possible combination of the UD Slide edges ( the slice between the U slice to the D slice )
    //meaning how many option there are to take the 4 UD Slice edges from all the 12 edges.
    //Mathematically described as nCk, 12C4 = 495
    public static final short UDSLICE_COMBINATION_NUMBER = 495;

    //The number of possible permutations there are to the 4 UD slice.
    //Mathematically described as 4! = 1 * 2 * 3 * 4 = 24
    public static final short UDSLICE_PERMUTATION_NUMBER = 24;

    //The number of possible combination there are for the edges from URF to DLF ( URF, UFL, ULB, UBR, DFR, DLF ) is 28
    //The reason I don't take the last two corners ( DBL, DRB ) is because their location is determined by the parity of the cube.
    //Mathematically described as nCk, 8C6 = 28
    public static final short URF_TO_DLF_COMBINATION_NUMBER = 28;
    //Mathematically described as 6! = 1 * 2 * 3 * 4 * 5 * 6 = 720
    public static final short URF_TO_DLF_PERMUTATION_NUMBER = 720;


    //The reason I take UR to UL edges (UR, UF, UL) and UB to DF(UB, DR, DF) without taking DL and DB is
    //because their location is determined by the parity.
    //Mathematically described as nCk, 12C3 = 220
    public static final short UR_TO_UL_COMBINATION_NUMBER = 220;
    //Mathematically described as 3! = 1 * 2 * 3
    public static final short UR_TO_UL_PERMUTATION_NUMBER = 6;
    //Mathematically described as nCk, 12C3 = 220
    public static final short UB_TO_DF_COMBINATION_NUMBER = 220;
    //Mathematically described as 3! = 1 * 2 * 3
    public static final short UB_TO_DF_PERMUTATION_NUMBER = 6;

    public static final short UR_TO_DF_COMBINATION_NUMBER = 28;
    public static final short UR_TO_DF_PERMUTATION_NUMBER = 720;

    public static short[][] EdgeOrientationMoveTable = new short[EDGE_ORIENTATIONS_NUMBER][MOVES_NUMBER];
    public static short[][] CornerOrientationMoveTable = new short[CORNER_ORIENTATIONS_NUMBER][MOVES_NUMBER];
    public static short[][] UDSliceMoveTable = new short[UDSLICE_COMBINATION_NUMBER*UDSLICE_PERMUTATION_NUMBER][MOVES_NUMBER];
    public static short[][] URFtoDLFMoveTable = new short[URF_TO_DLF_COMBINATION_NUMBER*URF_TO_DLF_PERMUTATION_NUMBER][MOVES_NUMBER];
    public static short[][] URtoULMoveTable = new short[UR_TO_UL_COMBINATION_NUMBER*UR_TO_UL_PERMUTATION_NUMBER][MOVES_NUMBER];
    public static short[][] UBtoDFMoveTable = new short[UB_TO_DF_COMBINATION_NUMBER*UB_TO_DF_PERMUTATION_NUMBER][MOVES_NUMBER];
    public static short[][] URtoDFMoveTable = new short[UR_TO_DF_COMBINATION_NUMBER*UR_TO_DF_PERMUTATION_NUMBER][MOVES_NUMBER - 8];

    //Taking 3 elements out of 8 = 3P8 = 336
    public static short[][] UniteURtoDF = new short[336][336];

    public static short[] TranslateURtoUL = new short[UR_TO_UL_COMBINATION_NUMBER * UR_TO_UL_PERMUTATION_NUMBER];
    public static short[] TranslateUBtoDF = new short[UB_TO_DF_COMBINATION_NUMBER * UB_TO_DF_PERMUTATION_NUMBER];

    //Every turn of any side change the parity from 0 to 1 or vice versa
    //So if the parity is 0 any move will turn to a 1 unless it is a twice-turn move like U2
    //and if the parity is 1 any move will turn to a 0 unless it is a twice-turn
    // moves order: U, U2, U', D, D2, D', F, F2, F', B, B2, B', R, R2, R', L, L2, L'
    public static byte[][] ParityMoveTable = {  { 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1 },
                                                { 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0 } };



    public static final Cubie[] movesCubes = new Cubie[]{

            // U,
            new Cubie(new CORNER[]{UBR, URF, UFL, ULB, DFR, DLF, DBL, DRB},
                    new byte[]{0, 0, 0, 0, 0, 0, 0, 0},
                    new EDGE[]{UB, UR, UF, UL, DR, DF, DL, DB, FR, FL, BL, BR},
                    new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}),
            // D,
            new Cubie(new CORNER[]{URF, UFL, ULB, UBR, DLF, DBL, DRB, DFR},
                    new byte[]{0, 0, 0, 0, 0, 0, 0, 0},
                    new EDGE[]{UR, UF, UL, UB, DF, DL, DB, DR, FR, FL, BL, BR},
                    new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}),
            // F,
            new Cubie(new CORNER[]{UFL, DLF, ULB, UBR, URF, DFR, DBL, DRB},
                    new byte[]{1, 2, 0, 0, 2, 1, 0, 0},
                    new EDGE[]{UR, FL, UL, UB, DR, FR, DL, DB, UF, DF, BL, BR},
                    new byte[]{0, 1, 0, 0, 0, 1, 0, 0, 1, 1, 0, 0}),
            // B,
            new Cubie(new CORNER[]{URF, UFL, UBR, DRB, DFR, DLF, ULB, DBL},
                    new byte[]{0, 0, 1, 2, 0, 0, 2, 1},
                    new EDGE[]{UR, UF, UL, BR, DR, DF, DL, BL, FR, FL, UB, DB},
                    new byte[]{0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 1}),
            // R,
            new Cubie(new CORNER[]{DFR, UFL, ULB, URF, DRB, DLF, DBL, UBR},
                    new byte[]{2, 0, 0, 1, 1, 0, 0, 2},
                    new EDGE[]{FR, UF, UL, UB, BR, DF, DL, DB, DR, FL, BL, UR},
                    new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}),
            // L
            new Cubie(new CORNER[]{URF, ULB, DBL, UBR, DFR, UFL, DLF, DRB},
                    new byte[]{0, 1, 2, 0, 0, 2, 1, 0},
                    new EDGE[]{UR, UF, BL, UB, DR, DF, FL, DB, FR, UL, DL, BR},
                    new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}),

    };

    //MOVE TABLES
    static {

        Cubie cubie = new Cubie();

        for(int i = 0; i < EDGE_ORIENTATIONS_NUMBER; i++){

            cubie.setEdgeOrientationNumber((short) i);
            for(int side = 0; side < 6; side++){

                for(int dir = 0; dir < 3; dir++){

                    //Multiply the cube by the move cubes is the same as turning a side on the cube.
                    //Multiply the cube by U move cube is the same as turning the U side one time clockwise and so on.
                    //The cube side is turned 3 time, let say the current side is U
                    //The side is turned one time for : U
                    //                     second for : U2
                    //             and third time for : U'


                    cubie.multiplyCubie(movesCubes[side]);
                    EdgeOrientationMoveTable[i][side*3 + dir] = cubie.getEdgeOrientationNumber();

                }
                //Setting the cube to the next iteration.
                //Turn the cube again will be like doing nothing because the cube already turned 3 times
                //So the 4th time does nothing, it's like doing U * U * U * U or U2 * U2 = Nothing
                cubie.multiplyCubie(movesCubes[side]);

            }


        }


    }

    static {

        Cubie cubie = new Cubie();

        for(int i = 0; i < CORNER_ORIENTATIONS_NUMBER; i++){

            cubie.setCornerOrientationNumber((short)i);
            for(int side = 0; side < 6; side++){

                for(int dir = 0; dir < 3; dir++){

                    cubie.multiplyCubie(movesCubes[side]);
                    CornerOrientationMoveTable[i][side*3 + dir] = cubie.getCornerOrientationNumber();

                }
                cubie.multiplyCubie(movesCubes[side]);
            }

        }

    }

    static {

        Cubie cubie = new Cubie();

        for(int i = 0; i < UDSLICE_COMBINATION_NUMBER*UDSLICE_PERMUTATION_NUMBER; i++){

            cubie.setUDSliceNumber((short)i);
            for(int side = 0; side < 6; side++){

                for(int dir = 0; dir < 3; dir++){

                    cubie.multiplyCubie(movesCubes[side]);
                    UDSliceMoveTable[i][side*3 + dir] = cubie.getUDSliceNumber();

                }

                cubie.multiplyCubie(movesCubes[side]);
            }


        }

    }

    static {

        Cubie cubie = new Cubie();

        for(int i = 0; i < URF_TO_DLF_COMBINATION_NUMBER * URF_TO_DLF_PERMUTATION_NUMBER; i++){

            cubie.setCornerPermutationNumber((short)i);
            for(int side = 0; side < 6; side++){

                for(int dir = 0; dir < 3; dir++){

                    cubie.multiplyCubie(movesCubes[side]);
                    URFtoDLFMoveTable[i][side*3 + dir] = cubie.getCornerPermutationNumber();

                }
                cubie.multiplyCubie(movesCubes[side]);

            }

        }


    }

    static {

        Cubie cubie = new Cubie();

        for(int i = 0; i < UR_TO_UL_COMBINATION_NUMBER*UR_TO_UL_PERMUTATION_NUMBER; i++){

            cubie.setURtoULNumber((short) i);
            for(int side = 0; side < 6; side++){
                for(int dir = 0; dir < 3; dir++){

                    cubie.multiplyCubie(movesCubes[side]);
                    URtoULMoveTable[i][side*3 + dir] = cubie.getURtoUL();

                }
                cubie.multiplyCubie(movesCubes[side]);
            }


        }


    }

    static {

        Cubie cubie = new Cubie();
        for(int i = 0; i < UB_TO_DF_COMBINATION_NUMBER*UB_TO_DF_PERMUTATION_NUMBER; i++){

            cubie.setUBtoDFNumber((short)i);
            for(int side = 0; side < 6; side++){

                for(int dir = 0; dir < 3; dir++){

                    cubie.multiplyCubie(movesCubes[side]);
                    UBtoDFMoveTable[i][side*3 + dir] = cubie.getUBtoDF();

                }
                cubie.multiplyCubie(movesCubes[side]);

            }

        }


    }

    static {

        Cubie cubie = new Cubie();
        for(int URtoUL = 0; URtoUL < 336; URtoUL++){
            for(int UBtoDF = 0; UBtoDF < 336; UBtoDF++){
                //8P3 => 8P6
                UniteURtoDF[URtoUL][UBtoDF] = cubie.getURtoDF((short) URtoUL, (short) UBtoDF);

            }
        }


    }

    static {

        Cubie cubie = new Cubie();
        NumberFinder n = (a) -> (a >= UR.ordinal() && a <= UL.ordinal());
        for(short i = 0; i < UR_TO_UL_COMBINATION_NUMBER * UR_TO_UL_PERMUTATION_NUMBER; i++){

            cubie.setURtoULNumber(i);
            TranslateURtoUL[i] = cubie.getThreePartsCode(n, (byte) UR.ordinal());

        }
//        System.out.println("FLAG!");

    }

    static {

        Cubie cubie = new Cubie();
        NumberFinder n = (a) -> (a >= UB.ordinal() && a <= DF.ordinal());
        for(short i = 0; i < UB_TO_DF_COMBINATION_NUMBER * UB_TO_DF_PERMUTATION_NUMBER; i++){

            cubie.setUBtoDFNumber(i);
            TranslateUBtoDF[i] = cubie.getThreePartsCode(n, (byte) UB.ordinal());

        }
//        System.out.println("FLAG!");

    }

    static {

        //U, U2, U', D, D2, D', F2, B2, R2, L2

        Cubie cubie = new Cubie();
        for(int i = 0; i < UR_TO_DF_COMBINATION_NUMBER*UR_TO_DF_PERMUTATION_NUMBER; i++){

            int side;
            cubie.setURtoDFNumber((short) i);
            for(side = 0; side < 2; side++){

                for(int dir = 0; dir < 3; dir++){

                    cubie.multiplyCubie(movesCubes[side]);
                    URtoDFMoveTable[i][side*3 + dir] = cubie.getURtoDF();

                }
                cubie.multiplyCubie(movesCubes[side]);

            }

            side = 2;
            for(int move = 6; move < 10; move++){

                cubie.multiplyCubie(movesCubes[side]);
                cubie.multiplyCubie(movesCubes[side]);

                URtoDFMoveTable[i][move] = cubie.getURtoDF();

                //Set back for next iteration
                cubie.multiplyCubie(movesCubes[side]);
                cubie.multiplyCubie(movesCubes[side]);

                side++;

            }

        }


    }

}
