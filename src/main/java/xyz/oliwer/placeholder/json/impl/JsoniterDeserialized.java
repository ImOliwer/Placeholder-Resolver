package xyz.oliwer.placeholder.json.impl;

import com.jsoniter.JsonIterator;
import com.jsoniter.any.Any;
import xyz.oliwer.placeholder.json.Deserialized;

/**
 * @author Oliwer - https://www.github.com/ImOliwer
 */
public final class JsoniterDeserialized implements Deserialized {
  private Any content;

  public JsoniterDeserialized(String content) {
    this.content = JsonIterator.deserialize(content);
  }

  public JsoniterDeserialized(Any content) {
    this.content = content;
  }

  @Override
  public Deserialized get(int index) {
    this.content = content.get(index);
    return this;
  }

  @Override
  public Deserialized get(String key) {
    this.content = content.get(key);
    return this;
  }

  @Override
  public Deserialized copy() {
    return new JsoniterDeserialized(this.content);
  }

  @Override
  public String toString() {
    return content.toString();
  }
}