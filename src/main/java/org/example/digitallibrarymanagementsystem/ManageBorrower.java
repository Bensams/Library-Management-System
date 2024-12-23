// ManageBorrower.java
package org.example.digitallibrarymanagementsystem;

import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.Accounts.Borrower;
import org.example.Library.Book;
import org.example.Library.BorrowedBook;
import org.example.Library.Library;
import org.example.Management.SerializedManagement;
import org.example.Strategy.AuthorSearchStrategy;
import org.example.Strategy.ISBNSearchStrategy;
import org.example.Strategy.TitleSearchStrategy;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class ManageBorrower {

    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<String> searchTypeComboBox;

    @FXML
    private TableView<BorrowedBook> borrowedBooksTableView;
    @FXML
    private TableColumn<BorrowedBook, String> borrowedTitleColumn;
    @FXML
    private TableColumn<BorrowedBook, String> borrowedDateColumn;
    @FXML
    private TableColumn<BorrowedBook, String> dueDateColumn;
    @FXML
    private TableColumn<BorrowedBook, Void> returnActionColumn;

    @FXML
    private TableView<Book> availableBooksTableView;
    @FXML
    private TableColumn<Book, String> availableTitleColumn;
    @FXML
    private TableColumn<Book, String> availableAuthorColumn;
    @FXML
    private TableColumn<Book, String> availableISBNColumn;
    @FXML
    private TableColumn<Book, String> availableQuantityColumn;
    @FXML
    private TableColumn<Book, Void> borrowActionColumn;

    private Borrower borrower;

    @FXML
    private Label resultsLabel;

    public ManageBorrower(Borrower borrower) {
        this.borrower = borrower;
        deserializeBorrowedBooks();
    }

    @FXML
    private void initialize() {
        initializeSearchTypeComboBox();
        initializeBorrowedBookTableColumns();
        initializeAvailableBookTableColumns();
        borrowedBooksTableView.setItems(getBorrowedBooksList());
        availableBooksTableView.setItems(getAvailableBooksList());
        addReturnButtonToTable();
        addBorrowButtonToTable();
    }

    private void initializeSearchTypeComboBox() {
        searchTypeComboBox.getItems().addAll("Title", "Author", "ISBN");
        searchTypeComboBox.setValue("Title");
    }

    private void initializeBorrowedBookTableColumns() {
        borrowedTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        borrowedDateColumn.setCellValueFactory(new PropertyValueFactory<>("borrowedDate"));
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
    }

    private void initializeAvailableBookTableColumns() {
        availableTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        availableAuthorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        availableISBNColumn.setCellValueFactory(new PropertyValueFactory<>("ISBN"));
        availableQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
    }

    private ObservableList<BorrowedBook> getBorrowedBooksList() {
        SerializedManagement serializedManagement = SerializedManagement.getInstance();
        List<BorrowedBook> borrowedBooks = serializedManagement.getBorrowedBooks(borrower);
        if (borrowedBooks == null) {
            borrowedBooks = new ArrayList<>();
        }
        return FXCollections.observableArrayList(borrowedBooks);
    }

    private ObservableList<Book> getAvailableBooksList() {
        Library library = Library.getInstance();
        return FXCollections.observableArrayList(library.getAvailableBooks());
    }

    private void addReturnButtonToTable() {
        returnActionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button returnButton = new Button("Return");

            {
                returnButton.setOnAction(event -> {
                    BorrowedBook borrowedBook = getTableView().getItems().get(getIndex());
                    Library library = Library.getInstance();
                    library.setCurrentBorrower(borrower); // Set the current borrower
                    library.returnBook(borrowedBook);
                    getTableView().getItems().remove(borrowedBook);
                    refreshAllTable();
                    System.out.println("Book returned: " + borrowedBook.getTitle());
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(returnButton);
                }
            }
        });
    }

    private void addBorrowButtonToTable() {
        borrowActionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button borrowButton = new Button("Borrow");

            {
                borrowButton.setOnAction(event -> {
                    Book book = getTableView().getItems().get(getIndex());
                    Library library = Library.getInstance();
                    if (library.isBookAlreadyBorrowedByUser(book, borrower)) {
                        showAlert("Borrow Error", "You have already borrowed this book.");
                    } else {
                        showDueDateInputDialog(book);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(borrowButton);
                }
            }
        });
    }

    private void showDueDateInputDialog(Book book) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/digitallibrarymanagementsystem/DueDateInputDialog.fxml"));
            AnchorPane page = loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Due Date Input");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(availableBooksTableView.getScene().getWindow());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the book, borrower, and manageBorrower into the controller.
            DueDateInputDialogController controller = loader.getController();
            controller.setBook(book);
            controller.setBorrower(borrower);
            controller.setManageBorrower(this);

            // Show the dialog and wait until the user closes it.
            dialogStage.showAndWait();
            refreshAllTable();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void displayAvailableBooks() {
        initializeAvailableBookTableColumns();
        availableBooksTableView.setItems(getAvailableBooksList());
    }

    public void refreshAllTable() {
        initializeBorrowedBookTableColumns();
        borrowedBooksTableView.setItems(getBorrowedBooksList());
        displayAvailableBooks();
        addReturnButtonToTable();
        addBorrowButtonToTable();
    }

    private void deserializeBorrowedBooks() {
        SerializedManagement serializedManagement = SerializedManagement.getInstance();
        List<BorrowedBook> borrowedBooks = serializedManagement.deserializeBorrowedBooksByUser(borrower);
        borrower.setBorrowedBooks(borrowedBooks);
    }

    public void displayResultMessage(String message, javafx.scene.paint.Color color) {
        resultsLabel.setTextFill(color);
        resultsLabel.setText(message);
        clearRegisterMsgAfterDelay();
    }

    private void clearRegisterMsgAfterDelay() {
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(event -> resultsLabel.setText(""));
        pause.play();
    }

    @FXML
    private void onSearchBtnClick() {
        String query = searchField.getText();
        String searchType = searchTypeComboBox.getValue();
        setSearchStrategy(searchType);
        List<Book> searchResults = Library.getInstance().searchBooks(query);
        displaySearchResults(searchResults);
    }

    private void setSearchStrategy(String searchType) {
        switch (searchType) {
            case "Title":
                Library.getInstance().setSearchStrategy(new TitleSearchStrategy());
                break;
            case "Author":
                Library.getInstance().setSearchStrategy(new AuthorSearchStrategy());
                break;
            case "ISBN":
                Library.getInstance().setSearchStrategy(new ISBNSearchStrategy());
                break;
        }
    }

    private void displaySearchResults(List<Book> searchResults) {
        if (searchResults.isEmpty()) {
            displayResultMessage("No book found.", javafx.scene.paint.Color.RED);
            displayAvailableBooks();
        } else {
            ObservableList<Book> books = FXCollections.observableArrayList(searchResults);
            availableBooksTableView.setItems(books);
            displayResultMessage(searchResults.size() + " books found available.", javafx.scene.paint.Color.GREEN);
        }
    }

    @FXML
    private void onLogoutBtnClick() {
        // Implementation for logout button click
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout Confirmation");
        alert.setHeaderText("You are about to log out.");
        alert.setContentText("Are you sure you want to log out?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
                Parent loginRoot = loader.load();
                Scene loginScene = new Scene(loginRoot);
                Stage primaryStage = (Stage) resultsLabel.getScene().getWindow();
                primaryStage.setScene(loginScene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void onRefreshBtnClick() {
        refreshAllTable();
    }
}