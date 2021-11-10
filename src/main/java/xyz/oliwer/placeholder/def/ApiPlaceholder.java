package xyz.oliwer.placeholder.def;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import xyz.oliwer.placeholder.Placeholder;
import xyz.oliwer.placeholder.json.Deserialized;
import xyz.oliwer.placeholder.json.JsonParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;
import static java.lang.String.format;
import static java.net.http.HttpRequest.BodyPublisher;
import static java.net.http.HttpRequest.BodyPublishers;
import static java.net.http.HttpResponse.BodyHandlers;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.time.Duration.ofSeconds;
import static java.util.regex.Pattern.compile;

/**
 * This class represents the "API" implementation of {@link Placeholder}.
 * Fetch <b>JSON</b> properties from an <b>Endpoint</b> by passed link and target (if any).
 *
 * @author Oliwer - https://www.github.com/ImOliwer
 */
public final class ApiPlaceholder implements Placeholder {
  /**
   * {@link Pattern} the origin cutter pattern.
   */
  private static final Pattern CUT_ORIGIN_PATTERN = compile(
    "api\\((.+),(.*),(.*),(.*),(.*)\\)"
  );

  /**
   * {@link JsonParser} this property represents the json parser.
   */
  private final JsonParser json;

  /**
   * {@link Cache} this property represents the 30 second' cache.
   */
  private final Cache<String, Deserialized> cache = Caffeine
    .newBuilder()
    .expireAfterWrite(ofSeconds(30))
    .maximumSize(1_500)
    .build();

  /**
   * Primary constructor.
   *
   * @param jsonParser {@link JsonParser} parser used to serialize and deserialize json content.
   */
  public ApiPlaceholder(JsonParser jsonParser) {
    this.json = jsonParser;
  }

  /** @see Placeholder#parse(String, String[], char, char)  **/
  @Override
  public Object parse(String origin, String[] parameters, char startDelimiter, char endDelimiter) {
    // necessities
    final String originCut = cutOrigin(origin, startDelimiter, endDelimiter);

    // check cache
    final Deserialized cached = cache.getIfPresent(origin);
    if (cached != null)
      return extract(originCut, cached.copy(), parameters[1], false);

    // handle request
    try {
      // create the client & request
      final HttpClient client = HttpClient.newHttpClient();
      final HttpRequest.Builder request = HttpRequest
        .newBuilder(URI.create(parameters[0]));

      // prepare request headers
      final String[] headers = parameters[3].split(";;");
      for (final String header : headers) {
        final String[] pair = header.split("=");
        if (pair.length < 2)
          continue;
        request.setHeader(pair[0], pair[1]);
      }

      // overridden headers
      request.setHeader("User-Agent", "API-Text-Action");
      request.setHeader("Content-Type", "application/json");

      // prepare request body
      final String requestType = parameters[2];
      switch (requestType) {
        case "GET":
          request.GET();
          break;
        case "DELETE":
          request.DELETE();
          break;
        default: {
          // fetch and build the body
          final Map<String, String> body = new LinkedHashMap<>();
          final String[] bodyProperties = parameters[4].split(";;");

          for (final String bodyProperty : bodyProperties) {
            final String[] pair = bodyProperty.split("=");
            if (pair.length < 2)
              continue;
            body.put(pair[0], pair[1]);
          }

          final BodyPublisher bodyPublisher = BodyPublishers.ofString(json.serialize(body));
          // set request type accordingly
          switch (requestType) {
            case "POST":
              request.POST(bodyPublisher);
              break;
            case "PUT":
              request.PUT(bodyPublisher);
              break;
            default:
              return origin;
          }
        }
      }

      // send request
      final HttpResponse<String> response = client.send(request.build(), BodyHandlers.ofString(UTF_8));
      final String responseBody = response.body();

      // return value
      return extract(originCut, json.deserialize(responseBody), parameters[1], true);
    } catch (Exception ignored) {}

    // an exception was caught and has relinquished the url - return the origin
    return origin;
  }

  /**
   * Destroy this placeholder.
   */
  public void destroy() {
    cache.cleanUp();
  }

  /**
   * Extract specified property from the formatted paths.
   */
  private Deserialized extract(String origin, Deserialized next, String formattedPaths, boolean cache) {
    // cache if specified
    if (cache)
      this.cache.put(origin, next.copy());

    // necessity
    final String[] paths = formattedPaths.split("\\.");
    final int lastIndex = paths.length - 1;
    Deserialized value = null;

    // return the deserialized value if there is no path
    if (lastIndex == -1 || lastIndex == 0 && paths[0].isBlank())
      return next;

    // extract content
    for (int index = 0; index < paths.length; index++) {
      if (value != null)
        return null;

      final String path = paths[index];
      final int pathLength = path.length();

      if (pathLength >= 3 && path.charAt(0) == '[' && path.charAt(pathLength - 1) == ']') {
        next = next.get(indexFromSpec(path));
      } else {
        next = next.get(path);
      }

      if (index == lastIndex)
        value = next;
    }

    // return value
    return value;
  }

  /**
   * Cut the passed down origin.
   */
  private String cutOrigin(String origin, char startDelimiter, char endDelimiter) {
    final Matcher matcher = CUT_ORIGIN_PATTERN.matcher(origin);
    if (!matcher.find())
      throw new RuntimeException("failed to match origin");
    return matcher.replaceFirst(
      format(
        "%sapi($1,,$3,$4,$5)%s",
        startDelimiter,
        endDelimiter
      )
    );
  }

  /** @see Placeholder#tag() **/
  @Override
  public String tag() {
    return "api";
  }

  /** @see Placeholder#separator() **/
  @Override
  public char separator() {
    return ',';
  }

  /**
   * <b>Example:</b> [index]
   * @return {@link Integer}
   */
  private static int indexFromSpec(String spec) {
    return parseInt(spec.substring(1, spec.length() - 1));
  }
}