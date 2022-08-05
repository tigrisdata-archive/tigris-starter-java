package com.tigrisdata.starter.models;

import com.tigrisdata.db.client.TigrisFilter;
import java.util.List;
import java.util.Objects;

public final class SearchRequest {
  private String q;
  private List<String> searchFields;
  private List<String> facetFields;
  private TigrisFilter filter;

  public String getQ() {
    return q;
  }

  /**
   * Q is the text to query in the collection
   * @param q string
   */
  public void setQ(String q) {
    this.q = q;
  }

  public List<String> getSearchFields() {
    return searchFields;
  }

  /**
   * Fields against which the search query will be projected
   *
   * @param searchFields List<String>
   */
  public void setSearchFields(List<String> searchFields) {
    this.searchFields = searchFields;
  }

  public List<String> getFacetFields() {
    return facetFields;
  }

  /**
   * List of fields to generate facets for
   *
   * @param facetFields List<String>
   */
  public void setFacetFields(List<String> facetFields) {
    this.facetFields = facetFields;
  }

  public TigrisFilter getFilter() {
    return filter;
  }

  public void setFilter(TigrisFilter filter) {
    this.filter = filter;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    SearchRequest that = (SearchRequest) o;

    if (!Objects.equals(q, that.q)) {
      return false;
    }
    if (!Objects.equals(searchFields, that.searchFields)) {
      return false;
    }
    return Objects.equals(facetFields, that.facetFields);
  }

  @Override
  public int hashCode() {
    int result = q != null ? q.hashCode() : 0;
    result = 31 * result + (searchFields != null ? searchFields.hashCode() : 0);
    result = 31 * result + (facetFields != null ? facetFields.hashCode() : 0);
    return result;
  }
}
