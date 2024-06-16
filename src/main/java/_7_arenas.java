import java.lang.foreign.Arena;
import java.lang.foreign.ValueLayout;

void main() {
  try(var arena = Arena.ofConfined()) {
    var segment = arena.allocate(16);
    segment.set(ValueLayout.JAVA_BYTE, 7, (byte) 12);
    System.out.println(segment.get(ValueLayout.JAVA_BYTE, 7));
  }

  try(var arena = Arena.ofShared()) {
    var segment = arena.allocate(16);
    segment.set(ValueLayout.JAVA_BYTE, 7, (byte) 12);
    System.out.println(segment.get(ValueLayout.JAVA_BYTE, 7));
  }
}
