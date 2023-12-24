package org.example.Cubes;

import org.example.Enums.COLOR;

public class FaceCube {

    private static final int SIDES = 6;
    private static final int FACELETS = 9;
    private COLOR[] color = new COLOR[SIDES * FACELETS];


    /**
     *    U- face:
     *
     *
     *          U1     U2     U3
     *
     *          U4     U5     U6
     *
     *          U7     U8     U9
     *
     *
     * */

    public FaceCube(){

        for(int i = 0; i < SIDES * FACELETS; i++){

            color[i] = COLOR.values()[i/FACELETS];

        }

    }

    public FaceCube(FaceCube f){

        COLOR[] fColors = f.getColor();
        for(int i = 0; i < SIDES*FACELETS; i++){

            this.color[i] = fColors[i];

        }

    }

    public COLOR[] getColor(){

        return this.color;

    }

}
