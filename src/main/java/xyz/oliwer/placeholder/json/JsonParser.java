package xyz.oliwer.placeholder.json;

/**
 * This interface represents the base for every JSON parser implementation
 * used in some placeholders such as {@link xyz.oliwer.placeholder.def.ApiPlaceholder}.
 *
 * @see xyz.oliwer.placeholder.json.impl.JsoniterParser
 * @author Oliwer - https://www.github.com/ImOliwer
 */
public interface JsonParser {
  /**
   * Deserialize the JSON content in a string.
   *
   * @param content {@link String} the content to be deserialized.
   * @return {@link Deserialized}
   */
  Deserialized deserialize(String content);

  /**
   * Serialize the content of an object into a string.
   *
   * @param object {@link Object} the object to be serialized.
   * @return {@link String}
   */
  String serialize(Object object);
}