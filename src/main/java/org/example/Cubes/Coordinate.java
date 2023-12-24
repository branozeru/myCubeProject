package org.example.Cubes;

import org.example.DataBases.MoveTable;

public class Coordinate {

    private short cornerOrientation;
    private short edgeOrientation;
    private short UDSlice;

    private short cornerPermutation;
    private short URtoUL;
    private short UBtoDF;
    private byte parity;

    public Coordinate(Cubie cubie){

        this.cornerOrientation = cubie.getCornerOrientationNumber();
        this.edgeOrientation = cubie.getEdgeOrientationNumber();
        this.UDSlice = cubie.getUDSliceNumber();

        this.cornerPermutation = cubie.getCornerPermutationNumber();
        this.URtoUL = cubie.getURtoUL();
        this.UBtoDF = cubie.getUBtoDF();
        this.parity = cubie.getParity();

    }

    public Coordinate(Coordinate other){

        this.cornerOrientation = other.getCornerOrientation();
        this.edgeOrientation = other.getEdgeOrientation();
        this.UDSlice = other.getUDSlice();
        this.cornerPermutation = other.getCornerPermutation();
        this.URtoUL = other.getURtoUL();
        this.UBtoDF = other.getUBtoDF();
        this.parity = other.getParity();

    }

    public void move(int move){

        this.cornerOrientation = MoveTable.CornerOrientationMoveTable[this.cornerOrientation][move];
        this.edgeOrientation = MoveTable.EdgeOrientationMoveTable[this.edgeOrientation][move];
        this.UDSlice = MoveTable.UDSliceMoveTable[this.UDSlice][move];
        this.cornerPermutation = MoveTable.URFtoDLFMoveTable[this.cornerPermutation][move];
        this.URtoUL = MoveTable.URtoULMoveTable[this.URtoUL][move];
        this.UBtoDF = MoveTable.UBtoDFMoveTable[this.UBtoDF][move];
        this.parity = MoveTable.parityMoveTable[this.parity][move];


    }

    public short getCornerOrientation() {
        return cornerOrientation;
    }
    public short getEdgeOrientation() {
        return edgeOrientation;
    }
    public short getUDSlice() {
        return UDSlice;
    }
    public short getCornerPermutation() {
        return cornerPermutation;
    }
    public short getURtoUL() {
        return URtoUL;
    }
    public short getUBtoDF() {
        return UBtoDF;
    }
    public byte getParity() {
        return parity;
    }


    public boolean equals(Coordinate other) {

        return this.cornerOrientation   ==  other.getCornerOrientation()    &&
               this.edgeOrientation     ==  other.getEdgeOrientation()      &&
               this.UDSlice             ==  other.getUDSlice()              &&
               this.cornerPermutation   ==  other.getCornerPermutation()    &&
               this.URtoUL              ==  other.getURtoUL()               &&
               this.UBtoDF              ==  other.getUBtoDF()               &&
               this.parity              ==  other.getParity();

    }

    @Override
    public String toString() {
        return "Coordinate{" +
                "cornerOrientation=" + cornerOrientation +
                ", edgeOrientation=" + edgeOrientation +
                ", UDSlice=" + UDSlice +
                ", cornerPermutation=" + cornerPermutation +
                ", URtoUL=" + URtoUL +
                ", UBtoDF=" + UBtoDF +
                ", parity=" + parity +
                '}';
    }
}
