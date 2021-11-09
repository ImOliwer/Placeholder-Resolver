package xyz.oliwer.placeholder;

import xyz.oliwer.placeholder.parser.PatternResolver;

import java.util.HashSet;
import java.util.Set;

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
   * @param <A> type of {@link Placeholder}.
   * @author Oliwer - https://www.github.com/ImOliwer
   */
  abstract class Resolver<P extends Wrapper> {
    /** {@link Character} start delimiter for this parser. **/
    public final char startDelimiter;

    /** {@link Character} end delimiter for this parser. **/
    public final char endDelimiter;

    /** {@link Set<A>} set of registered actions. **/
    protected final Set<P> placeholders = new HashSet<>();

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
    public abstract Resolver<P> withPlaceholder(Placeholder placeholder);

    /**
     * Resolve the actions inside the origin passed.
     *
     * @param origin {@link String} the string to be processed & resolved.
     * @return {@link String}
     */
    public abstract String resolve(String origin);
  }

  /**
   * Parse this action from origin & parameters accordingly.
   *
   * @param origin {@link String} the origin of this action (i.e `%action(first_param,second_param)%`).
   * @param parameters {@link String} array of parameters provided in the action invocation.
   * @param startDelimiter {@link Character} start delimiter of involved parser.
   * @param endDelimiter {@link Character} end delimiter of involved parser.
   * @return {@link Object} parsed action.
   */
  Object parse(String origin, String[] parameters, char startDelimiter, char endDelimiter);

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