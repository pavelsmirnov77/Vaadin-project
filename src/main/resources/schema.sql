create schema if not exists vaadin_schema;

INSERT INTO category VALUES (1, 'Техника');
INSERT INTO category VALUES (2, 'Спортивный инвентарь');
INSERT INTO category VALUES (3, 'Еда');

INSERT INTO product VALUES (1, 'Стиральная машина', 10000, 100, 1);
INSERT INTO product VALUES (2, 'Холодильник', 8000, 120, 1);
INSERT INTO product VALUES (3, 'Пылесос', 5000, 50, 1);

INSERT INTO product VALUES (4, 'Футбольный мяч', 500, 100, 2);
INSERT INTO product VALUES (5, 'Ракетка', 300, 100, 2);
INSERT INTO product VALUES (6, 'Баскетбольный мяч', 400, 300, 2);

INSERT INTO product VALUES (7, 'Сэндвичи', 200, 100, 3);
INSERT INTO product VALUES (8, 'Бургеры', 320, 100, 3);
INSERT INTO product VALUES (9, 'Салат оливье', 180, 300, 3);

INSERT INTO supplier VALUES (1, 'СпортДом');
INSERT INTO supplier VALUES (2, 'Дядя Ваня');
INSERT INTO supplier VALUES (3, 'Веселый молочник');
INSERT INTO supplier VALUES (4, 'Бургер Кинг');
INSERT INTO supplier VALUES (5, 'Эльдорадо');

INSERT INTO product_supplier VALUES (1, 5);
INSERT INTO product_supplier VALUES (2, 5);
INSERT INTO product_supplier VALUES (3, 5);
INSERT INTO product_supplier VALUES (4, 1);
INSERT INTO product_supplier VALUES (5, 1);
INSERT INTO product_supplier VALUES (6, 1);
INSERT INTO product_supplier VALUES (8, 4);
