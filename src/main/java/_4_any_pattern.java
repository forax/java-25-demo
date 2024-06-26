import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

record Person(String name, int age) {
  Person {
    Objects.requireNonNull(name);
    if (age < 0) {
      throw new IllegalArgumentException("age < 0");
    }
  }

  @Override
  public boolean equals(Object obj) {
    //return obj instanceof Person(var name, var age) && this.name.equals(name);
    //return obj instanceof Person(var name, var _) && this.name.equals(name);
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

  System.out.println(set.contains(new Person("Jane", 12)));
  //set.removeIf(_ -> true);
  //System.out.println(set);
}
