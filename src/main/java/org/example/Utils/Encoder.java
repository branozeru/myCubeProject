package org.example.Utils;


public class Encoder extends LehmerCodeConvertor {


    public static int encodeCombination(int[] arr, NumberFinder numberFinder){

        int k = 0;
        int combination = 0;
        for(int i = arr.length - 1; i >= 0; i--){

            if(numberFinder.isInNumbers(arr[i])){

                k++;

            }
            else if(k>=1){
                combination += nCk(arr.length - i - 1,k - 1);
            }

        }

        return combination;

    }

    public static int[] decodeCombination(int code, int n, int k) {

        int[] resultArray = new int[n];

        int i;
//        for(i = 0; i < resultArray.length; i++){
//            resultArray[i] = 0;
//        }

        int bin, a = k;
        for(i = n-1; i >= 0; i--){

            bin = nCk(i,a-1);

            if(code >= bin){

                code -= bin;

            }
            else{

                resultArray[n-i-1] = 1;
                a--;

            }

        }
        return resultArray;

    }


    public static int nCk(int n, int k){

        if( k > n || k < 0 )
            return 0;

        int nPk = factorial(n) / factorial(n - k);

        return nPk / factorial(k);


    }

}
