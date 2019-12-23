SELECT pg_catalog.setval('public.bill_seq', 123456, false);

INSERT INTO bill (
    no,
    date,
    month,
    receiver_username,
    receiver
) VALUES (
    pg_catalog.nextval('public.bill_seq'),
    '1. Dezember 2019',
    '2019-11',
    'max',
    'Max Mustermann
Eine Straße 2

12345 Musterstadt'
);


INSERT INTO bill_position (
    no,
    amount,
    description,
    ust_factor,
    bill_no
) VALUES (
    1,
    5000,
    'Vertrag Nr. 987654
Monat November 2019',
    19,
    123456
);

INSERT INTO bill_position (
    no,
    amount,
    description,
    ust_factor,
    bill_no
) VALUES (
    2,
    -500,
    'Rabatt im 1. Jahr',
    19,
    123456
);