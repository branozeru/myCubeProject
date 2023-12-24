package org.example.Enums;


public enum CORNER {

    URF, UFL, ULB, UBR,

    DFR, DLF, DBL, DRB;

    public static CORNER convertFacelets(COLOR[] color){

        int index = 0;

        for(int i = 1; i < color.length; i++){
            if(color[i] == COLOR.U || color[i] == COLOR.D){
                COLOR temp = color[i];
                color[i] = color[0];
                color[0] = temp;
                break;
            }
        }

        if(color[0] == COLOR.D)
            index += 4;

        if(color[1] == COLOR.B || color[2] == COLOR.B){

            index += 2;
            if(color[1] == COLOR.R || color[2] == COLOR.R){
                index++;
            }

        } else if (color[1] == COLOR.L || color[2] == COLOR.L) {

            index++;

        }

        return CORNER.values()[index];
    }

}
