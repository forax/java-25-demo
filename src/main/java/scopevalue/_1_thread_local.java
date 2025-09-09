void m(int value) {
  var message = MESSAGE.get();
  IO.println(message + " " + value);
}

static final ThreadLocal<String> MESSAGE = new ThreadLocal<>();

void main() {
  MESSAGE.set("hello");
  try {
    m(42);
  } finally {
    MESSAGE.remove();
  }
}
