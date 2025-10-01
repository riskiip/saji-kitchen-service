-- DDL for Saji Kitchen Service
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Table for user roles
CREATE TABLE roles (
    role_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    role_name VARCHAR(50) UNIQUE NOT NULL
);

-- Table for application users
CREATE TABLE users (
    user_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    username VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role_id UUID NOT NULL,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    CONSTRAINT fk_role
        FOREIGN KEY(role_id)
        REFERENCES roles(role_id)
);

-- Table for main products
CREATE TABLE products (
    product_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(255) NOT NULL,
    description TEXT,
    image_url VARCHAR(255),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMPTZ DEFAULT NOW()
);

-- Table for product variations
CREATE TABLE product_variants (
    variant_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    product_id UUID NOT NULL,
    name VARCHAR(100) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    CONSTRAINT fk_product
        FOREIGN KEY(product_id)
        REFERENCES products(product_id)
);

-- Table for toppings
CREATE TABLE toppings (
    topping_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(100) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    image_url VARCHAR(255),
    created_at TIMESTAMPTZ DEFAULT NOW()
);

-- Table for customers
CREATE TABLE customers (
    customer_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    email VARCHAR(255) UNIQUE NOT NULL,
    name VARCHAR(255),
    created_at TIMESTAMPTZ DEFAULT NOW()
);

-- Table for orders/transactions
CREATE TABLE orders (
    order_id VARCHAR(50) PRIMARY KEY,
    customer_id UUID NOT NULL,
    user_id UUID NOT NULL,
    total_amount DECIMAL(10, 2) NOT NULL,
    payment_status VARCHAR(50) NOT NULL,
    order_date TIMESTAMPTZ DEFAULT NOW(),
    payment_confirmed_at TIMESTAMPTZ,
    CONSTRAINT fk_customer
        FOREIGN KEY(customer_id)
        REFERENCES customers(customer_id),
    CONSTRAINT fk_user
        FOREIGN KEY(user_id)
        REFERENCES users(user_id)
);

-- Table for detailed order items
CREATE TABLE order_items (
    order_item_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    order_id VARCHAR(50) NOT NULL,
    variant_id UUID NOT NULL,
    topping_id UUID,
    quantity INT NOT NULL,
    price_at_purchase DECIMAL(10, 2) NOT NULL,
    topping_price_at_purchase DECIMAL(10, 2),
    CONSTRAINT fk_order
        FOREIGN KEY(order_id)
        REFERENCES orders(order_id) ON DELETE CASCADE,
    CONSTRAINT fk_variant
        FOREIGN KEY(variant_id)
        REFERENCES product_variants(variant_id),
    CONSTRAINT fk_topping
        FOREIGN KEY(topping_id)
        REFERENCES toppings(topping_id)
);


-- Table for API response codes
CREATE TABLE response_codes (
                                code VARCHAR(50) PRIMARY KEY,
                                message_en TEXT NOT NULL,
                                message_id TEXT NOT NULL,
                                description TEXT
);

-- Insert Response Codes
INSERT INTO response_codes (code, message_en, message_id, description) VALUES
                                                                           ('SAJI-00-001', 'Success.', 'Berhasil.', 'Generic success response'),
                                                                           ('SAJI-00-400', 'Bad Request.', 'Request Tidak Valid.', 'Generic bad request from client'),
                                                                           ('SAJI-01-404', 'Order not found.', 'Pesanan tidak ditemukan.', 'Used when an order ID does not exist');

-- Insert Roles
INSERT INTO roles (role_name) VALUES ('ADMIN'), ('CASHIER');