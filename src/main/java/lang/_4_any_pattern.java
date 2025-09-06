sealed interface Vehicle {}
record Car(int passenger, int year) implements Vehicle {}
record Truck(String driverName, int weight) implements Vehicle {}

int computeTax(Vehicle vehicle) {
  return switch (vehicle) {
    case Car(int passenger, int _) -> passenger * 10;
    case Truck(_, int weight) -> weight * 42;
  };
}

void main() {
  var vehicles = List.of(
      new Car(5, 1999),
      new Truck("Bob", 100));

  var tax = vehicles.stream().mapToInt(this::computeTax).sum();
  IO.println(tax);
}
