package advertissment.board;


import static advertissment.board.Persistence.getDatastore;
import static advertissment.board.Persistence.getKeyFactory;
import static com.google.cloud.datastore.StructuredQuery.OrderBy.desc;
import static com.google.cloud.datastore.StructuredQuery.PropertyFilter.hasAncestor;

import java.util.List;
import java.util.Objects;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.EntityQuery;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;
import com.google.cloud.datastore.StructuredQuery.CompositeFilter;
import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

//[START all]
@SuppressWarnings("JavadocMethod")
public class Advertissment {

  private static final KeyFactory keyFactory = getKeyFactory(Advertissment.class);
  private final Key key;

  public final String advert;

  public Advertissment(String advert) {
    this.advert = advert == null ? "default" : advert;
    key =
        keyFactory.newKey(
            this.advert); // There is a 1:1 mapping between advertissment names and advertissment objects
  }

  public Key getKey() {
    return key;
  }

  public List<Greeting> getGreetings() {
    // This query requires the index defined in index.yaml to work because of the orderBy on date.
    EntityQuery query =
        Query.newEntityQueryBuilder()
            .setKind("Greeting")
            .setFilter(hasAncestor(key))
            .setLimit(10)
            .build();

    QueryResults<Entity> results = getDatastore().run(query);

    Builder<Greeting> resultListBuilder = ImmutableList.builder();
    while (results.hasNext()) {
      resultListBuilder.add(new Greeting(results.next()));
    }

    return resultListBuilder.build();
  }

  public List<Greeting> getGreetings(String filterForName) {
	    // This query requires the index defined in index.yaml to work because of the orderBy on date.
	    EntityQuery query =
	        Query.newEntityQueryBuilder()
	            .setKind("Greeting")
	            .setFilter(PropertyFilter.eq("content", filterForName))
	            .setOrderBy(desc("date"))
	            .setLimit(10)
	            .build();

	    QueryResults<Entity> results = getDatastore().run(query);

	    Builder<Greeting> resultListBuilder = ImmutableList.builder();
	    while (results.hasNext()) {
	      resultListBuilder.add(new Greeting(results.next()));
	    }

	    return resultListBuilder.build();
	  }
  
  public List<Greeting> getGreetings(String filterForName, String priceMin, String priceMax) {
	    // This query requires the index defined in index.yaml to work because of the orderBy on date.
	    EntityQuery query =
	        Query.newEntityQueryBuilder()
	            .setKind("Greeting")
	            .setFilter(CompositeFilter.and(PropertyFilter.eq("content", filterForName), PropertyFilter.gt("price", priceMin), PropertyFilter.lt("price", priceMax)))
	            .setOrderBy(desc("date"))
	            .setLimit(10)
	            .build();

	    QueryResults<Entity> results = getDatastore().run(query);

	    Builder<Greeting> resultListBuilder = ImmutableList.builder();
	    while (results.hasNext()) {
	      resultListBuilder.add(new Greeting(results.next()));
	    }

	    return resultListBuilder.build();
	  }
  
  public List<Greeting> getGreetings(String priceMin, String priceMax) {
	    // This query requires the index defined in index.yaml to work because of the orderBy on date.
	    EntityQuery query =
	        Query.newEntityQueryBuilder()
	            .setKind("Greeting")
	            .setFilter(CompositeFilter.and(PropertyFilter.gt("price", priceMin), PropertyFilter.lt("price", priceMax)))
	            .setOrderBy(desc("date"))
	            .setLimit(10)
	            .build();

	    QueryResults<Entity> results = getDatastore().run(query);

	    Builder<Greeting> resultListBuilder = ImmutableList.builder();
	    while (results.hasNext()) {
	      resultListBuilder.add(new Greeting(results.next()));
	    }

	    return resultListBuilder.build();
	  }
  
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Advertissment advertissment = (Advertissment) o;
    return Objects.equals(advert, advertissment.advert) && Objects.equals(key, advertissment.key);
  }

  @Override
  public int hashCode() {
    return Objects.hash(advert, key);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("keyFactory", keyFactory)
        .add("advert", advert)
        .add("key", key)
        .toString();
  }
}
//[END all]
