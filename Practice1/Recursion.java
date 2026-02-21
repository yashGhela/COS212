public class Recursion{
    public static void main(String [] args){
        sayHi(5);
    }

    public static void sayHi(int n){ //this is tail recursion 
        if (n<=1){
            return;
        }else if(n%2==0){
            System.out.println("Even: "+ n);
            sayHi(n-1);
        }else{
            System.out.println("Odd: "+ n);
            sayHi(n-1);
        }
        
    }
}