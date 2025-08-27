void main() {
  var arena = Arena.global();
  var segment = arena.allocate(16);
  segment.set(ValueLayout.JAVA_BYTE, 7, (byte) 12);
  System.out.println(segment.get(ValueLayout.JAVA_BYTE, 7));
}
