//package com.hotel.system.entity;
//
//import jakarta.persistence.*;
//
//@Entity
//@Table(name = "order_items")
//public class OrderItem {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private int quantity;
//    private double price;
//
//    @ManyToOne
//    @JoinColumn(name = "menu_item_id")
//    private MenuItem menuItem;
//
//    @ManyToOne
//    @JoinColumn(name = "order_id")
//    private Order order;
//
//    public OrderItem() {}
//
//    public Long getId() { return id; }
//
//    public int getQuantity() { return quantity; }
//    public void setQuantity(int quantity) { this.quantity = quantity; }
//
//    public double getPrice() { return price; }
//    public void setPrice(double price) { this.price = price; }
//
//    public MenuItem getMenuItem() { return menuItem; }
//    public void setMenuItem(MenuItem menuItem) { this.menuItem = menuItem; }
//
//    public Order getOrder() { return order; }
//    public void setOrder(Order order) { this.order = order; }
//}
package com.hotel.system.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantity;
    private double price;

    @ManyToOne
    @JoinColumn(name = "menu_item_id")
    private MenuItem menuItem;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonIgnore
    private Order order;

    public OrderItem() {}

    public Long getId() { return id; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public MenuItem getMenuItem() { return menuItem; }
    public void setMenuItem(MenuItem menuItem) { this.menuItem = menuItem; }

    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }
}

