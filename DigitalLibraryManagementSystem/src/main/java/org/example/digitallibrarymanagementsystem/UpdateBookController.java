package org.example.digitallibrarymanagementsystem;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.Library.Book;
import org.example.Management.BookManagement;

import java.time.LocalDate;

public class UpdateBookController {
    @FXML
    private TextField title_TF;
    @FXML
    private TextField author_TF;
    @FXML
    private TextField isbn_TF;
    @FXML
    private DatePicker publicationDate_DP;
    @FXML
    private TextField quantity_TF;
    @FXML
    private Label updateMSG_LBL;
    @FXML
    private Slider quantity_SLDR;

    private Book book;
    private BookManagement bookManagement;

    public void setBook(Book book, BookManagement bookManagement) {
        this.book = book;
        this.bookManagement = bookManagement;
        title_TF.setText(book.getTitle());
        author_TF.setText(book.getAuthor());
        isbn_TF.setText(book.getISBN());
        publicationDate_DP.setValue(LocalDate.parse(book.getPublicationDate()));
        quantity_TF.setText(String.valueOf(book.getQuantity()));
        quantity_SLDR.setValue(book.getQuantity());

        // Add listeners to synchronize the TextField and Slider
        quantity_TF.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                try {
                    int value = Integer.parseInt(newValue);
                    quantity_SLDR.setValue(value);
                } catch (NumberFormatException e) {
                    quantity_TF.setText(oldValue);
                }
            } else {
                quantity_SLDR.setValue(0);
            }
        });

        quantity_SLDR.valueProperty().addListener((observable, oldValue, newValue) -> {
            quantity_TF.setText(String.valueOf(newValue.intValue()));
        });
    }

    @FXML
    private void onUpdateBtnClick() {
        book.setTitle(title_TF.getText());
        book.setAuthor(author_TF.getText());
        book.setISBN(isbn_TF.getText());
        book.setPublicationDate(publicationDate_DP.getValue().toString());
        try {
            book.setQuantity(Integer.parseInt(quantity_TF.getText()));
        } catch (NumberFormatException e) {
            updateMSG_LBL.setText("Invalid quantity input");
            return;
        }
        bookManagement.updateBook(book.getBookID(), book.getTitle(), book.getAuthor(), book.getISBN(), book.getPublicationDate(), book.isAvailable(), book.getQuantity());
        closeWindow();
    }

    @FXML
    private void onCancelBtnClick() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) updateMSG_LBL.getScene().getWindow();
        stage.close();
    }
}