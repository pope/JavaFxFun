package com.shifteleven;

import java.util.EnumSet;
import java.util.concurrent.atomic.AtomicReference;

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

    private final AtomicReference<PizzaTypes> selectedPizza = new AtomicReference<>(PizzaTypes.CHEESE);
    private final AtomicReference<ImmutableSet<ExtraIngredients>> extraIngredients = new AtomicReference<>(
            ImmutableSet.of());

    // TODO(pope): Verify if this is thread-safe.
    // If not, I'm doing extra work for no reason.
    private final SimpleDoubleProperty totalCost = new SimpleDoubleProperty(0.00);

    State() {
        updateTotals();
    }

    public void setSelectedPizza(PizzaTypes pizza) {
        selectedPizza.lazySet(pizza);
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
        extraIngredients.updateAndGet(prev -> {
            if (prev.contains(ingredient)) {
                return prev;
            }
            if (prev.isEmpty()) {
                return Sets.immutableEnumSet(EnumSet.of(ingredient));
            }
            EnumSet<ExtraIngredients> current = EnumSet.copyOf(prev);
            current.add(ingredient);
            return Sets.immutableEnumSet(current);
        });
        updateTotals();
    }

    public void removeExtraIngredient(ExtraIngredients ingredient) {
        extraIngredients.updateAndGet(prev -> {
            if (!prev.contains(ingredient)) {
                return prev;
            }
            EnumSet<ExtraIngredients> current = EnumSet.copyOf(prev);
            current.remove(ingredient);
            return Sets.immutableEnumSet(current);
        });
        updateTotals();
    }

    public DoubleProperty getTotalCost() {
        return totalCost;
    }

    private void updateTotals() {
        double cost = selectedPizza.get().amount;
        for (ExtraIngredients ingredient : extraIngredients.get()) {
            cost += ingredient.amount;
        }
        totalCost.set(cost);
    }
}
