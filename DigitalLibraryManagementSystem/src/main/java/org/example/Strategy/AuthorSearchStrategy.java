package org.example.Strategy;

import org.example.Library.Book;

import java.util.List;
import java.util.stream.Collectors;

public class AuthorSearchStrategy implements SearchStrategy {
    @Override
    public List<Book> search(List<Book> books, String query) {
        return books.stream()
                .filter(book -> book.getAuthor().equalsIgnoreCase(query))
                .collect(Collectors.toList());
    }

}
