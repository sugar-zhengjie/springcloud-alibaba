package Test0812;


import java.util.Scanner;

public class Test02 {

    public static void main(String[] args) {
        int[] arr = {0, 1, 2, 3, 2, 4};
        int[] arr2 = {0, 1, 4, 3, 1, 0, 0, 1, 2, 3, 1, 2, 1, 0};
        System.out.println(deal(arr));
        System.out.println(deal(arr2));
    }

    public static int deal(int[] inArr) {
        int count = 0;
        for (int i = 0; i <= inArr.length -1; i ++) {
            if (i == 0) {
                if (inArr[0] > inArr[1]) {
                    count ++ ;
                }
            } else if (i == inArr.length - 1) {
                if (inArr[i] > inArr[i -1]) {
                    count ++ ;
                }
            } else {
                if (inArr[i] > inArr[i-1] && inArr[i] > inArr[i+1]) {
                    count ++ ;
                }
            }
        }
        return count;
    }

}
