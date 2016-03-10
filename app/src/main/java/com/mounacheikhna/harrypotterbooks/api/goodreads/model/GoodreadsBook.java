package com.mounacheikhna.harrypotterbooks.api.goodreads.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by mouna on 06/12/15.
 */
@Root(strict = false) public class GoodreadsBook {

  @Element(name = "id", required = false) String id;
  @Element(name = "title", required = false) String title;
  @Element(name = "isbn", required = false) String isbn;
  @Element(name = "image_url", required = false) String imageUrl;
  @Element(name = "small_image_url", required = false) String smallImageUrl;
  @Element(name = "description", required = false) String description;
  @Element(name = "author", required = false) Author author;

  @Element(name = "publisher", required = false) String publisher;
  @Element(name = "num_pages", required = false) String nbPages;

  public String getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
  }

  public Author getAuthor() {
    return author;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public String getPublisher() {
    return publisher;
  }

  public String getNbPages() {
    return nbPages;
  }

  @Root(strict = false) public static class Author {

    @Element(name = "name", required = false) String name;

    public String getName() {
      return name;
    }
  }
}