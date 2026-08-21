public class BadCode {
public static void main(String[] args) {
int x=1;
String veryLongVariableNameThatExceedsTheLineLengthLimitSetByGoogleJavaStyleGuideForCheckstyleToDetect = "this is a very long string that should trigger a line length violation in checkstyle google checks";
}
}
