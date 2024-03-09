import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] a = new int[n+1];
        for(int i=1;i<=n;i++){
            a[i]=sc.nextInt();
        }Arrays.sort(a);
        for(int i=1;i<=n;i++)
        {
            if(a[i]==i)
                continue;
            System.out.println("0");
            return;
        }
        System.out.println("1");
        System.out.println(1+" "+100);
    }
}

