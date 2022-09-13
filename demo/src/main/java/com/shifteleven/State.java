package com.shifteleven;

import java.util.EnumSet;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.inject.Singleton;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

@Singleton
public final class State {

    public static enum PizzaTypes {
        CHEESE(10.00),
        PEPPERONI(13.00),
        VEGGIE(11.50);

        final double amount;

        PizzaTypes(double amount) {
            this.amount = amount;
        }
    }

    public static enum ExtraIngredients {
        XTRA_CHEESE(0.99),
        BACON(2.99),
        SHROOMS(1.99);

        final double amount;

        ExtraIngredients(double amount) {
            this.amount = amount;
        }
    }

    private PizzaTypes selectedPizza = PizzaTypes.CHEESE;
    private ImmutableSet<ExtraIngredients> extraIngredients = ImmutableSet.of();

    private final SimpleDoubleProperty totalCost = new SimpleDoubleProperty(0.00);

    State() {
        updateTotals();
    }

    public void setSelectedPizza(PizzaTypes pizza) {
        selectedPizza = pizza;
        updateTotals();
    }

    public void updateExtraIngredient(ExtraIngredients ingredient, boolean isAdded) {
        if (isAdded) {
            addExtraIngredient(ingredient);
        } else {
            removeExtraIngredient(ingredient);
        }
    }

    public void addExtraIngredient(ExtraIngredients ingredient) {
        if (extraIngredients.contains(ingredient)) {
            return;
        }

        if (extraIngredients.isEmpty()) {
            extraIngredients = Sets.immutableEnumSet(EnumSet.of(ingredient));
        } else {
            EnumSet<ExtraIngredients> current = EnumSet.copyOf(extraIngredients);
            current.add(ingredient);
            extraIngredients = Sets.immutableEnumSet(current);
        }
        updateTotals();
    }

    public void removeExtraIngredient(ExtraIngredients ingredient) {
        if (!extraIngredients.contains(ingredient)) {
            return;
        }

        EnumSet<ExtraIngredients> current = EnumSet.copyOf(extraIngredients);
        current.remove(ingredient);
        extraIngredients = Sets.immutableEnumSet(current);
        updateTotals();
    }

    public DoubleProperty getTotalCost() {
        return totalCost;
    }

    private void updateTotals() {
        double cost = selectedPizza.amount;
        for (ExtraIngredients ingredient : extraIngredients) {
            cost += ingredient.amount;
        }
        totalCost.set(cost);
    }
}
