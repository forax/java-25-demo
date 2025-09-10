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

private static final Supplier<BigObject> BIG_OBJECT_SUPPLIER =
    StableValue.supplier(() -> new BigObject());

static BigObject getBigObject() {
  return BIG_OBJECT_SUPPLIER.get();
}

void main() {
  IO.println(getBigObject());
}
