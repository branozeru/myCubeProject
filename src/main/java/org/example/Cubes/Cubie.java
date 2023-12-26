package org.example.Cubes;

import org.example.Enums.COLOR;
import org.example.Enums.CORNER;
import org.example.Enums.EDGE;
import org.example.Enums.FACELET;
import org.example.Utils.CubieUtils.Encoder;
import org.example.Utils.CubieUtils.LehmerCoder;
import org.example.Utils.CubieUtils.NumberFinder;

import java.util.Arrays;

import static org.example.Enums.COLOR.*;
import static org.example.Enums.CORNER.*;
import static org.example.Enums.EDGE.*;
import static org.example.Enums.FACELET.*;

public class Cubie {

    private final CORNER[] cornerPermutation = new CORNER[CORNER.values().length];
    private final byte[] cornerOrientation = new byte[CORNER.values().length];
    private final EDGE[] edgePermutation = new EDGE[EDGE.values().length];
    private final byte[] edgeOrientation = new byte[EDGE.values().length];


    public Cubie(){

        //Create a new solved cube
        FaceCube f = new FaceCube();
        setCorners(f.getColor());
        setEdges(f.getColor());

    }

    public Cubie(FaceCube f){

        //Create a new cube using a Facelet Cube
        COLOR[] fColors = f.getColor();
        setCorners(fColors);
        setEdges(fColors);

    }

    public Cubie(Cubie other){

        //Copy constructor, create a copy of a cube
        setCorners(other.getCornerPermutation(),other.getCornerOrientation());
        setEdges(other.getEdgePermutation(),other.getEdgeOrientation());

    }

    public Cubie(CORNER[] cornerPermutation, byte[] cornerOrientation, EDGE[] edgePermutation, byte[] edgeOrientation){

        setCorners(cornerPermutation,cornerOrientation);
        setEdges(edgePermutation, edgeOrientation);

    }

    public void multiplyCubie(Cubie other){

        multiplyCorners(other);
        multiplyEdges(other);


    }
    private void multiplyCorners(Cubie other){

        CORNER[] CornerPermutation = new CORNER[this.cornerPermutation.length];
        byte[] CornerOrientation = new byte[this.cornerOrientation.length];

        for (int i = 0; i < CornerPermutation.length; i++) {

            CornerPermutation[i] = this.cornerPermutation[other.getCornerPermutation()[i].ordinal()];

            CornerOrientation[i] = this.cornerOrientation[other.getCornerPermutation()[i].ordinal()];
            CornerOrientation[i] += other.getCornerOrientation()[i];
            CornerOrientation[i] %= 3;

        }

        setCorners(CornerPermutation,CornerOrientation);

    }
    private void multiplyEdges(Cubie other){

        EDGE[] edgePermutation = new EDGE[this.edgePermutation.length];
        byte[] edgeOrientation = new byte[this.edgeOrientation.length];

        for (int i = 0; i < edgePermutation.length; i++) {

            edgePermutation[i] = this.edgePermutation[other.getEdgePermutation()[i].ordinal()];

            edgeOrientation[i] = this.edgeOrientation[other.getEdgePermutation()[i].ordinal()];
            edgeOrientation[i] += other.getEdgeOrientation()[i];
            edgeOrientation[i] %= 2;


        }

        setEdges(edgePermutation, edgeOrientation);

    }

    public short getCornerOrientationNumber(){

        short code = 0;
        for(int i = 0; i < this.cornerOrientation.length - 1; i++){

            code *= 3;
            code += this.cornerOrientation[i];

        }

        return code;

    }
    public short getEdgeOrientationNumber(){

        short code = 0;
        for(int i = 0; i < this.edgeOrientation.length - 1; i++){

            code *= 2;
            code += this.edgeOrientation[i];

        }

        return code;

    }
    public short getUDSliceNumber(){

        NumberFinder n = (a) -> (a >= FR.ordinal() && a <= BR.ordinal());

        int[] combinationArr = setCombinationArr(this.edgePermutation, n);
        int[] permutationArr = setPermutationArr(this.edgePermutation,n,4, FR.ordinal());


        short combination = (short) Encoder.encodeCombination(combinationArr, n);

        //THIS IS A MATHEMATICAL FUNCTION FOR ENCODING A PERMUTATION
        //GIVES A CODE FOR THIS RANDOM PERMUTATION OF THE UD SLICE EDGES
        short permutation = (short) Encoder.toLehmerCode(permutationArr);


        //COMBINING THE COMBINATION CODE AND THE PERMUTATION CODE INTO A SINGLE CODE
        return (short) (combination * 24 + permutation);

    }
    public short getCornerPermutationNumber(){


        NumberFinder n = (a) -> (a <= DLF.ordinal() && a >= 0);

        int[] combinationArr = setCombinationArr(this.cornerPermutation,n);
        int[] permutationArr = setPermutationArr(this.cornerPermutation, n, 6, 0);


        //THIS IS A MATHEMATICAL FUNCTION FOR ENCODING A COMBINATION
        // ( TAKING K ELEMENTS FROM N ELEMENTS WITHOUT CONSIDERING THE ORDER )
        short combination = (short) Encoder.encodeCombination(combinationArr,n);

        //THIS IS A MATHEMATICAL FUNCTION FOR ENCODING A PERMUTATION
        //GIVES A CODE FOR THIS RANDOM PERMUTATION OF THE UD SLICE EDGES
        short permutation = (short) LehmerCoder.toLehmerCode(permutationArr);


        //COMBINING THE COMBINATION CODE AND THE PERMUTATION CODE INTO A SINGLE CODE
        return (short) (combination * 720 + permutation);


    }
    public short getURtoUL(){

        NumberFinder n = (a) -> (a >= UR.ordinal() && a <= UL.ordinal());

        int[] combinationArr = setCombinationArr(this.edgePermutation, n);
        int[] permutationArr = setPermutationArr(this.edgePermutation, n, 3, 0);

        short combination = (short) Encoder.encodeCombination(combinationArr,n);
        short permutation = (short) Encoder.toLehmerCode(permutationArr);



        return (short) (combination * 6 + permutation);

    }
    public short getUBtoDF(){

        NumberFinder n = (a) -> (a >= UB.ordinal() && a <= DF.ordinal());

        int[] combinationArr = setCombinationArr(this.edgePermutation,n);
        int[] permutationArr = setPermutationArr(this.edgePermutation,n,3,UB.ordinal());

        short combination = (short) Encoder.encodeCombination(combinationArr,n);
        short permutation = (short) Encoder.toLehmerCode(permutationArr);

        return (short) (combination * 6 + permutation);

    }
    public byte getParity(){

        int s = 0;
        for (int i = DRB.ordinal(); i >= URF.ordinal() + 1; i--)
            for (int j = i - 1; j >= URF.ordinal(); j--)
                if (this.cornerPermutation[j].ordinal() > this.cornerPermutation[i].ordinal())
                    s++;
        return (byte) (s % 2);

    }
    public short getURtoDF(){

        //Notice this function assume the cube is already in G2 !
        //If the cube isn't, unexpected things may happen.
        NumberFinder n = (a) -> (a >= UR.ordinal() && a <= DF.ordinal());

        EDGE[] shorterEdges = new EDGE[8];
        System.arraycopy(this.edgePermutation, 0, shorterEdges, 0, shorterEdges.length);

        int[] combinationArr = setCombinationArr(shorterEdges,n);
        int[] permutationArr = setPermutationArr(shorterEdges, n,6,0);

        short combination = (short) Encoder.encodeCombination(combinationArr,n);
        short permutation = (short) Encoder.toLehmerCode(permutationArr);


        return (short) (combination * 720 + permutation);

    }
    public short getURtoDF(short URtoUL, short UBtoDF){

        Cubie a = new Cubie();
        Cubie b = new Cubie();

        a.setURtoULNumber(URtoUL);
        b.setUBtoDFNumber(UBtoDF);

        Cubie c = new Cubie();
        EDGE[] merged = c.getEdgePermutation();

        EDGE currentA,currentB;


        for(int i = 0; i < EDGE.values().length; i++){

            currentA = a.getEdgePermutation()[i];
            currentB = b.getEdgePermutation()[i];
            if(currentA.ordinal() <= UL.ordinal() &&
               currentB.ordinal() >= UB.ordinal() && currentB.ordinal() <= DF.ordinal()) {
                    return -1;
            } else if (currentA.ordinal() > UL.ordinal()) {
                merged[i] = currentB;
            }
            else{
                merged[i] = currentA;
            }

        }

        return c.getURtoDF();


    }
    public void setCornerOrientationNumber(short code){

        int cornerOrientationSum = 0;
        for(int i = this.cornerOrientation.length - 2; i >= 0; i--){

            this.cornerOrientation[i] = (byte) (code % 3);
            code /= 3;

            cornerOrientationSum += this.cornerOrientation[i];
        }


        //calc the last orientation
        cornerOrientationSum %= 3;
        this.cornerOrientation[CORNER.values().length-1] = (byte) ((cornerOrientationSum == 0) ? (0) : (3-cornerOrientationSum));

    }
    public void setEdgeOrientationNumber(short code){

        int edgeOrientationSum = 0;
        for(int i = this.edgeOrientation.length - 2; i >= 0; i--){

            this.edgeOrientation[i] = (byte) (code % 2);
            code /= 2;


            edgeOrientationSum += this.edgeOrientation[i];
        }

//        calc last orientation
        this.edgeOrientation[EDGE.values().length-1] = (byte) ((edgeOrientationSum % 2 == 0) ? 0 : 1);

    }
    public void setUDSliceNumber(short code){

        int combinationNumber = code / 24;
        int permutationNumber = code % 24;

        int[] combinationArr = Encoder.decodeCombination(combinationNumber,EDGE.values().length, 4);
        int[] permutationArr = Encoder.fromLehmerCode(permutationNumber,4);

        int i,j = 0;
        for(i = 0; i < combinationArr.length; i++){

            if(combinationArr[i] == 1){
                this.edgePermutation[i] = EDGE.values()[permutationArr[j++]+FR.ordinal()];
            }
            else{
                this.edgePermutation[i] = UR;
            }

        }


    }
    public void setCornerPermutationNumber(short code){

        int combinationNumber = code / 720;
        int permutationNumber = code % 720;

        int[] combinationArr = Encoder.decodeCombination(combinationNumber,CORNER.values().length, 6);
        int[] permutationArr = Encoder.fromLehmerCode(permutationNumber,6);

        int i,j = 0;
        for(i = 0; i < combinationArr.length; i++){

            if(combinationArr[i] == 1){
                this.cornerPermutation[i] = CORNER.values()[permutationArr[j++]];
            }
            else{
                this.cornerPermutation[i] = DRB;
            }

        }


    }
    public void setURtoULNumber(short code){

        int combinationNumber = code / 6;
        int permutationNumber = code % 6;

        int[] combinationArr = Encoder.decodeCombination(combinationNumber, EDGE.values().length, 3);
        int[] permutationArr = Encoder.fromLehmerCode(permutationNumber, 3);

        int i,j = 0;
        for(i = 0; i < combinationArr.length; i++){

            if(combinationArr[i] == 1){
                this.edgePermutation[i] = EDGE.values()[permutationArr[j++]];
            }
            else{
                this.edgePermutation[i] = BR;
            }

        }


    }
    public void setUBtoDFNumber(short code){

        int combinationNumber = code / 6;
        int permutationNumber = code % 6;

        int[] combinationArr = Encoder.decodeCombination(combinationNumber, EDGE.values().length, 3);
        int[] permutationArr = Encoder.fromLehmerCode(permutationNumber, 3);

        int i,j = 0;
        for(i = 0; i < combinationArr.length; i++){

            if(combinationArr[i] == 1){
                this.edgePermutation[i] = EDGE.values()[permutationArr[j++]+3];
            }
            else{
                this.edgePermutation[i] = BR;
            }

        }

    }
    public void setURtoDFNumber(short code){

        int combination = code / 720;
        int permutation = code % 720;

        int[] combinationArr = Encoder.decodeCombination(combination,8, 6);
        int[] permutationArr = Encoder.fromLehmerCode(permutation,6);

        int i,j = 0;
        for(i = 0; i < combinationArr.length; i++){

            if(combinationArr[i] == 1){
                this.edgePermutation[i] = EDGE.values()[permutationArr[j++]];
            }
            else{
                this.edgePermutation[i] = BR;
            }

        }


    }


    //ADDED CODE
    public void setURtoDFNumber(short URtoUL, short UBtoDF){

        Cubie a = new Cubie();
        Cubie b = new Cubie();

        a.setURtoULNumber(URtoUL);
        b.setUBtoDFNumber(UBtoDF);

        Cubie c = new Cubie();
        EDGE[] merged = c.getEdgePermutation();

        EDGE currentA,currentB;


        for(int i = 0; i < EDGE.values().length; i++){

            currentA = a.getEdgePermutation()[i];
            currentB = b.getEdgePermutation()[i];
            if (currentA.ordinal() > UL.ordinal()) {
                merged[i] = currentB;
            }
            else{
                merged[i] = currentA;
            }

        }

    }
    private <T extends Enum<T>> int[] setCombinationArr(T[] arr, NumberFinder n){

        int[] result = new int[arr.length];
        for(int i = 0; i < arr.length; i++){

            if(n.isInNumbers(arr[i].ordinal())){
                result[i] = arr[i].ordinal();
            }
            else{
                result[i] = -1;
            }

        }
        return result;

    }
    private <T extends Enum<T>> int[] setPermutationArr(T[] arr,  NumberFinder n, int length, int offset){

        int[] result = new int[length];

        int j = 0;
        for (T t : arr) {

            if (n.isInNumbers(t.ordinal())) {
                result[j++] = t.ordinal() - offset;
            }

        }

        return result;

    }


    private void setCorners(COLOR[] fColors){

        FACELET[][] mapToCorner = new FACELET[][]{

                /*URF*/ {U9, R1, F3},
                /*UFL*/ {U7, F1, L3},
                /*ULB*/ {U1, L1, B3},
                /*UBR*/ {U3, B1, R3},
                /*DFR*/ {D3, F9, R7},
                /*DLF*/ {D1, L9, F7},
                /*DBL*/ {D7, B9, L7},
                /*DRB*/ {D9, R9, B7}

        };

        FACELET[] currentCornerFacelets;
        COLOR[] currentColors = new COLOR[3];
        byte currentOrientation = 0;
        for(int i = 0; i < mapToCorner.length; i++){

            currentCornerFacelets = mapToCorner[i];

            for(byte j = 0; j < currentCornerFacelets.length; j++){

                currentColors[j] = fColors[currentCornerFacelets[j].ordinal()];
                if(currentColors[j] == U||
                   currentColors[j] == D)
                    currentOrientation = j;

            }

            this.cornerPermutation[i] = CORNER.convertFacelets(currentColors);
            this.cornerOrientation[i] = currentOrientation;


        }


    }
    private void setEdges(COLOR[] fColors){

        FACELET[][] mapToEdge = {
                /*UR*/      {U6, R2},
                /*UF*/      {U8, F2},
                /*UL*/      {U4, L2},
                /*UB*/      {U2, B2},
                /*DR*/      {D6, R8},
                /*DF*/      {D2, F8},
                /*DL*/      {D4, L8},
                /*DB*/      {D8, B8},
                /*FR*/      {F6, R4},
                /*FL*/      {F4, L6},
                /*BL*/      {B6, L4},
                /*BR*/      {B4, R6}
        };

        FACELET[] currentFacelets;
        COLOR[] currentColor = new COLOR[2];
        byte currentOrientation = 0;

        for(int i = 0; i < mapToEdge.length; i++){

            currentFacelets = mapToEdge[i];
            for(int j = 0; j < currentFacelets.length; j++){

                currentColor[j] = fColors[currentFacelets[j].ordinal()];

                if(j == 1){

                    if(currentColor[j] == U || currentColor[j] == D){
                        currentOrientation = 1;
                    } else if (currentColor[j-1] == U || currentColor[j-1] == D) {
                        currentOrientation = 0;
                    }
                    else if(currentColor[j-1] == F || currentColor[j-1] == B){
                        currentOrientation = 0;
                    }
                    else{
                        currentOrientation = 1;
                    }

                }

            }

            this.edgePermutation[i] = EDGE.convertFacelets(currentColor);
            this.edgeOrientation[i] = currentOrientation;

        }

    }
    private void setCorners(CORNER[] cornerPermutation, byte[] cornerOrientation){

        for(int i = 0; i < CORNER.values().length; i++){

            this.cornerPermutation[i] = cornerPermutation[i];
            this.cornerOrientation[i] = cornerOrientation[i];

        }

    }
    private void setEdges(EDGE[] edgePermutation, byte[] edgeOrientation){

        for(int j = 0; j < EDGE.values().length; j++){

            this.edgePermutation[j] = edgePermutation[j];
            this.edgeOrientation[j] = edgeOrientation[j];

        }

    }


    public CORNER[] getCornerPermutation(){

        return this.cornerPermutation;

    }

    public byte[] getCornerOrientation() {

        return this.cornerOrientation;

    }

    public EDGE[] getEdgePermutation() {

        return this.edgePermutation;

    }

    public byte[] getEdgeOrientation() {

        return this.edgeOrientation;

    }

    @Override
    public String toString() {
        return "Cubie{" +
                "cornerPermutation=" + Arrays.toString(cornerPermutation) +
                ", cornerOrientation=" + Arrays.toString(cornerOrientation) +
                ", edgePermutation=" + Arrays.toString(edgePermutation) +
                ", edgeOrientation=" + Arrays.toString(edgeOrientation) +
                '}';
    }
}
