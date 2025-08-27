import static java.lang.foreign.ValueLayout.JAVA_INT;

static final MemoryLayout POINT = MemoryLayout.structLayout(
    JAVA_INT.withName("x"),
    JAVA_INT.withName("y")
);

static final VarHandle X = POINT.varHandle(MemoryLayout.PathElement.groupElement("x"));
static final VarHandle Y = POINT.varHandle(MemoryLayout.PathElement.groupElement("y"));

static final VarHandle ARRAY_X = POINT.arrayElementVarHandle(MemoryLayout.PathElement.groupElement("x"));
static final VarHandle ARRAY_Y = POINT.arrayElementVarHandle(MemoryLayout.PathElement.groupElement("y"));

void main() {
  try(var arena = Arena.ofConfined()) {
    var segment = arena.allocate(POINT, 16);
    for(var i = 0; i < 16; i++) {
      ARRAY_X.set(segment, 0L, i, i);
    }

    for(var i = 0; i < 16; i++) {
      System.out.println((int) ARRAY_X.get(segment, 0L, i));
    }
  }
}
