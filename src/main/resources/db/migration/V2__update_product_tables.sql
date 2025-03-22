ALTER TABLE products ADD created_on timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL;
ALTER TABLE products ADD updated_on timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL;

ALTER TABLE shoppers ADD created_on timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL;

