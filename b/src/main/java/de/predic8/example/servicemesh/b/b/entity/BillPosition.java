package de.predic8.example.servicemesh.b.b.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class BillPosition {

    @Id
    @GeneratedValue
    public long no;

    @JsonIgnore
    @ManyToOne
    public Bill bill;

    public String description;
    public long amount; // netto

    public long ustFactor = 19; // 19% USt
}
