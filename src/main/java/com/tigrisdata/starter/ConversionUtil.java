package com.tigrisdata.starter;

import com.tigrisdata.starter.models.SearchRequest;
import java.util.Objects;

public final class ConversionUtil {

  private ConversionUtil() {
  }

  /**
   * Utility to build {@code SearchRequest} for Tigris client
   *
   * @param source - User defined input
   * @return {@code SearchRequest} for Tigris client
   */
  public static com.tigrisdata.db.client.search.SearchRequest toInternalSearchRequest(
      SearchRequest source) {
    com.tigrisdata.db.client.search.SearchRequest.Builder builder =
        com.tigrisdata.db.client.search.SearchRequest.newBuilder();

    if (Objects.nonNull(source.getQ())) {
      builder.withQuery(source.getQ());
    }

    if (Objects.nonNull(source.getSearchFields())) {
      source.getSearchFields().forEach(builder::withSearchFields);
    }

    if (Objects.nonNull(source.getFacetFields())) {
      source.getFacetFields().forEach(builder::withFacetFields);
    }

    return builder.build();
  }
}
