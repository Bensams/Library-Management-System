// DueDateInputDialogController.java
package org.example.digitallibrarymanagementsystem;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import org.example.Accounts.Borrower;
import org.example.Library.Book;
import org.example.Library.Library;

import java.time.LocalDate;

public class DueDateInputDialogController {
    @FXML
    private DatePicker dueDatePicker;

    private Book book;
    private Borrower borrower;
    private ManageBorrower manageBorrower;

    public void setBook(Book book) {
        this.book = book;
    }

    public void setBorrower(Borrower borrower) {
        this.borrower = borrower;
    }

    public void setManageBorrower(ManageBorrower manageBorrower) {
        this.manageBorrower = manageBorrower;
    }

    @FXML
    private void onConfirmBtnClick() {
        LocalDate dueDate = dueDatePicker.getValue();
        if (dueDate != null && !dueDate.isBefore(LocalDate.now()) && dueDate.isBefore(LocalDate.now().plusWeeks(2))) {
            Library.getInstance().borrowBook(book, borrower, dueDate);
            Stage stage = (Stage) dueDatePicker.getScene().getWindow();
            stage.close();
        } else {
            manageBorrower.displayResultMessage("Due date must be within 2 weeks from today and cannot be a past date.", javafx.scene.paint.Color.RED);
        }
    }
}