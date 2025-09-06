record Person(String name, int age) {
  Person {
    Objects.requireNonNull(name);
    if (age < 0) {
      throw new IllegalArgumentException("age < 0");
    }
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof Person(var name, _) && this.name.equals(name);
  }

  @Override
  public int hashCode() {
    return name.hashCode();
  }
}

void main() {
  var set = new HashSet<>(Set.of(
      new Person("Jane", 42),
      new Person("Bob", 24),
      new Person("Janice", 36)));

  IO.println(set.contains(new Person("Jane", 12)));
  //set.removeIf(_ -> true);
  //IO.println(set);
}
