static final class Person {
  private final String name;
  private final int age;

  Person(String name, int age) {
    //super();
    Objects.requireNonNull(name);
    if (age < 0) {
      throw new IllegalArgumentException("age < 0");
    }
    //System.out.println(this);
    super();
    this.name = name;
    this.age = age;
    // super();
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof Person p && this.name.equals(p.name);
  }

  @Override
  public int hashCode() {
    return name.hashCode();
  }

  @Override
  public String toString() {
    return "Person[" +
        "name=" + name + ", " +
        "age=" + age + ']';
  }

}

void main() {
  var set = new HashSet<>(Set.of(
      new Person("Jane", 42),
      new Person("Bob", 24),
      new Person("Janice", 36)));

  System.out.println(set);
}
