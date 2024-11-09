drop table if exists crypto_price;

create table crypto_price (
    price_ts timestamp(3) NOT NULL,
    currency_code text NOT NULL,
    price numeric NOT NULL,
    CONSTRAINT crypto_price_pk PRIMARY KEY (price_ts, currency_code)
);
