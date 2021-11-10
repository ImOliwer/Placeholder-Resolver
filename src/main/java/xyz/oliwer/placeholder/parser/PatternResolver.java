package xyz.oliwer.placeholder.parser;

import xyz.oliwer.placeholder.Placeholder;

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
  public Resolver<Wrapper> withPlaceholder(Placeholder placeholder) {
    this.placeholders.add(new Wrapper(placeholder, startDelimiter, endDelimiter));
    return this;
  }

  /**
   * @see Placeholder.Resolver#resolve(String)
   **/
  @Override
  public String resolve(String origin) {
    for (final Wrapper placeholder : this.placeholders) {
      final Placeholder parent = placeholder.getParent();
      origin = placeholder
        .pattern
        .matcher(origin)
        .replaceAll(result ->
          parent.parse(
            result.group(0),
            result.group(2).split(valueOf(parent.separator())),
            startDelimiter,
            endDelimiter
          ).toString()
        );
    }
    return origin;
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