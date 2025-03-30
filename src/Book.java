package src;

import java.lang.NumberFormatException;

public class Book {
    private String title;
    private String author;
    private long isbn;
    private String status;
    private String borrowedBy;

    public Book(String title, String author, long isbn, String status) throws NumberFormatException {
        if (validISBN(isbn)) {
            this.title = title;
            this.author = author;
            this.isbn = isbn;
            this.status = status;
            this.borrowedBy = null;
        } else {
            System.out.println("Invalid ISBN: " + isbn);
            throw new NumberFormatException();
        }
    }

    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public long getIsbn() { return isbn; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getBorrowedBy() { return borrowedBy; }
    public void setBorrowedBy(String borrowedBy) { this.borrowedBy = borrowedBy; }

    @Override
    public String toString() {
        return title + " by " + author + " (ISBN-13: " + isbn + ") - " + status +
               (borrowedBy != null ? " (Borrowed by: " + borrowedBy + ")" : "");
    }

    private boolean validISBN(long isbn) {
        int last = (int) (isbn % 10);
        isbn /= 10;
        int sum = 0;
        for (int i = 1; i <= 12; i++) {
            int digit = (int) (isbn % 10);
            sum += (i % 2 == 0) ? digit : digit * 3;
            isbn /= 10;
        }
        int check = 10 - (sum % 10);
        if (check == 10) check = 0;
        return check == last;
    }
}
