package org.example.Utils.CubieUtils;

public class LehmerCoder {

    private static final int[] factorial = new int[]{
            1, 1, 2, 6, 24, 120, 720, 5040, 40320, 362880, 3628800, 39916800
    };

    public static int factorial(int n) {

        if( n < factorial.length ){
            return factorial[n];
        }

        int result = 1;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }


    public static int toLehmerCode(int[] number){

        int digit;
        int code = 0;
        boolean[] used = new boolean[number.length];

        int smallerNotUsed = 0;

        for(int i = 0; i < number.length; i++){

            digit = number[i];

            for(int j = 0; j < digit; j++){
                if(!used[j]){
                    smallerNotUsed++;
                }
            }

            code += smallerNotUsed * factorial(number.length - i - 1);

            used[digit] = true;
            smallerNotUsed = 0;

        }


        return code;

    }

    public static int[] fromLehmerCode(int code, int length) {

        int[] digits = new int[length];
        boolean[] used = new boolean[length + 1];

        for (int i = 0; i < length; i++) {
            int f = factorial(length - i - 1);
            int index = code / f + 1;
            code %= f;

            for (int j = 1; j <= length; j++) {
                if (!used[j]) {
                    index--;
                    if (index == 0) {
                        digits[i] = j-1;
                        used[j] = true;
                        break;
                    }
                }
            }
        }

        return digits;
    }


}

