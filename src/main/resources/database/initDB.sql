CREATE TABLE IF NOT EXISTS public.customer
(
    id bigint NOT NULL,
    title text COLLATE pg_catalog."default",
    CONSTRAINT customer_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.customer
    OWNER to postgres;



CREATE TABLE IF NOT EXISTS public.item
(
    id bigint NOT NULL,
    title text COLLATE pg_catalog."default",
    CONSTRAINT item_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.item
    OWNER to postgres;



CREATE TABLE IF NOT EXISTS public."order"
(
    id bigint NOT NULL,
    created timestamp without time zone,
    customer bigint,
    CONSTRAINT order_pkey PRIMARY KEY (id),
    CONSTRAINT customer FOREIGN KEY (customer)
        REFERENCES public.customer (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public."order"
    OWNER to postgres;




CREATE TABLE IF NOT EXISTS public.order_item
(
    id bigint NOT NULL,
    item bigint,
    price numeric,
    quantity numeric,
    order_id bigint,
    CONSTRAINT order_item_pkey PRIMARY KEY (id),
    CONSTRAINT item FOREIGN KEY (item)
        REFERENCES public.item (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID,
    CONSTRAINT order_id FOREIGN KEY (order_id)
        REFERENCES public."order" (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.order_item
    OWNER to postgres;