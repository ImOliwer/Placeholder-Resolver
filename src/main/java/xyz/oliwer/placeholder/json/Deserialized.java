package xyz.oliwer.placeholder.json;

/**
 * This interface represents the base of "deserialized object" implementations
 * used in placeholders such as {@link xyz.oliwer.placeholder.def.ApiPlaceholder}.
 *
 * @see xyz.oliwer.placeholder.json.impl.JsoniterDeserialized
 * @author Oliwer - https://www.github.com/ImOliwer
 */
public interface Deserialized {
  /**
   * Get an element in an array from the deserialized hierarchy.
   *
   * @param index {@link Integer} index of said element.
   * @return {@link Deserialized}
   */
  Deserialized get(int index);

  /**
   * Get an element from this deserialized object's map hierarchy.
   *
   * @param key {@link String} key of the element to fetch.
   * @return {@link Deserialized}
   */
  Deserialized get(String key);

  /**
   * Create a copy of this instance.
   *
   * @return {@link Deserialized}
   */
  Deserialized copy();
}