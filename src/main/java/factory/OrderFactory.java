package factory;

import ingridients.Ingredients;
import order.Order;

public class OrderFactory {

    public static Order orderWithValidIngredients() {
        return new Order(Ingredients.VALID);
    }

    public static Order orderWithInvalidIngredients() {
        return new Order(Ingredients.INVALID);
    }

    public static Order orderWithoutIngredients() {
        return new Order(null);
    }
}
