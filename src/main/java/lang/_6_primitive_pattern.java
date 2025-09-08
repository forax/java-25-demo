void main() {
  var list = List.of(42, 420, 4200000);

  for(int value : list) {
    switch (value) {
      case byte b -> IO.println("byte " + b);
      case short s -> IO.println("short " + s);
      case int i -> IO.println("int " + i);
    }
  }
}
