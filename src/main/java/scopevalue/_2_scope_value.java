void m(int value) {
  var message = MESSAGE.get();
  IO.println(message + " " + value);
}

static final ScopedValue<String> MESSAGE = ScopedValue.newInstance();

void main() {
  ScopedValue
      .where(MESSAGE, "hello")
      .run(() -> m(42));
}
