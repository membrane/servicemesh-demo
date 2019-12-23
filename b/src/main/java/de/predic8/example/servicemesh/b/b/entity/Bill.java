package de.predic8.example.servicemesh.b.b.entity;

import javax.persistence.*;

@Entity
@SequenceGenerator(name="billSeq", initialValue=100000, allocationSize=100)
public class Bill {

    @Id
    @GeneratedValue(generator = "billSeq")
    public long no;

    public String month;

    public String date;

    public String receiverUsername;

    @Column(columnDefinition="TEXT")
    public String receiver;
}
