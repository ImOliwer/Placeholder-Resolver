package xyz.oliwer.placeholder.json.impl;

import com.jsoniter.output.JsonStream;
import xyz.oliwer.placeholder.json.Deserialized;
import xyz.oliwer.placeholder.json.JsonParser;

/**
 * @author Oliwer - https://www.github.com/ImOliwer
 */
public final class JsoniterParser implements JsonParser {
  @Override
  public Deserialized deserialize(String content) {
    return new JsoniterDeserialized(content);
  }

  @Override
  public String serialize(Object object) {
    return JsonStream.serialize(object);
  }
}