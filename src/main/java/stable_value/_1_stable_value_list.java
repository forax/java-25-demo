static class BigObject {
  int field1, field2, field3, field4;

  BigObject(int value) {
    this.field1 = value;
    this.field2 = value;
    this.field3 = value;
    this.field4 = value;
    // ...
  }
}

private static final List<BigObject> BIG_OBJECT_LIST =
    StableValue.list(100, index -> new BigObject(index));

static BigObject getBigObject(int index) {
  return BIG_OBJECT_LIST.get(index);
}

void main() {
  IO.println(getBigObject(42));
}
