package xyz.oliwer.placeholder;

import xyz.oliwer.placeholder.data.DefaultData;
import xyz.oliwer.placeholder.parser.PatternResolver;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This interface represents the base of every placeholder.
 *
 * @see PatternResolver
 * @author Oliwer - https://www.github.com/ImOliwer
 */
public interface Placeholder {
  /**
   * This interface represents the wrapper for placeholder resolvers.
   *
   * @author Oliwer - https://www.github.com/ImOliwer
   */
  @FunctionalInterface
  interface Wrapper {
    /**
     * Get the parent of this wrapper.
     *
     * @return {@link Placeholder}
     */
    Placeholder getParent();
  }

  /**
   * This abstract class represents the base of every {@link Placeholder} parser.
   *
   * @see PatternResolver
   * @param <P> type of {@link Placeholder} wrapper.
   * @author Oliwer - https://www.github.com/ImOliwer
   */
  abstract class Resolver<W extends Wrapper> {
    /** {@link Character} start delimiter for this parser. **/
    public final char startDelimiter;

    /** {@link Character} end delimiter for this parser. **/
    public final char endDelimiter;

    /** {@link Map} map of registered placeholders. **/
    protected final Map<Class<? extends Placeholder>, W> placeholders = new ConcurrentHashMap<>();

    /**
     * Primary constructor.
     *
     * @param startDelimiter {@link Character} the start delimiter to be set for this parser.
     * @param endDelimiter {@link Character} the end delimiter to be set for this parser.
     */
    public Resolver(char startDelimiter, char endDelimiter) {
      // ensure the delimiters are not whitespace
      if (startDelimiter == ' ' || endDelimiter == ' ')
        throw new RuntimeException("start and end delimiters must NOT be a whitespace");

      // initialize
      this.startDelimiter = startDelimiter;
      this.endDelimiter = endDelimiter;
    }

    /**
     * Register a new placeholder to this resolver.
     *
     * @param placeholder {@link Placeholder} placeholder to be registered.
     * @return {@link Resolver} current instance.
     */
    public abstract <P extends Placeholder> Resolver<W> withPlaceholder(P placeholder);

    /**
     * Resolve the placeholders inside the origin passed.
     *
     * @param origin {@link String} the string to be processed and resolved.
     * @param customData {@link Object} the custom data for this resolve operation.
     * @return {@link String}
     */
    public abstract String resolve(String origin, Object customData);

    /**
     * @param origin {@link String} the string to be processed and resolved.
     * @see Resolver#resolve(String, Object)
     */
    public final String resolve(String origin) {
      return this.resolve(origin, (Object) null);
    }

    /**
     * Resolve an array of placeholders of passed classes in a string origin.
     *
     * @param origin {@link String} the origin to be processed of passed placeholder.
     * @param customData {@link Object} custom data passed through to the placeholder.
     * @param types {@link Class} array of placeholder types to be processed.
     * @return {@link String}
     */
    public abstract String resolve(String origin, Object customData, Class<? extends Placeholder>[] types);

    /**
     * @param origin {@link String} the origin to be processed of passed placeholder.
     * @param types {@link Class} array of placeholder types to be processed.
     * @see Resolver#resolve(String, Object, Class[])
     * @return {@link String}
     */
    public final String resolve(String origin, Class<? extends Placeholder>[] types) {
      return this.resolve(origin, null, types);
    }
  }

  /**
   * Parse this action from origin and parameters accordingly.
   *
   * @param customData {@link Object} the custom data of this parse.
   * @param defaultData {@link DefaultData} the default data from the resolver.
   * @return {@link Object} parsed action.
   */
  Object parse(Object customData, DefaultData defaultData);

  /**
   * Get the tag of this action.
   *
   * @return {@link String}
   */
  String tag();

  /**
   * Get the separator for this action.
   *
   * @return {@link Character}
   */
  default char separator() {
    return ',';
  }
}