import java.util.Arrays;

public class Application {

  public static void main(String[] args) {
    System.out.println("Hello World");
  }

  public void CommandLineRunner() {
    var LongString = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Porta lorem";

    String[] beanNames = {"foo", "bar"};
    Arrays.sort(beanNames);
    for (String beanName : beanNames)
        {
          System.out.println(beanName);
      }
  }

}
