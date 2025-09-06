void main() {
  var list = IntStream.range(0, 3).boxed().toList();
  for(var _ : list) {
    IO.println("hello");
  }

  boolean exist;
  try(var _ = Files.newInputStream(Path.of("README.md"))) {
    var op = (UnaryOperator<Boolean>) _ -> true;
    exist = op.apply(true);
  } catch (IOException _) {
    exist = false;
  }
  IO.println("README exists " + exist);
}
