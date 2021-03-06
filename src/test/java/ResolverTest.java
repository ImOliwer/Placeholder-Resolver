import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import xyz.oliwer.placeholder.def.AddressAlivePlaceholder;
import xyz.oliwer.placeholder.def.ApiPlaceholder;
import xyz.oliwer.placeholder.def.RandomPlaceholder;
import xyz.oliwer.placeholder.def.RangePlaceholder;
import xyz.oliwer.placeholder.json.impl.JsoniterParser;
import xyz.oliwer.placeholder.parser.PatternResolver;

import static java.lang.String.format;
import static xyz.oliwer.placeholder.Placeholder.Resolver;

/**
 * @author Oliwer - https://www.github.com/ImOliwer
 */
@TestInstance(Lifecycle.PER_CLASS)
public class ResolverTest {
  private static final Resolver<PatternResolver.Wrapper> PATTERN_RESOLVER = new PatternResolver('<', '>')
    .withDefault(new JsoniterParser());

  @Test
  void pattern_api() {
    // query
    final String endpoint = "<api(http://localhost:4000/example/post,%s,POST,some_header_property=test,name=Oliwer;;age=18)>";
    final String name     = format(endpoint, "bodyReceived.name");
    final String age      = format(endpoint, "bodyReceived.age");
    final String query    = format("the name is %s, age %s", name, age);

    // response
    final long start = System.nanoTime();
    final String response = PATTERN_RESOLVER.resolveSingle(query, ApiPlaceholder.class);
    final long end = System.nanoTime();

    // result
    System.out.printf("%s (%sns)%n", response, end - start);
  }

  @Test
  void pattern_range() {
    // query
    final String query = "apple pies are a <range(single,0,10)>/10";

    // response
    final long start = System.nanoTime();
    final String response = PATTERN_RESOLVER.resolveSingle(query, RangePlaceholder.class);
    final long end = System.nanoTime();

    // result
    System.out.printf("%s (%sns)%n", response, end - start);
  }

  @Test
  void pattern_random() {
    // query
    final String query = "5, you win. 10, i win. it's........ <random(5,10)>";

    // response
    final long start = System.nanoTime();
    final String response = PATTERN_RESOLVER.resolveSingle(query, RandomPlaceholder.class);
    final long end = System.nanoTime();

    // result
    System.out.printf("%s (%sns)%n", response, end - start);
  }

  @Test
  void pattern_address_alive() {
    // query
    final String query = "is address alive = <address_alive(127.0.0.1,4000,yes,no)>";

    // response
    final long start = System.nanoTime();
    final String response = PATTERN_RESOLVER.resolveSingle(query, AddressAlivePlaceholder.class);
    final long end = System.nanoTime();

    // result
    System.out.printf("%s (%sns)%n", response, end - start);
  }
}