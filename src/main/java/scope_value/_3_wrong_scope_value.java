void m(int value) {
  var message = MESSAGE2.get();
  IO.println(message + " " + value);
}

static final ScopedValue<String> MESSAGE = ScopedValue.newInstance();
static final ScopedValue<String> MESSAGE2 = ScopedValue.newInstance();

void main() {
  ScopedValue
      .where(MESSAGE, "hello")
      .run(() -> m(42));
}
