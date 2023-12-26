package org.example.Utils.AlgorithmUtils;

import org.example.Cubes.Coordinate;
import org.example.Cubes.Cubie;
import org.example.DataBases.MoveTable;
import org.example.DataBases.PruningTable;
import org.example.Enums.PhaseNumber;
import org.example.Utils.AlgorithmUtils.CubeChecker;
import org.example.Utils.AlgorithmUtils.heuristicValuer;

import static org.example.DataBases.MoveTable.UDSLICE_COMBINATION_NUMBER;
import static org.example.DataBases.MoveTable.UDSLICE_PERMUTATION_NUMBER;
import static org.example.DataBases.PruningTable.PARITY;

public class Phase {

    private CubeChecker cubeChecker;
    private heuristicValuer heuristicValuer;

    private final PhaseNumber phaseNumber;

    public Phase(PhaseNumber phaseNumber){

        this.phaseNumber = phaseNumber;

        switch (phaseNumber) {

            case FirstPhase -> cubeChecker = (Coordinate coordinate) -> {

                short UDSliceCombination = (short) (coordinate.getUDSlice() / 24);
                short EdgeOrientation = coordinate.getEdgeOrientation();
                short CornerOrientation = coordinate.getCornerOrientation();

                return (UDSliceCombination == 494) && (EdgeOrientation == 0) && (CornerOrientation == 0);

            };

            case SecondPhase -> cubeChecker = (Coordinate coordinate) -> {

                Coordinate solvedCoordinate = new Coordinate(new Cubie());
                return coordinate.equals(solvedCoordinate);

            };

        }

        switch (phaseNumber){

            case FirstPhase -> heuristicValuer = (Coordinate coordinate) -> {

                int index1;
                byte value1;
                short edgeOrientation = coordinate.getEdgeOrientation();
                int UDSliceCombination = coordinate.getUDSlice() / UDSLICE_PERMUTATION_NUMBER;
                index1 = edgeOrientation * UDSLICE_COMBINATION_NUMBER + UDSliceCombination;
                value1 = PruningTable.getFromTable(PruningTable.UDSliceAndEoPruningTable, index1);

                int index2;
                byte value2;
                int NewCornerOrientation = coordinate.getCornerOrientation();
                index2 = NewCornerOrientation * UDSLICE_COMBINATION_NUMBER + UDSliceCombination;
                value2 = PruningTable.getFromTable(PruningTable.UDSliceAndCoPruningTable, index2);

                return (byte) Math.max(value1, value2);

            };

            case SecondPhase -> heuristicValuer = (Coordinate coordinate) -> {

                int index1;
                byte value1;
                int UDSlicePermutation = coordinate.getUDSlice() % UDSLICE_PERMUTATION_NUMBER;
                int cornerPermutation = coordinate.getCornerPermutation();
                int parity = coordinate.getParity();
                index1 = cornerPermutation * UDSLICE_PERMUTATION_NUMBER * PARITY + UDSlicePermutation * PARITY + parity;
                value1 = PruningTable.getFromTable(PruningTable.UDSlicePermutationAndCpTable, index1);

                int index2;
                byte value2;
                short URtoUL = coordinate.getURtoUL();
                short UBtoDF = coordinate.getUBtoDF();

                Cubie cubie = new Cubie();
                cubie.setURtoDFNumber(URtoUL, UBtoDF);
                int URtoDF = cubie.getURtoDF();

//                int URtoDF = MoveTable.UniteURtoDF[URtoUL][UBtoDF];
                index2 = URtoDF * UDSLICE_PERMUTATION_NUMBER * PARITY + UDSlicePermutation * PARITY + parity;
                value2 = PruningTable.getFromTable(PruningTable.UDSlicePermutationAndEpTable,index2);

                return (byte) Math.max(value1, value2);

            };

        }


    }
    public CubeChecker getCubeChecker() {
        return cubeChecker;
    }
    public PhaseNumber getPhaseNumber() {
        return phaseNumber;
    }

    public org.example.Utils.AlgorithmUtils.heuristicValuer getHeuristicValuer() {
        return heuristicValuer;
    }
}
