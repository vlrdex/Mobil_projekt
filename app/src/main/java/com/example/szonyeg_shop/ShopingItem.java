package com.example.szonyeg_shop;

import android.content.Intent;

import java.util.Objects;

public class ShopingItem {

    public String id;
    private String name;

    private String price;
    private int imageResource;
    private Integer amount;

    public ShopingItem(String name, String price, int imageResource) {
        this.name = name;
        this.price = price;
        this.imageResource = imageResource;
        this.amount=1;
    }

    public ShopingItem(){
        amount=1;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }
    public int getImageResource() {
        return imageResource;
    }

    public Integer getAmount() {
        return amount;
    }


    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public void increment(){
        amount=Math.min(++amount,10);
    }

    public void decrement(){
        amount=Math.max(0,--amount);
    }

    @Override
    public boolean equals(Object obj){
        if (obj == null){
            return false;
        }

        if(obj.getClass()!=this.getClass()){
            return false;
        }
        ShopingItem other=(ShopingItem) obj;
        if(this.name.equals(other.name) && Objects.equals(this.price, other.price)){
            return true;
        }


        return false;
    }

    @Override
    public String toString() {
        return "ShopingItem{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", imageResource=" + imageResource +
                ", amount=" + amount +
                '}';
    }
}
