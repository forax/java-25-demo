import java.lang.foreign.Arena;
import java.lang.foreign.MemoryLayout;
import java.lang.invoke.VarHandle;

import static java.lang.foreign.MemoryLayout.PathElement.groupElement;
import static java.lang.foreign.MemoryLayout.structLayout;
import static java.lang.foreign.ValueLayout.JAVA_INT;

static final MemoryLayout POINT = structLayout(
    JAVA_INT.withName("x"),
    JAVA_INT.withName("y")
);

static final VarHandle X = POINT.varHandle(groupElement("x"));
static final VarHandle Y = POINT.varHandle(groupElement("y"));

void main() {
  try(var arena = Arena.ofConfined()) {
    var segment = arena.allocate(POINT);
    X.set(segment, 0L, 12);
    System.out.println( (int) X.get(segment, 0L));  // box if not cast ahhhhh
  }
}
