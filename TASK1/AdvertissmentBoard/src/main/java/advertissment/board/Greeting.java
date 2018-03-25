package advertissment.board;

import static advertissment.board.Persistence.getDatastore;

import com.google.cloud.Timestamp;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.FullEntity.Builder;
import com.google.cloud.datastore.IncompleteKey;
import com.google.cloud.datastore.Key;
import com.google.common.base.MoreObjects;
import java.util.Date;
import java.util.Objects;

@SuppressWarnings("JavadocMethod")
public class Greeting {

  private Advertissment advert;

  public Key key;
  public String authorEmail;
  public String authorId;
  public String content;
  public String price;
  public Date date;

  public Greeting() {
    date = new Date();
  }

  public Greeting(String advert, String content, String price) {
    this();
    this.advert = new Advertissment(advert);
    this.content = content;
    this.price = price;
  }

  public Greeting(String advert, String content, String price, String id, String email) {
    this(advert, content, price);
    authorEmail = email;
    authorId = id;
  }

  public Greeting(Entity entity) {
    key = entity.hasKey() ? entity.getKey() : null;
    authorEmail = entity.contains("authorEmail") ? entity.getString("authorEmail") : null;
    authorId = entity.contains("authorId") ? entity.getString("authorId") : null;

    date = entity.contains("date") ? entity.getTimestamp("date").toSqlTimestamp() : null;
    content = entity.contains("content") ? entity.getString("content") : null;
    price = entity.contains("price") ? entity.getString("price") : null;
  }

  public void save() {
    if (key == null) {
      key = getDatastore().allocateId(makeIncompleteKey()); // Give this greeting a unique ID
    }

    Builder<Key> builder = FullEntity.newBuilder(key);

    if (authorEmail != null) {
      builder.set("authorEmail", authorEmail);
    }

    if (authorId != null) {
      builder.set("authorId", authorId);
    }

    builder.set("content", content);
    builder.set("price", price);
    builder.set("date", Timestamp.of(date));

    getDatastore().put(builder.build());
  }
  
  public void delete(Key key){
	  getDatastore().delete(key);
  }
  
  private IncompleteKey makeIncompleteKey() {
    // The book is our ancestor key.
    return Key.newBuilder(advert.getKey(), "Greeting").build();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Greeting greeting = (Greeting) o;
    return Objects.equals(key, greeting.key)
        && Objects.equals(authorEmail, greeting.authorEmail)
        && Objects.equals(authorId, greeting.authorId)
        && Objects.equals(content, greeting.content)
        && Objects.equals(price, greeting.price)
        && Objects.equals(date, greeting.date);
  }

  @Override
  public int hashCode() {
    return Objects.hash(key, authorEmail, authorId, content, price, date);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("key", key)
        .add("authorEmail", authorEmail)
        .add("authorId", authorId)
        .add("content", content)
        .add("price", price)
        .add("date", date)
        .toString();
  }
}
//[END all]

