package com.shifteleven;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

import com.google.inject.Inject;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;

import com.shifteleven.State.ExtraIngredients;
import com.shifteleven.State.PizzaTypes;

public final class AppController implements Initializable {
    private final State state;

    @FXML
    private RadioButton cheesePizza;
    @FXML
    private RadioButton pepPizza;
    @FXML
    private RadioButton vegPizza;

    @FXML
    private CheckBox xtraCheeseTop;
    @FXML
    private CheckBox baikenTop;
    @FXML
    private CheckBox shroomsTop;

    @FXML
    private TextArea total;

    @Inject
    AppController(State state) {
        this.state = state;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DecimalFormat df = new DecimalFormat("$####0.00");
        StringBinding totalCostUi = Bindings.createStringBinding(() -> df.format(state.getTotalCost().get()),
                state.getTotalCost());
        total.textProperty().bind(totalCostUi);
    }

    @FXML
    void selectPizzaType(ActionEvent event) {
        if (event.getSource().equals(cheesePizza)) {
            state.setSelectedPizza(PizzaTypes.CHEESE);
        } else if (event.getSource().equals(pepPizza)) {
            state.setSelectedPizza(PizzaTypes.PEPPERONI);
        } else if (event.getSource().equals(vegPizza)) {
            state.setSelectedPizza(PizzaTypes.VEGGIE);
        } else {
            throw new IllegalArgumentException("Unknown event source: " + event);
        }
    }

    @FXML
    void updateToppings(ActionEvent event) {
        if (event.getSource().equals(xtraCheeseTop)) {
            state.updateExtraIngredient(ExtraIngredients.XTRA_CHEESE, xtraCheeseTop.isSelected());
        } else if (event.getSource().equals(baikenTop)) {
            state.updateExtraIngredient(ExtraIngredients.BACON, baikenTop.isSelected());
        } else if (event.getSource().equals(shroomsTop)) {
            state.updateExtraIngredient(ExtraIngredients.SHROOMS, shroomsTop.isSelected());
        } else {
            throw new IllegalArgumentException("Unknown event source: " + event);
        }
    }
}
