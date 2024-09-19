package com.ruben.project.seliga.data.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.room.Index;

import java.util.Date;

@Entity(
        tableName = "payments",
        foreignKeys = {
                @ForeignKey(
                        entity = Customer.class,
                        parentColumns = "id",
                        childColumns = "customerId",
                        onDelete = ForeignKey.CASCADE
                )
        },
        indices = {@Index(value = "customerId")}
)
public class Payments {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "customerId")
    private int customerId;

    private boolean paid;

    @ColumnInfo(name = "date")
    private Date date;

    public Payments(int customerId, boolean paid, Date date) {
        this.customerId = customerId;
        this.paid = paid;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}