import java.util.stream.IntStream;
//import module java.base;

void main() {
  var list = IntStream.range(0, 5).boxed().toList();
  //for(var element : list) {
  //  System.out.println("hello");
  //}

  for(var _ : list) {
    System.out.println("hello");
  }
}
