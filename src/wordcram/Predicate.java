package wordcram;

interface Predicate<T> {
  boolean matches(T object);
}
