CREATE TABLE IF NOT EXISTS products (
    product_id VARCHAR(255) PRIMARY KEY,
    category TEXT,
    brand TEXT
);

CREATE TABLE IF NOT EXISTS shoppers (
    shopper_id VARCHAR(255) PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS shopper_products (
    product_id VARCHAR(255) NOT NULL,
    shopper_id VARCHAR(255) NOT NULL,
    relevancy_score DOUBLE PRECISION NOT NULL,
    PRIMARY KEY (product_id, shopper_id),  -- Composite Primary Key
    FOREIGN KEY (shopper_id) REFERENCES shoppers(shopper_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE
);


-- Indexes
CREATE INDEX IF NOT EXISTS idx_product_id ON shopper_products (product_id);
CREATE INDEX IF NOT EXISTS idx_shopper_id ON shopper_products (shopper_id);
CREATE INDEX IF NOT EXISTS idx_category ON products (category);
CREATE INDEX IF NOT EXISTS idx_brand ON products (brand);
CREATE INDEX IF NOT EXISTS idx_relevancy_score ON shopper_products (shopper_id, relevancy_score DESC);

-- Materialized View for Fast Filtering
CREATE MATERIALIZED VIEW IF NOT EXISTS shopper_product_summary AS
SELECT sp.shopper_id, sp.product_id, p.category, p.brand, sp.relevancy_score
FROM shopper_products sp
JOIN products p ON sp.product_id = p.product_id;
