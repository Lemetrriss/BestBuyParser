import java.util.Scanner;

public class TaskB {

    private static int M;
    private static int N;

    private double factorial(double n) {
        if( n <= 1 ){
            return 1;
        } else
            return n * factorial( n - 1 );
    }

    private int laminate() {
        int count=0;
        if (N<M) {
            return 1;
        } else if (N==M){
            return 2;
        } else if(N<M*2){
            return N-M+2;
        } else {
            int kvadrat = N/M;
            for (int j=1; j<=kvadrat; j++){
                int polosa = N-M*j;
                count += factorial(j+polosa)/(factorial(j)*factorial(polosa));
            }
        }
        return count+1;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        if (sc.hasNextInt()){
            M = sc.nextInt();
            N = sc.nextInt();
        }
        if(M>=2 && M<=10 && N>=1 && N <= 40){
            System.out.print(new TaskB().laminate());
        }
        sc.close();
    }
}