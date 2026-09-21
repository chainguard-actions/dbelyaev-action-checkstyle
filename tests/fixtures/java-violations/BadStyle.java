import java.util.Arrays;

@SuppressWarnings("all")
public class BadStyle {
    public static void main(String[] args) {
        System.out.println("Hello");
        String LongVariableName = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Porta lorem";
        String[] beanNames = {"a","b","c"};
        Arrays.sort(beanNames);
        for (String beanName : beanNames)
            {
                System.out.println(beanName);
        }
    }
}
