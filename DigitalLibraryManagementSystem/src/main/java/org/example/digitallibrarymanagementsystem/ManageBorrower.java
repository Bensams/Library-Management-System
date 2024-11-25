package org.example.digitallibrarymanagementsystem;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.Accounts.Borrower;
import org.example.Library.Book;
import org.example.Library.BorrowedBook;
import org.example.Library.Library;
import org.example.Management.BookManagement;
import org.example.Strategy.AuthorSearchStrategy;
import org.example.Strategy.ISBNSearchStrategy;
import org.example.Strategy.TitleSearchStrategy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ManageBorrower {
    private Borrower borrower;
    private BookManagement bookManagement;

    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<String> searchTypeComboBox;
    @FXML
    private Label resultsLabel;
    @FXML
    private TableView<Book> bookTableView;
    @FXML
    private TableColumn<Book, String> bookIDColumn;
    @FXML
    private TableColumn<Book, String> titleColumn;
    @FXML
    private TableColumn<Book, String> authorColumn;
    @FXML
    private TableColumn<Book, String> isbnColumn;
    @FXML
    private TableColumn<Book, Integer> quantityColumn;
    @FXML
    private TableColumn<Book, Void> actionColumn;
    @FXML
    private AnchorPane main_AP;
    @FXML
    private TableView<BorrowedBook> borrowedBookTableView;
    @FXML
    private TableColumn<BorrowedBook, String> borrowedBookIDColumn;
    @FXML
    private TableColumn<BorrowedBook, String> borrowedTitleColumn;
    @FXML
    private TableColumn<BorrowedBook, String> borrowedAuthorColumn;
    @FXML
    private TableColumn<BorrowedBook, String> borrowedISBNColumn;
    @FXML
    private TableColumn<BorrowedBook, String> borrowedDateColumn;
    @FXML
    private TableColumn<BorrowedBook, Void> returnActionColumn;

    // No-argument constructor
    public ManageBorrower() {
        this.bookManagement = new BookManagement();
    }

    // Constructor with Borrower parameter
    public ManageBorrower(Borrower borrower) {
        this.borrower = borrower;
        this.bookManagement = new BookManagement();
    }

    // Ensure the borrower is set before calling displayBorrowedBooks
    public void setBorrower(Borrower borrower) {
        this.borrower = borrower;
        if (this.borrower != null) {
            displayBorrowedBooks();
        } else {
            System.out.println("Borrower is not set.");
        }
    }

    @FXML
    private void initialize() {
        initializeSearchTypeComboBox();
        initializeBookTableColumns();
        initializeBorrowedBookTableColumns();
        Library.getInstance().setManageBorrower(this);
        addButtonToTable();
        addReturnButtonToTable();
        refreshAllTable();
        displayBorrowedBooks();
    }

    private void initializeSearchTypeComboBox() {
        searchTypeComboBox.getItems().addAll("Title", "Author", "ISBN");
        searchTypeComboBox.setValue("Title");
    }

    private void initializeBookTableColumns() {
        bookIDColumn.setCellValueFactory(new PropertyValueFactory<>("bookID"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("ISBN"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
    }

    private void initializeBorrowedBookTableColumns() {
        borrowedBookIDColumn.setCellValueFactory(new PropertyValueFactory<>("bookID"));
        borrowedTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        borrowedAuthorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        borrowedISBNColumn.setCellValueFactory(new PropertyValueFactory<>("ISBN"));
        borrowedDateColumn.setCellValueFactory(new PropertyValueFactory<>("borrowedDate"));
    }

    private void displayResultMessage(String message, javafx.scene.paint.Color color) {
        resultsLabel.setTextFill(color);
        resultsLabel.setText(message);
        clearRegisterMsgAfterDelay();
    }

    @FXML
    private void onLogoutBtnClick() {
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

    private void clearRegisterMsgAfterDelay() {
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(event -> resultsLabel.setText(""));
        pause.play();
    }

    public void refreshAllTable() {
        initializeBorrowedBookTableColumns();
        initializeBookTableColumns();
        displayAvailableBooks();
        displayBorrowedBooks();

    }

    public void displayAvailableBooks() {
        List<Book> availableBooks = Library.getInstance().getAvailableBooks();
        ObservableList<Book> books = FXCollections.observableArrayList(availableBooks);
        bookTableView.setItems(books);
    }

    public void displayBorrowedBooks() {
        if (borrower != null) {
            Library.getInstance().setCurrentBorrower(borrower);
            List<BorrowedBook> borrowedBooks = Library.getInstance().getBorrowedBooksByUser(borrower);
            if (borrowedBooks != null) {
                borrower.setBorrowedBooks(borrowedBooks);
                ObservableList<BorrowedBook> books = FXCollections.observableArrayList(borrowedBooks);
                borrowedBookTableView.setItems(books);
            } else {
                borrowedBookTableView.setItems(FXCollections.observableArrayList());
            }
        } else {
            System.out.println("Borrower is not set.");
        }
    }

    private boolean showLogoutConfirmation() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout Confirmation");
        alert.setHeaderText("You are about to log out.");
        alert.setContentText("Are you sure you want to log out?");
        return alert.showAndWait().get() == ButtonType.OK;
    }

    private void loadLoginScene() {
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
            bookTableView.setItems(books);
            displayResultMessage(searchResults.size() + " books found available.", javafx.scene.paint.Color.GREEN);
        }
    }

    @FXML
    private void onRefreshBtnClick() {
        refreshAllTable();
        displayResultMessage("Tables refreshed.", javafx.scene.paint.Color.GREEN);
    }

    private void addButtonToTable() {
        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button borrowButton = new Button("Borrow");

            {
                borrowButton.setOnAction(event -> handleBorrowAction());
            }

            private void handleBorrowAction() {
                Book book = getTableView().getItems().get(getIndex());
                Library.getInstance().setCurrentBorrower(borrower);
                boolean borrowResult = Library.getInstance().borrowBook(book);
                showBorrowResultAlert(borrowResult, book);
                refreshAllTable();
            }

            private void showBorrowResultAlert(boolean borrowResult, Book book) {
                Alert alert = new Alert(borrowResult ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
                alert.setTitle(borrowResult ? "Success" : "Error");
                alert.setHeaderText(null);
                alert.setContentText(borrowResult ? "Book successfully borrowed: " + book.getTitle() : "Book not available or out of stock.");
                alert.showAndWait();
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : borrowButton);
            }
        });
    }

    private void addReturnButtonToTable() {
        returnActionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button returnButton = new Button("Return");

            {
                returnButton.setOnAction(event -> handleReturnAction());
            }

            private void handleReturnAction() {
                BorrowedBook borrowedBook = getTableView().getItems().get(getIndex());
                Book book = Library.getInstance().getBookById(borrowedBook.getBookID());
                Library.getInstance().setCurrentBorrower(borrower);
                Library.getInstance().returnBook(book);
                showReturnResultAlert(book);
                refreshAllTable();
            }

            private void showReturnResultAlert(Book book) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Book successfully returned: " + book.getTitle());
                alert.showAndWait();
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : returnButton);
            }
        });
    }
}