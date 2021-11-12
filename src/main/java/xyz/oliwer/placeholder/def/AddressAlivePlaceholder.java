package xyz.oliwer.placeholder.def;

import xyz.oliwer.placeholder.Placeholder;
import xyz.oliwer.placeholder.data.DefaultData;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import static java.lang.Integer.parseInt;

/**
 * @author Oliwer - https://www.github.com/ImOliwer
 */
public final class AddressAlivePlaceholder implements Placeholder {
  /**
   * @see Placeholder#parse(Object, DefaultData)
   */
  @Override
  public Object parse(Object customData, DefaultData defaultData) {
    // ensure the length of the params
    final var parameters = defaultData.parameters;
    if (parameters.length < 4)
      return defaultData.origin;

    // try to connect to socket
    // 0 = address, 1 = port, 2 = true value, 3 = false value
    try {
      final var socket = new Socket();
      socket.connect(new InetSocketAddress(parameters[0], parseInt(parameters[1])), 500); // timeout in ms
      socket.close();
      return parameters[2];
    } catch (IOException ignored) {
      return parameters[3];
    }
  }

  /**
   * @see Placeholder#tag()
   */
  @Override
  public String tag() {
    return "address_alive";
  }
}