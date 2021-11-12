package xyz.oliwer.placeholder.def;

import xyz.oliwer.placeholder.Placeholder;
import xyz.oliwer.placeholder.data.DefaultData;

import java.util.concurrent.ThreadLocalRandom;

/**
 * This class represents a "random of" implementation of {@link Placeholder}.
 *
 * @author Oliwer - https://www.github.com/ImOliwer
 */
public final class RandomPlaceholder implements Placeholder {
  /** @see Placeholder#parse(Object, DefaultData) **/
  @Override
  public Object parse(Object customData, DefaultData defaultData) {
    final String[] parameters = defaultData.parameters;
    final int length = parameters.length;

    if (length == 0)
      return defaultData.origin;

    final ThreadLocalRandom random = ThreadLocalRandom.current();
    return parameters[random.nextInt(length)];
  }

  /** @see Placeholder#tag() **/
  @Override
  public String tag() {
    return "random";
  }
}