package org.example.home;

import org.hibernate.Session;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class OrderTest extends AbstractHomeTest{
        @Test
        void testInsertOrder() {
            try (Session session = getSession()) {
                session.createSQLQuery("INSERT INTO orders (customer_id, date_get) VALUES (1, datetime('now'));").executeUpdate();

                Long orderId = (Long) session.createSQLQuery("SELECT last_insert_rowid();").uniqueResult();
                Assertions.assertNotNull(orderId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Test
        void testSelectOrder() {
            try (Session session = getSession()) {
                session.createSQLQuery("INSERT INTO orders (customer_id, date_get) VALUES (2, datetime('now'));").executeUpdate();

                OrdersEntity order = session.get(OrdersEntity.class, 1);
                Assertions.assertNotNull(order);
                Assertions.assertEquals(2, order.getCustomerId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Test
        void testUpdateOrder() {
            try (Session session = getSession()) {
                session.createSQLQuery("INSERT INTO orders (customer_id, date_get) VALUES (3, datetime('now'));").executeUpdate();

                OrdersEntity order = session.get(OrdersEntity.class, 1);
                Assertions.assertNotNull(order);

                order.setCustomerId(4);
                session.update(order);

                OrdersEntity updatedOrder = session.get(OrdersEntity.class, 1);
                Assertions.assertNotNull(updatedOrder);
                Assertions.assertEquals(4, updatedOrder.getCustomerId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Test
        void testDeleteOrder() {
            try (Session session = getSession()) {
                session.createSQLQuery("INSERT INTO orders (customer_id, date_get) VALUES (5, datetime('now'));").executeUpdate();

                OrdersEntity order = session.get(OrdersEntity.class, 1);
                Assertions.assertNotNull(order);

                session.delete(order);

                OrdersEntity deletedOrder = session.get(OrdersEntity.class, 1);
                Assertions.assertNull(deletedOrder);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
