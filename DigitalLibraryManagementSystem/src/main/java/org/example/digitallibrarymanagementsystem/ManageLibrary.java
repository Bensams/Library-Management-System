package org.example.digitallibrarymanagementsystem;

import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import org.example.Accounts.Admin;
import org.example.Accounts.User;
import org.example.Library.Book;
import org.example.Library.BorrowedBook;
import org.example.Library.Library;
import org.example.Management.BookManagement;
import org.example.Management.SerializedManagement;
import org.example.Management.UserManagement;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Controller class for managing the library system.
 */
public class ManageLibrary {
    // Constants for various messages
    private static final String ERROR_FILL_FIELDS = "Please fill in all the fields!";
    private static final String ERROR_BOOK_EXISTS = "Book ID already exists!";
    private static final String SUCCESS_BOOK_ADDED = " added successfully!";
    private static final String ERROR_USER_EXISTS = "User ID or Username already exists.";
    private static final String ERROR_ALL_FIELDS_REQUIRED = "All fields must be filled out.";
    private static final String SUCCESS_USER_REGISTERED = "User registered successfully.";
    private static final String ERROR_NO_BORROWED_BOOKS = "No borrowed books found!";
    private static final String DELETE_ACCOUNT_SUCCESS = "Deleted account successfully!";

    private Admin admin;
    private BorderPane originalAnchorPane;

    // FXML injected fields
//    @FXML
//    private BorderPane manageLibrary_BP;
    @FXML
    TabPane management_TP;
    @FXML
    private TableView<User> accountsTableView;
    @FXML
    private TableColumn<User, String> accountIDColumn;
    @FXML
    private TableColumn<User, String> accountNameColumn;
    @FXML
    private TableColumn<User, String> accountContactColumn;
    @FXML
    private TableColumn<User, String> accountUsernameColumn;
    @FXML
    private TableColumn<User, String> accountRoleColumn;
    @FXML
    private TableColumn<User, String> accountPasswordColumn;
    @FXML
    private TextField userID_TF;
    @FXML
    private TextField name_TF;
    @FXML
    private TextField contactInfo_TF;
    @FXML
    private TextField username_TF;
    @FXML
    private TextField password_TF;
    @FXML
    private ChoiceBox<String> role_CB;
    @FXML
    private Label registerMsg_LBL;
    @FXML
    private TextField bookID_TF;
    @FXML
    private TextField title_TF;
    @FXML
    private TextField author_TF;
    @FXML
    private TextField isbn_TF;
    @FXML
    private DatePicker publicationDate_TF;
    @FXML
    private TextField quantity_TF;
    @FXML
    private Slider counter_Sldr;
    @FXML
    private Label message_LBL;
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
    private TableColumn<Book, String> publicationDateColumn;
    @FXML
    private TableColumn<Book, Integer> quantityColumn;
    @FXML
    private TableView<BorrowedBook> borrowedBooksTableView;
    @FXML
    private TableColumn<BorrowedBook, String> userIDColumn;
    @FXML
    private TableColumn<BorrowedBook, String> userNameColumn;
    @FXML
    private TableColumn<BorrowedBook, String> bookBorrowIDColumn;
    @FXML
    private TableColumn<BorrowedBook, String> titleBorrowColumn;
    @FXML
    private TableColumn<BorrowedBook, String> borrowedDateColumn;
    @FXML
    private Label borrowMsg_LBL;
    @FXML
    private AnchorPane manageLibrary_AP;

    @FXML
    private Button updateBookBtn;
    @FXML
    private Button deleteBookBtn;


    @FXML
    private void initialize() {
        try {
            initializeRoleChoices();
            storeOriginalArchorPane();
            initializeAccountTableColumns();
            initializeBookTableColumns();
            bookTableView.setItems(getBookList());

            // Add listener for row selection
            bookTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    onBookSelected(newValue);
                }
            });
            refreshAvailableBooks();
        } catch (Exception e) {
            e.printStackTrace();
            // Optionally, display an error message to the user
        }
    }

    @FXML
    private void onUpdateBookBtnClick() {
        if (selectedBook != null) {
            // Implement the update logic here
            // For example, open a new window with the book details for editing
            openUpdateBookWindow(selectedBook);
        }
    }

    @FXML
    private void onDeleteBookBtnClick() {
        if (selectedBook != null) {
            // Implement the delete logic here
            deleteBook(selectedBook);
            refreshBookTable();
        }
    }

    private void openUpdateBookWindow(Book book) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ManageLibraryGUI/UpdateBook.fxml"));
            Parent root = loader.load();

            UpdateBookController controller = loader.getController();
            controller.setBook(book, new BookManagement());

            Stage stage = new Stage();
            stage.setTitle("Update Book");
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.show();
            refreshAvailableBooks();
            // Add a listener to refresh the table when the update window is closed
            stage.setOnHiding(event -> refreshBookTable());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteBook(Book book) {
        // Logic to delete the book from the data source
        BookManagement bookManagement = new BookManagement();
        bookManagement.deleteBook(book.getBookID());
        displayRegisterMessage("Book deleted successfully.", javafx.scene.paint.Color.GREEN);
    }

    private void refreshBookTable() {
        bookTableView.setItems(getBookList());
    }

    private Book selectedBook;

    private void onBookSelected(Book book) {
        selectedBook = book;
        // Enable the update and delete buttons
        updateBookBtn.setDisable(false);
        deleteBookBtn.setDisable(false);
    }

    /**
     * Default constructor.
     */
    public ManageLibrary() {

    }

    /**
     * Constructor with Admin parameter.
     * @param admin The admin instance.
     */
    public ManageLibrary(Admin admin) {
        this.admin = admin;
    }

    /**
     * Initializes the controller class. This method is automatically called after the FXML file has been loaded.
     */


    /**
     * Handles the logout button click event.
     */
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
                Stage primaryStage = (Stage) manageLibrary_AP.getScene().getWindow();
                primaryStage.setScene(loginScene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Initializes the role choices for the role choice box.
     */
    private void initializeRoleChoices() {
        role_CB.setItems(FXCollections.observableArrayList("Admin", "Borrower"));
    }

    /**
     * Stores the original TabPane for later use.
     */
    private void storeOriginalArchorPane() {
        originalAnchorPane = (BorderPane) manageLibrary_AP.getChildren().get(0);
    }

    /**
     * Initializes the columns of the accounts table.
     */
    private void initializeAccountTableColumns() {
        accountIDColumn.setCellValueFactory(new PropertyValueFactory<>("userID"));
        accountNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        accountContactColumn.setCellValueFactory(new PropertyValueFactory<>("contactInfo"));
        accountUsernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        accountRoleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        if (admin.getUserID().equals("admin")) {
            accountPasswordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        } else {
            accountPasswordColumn.setVisible(false);
        }

        // Check if the actions column already exists
        boolean actionsColumnExists = accountsTableView.getColumns().stream()
                .anyMatch(column -> "Actions".equals(column.getText()));

        if (!actionsColumnExists) {
            TableColumn<User, Void> actionColumn = new TableColumn<>("Actions");
            Callback<TableColumn<User, Void>, TableCell<User, Void>> cellFactory = new Callback<>() {
                @Override
                public TableCell<User, Void> call(final TableColumn<User, Void> param) {
                    final TableCell<User, Void> cell = new TableCell<>() {
                        private final Button deleteButton = new Button("Delete");
                        private final Button updateButton = new Button("Update");

                        {
                            deleteButton.setOnAction(event -> {
                                User user = getTableView().getItems().get(getIndex());
                                deleteUser(user);
                            });

                            updateButton.setOnAction(event -> {
                                User user = getTableView().getItems().get(getIndex());
                                updateUser(user);
                            });

                            HBox pane = new HBox(deleteButton, updateButton);
                            setGraphic(pane);
                        }

                        @Override
                        protected void updateItem(Void item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty) {
                                setGraphic(null);
                            } else {
                                User user = getTableView().getItems().get(getIndex());
                                deleteButton.setDisable(user.getUserID().equals(admin.getUserID()));
                                setGraphic(getGraphic());
                            }
                        }
                    };
                    return cell;
                }
            };

            actionColumn.setCellFactory(cellFactory);
            accountsTableView.getColumns().add(actionColumn);
        }

        // Apply custom sorting
        applyCustomSorting();
    }
    /**
     * Retrieves the list of accounts.
     * @return An observable list of users.
     */
    private ObservableList<User> getAccountsList() {
        SerializedManagement serializedManagement = SerializedManagement.getInstance();
        return FXCollections.observableArrayList(serializedManagement.getUsers());
    }

    private void applyCustomSorting() {
        ObservableList<User> accountsList = getAccountsList();
        SortedList<User> sortedList = new SortedList<>(accountsList);
        sortedList.setComparator(new Comparator<User>() {
            @Override
            public int compare(User u1, User u2) {
                if (u1.getRole().equalsIgnoreCase("Admin") && !u2.getRole().equalsIgnoreCase("Admin")) {
                    return -1;
                } else if (!u1.getRole().equalsIgnoreCase("Admin") && u2.getRole().equalsIgnoreCase("Admin")) {
                    return 1;
                } else {
                    return u1.getRole().compareToIgnoreCase(u2.getRole());
                }
            }
        });
        accountsTableView.setItems(sortedList);
    }

    /**
     * Deletes a user.
     * @param user The user to delete.
     */
    private void deleteUser(User user) {
        UserManagement userManagement = new UserManagement(admin);
        userManagement.deleteRegisteredAccount(user.getUserID());
        accountsTableView.setItems(getAccountsList());
        displayRegisterMessage(DELETE_ACCOUNT_SUCCESS, javafx.scene.paint.Color.GREEN);
        refreshAccountsTable();
    }

    private void refreshAccountsTable() {
        accountsTableView.setItems(getAccountsList());
        applyCustomSorting();
    }

    /**
     * Updates a user.
     * @param user The user to update.
     */
    private void updateUser(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ManageLibraryGUI/UpdateUser.fxml"));
            Parent root = loader.load();

            UpdateUserController controller = loader.getController();
            controller.setUser(user, new UserManagement(admin));

            Stage stage = new Stage();
            stage.setTitle("Update User");
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.show();

            // Add a listener to refresh the table when the update window is closed
            stage.setOnHiding(event -> refreshAccountsTable());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean areFieldsValid(String... fields) {
        for (String field : fields) {
            if (field == null || field.trim().isEmpty()) {
                return false;
            }
            // Add more specific validation if needed (e.g., regex for email, etc.)
        }
        return true;
    }

    /**
     * Handles the register user button click event.
     */
    @FXML
    private void onRegisterUserBtnClick() {
        UserManagement userManagement = new UserManagement(admin);

        String userID = userID_TF.getText().trim();
        String name = name_TF.getText().trim();
        String contactInfo = contactInfo_TF.getText().trim();
        String username = username_TF.getText().trim();
        String password = password_TF.getText().trim();
        String role = role_CB.getValue();

        if (!areFieldsValid(userID, name, contactInfo, username, password, role)) {
            displayRegisterMessage(ERROR_ALL_FIELDS_REQUIRED, javafx.scene.paint.Color.RED);
            return;
        }

        if (userManagement.userExists(userID, username)) {
            displayRegisterMessage(ERROR_USER_EXISTS, javafx.scene.paint.Color.RED);
            return;
        }

        userManagement.registerUser(userID, name, contactInfo, username, password, role);
        displayRegisterMessage(SUCCESS_USER_REGISTERED, javafx.scene.paint.Color.GREEN);
        accountsTableView.setItems(getAccountsList());

        clearFields();
        refreshAccountsTable();
        initializeAccountTableColumns();
    }

    /**
     * Checks if any of the provided fields are empty.
     * @param fields The fields to check.
     * @return True if any field is empty, false otherwise.
     */
    private boolean areFieldsEmpty(String... fields) {
        for (String field : fields) {
            if (field == null || field.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Displays a registration message.
     * @param message The message to display.
     * @param color The color of the message text.
     */
    private void displayRegisterMessage(String message, javafx.scene.paint.Color color) {
        registerMsg_LBL.setTextFill(color);
        registerMsg_LBL.setText(message);
        clearRegisterMsgAfterDelay();
    }

    /**
     * Clears the registration message after a delay.
     */
    private void clearRegisterMsgAfterDelay() {
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(event -> registerMsg_LBL.setText(""));
        pause.play();
    }

    /**
     * Clears the input fields for user registration.
     */
    @FXML
    private void clearFields() {
        userID_TF.clear();
        name_TF.clear();
        contactInfo_TF.clear();
        username_TF.clear();
        password_TF.clear();
        role_CB.setValue(null);
    }

    /**
     * Handles the add book button click event.
     */
    @FXML
    private void onAddBookBtnClick() {
        loadFXML("ManageLibraryGUI/addBook.fxml", this::initializeAddBookListeners);
    }

    /**
     * Initializes listeners for adding a book.
     */
    private void initializeAddBookListeners() {
        counter_Sldr.valueProperty().addListener((observable, oldValue, newValue) -> {
            quantity_TF.setText(String.valueOf(newValue.intValue()));
        });
        quantity_TF.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                try {
                    int value = Integer.parseInt(newValue);
                    counter_Sldr.setValue(value);
                } catch (NumberFormatException e) {
                    quantity_TF.setText(oldValue);
                }
            }
        });
    }

    /**
     * Handles the cancel button click event.
     */
   @FXML
    private void onCancelBtnClick() {
       if (originalAnchorPane.getParent() != null) {
           ((Pane) originalAnchorPane.getParent()).getChildren().remove(originalAnchorPane);
       }
       if (!manageLibrary_AP.getChildren().contains(originalAnchorPane)) {
           manageLibrary_AP.getChildren().clear();
           manageLibrary_AP.getChildren().add(originalAnchorPane);
       }
    }


    /**
     * Handles the manage user button click event.
     */
    @FXML
    private void onManageUserBtnClick() {
        onManageUserTabSelected(1);
    }

    private void onManageUserTabSelected(int index) {
        management_TP.getSelectionModel().select(index);
    }

    /**
     * Handles the reset button click event.
     */
    @FXML
    private void onResetBtnClick() {
        bookID_TF.clear();
        title_TF.clear();
        author_TF.clear();
        isbn_TF.clear();
        publicationDate_TF.setValue(null);
        quantity_TF.clear();
        counter_Sldr.setValue(0);
    }

    /**
     * Handles the add button click event for adding a book.
     */
    @FXML
    private void onAddBtnClick() {
        if (areFieldsEmpty(bookID_TF.getText(), title_TF.getText(), author_TF.getText(), quantity_TF.getText())) {
            displayMessage(ERROR_FILL_FIELDS, javafx.scene.paint.Color.RED);
            return;
        }
        if (admin.bookExists(bookID_TF.getText())) {
            displayMessage(ERROR_BOOK_EXISTS, javafx.scene.paint.Color.RED);
            return;
        }
        admin.addBook(bookID_TF.getText(), title_TF.getText(), author_TF.getText(), isbn_TF.getText(), publicationDate_TF.getValue().toString(), (int) counter_Sldr.getValue());
        displayMessage(title_TF.getText() + SUCCESS_BOOK_ADDED, javafx.scene.paint.Color.GREEN);
        refreshAvailableBooks();
    }

    /**
     * Displays a message.
     * @param message The message to display.
     * @param color The color of the message text.
     */
    private void displayMessage(String message, javafx.scene.paint.Color color) {
        message_LBL.setTextFill(color);
        message_LBL.setText(message);
    }



    /**
     * Initializes the tables for the library report.
     */
    private void initializeLibraryReportTables() {
        initializeBookTableColumns();
        bookTableView.setItems(getBookList());

        initializeBorrowedBooksTableColumns();
        borrowedBooksTableView.setItems(getBorrowedBooksList());
    }

    /**
     * Initializes the columns of the book table.
     */
    private void initializeBookTableColumns() {
        bookIDColumn.setCellValueFactory(new PropertyValueFactory<>("bookID"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("ISBN"));
        publicationDateColumn.setCellValueFactory(new PropertyValueFactory<>("publicationDate"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
    }

    /**
     * Initializes the columns of the borrowed books table.
     */
    private void initializeBorrowedBooksTableColumns() {
        userIDColumn.setCellValueFactory(new PropertyValueFactory<>("userID"));
        userNameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));
        bookBorrowIDColumn.setCellValueFactory(new PropertyValueFactory<>("bookID"));
        titleBorrowColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        borrowedDateColumn.setCellValueFactory(new PropertyValueFactory<>("borrowedDate"));
    }

    @FXML
    private void onRefreshBtnClick() {
        refreshAvailableBooks();
    }

    private void refreshAvailableBooks() {
        initializeBookTableColumns();
        initializeBorrowedBooksTableColumns();
        bookTableView.setItems(getBookList());
        borrowedBooksTableView.setItems(getBorrowedBooksList());

        displayRegisterMessage("Tables refreshed successfully.", javafx.scene.paint.Color.GREEN);
    }

    /**
     * Retrieves the list of available books.
     * @return An observable list of books.
     */
    private ObservableList<Book> getBookList() {
        return FXCollections.observableArrayList(Library.getInstance().getAvailableBooks());
    }



    /**
     * Retrieves the list of borrowed books.
     * @return An observable list of borrowed books.
     */
    private ObservableList<BorrowedBook> getBorrowedBooksList() {
        Library library = Library.getInstance();
        Map<User, List<BorrowedBook>> borrowedBooksMap = library.trackBorrowedBooks();
        ObservableList<BorrowedBook> borrowedBooksList = FXCollections.observableArrayList();

        if (borrowedBooksMap == null || borrowedBooksMap.isEmpty()) {
            borrowMsg_LBL.setText("No borrowed books found!");
            return borrowedBooksList;
        }

        for (List<BorrowedBook> userBorrowedBooks : borrowedBooksMap.values()) {
            if (userBorrowedBooks != null && !userBorrowedBooks.isEmpty()) {
                borrowedBooksList.addAll(userBorrowedBooks);
            }
        }
        return borrowedBooksList;
    }

    /**
     * Loads an FXML file and initializes it.
     * @param fxmlPath The path to the FXML file.
     * @param initializer The initializer to run after loading the FXML.
     */
    private void loadFXML(String fxmlPath, Runnable initializer) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            loader.setController(this);
            Pane centerPane = loader.load();

            // Ensure the node is not already added
            if (!manageLibrary_AP.getChildren().contains(centerPane)) {
                manageLibrary_AP.getChildren().add(centerPane);
            }

            initializer.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}