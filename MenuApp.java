import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class MenuApp extends Application {

    private final TextArea textArea = new TextArea();

    // Random green chosen once per program execution (so Menu Item 3 reuses it)
    private String storedGreenHex = null;

    private static final DateTimeFormatter DT_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void start(Stage stage) {
        // Text area setup
        textArea.setWrapText(true);
        textArea.setPromptText("Select a menu item...");

        // Menu bar + menu items
        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("Menu");

        MenuItem menuItem1 = new MenuItem("Menu Item 1");
        MenuItem menuItem2 = new MenuItem("Menu Item 2");
        MenuItem menuItem3 = new MenuItem("Menu Item 3");
        MenuItem menuItem4 = new MenuItem("Menu Item 4");

        menu.getItems().addAll(menuItem1, menuItem2, menuItem3, menuItem4);
        menuBar.getMenus().add(menu);

        // Root layout
        BorderPane root = new BorderPane();
        root.setTop(menuBar);
        root.setCenter(textArea);
        BorderPane.setMargin(textArea, new Insets(10));

        Scene scene = new Scene(root, 900, 600);

        // --- Menu Option 1: print date/time in text box ---
        menuItem1.setOnAction(e -> {
            String now = LocalDateTime.now().format(DT_FORMAT);
            textArea.appendText("Date/Time: " + now + System.lineSeparator());
        });

        // --- Menu Option 2: write text box contents to log.txt ---
        menuItem2.setOnAction(e -> {
            try {
                Path logPath = Path.of("log.txt"); // created in project working directory
                Files.writeString(logPath, textArea.getText());
                textArea.appendText("Saved text to log.txt" + System.lineSeparator());
            } catch (IOException ex) {
                textArea.appendText("ERROR writing to log.txt: " + ex.getMessage() + System.lineSeparator());
            }
        });

        // --- Menu Option 3: change background to random green hue (same per execution) ---
        menuItem3.setOnAction(e -> {
            if (storedGreenHex == null) {
                storedGreenHex = generateRandomGreenHex();
                textArea.appendText("Generated green hue: " + storedGreenHex + System.lineSeparator());
            } else {
                textArea.appendText("Reusing stored green hue: " + storedGreenHex + System.lineSeparator());
            }

            // Make the color change VERY visible:
            // Change the TextArea "inner" background to the stored green.
            textArea.setStyle("-fx-control-inner-background: " + storedGreenHex + ";");

            // Optional: also tint the outer root background slightly (helps around margins)
            root.setStyle("-fx-background-color: " + storedGreenHex + ";");
        });

        // --- Menu Option 4: exit ---
        menuItem4.setOnAction(e -> stage.close());

        stage.setTitle("Weekly HW - JavaFX Menu");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Generates a random green hex color where:
     * - Green is dominant
     * - Red and Blue are lower
     */
    private String generateRandomGreenHex() {
        Random rand = new Random();

        // Keep red/blue lower, green higher
        int r = rand.nextInt(80);           // 0 - 79
        int g = 120 + rand.nextInt(136);    // 120 - 255
        int b = rand.nextInt(80);           // 0 - 79

        // Ensure it's a valid JavaFX Color (not required, but nice)
        Color.color(r / 255.0, g / 255.0, b / 255.0);

        return String.format("#%02X%02X%02X", r, g, b);
    }

    // Keep this main method for IntelliJ launch reliability
    public static void main(String[] args) {
        launch(args);
    }
}
