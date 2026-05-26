package com.mongobook.book.web;

import com.mongobook.book.domain.Book;
import com.mongobook.book.repository.BookRepository;
import jakarta.validation.Valid;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/books")
@CrossOrigin(origins = "http://localhost:4200")
public class BookController {
    private static final Log logger = LogFactory.getLog(BookController.class);
    private final BookRepository books;

    public BookController(BookRepository books) {
        this.books = books;
    }

    @GetMapping
    public Page<Book> list(
            @RequestParam(name = "q", required = false) String q,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        logger.info("BookController.list called with q=" + q + " page=" + page + " size=" + size);
        Pageable pageable = PageRequest.of(page, size);
        if (q == null || q.isBlank()) {
            return books.findAll(pageable);
        }
        return books.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(q, q, pageable);
    }

    @GetMapping("/{id}")
    public Book get(@PathVariable("id") String id) {
        logger.info("BookController.get called with id=" + id);
        return books.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));
    }

    @PostMapping
    public ResponseEntity<Book> create(@Valid @RequestBody Book book) {
        logger.info("BookController.create called for title=" + book.getTitle());
        book.setId(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(books.save(book));
    }

    @PutMapping("/{id}")
    public Book update(@PathVariable("id") String id, @Valid @RequestBody Book book) {
        logger.info("BookController.update called with id=" + id + ", title=" + book.getTitle());
        if (!books.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found");
        }
        book.setId(id);
        return books.save(book);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        logger.info("BookController.delete called with id=" + id);
        if (!books.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found");
        }
        books.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
