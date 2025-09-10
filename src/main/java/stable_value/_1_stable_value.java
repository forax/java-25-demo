static class BigObject {
  final int field1, field2, field3, field4;

  BigObject() {
    this.field1 = 1;
    this.field2 = 2;
    this.field3 = 3;
    this.field4 = 4;
    // ...
    super();
  }
}

private static final StableValue<BigObject> BIG_OBJECT =
    StableValue.of();

public static BigObject getBigObject() {
  return BIG_OBJECT.orElseSet(() -> new BigObject());
}

void main() {
  IO.println(getBigObject());
}
