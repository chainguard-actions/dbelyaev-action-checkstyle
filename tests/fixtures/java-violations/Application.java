import java.util.Arrays;

public class Application {

  public static void main(String[] args) {
    String[] beanNames = {"foo", "bar"};
    Arrays.sort(beanNames);
    for (String beanName : beanNames)
        {
          System.out.println(beanName);
      }
  }

}
