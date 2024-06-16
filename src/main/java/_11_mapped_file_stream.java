import java.io.IOException;
import java.lang.foreign.Arena;
import java.lang.foreign.MemoryLayout;
import java.lang.invoke.VarHandle;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import static java.lang.foreign.MemoryLayout.PathElement.groupElement;
import static java.lang.foreign.MemoryLayout.structLayout;
import static java.lang.foreign.ValueLayout.JAVA_INT;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.READ;
import static java.nio.file.StandardOpenOption.WRITE;

static final MemoryLayout POINT = structLayout(
    JAVA_INT.withName("x"),
    JAVA_INT.withName("y")
);

static final VarHandle X = POINT.varHandle(groupElement("x"));
static final VarHandle Y = POINT.varHandle(groupElement("y"));

static final VarHandle ARRAY_X = POINT.arrayElementVarHandle(groupElement("x"));
static final VarHandle ARRAY_Y = POINT.arrayElementVarHandle(groupElement("y"));

void main() throws IOException {
  try(var channel = FileChannel.open(Path.of("demofile"), READ, WRITE, CREATE)) {
    try(var arena = Arena.ofConfined()) {
      var segment = channel.map(FileChannel.MapMode.READ_WRITE, 0L, 16L * POINT.byteSize(), arena);
      for(var i = 0; i < 16; i++) {
        ARRAY_X.set(segment, 0L, i, i);
      }

      for(var i = 0; i < 16; i++) {
        System.out.println((int) ARRAY_X.get(segment, 0L, i));
      }

      var sumX = segment.elements(POINT)
          .mapToInt(s -> (int) X.get(s, 0L))
          .sum();
      System.out.println(sumX);
    }
  }
}
