static class BigObject {
  int field1, field2, field3, field4;

  BigObject() {
    this.field1 = 1;
    this.field2 = 2;
    this.field3 = 3;
    this.field4 = 4;
    // ...
    super();
  }
}

private static final Object LOCK = new Object();
private static volatile BigObject BIG_OBJECT;

public static BigObject getBigObject() {
  if (BIG_OBJECT != null) {
    return BIG_OBJECT;
  }
  synchronized(LOCK) {
    if (BIG_OBJECT != null) {
      return BIG_OBJECT;
    }
    return BIG_OBJECT = new BigObject();
  }
}

void main() {
  IO.println(getBigObject());
}
