package cinema.repositories;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


public class Seat {

    private int row;

    private int column;
    private int price;
    @JsonIgnore
    private boolean isSold;

    public Seat(int row,int column) {
        this.row = row;
        this.column = column;
        this.price = row <= 4 ? 10 : 8;
    }

    @JsonIgnore
    public boolean isSold() {
        return isSold;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setSold(boolean sold) {
        isSold = sold;
    }
}

