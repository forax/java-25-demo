void main() {
  var list = """
      foo
      bar
      baz
      whizz
      weta
      """.lines().toList();

  // 1 to 0..1
  IO.println(list.stream().filter(s -> s.startsWith("b")).toList());

  // 1 to 1
  IO.println(list.stream().map(s -> "*" + s + "*").toList());

  // 1 to many
  IO.println(list.stream().flatMap(s -> Stream.of(s, "*" + s + "*")).toList());

  // 1 to many
  IO.println(list.stream()
      .<String>mapMulti((s, consumer) -> {
        consumer.accept(s);
        consumer.accept("*" + s + "*");
      })
      .toList());

  // many to 2
  IO.println(list.stream().gather(Gatherers.windowFixed(2)).toList());

  // 1 to 0..1
  IO.println(list.stream().gather(Gatherer.ofSequential(
      () -> new Object() { int seen; },
      (state, element, downstreamm) -> {
        if (state.seen++ % 2 == 0) {
          return downstreamm.push(element);
        }
        return true;
      }
  )).toList());

  // 2 to 1
  IO.println(list.stream().gather(Gatherer.ofSequential(
      () -> new Object() { String result = ""; },
      (state, element, downstreamm) -> {
        if (!state.result.isEmpty()) {
          try {
            return downstreamm.push(state.result.concat(element));
          } finally {
            state.result = "";
          }
        }
        state.result = element;
        return true;
      },
      (state, downstream) -> {
        if (!state.result.isEmpty()) {
          downstream.push(state.result);
        }
      }
  )).toList());
}
