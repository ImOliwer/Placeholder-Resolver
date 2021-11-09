package xyz.oliwer.placeholder.def;

import xyz.oliwer.placeholder.Placeholder;

import java.util.concurrent.ThreadLocalRandom;

/**
 * This class represents a "random of" implementation of {@link Placeholder}.
 *
 * @author Oliwer - https://www.github.com/ImOliwer
 */
public final class RandomPlaceholder implements Placeholder {
  /** @see Placeholder#parse(String, String[], char, char) **/
  @Override
  public Object parse(String origin, String[] parameters, char $1, char $2) {
    final int length = parameters.length;
    if (length == 0)
      return origin;

    final ThreadLocalRandom random = ThreadLocalRandom.current();
    return parameters[random.nextInt(length)];
  }

  /** @see Placeholder#tag() **/
  @Override
  public String tag() {
    return "random";
  }
}