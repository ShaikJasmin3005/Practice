import java.util.*;
public class Monotonic {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        System.out.print("Enter  array size:");
        int n = s.nextInt();
        int a[]=new int[n];
        for(int i =0;i<n;i++){
            a[i]=s.nextInt();
        }
        for(int j=0;j<n-1;j++){
            if(a[j]<a[j+1]){
                System.out.println("Array is in Non Monotonic nature");
                break;
            }
            else{
                System.out.println("Array is in  Monotonic nature");
                break;
            }
        }
        s.close();
    }
