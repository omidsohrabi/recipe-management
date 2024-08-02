CREATE TABLE IF NOT EXISTS recipe
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    name         VARCHAR(255) NOT NULL,
    vegetarian   BOOLEAN      NOT NULL,
    servings     INT          NOT NULL,
    instructions TEXT
);

-- Create the 'recipe_ingredients' table
CREATE TABLE IF NOT EXISTS recipe_ingredients
(
    recipe_id  BIGINT       NOT NULL,
    ingredients VARCHAR(255) NOT NULL,
    FOREIGN KEY (recipe_id) REFERENCES recipe (id) ON DELETE CASCADE
);

-- Insert sample data into the 'recipe' table
INSERT INTO recipe (name, vegetarian, servings, instructions)
VALUES ('Vegetable Soup', TRUE, 4, 'Chop vegetables and cook in broth until tender.'),
       ('Chicken Curry', FALSE, 2, 'Cook chicken with onions, add curry powder and coconut milk.'),
       ('Spaghetti Bolognese', FALSE, 8, 'Cook beef with onions, add tomato sauce and serve over spaghetti.');

-- Insert sample data into the 'recipe_ingredients' table
INSERT INTO recipe_ingredients (recipe_id, ingredients)
VALUES (1, 'Carrots'),
       (1, 'Potatoes'),
       (1, 'Onions'),
       (1, 'Celery'),
       (2, 'Chicken'),
       (2, 'Onions'),
       (2, 'Curry Powder'),
       (2, 'Coconut Milk'),
       (3, 'Ground Beef'),
       (3, 'Tomato Sauce'),
       (3, 'Spaghetti'),
       (3, 'Onions');