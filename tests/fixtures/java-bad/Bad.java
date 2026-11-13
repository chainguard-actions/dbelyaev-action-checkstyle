package fixtures;
public class Bad {
    public  void  doSomething(  ) {
        int   x=1;   // this line is intentionally long and badly formatted to trigger many google_checks style warnings, definitely beyond one hundred columns
        if(x==1){System.out.println("bad");}
    }
}
