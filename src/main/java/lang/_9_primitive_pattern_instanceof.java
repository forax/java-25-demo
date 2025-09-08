sealed interface Vehicle {}
record Plane(double x, double y) implements Vehicle {}

void main() {
  Vehicle vehicle = new Plane(.6, 1024);

  if (vehicle instanceof Plane(int x, int y)) {
    IO.println("plane " + x +  " " + y);
  } else {
    IO.println("not a plane");
  }
}
