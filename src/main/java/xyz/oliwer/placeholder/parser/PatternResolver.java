package xyz.oliwer.placeholder.parser;

import xyz.oliwer.placeholder.Placeholder;
import xyz.oliwer.placeholder.data.DefaultData;

import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import static java.lang.String.format;
import static java.lang.String.valueOf;
import static java.util.regex.Pattern.compile;
import static xyz.oliwer.placeholder.Placeholder.Resolver;

/**
 * This class represents the {@link Pattern} placeholder resolver.
 *
 * @author Oliwer - https://www.github.com/ImOliwer
 */
public final class PatternResolver extends Resolver<PatternResolver.Wrapper> {
  /**
   * This class represents the placeholder wrapper for {@link PatternResolver}.
   *
   * @author Oliwer - https://www.github.com/ImOliwer
   */
  public static final class Wrapper implements Placeholder.Wrapper {
    private final Placeholder parent;
    private final Pattern pattern;

    private Wrapper(Placeholder parent, char startDelimiter, char endDelimiter) {
      this.parent = parent;
      this.pattern = compile(
        format(
          "%s(%s)\\((.*?)\\)\\%s",
          startDelimiter,
          parent.tag(),
          endDelimiter
        )
      );
    }

    @Override
    public Placeholder getParent() {
      return this.parent;
    }
  }

  /**
   * @param startDelimiter {@link Character} the start delimiter to be set for this parser.
   * @param endDelimiter {@link Character} the end delimiter to be set for this parser.
   * @see Placeholder.Resolver#Resolver(char, char)
   **/
  public PatternResolver(char startDelimiter, char endDelimiter) {
    super(startDelimiter, endDelimiter);

    // ensure the delimiters are not reserved
    checkReserved(startDelimiter);
    checkReserved(endDelimiter);
  }

  /**
   * @see Placeholder.Resolver#withPlaceholder(Placeholder)
   */
  @Override
  public <P extends Placeholder> Resolver<Wrapper> withPlaceholder(P placeholder) {
    this.placeholders.putIfAbsent(
      placeholder.getClass(),
      new Wrapper(placeholder, startDelimiter, endDelimiter)
    );
    return this;
  }

  /**
   * @see Placeholder.Resolver#resolveAll(String, Object)
   **/
  @Override
  public String resolveAll(String origin, Object customData) {
    final var values = placeholders.values();
    for (final Wrapper wrapper : values)
      origin = this.handle(wrapper, origin, customData);
    return origin;
  }

  /**
   * @see Resolver#resolveAllWithout(String, Object, Set)
   */
  @Override
  public String resolveAllWithout(String origin, Object customData, Set<Class<? extends Placeholder>> without) {
    final var entries = placeholders.entrySet();
    for (Map.Entry<Class<? extends Placeholder>, Wrapper> entry : entries)
      if (!without.contains(entry.getKey()))
        origin = this.handle(entry.getValue(), origin, customData);
    return origin;
  }

  /**
   * @see Resolver#resolve(String, Object, Set)
   */
  @Override
  public String resolve(String origin, Object customData, Set<Class<? extends Placeholder>> types) {
    // ensure the presence
    if (types == null || origin == null)
      throw new NullPointerException("types and origin must NOT be null");

    // loop over and handle the parse
    for (final var type : types)
      origin = this.handle(placeholders.get(type), origin, customData);

    // return the processed origin
    return origin;
  }

  /**
   * @see Resolver#resolveSingle(String, Object, Class)
   */
  @Override
  public String resolveSingle(String origin, Object customData, Class<? extends Placeholder> type) {
    // ensure the presence
    if (type == null || origin == null)
      throw new NullPointerException("type and origin must NOT be null");

    // process and return
    return this.handle(placeholders.get(type), origin, customData);
  }

  /**
   * Handle the replacement of a placeholder by wrapper, origin and custom data.
   */
  private String handle(Wrapper wrapper, String origin, Object customData) {
    // ensure the presence of our wrapper
    if (wrapper == null)
      return origin;

    // necessity
    final var parent = wrapper.parent;

    // parse
    return wrapper
      .pattern
      .matcher(origin)
      .replaceAll(result ->
        parent.parse(
          customData,
          new DefaultData(
            result.group(0),
            result.group(2).split(valueOf(parent.separator())),
            this.startDelimiter,
            this.endDelimiter
          )
        ).toString()
      );
  }

  /**
   * Check if a delimiter (char) is reserved (due to regex limitations).
   *
   * @param delimiter {@link Character} the character to check.
   */
  private static void checkReserved(char delimiter) {
    if (delimiter == '{' || delimiter == '}' || delimiter == '[' || delimiter == ']')
      throw new RuntimeException(format("Delimiter %s is reserved", delimiter));
  }
}