package xyz.oliwer.placeholder.def;

import xyz.oliwer.placeholder.Placeholder;
import xyz.oliwer.placeholder.data.DefaultData;

import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Double.parseDouble;
import static java.lang.Long.parseLong;
import static java.lang.String.format;

/**
 * This class represents a "Random Number Generator - between range" implementation of {@link Placeholder}.
 *
 * @author Oliwer - https://www.github.com/ImOliwer
 */
public final class RangePlaceholder implements Placeholder {
  /** @see Placeholder#parse(Object, DefaultData) **/
  @Override
  public Object parse(Object $, DefaultData defaultData) {
    // necessities
    final String origin = defaultData.origin;
    final String[] parameters = defaultData.parameters;

    // work
    try {
      final int length = parameters.length;
      if (length == 0 || length == 1)
        return origin;

      final ThreadLocalRandom random = ThreadLocalRandom.current();
      final String type = parameters[0];

      switch (type) {
        // int, long etc
        case "single": {
          final long minimumValue = parseLong(parameters[1]);
          if (length == 2)
            return random.nextLong(minimumValue);
          return random.nextLong(minimumValue, parseLong(parameters[2]));
        }
        // double, float etc
        case "decimal": {
          final double minimumValue = parseDouble(parameters[1]);
          if (length == 2)
            return random.nextDouble(minimumValue);

          final double value = random.nextDouble(minimumValue, parseDouble(parameters[2]));
          return length == 4 ? format(format("%%.%sf", parameters[3]), value) : value;
        }
      }
    } catch (NumberFormatException ignored) {}
    return origin;
  }

  /** @see Placeholder#tag() **/
  @Override
  public String tag() {
    return "range";
  }
}