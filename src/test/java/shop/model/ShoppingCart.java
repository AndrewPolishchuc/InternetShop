package shop.model;

import java.util.List;

public class ShoppingCart {
    private Long id;
    private List<Product> products;
    private Long userId;

    public void setId(Long id) {
        this.id = id;
    }
}
