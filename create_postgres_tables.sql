
CREATE SCHEMA used_car_colors;

CREATE TABLE used_car_colors.used_car_colors
(
    vin text COLLATE pg_catalog."default",
    make text COLLATE pg_catalog."default",
    model text COLLATE pg_catalog."default",
    year bigint,
    color text COLLATE pg_catalog."default",
    "salePrice" text COLLATE pg_catalog."default",
    city text COLLATE pg_catalog."default",
    state text COLLATE pg_catalog."default",
    "zipCode" text COLLATE pg_catalog."default"
)

TABLESPACE pg_default;



CREATE TABLE used_car_colors.used_car_colors_yearcolor_group
(
    year bigint NOT NULL,
    color text COLLATE pg_catalog."default" NOT NULL,
    count bigint,
    CONSTRAINT used_car_colors_yearcolor_group_pkey PRIMARY KEY (year, color)
)

TABLESPACE pg_default;