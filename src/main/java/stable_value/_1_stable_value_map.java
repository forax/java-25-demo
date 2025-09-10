static class BigObject {
  final String field1, field2, field3, field4;

  BigObject(String key) {
    this.field1 = key;
    this.field2 = key;
    this.field3 = key;
    this.field4 = key;
    // ...
    super();
  }
}

private static final Map<String, BigObject> BIG_OBJECT_MAP =
    StableValue.map(Set.of("foo", "bar", "hello"), key -> new BigObject(key));

static BigObject getBigObject(String key) {
  return BIG_OBJECT_MAP.get(key);
}

void main() {
  IO.println(getBigObject("hello"));
}
