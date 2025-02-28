package Test0812;


import java.util.Scanner;

public class Test01 {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();
        String l = sc.nextLine();

        int sIndex = 0;
        int lIndex = 0;

        while (sIndex < s.length() && lIndex < l.length()) {
            if (s.charAt(sIndex) == l.charAt(lIndex)) {
                sIndex ++;
            }
            lIndex ++;
        }

        if (sIndex == s.length()) {
            System.out.println(lIndex-1);
        } else {
            System.out.println(-1);
        }

    }

}
