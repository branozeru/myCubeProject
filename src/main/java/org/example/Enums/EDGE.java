package org.example.Enums;

public enum EDGE {


    UR, UF, UL, UB,


    DR, DF, DL, DB,


    FR, FL, BL, BR;

    public static EDGE convertFacelets(COLOR[] color){

        int index = 0;

        if(color[0] == COLOR.U || color[1] == COLOR.U ||
           color[0] == COLOR.D || color[1] == COLOR.D) {

            if(color[0] == COLOR.D || color[1] == COLOR.D){
                index += 4;
            }

            if(color[0] == COLOR.F || color[1] == COLOR.F)
                index += 1;
            else if (color[0] == COLOR.L || color[1] == COLOR.L) {
                index += 2;
            }
            else if(color[0] == COLOR.B || color[1] == COLOR.B){
                index += 3;
            }


        }
        else if(color[0] == COLOR.F || color[1] == COLOR.F){

            index += 8;

            if(color[0] == COLOR.L || color[1] == COLOR.L)
                index+=1;


        }
        else if(color[0] == COLOR.L || color[1] == COLOR.L){
            index += 10;
        }

        else{
            index += 11;
        }

        return EDGE.values()[index];

    }

    public static boolean isUDSlice(EDGE e){
        return e.ordinal() >= FR.ordinal();
    }

}
