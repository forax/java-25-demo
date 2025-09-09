import org.openjdk.jol.info.ClassLayout;

// -XX:-UseCompressedClassPointers -XX:-UseCompressedOops
// -XX:+UseCompactObjectHeaders
class _1_header {
  public static void main(String[] args) throws InterruptedException {
    //Object instance = new Object();
    Object instance = new Long(42);
    //Object instance = new String("hello");
    IO.println(ClassLayout.parseInstance(instance).toPrintable());

    System.identityHashCode(instance);
    //IO.println(ClassLayout.parseInstance(instance).toPrintable());

    synchronized (instance) {
      //IO.println(ClassLayout.parseInstance(instance).toPrintable());

      new Thread(() -> {
        synchronized (instance) {

        }
      }).start();
      Thread.sleep(1_000);

      //IO.println(ClassLayout.parseInstance(instance).toPrintable());
    }
  }
}
