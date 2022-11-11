import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws IOException {
        System.out.println("Hello World!");
        List<String[]> collect =
                Files.lines(Paths.get("C:\\DEV\\studiehulp\\anais\\CSV\\data.csv"))
                        .map(line -> line.split(","))
                        .collect(Collectors.toList());


    }
}
