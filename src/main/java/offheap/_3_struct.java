import static java.lang.foreign.ValueLayout.JAVA_INT;

static final MemoryLayout POINT = MemoryLayout.structLayout(
    JAVA_INT.withName("x"),
    JAVA_INT.withName("y")
);

static final VarHandle X = POINT.varHandle(MemoryLayout.PathElement.groupElement("x"));
static final VarHandle Y = POINT.varHandle(MemoryLayout.PathElement.groupElement("y"));

void main() {
  try(var arena = Arena.ofConfined()) {
    var segment = arena.allocate(POINT);
    X.set(segment, 0L, 12);
    System.out.println( (int) X.get(segment, 0L));  // box if not cast ahhhhh
  }
}
