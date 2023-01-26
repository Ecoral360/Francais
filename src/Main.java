import org.ascore.ASCore;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        ASCore.makeProject("src/java", "codeMdr", ASCore.Template.BASIC, ASCore.Lang.JAVA, true);
    }
}
