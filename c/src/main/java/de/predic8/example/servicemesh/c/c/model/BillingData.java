package de.predic8.example.servicemesh.c.c.model;

import java.util.List;

public class BillingData {
    public String filenameBase;
    public String receiver;
    public String date;
    public String billNo;
    public List<BillingPositionData> positions;
    public String amountNetto;
    public String ustFactor;
    public String amountUst;
    public String amountBrutto;
}
