/*output:
Enter  array size:5
5 4 3 2 1
Array is in Monotonic Nature
 
Enter  array size:5
1 2 3 4 5
Array is in Monotonic Nature

Enter  array size:3
5 1 2
Array is not in Monotonic Nature
*/
import java.util.*;
public class Monotonic {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        System.out.print("Enter  array size:");
        int n = s.nextInt();
        int a[]=new int[n];
        boolean in=true;
        boolean de=true;
        for(int i =0;i<n;i++){
            a[i]=s.nextInt();
        }
        for(int j=0;j<n-1;j++){
            if(a[j]<a[j+1]){
                in=false;
            }
            else{
                de=false;
            }
        }
        if(in || de){
            System.out.println("Array is in Monotonic Nature");
        }
        else{
            System.out.println("Array is not in Monotonic Nature");   
        }
        s.close();
    }
}
