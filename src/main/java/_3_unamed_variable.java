void main() {
  var list = IntStream.range(0, 3).boxed().toList();
  for(var _ : list) {
    System.out.println("hello");
  }

  boolean exist;
  try(var _ = Files.newInputStream(Path.of("README.md"))) {
    var op = (UnaryOperator<Boolean>) _ -> true;
    exist = op.apply(true);
  } catch (IOException _) {
    exist = false;
  }
  System.out.println("README exists " + exist);
}
