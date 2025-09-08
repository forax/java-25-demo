record Plane(float x, float y) {}

void main() {
  Plane plane = new Plane(200_000_007, 16_777_219);

  switch (plane) {
    case Plane(int x, int y) -> IO.println("plane " + x +  " " + y);
    default -> IO.println("not a plane");
  }
}
