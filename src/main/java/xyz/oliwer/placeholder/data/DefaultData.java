package xyz.oliwer.placeholder.data;

/**
 * This class represents the default data population from parsers.
 *
 * @author Oliwer - https://www.github.com/ImOliwer
 */
public final class DefaultData {
  /** {@link String} the origin of operation. **/
  public final String origin;

  /** {@link String} array of parameters of operation. **/
  public final String[] parameters;

  /** {@link Character} the start delimiter of parser. **/
  public final char startDelimiter;

  /** {@link Character} the end delimiter of parser. **/
  public final char endDelimiter;

  /**
   * Primary constructor.
   *
   * @param origin {@link String} the origin being resolved.
   * @param params {@link String} the array of parameters specified inside the placeholder.
   * @param startDelimiter {@link Character} the start delimiter of our correspondent parser.
   * @param endDelimiter {@link Character} the end delimiter of our correspondent parser.
   */
  public DefaultData(
    String origin,
    String[] params,
    char startDelimiter,
    char endDelimiter
  ) {
    this.origin = origin;
    this.parameters = params;
    this.startDelimiter = startDelimiter;
    this.endDelimiter = endDelimiter;
  }
}