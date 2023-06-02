package org.example;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class BookServiceTest {
    private BookService bookService;
    private Book existingBook;
    private User user;

    @Before
    public void setUp() {
        bookService = new BookService();
        existingBook = Mockito.mock(Book.class);
        user = Mockito.mock(User.class);

        when(existingBook.getTitle()).thenReturn("Existing Book");
        when(existingBook.getAuthor()).thenReturn("John Doe");
        when(existingBook.getGenre()).thenReturn("Fiction");
        when(existingBook.getReviews()).thenReturn(new ArrayList<>());

        when(user.getPurchasedBooks()).thenReturn(new ArrayList<>());
    }

    @Test
    public void testSearchBook_ExistingKeyword() {
        bookService.addBook(existingBook);

        List<Book> searchResult = bookService.searchBook("Fiction");
        assertEquals(1, searchResult.size());
        assertEquals(existingBook, searchResult.get(0));
    }

    @Test
    public void testSearchBook_NonExistingKeyword() {
        List<Book> searchResult = bookService.searchBook("Non-existing");
        assertTrue(searchResult.isEmpty());
    }

    @Test
    public void testSearchBook_EmptyKeyword() {
        List<Book> searchResult = bookService.searchBook("");
        assertTrue(searchResult.isEmpty());
    }

    @Test
    public void testSearchBook_MultipleMatches() {
        Book book1 = Mockito.mock(Book.class);
        when(book1.getTitle()).thenReturn("Book1");
        when(book1.getAuthor()).thenReturn("Author1");
        when(book1.getGenre()).thenReturn("Genre1");
        bookService.addBook(book1);

        Book book2 = Mockito.mock(Book.class);
        when(book2.getTitle()).thenReturn("Book2");
        when(book2.getAuthor()).thenReturn("Author2");
        when(book2.getGenre()).thenReturn("Genre2");
        bookService.addBook(book2);

        List<Book> searchResult = bookService.searchBook("Book");
        assertEquals(2, searchResult.size());
    }

    @Test
    public void testSearchBook_PartialMatch() {
        Book book = Mockito.mock(Book.class);
        when(book.getTitle()).thenReturn("Complete Title");
        when(book.getAuthor()).thenReturn("Complete Author");
        when(book.getGenre()).thenReturn("Complete Genre");
        bookService.addBook(book);

        List<Book> searchResult = bookService.searchBook("Com");
        assertEquals(1, searchResult.size());
    }

    @Test
    public void testPurchaseBook_ExistingBook() {
        bookService.addBook(existingBook);

        boolean purchaseSuccessful = bookService.purchaseBook(user, existingBook);
        assertTrue(purchaseSuccessful);
    }

    @Test
    public void testPurchaseBook_NonExistingBook() {
        Book nonExistingBook = Mockito.mock(Book.class);

        boolean purchaseSuccessful = bookService.purchaseBook(user, nonExistingBook);
        assertFalse(purchaseSuccessful);
    }

    @Test
    public void testPurchaseBook_UserHasPurchasedBook() {
        bookService.addBook(existingBook);
        when(user.getPurchasedBooks()).thenReturn(List.of(existingBook));

        boolean purchaseSuccessful = bookService.purchaseBook(user, existingBook);
        assertTrue(purchaseSuccessful);
    }

    @Test
    public void testPurchaseBook_UserHasNotPurchasedBook() {
        boolean purchaseSuccessful = bookService.purchaseBook(user, existingBook);
        assertFalse(purchaseSuccessful);
    }

    @Test
    public void testAddBookReview_UserHasPurchasedBook() {
        bookService.addBook(existingBook);
        when(user.getPurchasedBooks()).thenReturn(List.of(existingBook));

        boolean reviewAdded = bookService.addBookReview(user, existingBook, "Great book!");
        assertTrue(reviewAdded);
        assertEquals(1, existingBook.getReviews().size());
        assertEquals("Great book!", existingBook.getReviews().get(0));
    }

    @Test
    public void testAddBookReview_UserHasNotPurchasedBook() {
        boolean reviewAdded = bookService.addBookReview(user, existingBook, "Great book!");
        assertFalse(reviewAdded);
        assertEquals(0, existingBook.getReviews().size());
    }

    @Test
    public void testAddBook_DuplicateBook() {
        bookService.addBook(existingBook);

        Book duplicateBook = Mockito.mock(Book.class);
        when(duplicateBook.getTitle()).thenReturn("Existing Book");
        when(duplicateBook.getAuthor()).thenReturn("John Doe");
        when(duplicateBook.getGenre()).thenReturn("Fiction");

        boolean bookAdded = bookService.addBook(duplicateBook);
        assertTrue(bookAdded);
    }

    @Test
    public void testAddBook_IncompleteInformation() {
        Book book = Mockito.mock(Book.class);
        when(book.getTitle()).thenReturn(null);
        when(book.getAuthor()).thenReturn("John Doe");
        when(book.getGenre()).thenReturn("Fiction");

        boolean bookAdded = bookService.addBook(book);
        assertTrue(bookAdded);
    }

    @Test
    public void testRemoveBook_ExistingBook() {
        bookService.addBook(existingBook);

        boolean bookRemoved = bookService.removeBook(existingBook);
        assertTrue(bookRemoved);
    }

    @Test
    public void testRemoveBook_NonExistingBook() {
        Book nonExistingBook = Mockito.mock(Book.class);

        boolean bookRemoved = bookService.removeBook(nonExistingBook);
        assertFalse(bookRemoved);
    }

    @Test
    public void testGetBooks() {
        bookService.addBook(existingBook);

        List<Book> books = bookService.getBooks();
        assertEquals(1, books.size());
        assertEquals(existingBook, books.get(0));
    }

    @Test
    public void testGetBooks_SizeAfterAddingAndRemovingBooks() {
        Book book1 = Mockito.mock(Book.class);
        when(book1.getTitle()).thenReturn("Book1");
        when(book1.getAuthor()).thenReturn("Author1");
        when(book1.getGenre()).thenReturn("Genre1");
        bookService.addBook(book1);

        Book book2 = Mockito.mock(Book.class);
        when(book2.getTitle()).thenReturn("Book2");
        when(book2.getAuthor()).thenReturn("Author2");
        when(book2.getGenre()).thenReturn("Genre2");
        bookService.addBook(book2);

        bookService.removeBook(book1);

        List<Book> books = bookService.getBooks();
        assertEquals(1, books.size());
    }

    @Test
    public void testGetBooks_Order() {
        Book book1 = Mockito.mock(Book.class);
        when(book1.getTitle()).thenReturn("Book1");
        when(book1.getAuthor()).thenReturn("Author1");
        when(book1.getGenre()).thenReturn("Genre1");
        bookService.addBook(book1);

        Book book2 = Mockito.mock(Book.class);
        when(book2.getTitle()).thenReturn("Book2");
        when(book2.getAuthor()).thenReturn("Author2");
        when(book2.getGenre()).thenReturn("Genre2");
        bookService.addBook(book2);

        List<Book> books = bookService.getBooks();
        assertEquals(book1, books.get(0));
        assertEquals(book2, books.get(1));
    }
}
