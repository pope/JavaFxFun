package com.shifteleven;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;

import org.pdfsam.rxjavafx.observables.JavaFxObservable;

import io.reactivex.rxjava3.core.Observable;

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
        controls.setPadding(new Insets(10,10,10,10));
        controls.setSpacing(10);

        Observable<Double> baseCost = JavaFxObservable.valuesOf(pizzaType.selectedToggleProperty())
                .startWithItem(cheesePizza).map(toggle -> {
                    if (toggle.equals(cheesePizza))
                        return 10.00;
                    if (toggle.equals(pepPizza))
                        return 12.00;
                    if (toggle.equals(vegPizza))
                        return 11.50;
                    throw new IllegalArgumentException("Unexpected toggle: " + toggle);
                });
        Observable<Double> extraCheeseCost = JavaFxObservable.valuesOf(extraCheese.selectedProperty())
                .startWithItem(false).map(selected -> selected ? 1.99 : 0.0);
        Observable<Double> baikenCost = JavaFxObservable.valuesOf(baikenTopping.selectedProperty())
                .startWithItem(false).map(selected -> selected ? 2.99 : 0.0);
        Observable<Double> mushroomsCost = JavaFxObservable.valuesOf(mushroomsTopping.selectedProperty())
                .startWithItem(false).map(selected -> selected ? 0.99 : 0.0);

        DecimalFormat df = new DecimalFormat("$####0.00");
        Observable<String> cost = Observable.combineLatest(
                Arrays.asList(baseCost, extraCheeseCost, baikenCost, mushroomsCost),
                costs -> Arrays.stream(costs).mapToDouble(Double.class::cast).sum()).map(df::format);

        cost.subscribe(price::setText);

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