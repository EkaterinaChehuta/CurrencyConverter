CREATE TABLE IF NOT EXISTS currency
(
    id SERIAL PRIMARY KEY,
    num_code VARCHAR(10) NOT NULL,
    char_code VARCHAR(10) NOT NULL,
    nominal INT NOT NULL,
    name_currency VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS currency_values
(
    id SERIAL PRIMARY KEY,
    rub_value NUMERIC(1000, 4) NOT NULL,
    value_date DATE NOT NULL,
    currency_id INT NOT NULL
);

CREATE TABLE IF NOT EXISTS conversion
(
    id SERIAL PRIMARY KEY,
    currency_from_id INT NOT NULL,
    currency_to_id INT NOT NULL,
    value_date DATE NOT NULL,
    amount INT NOT NULL,
    result NUMERIC(1000, 4) NOT NULL
);

CREATE TABLE IF NOT EXISTS history
(
    id SERIAL PRIMARY KEY,
    conversion_id INT NOT NULL,
    currency_values_from_id INT NOT NULL,
    currency_values_to_id INT NOT NULL
);