import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.IntStream;
//import module java.base;

void main() {
  var list = IntStream.range(0, 3).boxed().toList();
  for(var _ : list) {
    System.out.println("hello");
  }

  boolean exist;
  try(var _ = Files.newInputStream(Path.of("README.md"))) {
    exist = true;
  } catch (IOException _) {
    exist = false;
  }
  System.out.println("README exists " + exist);
}
