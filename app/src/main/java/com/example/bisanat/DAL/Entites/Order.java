package com.example.bisanat.DAL.Entites;

import java.util.Date;

public class Order {
    public int Id;
    public int OrderTo;
    public int OrderFrom;
    public Date OrderedAt;
    public Date DeliveredAt;
    public double TotalPrice;
}
