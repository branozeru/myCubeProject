package org.example.DataBases;

import static org.example.DataBases.MoveTable.*;

public class PruningTable {

    public static final short URF_TO_DLF_C_AND_P_NUMBER = URF_TO_DLF_COMBINATION_NUMBER * URF_TO_DLF_PERMUTATION_NUMBER;
    public static final short UR_TO_DF_C_AND_P_NUMBER = UR_TO_DF_COMBINATION_NUMBER * UR_TO_DF_PERMUTATION_NUMBER;
    public static final byte PARITY = 2;


    //PhaseNumber one Pruning tables:
    //"Eo" stands for edge orientation, and "Co" for corner orientation
    public static byte[] UDSliceAndEoPruningTable = new byte[UDSLICE_COMBINATION_NUMBER * EDGE_ORIENTATIONS_NUMBER / 2];
    public static byte[] UDSliceAndCoPruningTable = new byte[UDSLICE_COMBINATION_NUMBER * CORNER_ORIENTATIONS_NUMBER / 2 + 1];

    //PhaseNumber two Pruning tables:
    //"Cp" stands for corner permutation
    public static byte[] UDSlicePermutationAndCpTable = new byte[UDSLICE_PERMUTATION_NUMBER * URF_TO_DLF_C_AND_P_NUMBER * PARITY / 2];
    public static byte[] UDSlicePermutationAndEpTable = new byte[UDSLICE_PERMUTATION_NUMBER * UR_TO_DF_C_AND_P_NUMBER * PARITY / 2];

    static {

        for(int i = 0; i < UDSLICE_COMBINATION_NUMBER*EDGE_ORIENTATIONS_NUMBER/2; i++){
            UDSliceAndEoPruningTable[i] = -1;
        }


        int depth = 0;
        insertToTable(UDSliceAndEoPruningTable,494, (byte) depth);
        int counter = 1;

        while(counter != UDSLICE_COMBINATION_NUMBER * EDGE_ORIENTATIONS_NUMBER) {

            for (int i = 0; i < UDSLICE_COMBINATION_NUMBER * EDGE_ORIENTATIONS_NUMBER; i++) {

                int edgeOrientation = i / UDSLICE_COMBINATION_NUMBER;
                int UDSliceCombination = i % UDSLICE_COMBINATION_NUMBER;

                if(getFromTable(UDSliceAndEoPruningTable,i) == depth){

                    for(int move = 0; move < 18; move++) {

                        int NewEdgeOrientation = MoveTable.EdgeOrientationMoveTable[edgeOrientation][move];
                        int NewUDSliceCombination = MoveTable.UDSliceMoveTable[UDSliceCombination * 24][move] / 24;
                        int index = NewEdgeOrientation * UDSLICE_COMBINATION_NUMBER + NewUDSliceCombination;
                        if(getFromTable(UDSliceAndEoPruningTable,index) == 0x0f){

                            insertToTable(UDSliceAndEoPruningTable,index,(byte)(depth+1));
                            counter++;

                        }

                    }
                }


            }
            depth++;
        }


    }

    static {

        for(int i = 0; i < UDSLICE_COMBINATION_NUMBER * CORNER_ORIENTATIONS_NUMBER / 2 + 1; i++){
            UDSliceAndCoPruningTable[i] = -1;
        }

        int depth = 0;
        insertToTable(UDSliceAndCoPruningTable,494, (byte) depth);
        int counter = 1;


        while(counter != UDSLICE_COMBINATION_NUMBER * CORNER_ORIENTATIONS_NUMBER){

            for(int i = 0; i < UDSLICE_COMBINATION_NUMBER * CORNER_ORIENTATIONS_NUMBER; i++){

                int cornerOrientation = i / UDSLICE_COMBINATION_NUMBER;
                int UDSliceCombination = i % UDSLICE_COMBINATION_NUMBER;

                if(getFromTable(UDSliceAndCoPruningTable,i) == depth){

                    for(int move = 0; move < 18; move++){

                        int NewCornerOrientation = MoveTable.CornerOrientationMoveTable[cornerOrientation][move];
                        int NewUDSliceCombination = MoveTable.UDSliceMoveTable[UDSliceCombination * 24][move] / 24;
                        int index = NewCornerOrientation * UDSLICE_COMBINATION_NUMBER + NewUDSliceCombination;
                        if(getFromTable(UDSliceAndCoPruningTable,index) == 0x0f){

                            insertToTable(UDSliceAndCoPruningTable,index,(byte) (depth + 1));
                            counter++;

                        }

                    }

                }


            }
            depth++;


        }


    }

    static {

        for(int i = 0; i < UDSLICE_PERMUTATION_NUMBER * URF_TO_DLF_C_AND_P_NUMBER * PARITY / 2; i++){

            UDSlicePermutationAndCpTable[i] = -1;

        }

        int depth = 0;
        insertToTable(UDSlicePermutationAndCpTable,0, (byte) depth);
        int counter = 1;

        int[] G2Moves = new int[]{ 0, 1, 2, 3, 4, 5, 7, 10, 13, 16};

//        boolean flag;
        while(counter != UDSLICE_PERMUTATION_NUMBER * URF_TO_DLF_C_AND_P_NUMBER * PARITY){

//            flag = false;
            for(int i = 0; i < UDSLICE_PERMUTATION_NUMBER * URF_TO_DLF_C_AND_P_NUMBER * PARITY; i++){

                int parity = i % 2;
                int UDSlicePermutation = (i / 2) % UDSLICE_PERMUTATION_NUMBER;
                int URFtoDLF = i / (UDSLICE_PERMUTATION_NUMBER * PARITY);


                if(getFromTable(UDSlicePermutationAndCpTable, i) == depth){

                    for(int move : G2Moves){

                        int NewParity = MoveTable.parityMoveTable[parity][move];
                        int NewUDSlicePermutation = MoveTable.UDSliceMoveTable[(UDSLICE_COMBINATION_NUMBER-1)*(UDSLICE_PERMUTATION_NUMBER) + UDSlicePermutation][move] % UDSLICE_PERMUTATION_NUMBER;
                        int NewURFtoDLF = MoveTable.URFtoDLFMoveTable[URFtoDLF][move];

                        int index = NewURFtoDLF * UDSLICE_PERMUTATION_NUMBER * PARITY + NewUDSlicePermutation * PARITY + NewParity;
//                        int index = (NewURFtoDLF * UDSLICE_PERMUTATION_NUMBER + NewUDSlicePermutation) * PARITY + NewParity;
                        if(getFromTable(UDSlicePermutationAndCpTable, index) == 0x0f){

                            insertToTable(UDSlicePermutationAndCpTable, index, (byte) (depth+1));
                            counter++;

//                            System.out.println("Depth: " + depth + ", Counter: " + counter);
//                            flag = true;

                        }


                    }

                }


            }
            depth++;
//            if(!flag){
//                System.out.println("broke in depth: " + (depth-1));
//                break;
//            }

        }
//        System.out.println("Counter: " + counter);

    }

    static {

        for(int i = 0; i < UDSlicePermutationAndEpTable.length; i++){

            UDSlicePermutationAndEpTable[i] = -1;

        }

        int depth = 0;
        int counter = 0;
        insertToTable(UDSlicePermutationAndEpTable, 0, (byte) depth);
        counter++;

        int[] G2Moves = new int[]{ 0, 1, 2, 3, 4, 5, 7, 10, 13, 16};

        while(counter < UDSLICE_PERMUTATION_NUMBER * UR_TO_DF_C_AND_P_NUMBER * PARITY){

            for(int i = 0; i < UDSLICE_PERMUTATION_NUMBER * UR_TO_DF_C_AND_P_NUMBER * PARITY; i++){

                int parity = i % 2;
                int UDSlicePermutation = (i/2) % UDSLICE_PERMUTATION_NUMBER;
                int URtoDF = i / (UDSLICE_PERMUTATION_NUMBER * PARITY);

                if(getFromTable(UDSlicePermutationAndEpTable,i) == depth){

                    for(int move = 0; move < G2Moves.length; move++){

                        int NewParity = MoveTable.parityMoveTable[parity][G2Moves[move]];
                        int NewUDSlicePermutation = MoveTable.UDSliceMoveTable[(UDSLICE_COMBINATION_NUMBER-1)*(UDSLICE_PERMUTATION_NUMBER) + UDSlicePermutation][G2Moves[move]] % 24;
                        int NewURtoDF = MoveTable.URtoDFMoveTable[URtoDF][move];
                        int index = NewURtoDF * UDSLICE_PERMUTATION_NUMBER * PARITY + NewUDSlicePermutation * PARITY + NewParity;

                        if(getFromTable(UDSlicePermutationAndEpTable, index) == (0x0f)){

                            insertToTable(UDSlicePermutationAndEpTable, index, (byte) (depth+1));
                            counter++;

                        }

                    }

                }


            }
            depth++;

        }



    }


    // Set pruning value in table. Two values are stored in one byte.
    private static void insertToTable(byte[] table, int index, byte value) {
        if ((index & 1) == 0)
            table[index / 2] &= (byte) (0xf0 | value);
        else
            table[index / 2] &= (byte) (0x0f | (value << 4));
    }

    // Extract pruning value
    public static byte getFromTable(byte[] table, int index) {
        if ((index & 1) == 0) // if it's even
            return (byte) (table[index / 2] & 0x0f);
        else
            return (byte) ((table[index / 2] & 0xf0) >>> 4);
    }

    public static void main(String[] args) {

        System.out.println();

    }

}
