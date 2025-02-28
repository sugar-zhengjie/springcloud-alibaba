package com.zj.entity;

import java.io.Serializable;

public class Order implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer id;
    private Integer order_id;
    private String desc;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrder_id() {
        return order_id;
    }

    public void setOrder_id(Integer order_id) {
        this.order_id = order_id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Order(Integer id, Integer order_id, String desc) {
        this.id = id;
        this.order_id = order_id;
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", order_id=" + order_id +
                ", desc='" + desc + '\'' +
                '}';
    }
}
