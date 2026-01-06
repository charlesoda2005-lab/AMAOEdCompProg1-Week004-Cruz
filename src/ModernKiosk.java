import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;

import java.util.Random;

public class ModernKiosk extends Application {

    private TextArea receiptArea;
    private Label totalLabel;
    private double total = 0;
    private Random random = new Random();

    @Override
    public void start(Stage primaryStage) {

        AudioClip clickSound = new AudioClip(getClass().getResource("/click.wav").toString());

        VBox menuBox = new VBox(10);
        menuBox.setPadding(new Insets(10));
        menuBox.setAlignment(Pos.TOP_CENTER);

        Label menuLabel = new Label("MENU");
        menuLabel.setStyle("-fx-font-size: 24; -fx-font-weight: bold;");
        menuBox.getChildren().add(menuLabel);

        menuBox.getChildren().addAll(
                createMenuButton("🍔 Burger Stinky ₱39", 39, clickSound),
                createMenuButton("🍟 Fries ₱30", 30, clickSound),
                createMenuButton("🍗 Chicken na Joy ₱50", 50, clickSound),
                createSoftDrinkButton(clickSound),
                createMenuButton("🍧 Halo-Halo ₱79", 79, clickSound),
                createMenuButton("🍲 Sinigang na Ewan ₱120", 120, clickSound),
                createMenuButton("🌭 Hotdog ₱20", 20, clickSound),
                createMenuButton("🥟 Siomai ₱25", 25, clickSound),
                createMenuButton("☕ Starbucks na Peke ₱79", 79, clickSound)
        );

        receiptArea = new TextArea();
        receiptArea.setEditable(false);
        receiptArea.setPrefWidth(300);
        receiptArea.setStyle("-fx-font-family: monospace; -fx-font-size: 14;");

        totalLabel = new Label("TOTAL: ₱0");
        totalLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");

        VBox receiptBox = new VBox(10, new Label("RECEIPT"), receiptArea, totalLabel);
        receiptBox.setPadding(new Insets(10));

        Button checkoutBtn = new Button("CHECKOUT");
        Button newOrderBtn = new Button("NEW ORDER");
        checkoutBtn.setPrefWidth(150);
        newOrderBtn.setPrefWidth(150);

        VBox actionBox = new VBox(20, checkoutBtn, newOrderBtn);
        actionBox.setPadding(new Insets(10));
        actionBox.setAlignment(Pos.TOP_CENTER);

        HBox mainBox = new HBox(20, menuBox, receiptBox, actionBox);
        mainBox.setPadding(new Insets(10));

        Scene scene = new Scene(mainBox, 1000, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Modern Kiosk");
        primaryStage.show();

        checkoutBtn.setOnAction(e -> checkout(primaryStage));
        newOrderBtn.setOnAction(e -> resetOrder());
    }

    private Button createMenuButton(String name, int price, AudioClip sound) {
        Button btn = new Button(name);
        btn.setPrefSize(250, 60);
        btn.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

        btn.setOnAction(e -> {
            sound.play();
            TextInputDialog qtyDialog = new TextInputDialog("1");
            qtyDialog.setHeaderText("Enter quantity for " + name);
            qtyDialog.showAndWait().ifPresent(qtyStr -> {
                try {
                    int qty = Integer.parseInt(qtyStr);
                    if (qty <= 0) throw new NumberFormatException();
                    double itemTotal = price * qty;
                    total += itemTotal;
                    receiptArea.appendText(name + " x" + qty + " ₱" + itemTotal + "\n");
                    totalLabel.setText("TOTAL: ₱" + total);
                } catch (NumberFormatException ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid quantity!");
                    alert.showAndWait();
                }
            });
        });

        return btn;
    }

    private Button createSoftDrinkButton(AudioClip sound) {
        Button btn = new Button("🥤 Softdrinks ₱30");
        btn.setPrefSize(250, 60);
        btn.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

        btn.setOnAction(e -> {
            sound.play();
            String[] drinks = {"Coke", "Sprite", "Royal", "Mountain Dew"};
            ChoiceDialog<String> drinkDialog = new ChoiceDialog<>(drinks[0], drinks);
            drinkDialog.setHeaderText("Choose Softdrink");
            drinkDialog.showAndWait().ifPresent(drink -> {
                TextInputDialog qtyDialog = new TextInputDialog("1");
                qtyDialog.setHeaderText("Enter quantity for " + drink);
                qtyDialog.showAndWait().ifPresent(qtyStr -> {
                    try {
                        int qty = Integer.parseInt(qtyStr);
                        if (qty <= 0) throw new NumberFormatException();
                        double itemTotal = 30 * qty;
                        total += itemTotal;
                        receiptArea.appendText(drink + " x" + qty + " ₱" + itemTotal + "\n");
                        totalLabel.setText("TOTAL: ₱" + total);
                    } catch (NumberFormatException ex) {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid quantity!");
                        alert.showAndWait();
                    }
                });
            });
        });

        return btn;
    }

    private void checkout(Stage stage) {
        if (total == 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "No items ordered!");
            alert.showAndWait();
            return;
        }

        TextInputDialog cashDialog = new TextInputDialog();
        cashDialog.setHeaderText("Enter Cash:");
        cashDialog.showAndWait().ifPresent(cashStr -> {
            try {
                double cash = Double.parseDouble(cashStr);
                if (cash < total) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Insufficient cash!");
                    alert.showAndWait();
                    return;
                }

                double change = cash - total;
                receiptArea.appendText("\n------------------------\n");
                receiptArea.appendText("TOTAL: ₱" + total + "\n");
                receiptArea.appendText("CASH: ₱" + cash + "\n");
                receiptArea.appendText("CHANGE: ₱" + change + "\n");

                ChoiceDialog<String> dineDialog = new ChoiceDialog<>("Take Out", "Take Out", "Dine In");
                dineDialog.setHeaderText("Select Order Type");
                dineDialog.showAndWait().ifPresent(option -> {
                    int orderNumber = random.nextInt(9000) + 1000; // Random 1000-9999
                    receiptArea.appendText("ORDER TYPE: " + option + "\n");
                    receiptArea.appendText("ORDER NUMBER: " + orderNumber + "\n");
                    receiptArea.appendText("Thank you for ordering!\n\n");

                    Alert thankYou = new Alert(Alert.AlertType.INFORMATION,
                            "Thank you!\nOrder Type: " + option + "\nOrder Number: " + orderNumber +
                                    "\nChange: ₱" + change + "\nNext customer please!");
                    thankYou.showAndWait();

                    resetOrder();
                });

            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid cash input!");
                alert.showAndWait();
            }
        });
    }

    private void resetOrder() {
        total = 0;
        receiptArea.clear();
        totalLabel.setText("TOTAL: ₱0");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
