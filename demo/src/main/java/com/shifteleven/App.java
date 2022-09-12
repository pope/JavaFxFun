package com.shifteleven;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.StringBinding;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.DecimalFormat;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("Hi, Burrito!");

        ToggleGroup pizzaType = new ToggleGroup();
        RadioButton cheesePizza = new RadioButton("Cheese");
        cheesePizza.setToggleGroup(pizzaType);
        cheesePizza.setSelected(true);
        RadioButton pepPizza = new RadioButton("Pep");
        pepPizza.setToggleGroup(pizzaType);
        RadioButton vegPizza = new RadioButton("Veg");
        vegPizza.setToggleGroup(pizzaType);

        VBox pizzaTypeContainer = new VBox(cheesePizza, pepPizza, vegPizza);

        CheckBox extraCheese = new CheckBox("Extra Cheese");
        CheckBox baikenTopping = new CheckBox("Bacon");
        CheckBox mushroomsTopping = new CheckBox("Mushrooms");

        VBox extraIngredients = new VBox(extraCheese, baikenTopping, mushroomsTopping);

        TextField price = new TextField();
        price.setEditable(false);

        HBox controls = new HBox(createSpacer(), pizzaTypeContainer, createSpacer(), extraIngredients, createSpacer(),
                price, createSpacer());
        controls.setAlignment(Pos.TOP_CENTER);
        controls.setPadding(new Insets(10, 10, 10, 10));
        controls.setSpacing(10);

        DoubleBinding baseCost = Bindings.createDoubleBinding(() -> {
            Toggle toggle = pizzaType.getSelectedToggle();
            if (toggle.equals(cheesePizza))
                return 10.0;
            if (toggle.equals(pepPizza))
                return 13.0;
            if (toggle.equals(vegPizza))
                return 11.50;
            throw new IllegalArgumentException("Unexpected toggle: " + toggle);

        }, pizzaType.selectedToggleProperty());
        DoubleBinding ingredientsCost = Bindings.createDoubleBinding(() -> {
            double cheeseCost = extraCheese.isSelected() ? 1.99 : 0.0;
            double baikenCost = baikenTopping.isSelected() ? 2.99 : 0.0;
            double mushroomsCost = mushroomsTopping.isSelected() ? 0.99 : 0.0;
            return cheeseCost + baikenCost + mushroomsCost;
        }, extraCheese.selectedProperty(), baikenTopping.selectedProperty(), mushroomsTopping.selectedProperty());
        DoubleBinding totalCost = baseCost.add(ingredientsCost);

        DecimalFormat df = new DecimalFormat("$####0.00");
        StringBinding totalCostUi = Bindings.createStringBinding(() -> df.format(totalCost.get()), totalCost);
        price.textProperty().bind(totalCostUi);

        scene = new Scene(controls, 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    private static Node createSpacer() {
        final Region spacer = new Region();
        // Make it always grow or shrink according to the available space
        HBox.setHgrow(spacer, Priority.ALWAYS);
        return spacer;
    }

    public static void main(String[] args) {
        launch();
    }

}