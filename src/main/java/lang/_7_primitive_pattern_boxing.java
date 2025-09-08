void main() {
  var x = 42;

  switch (x) {
    case Integer _ -> IO.println("int");
    case byte _ -> IO.println("byte");
  }
}
