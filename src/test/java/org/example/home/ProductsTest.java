package org.example.home;

import org.hibernate.Query;
import org.hibernate.Session;
import org.junit.jupiter.api.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductsTest extends AbstractHomeTest{
    @Test
    @Order(1)
    void getProduct_whenValid_shouldReturn() throws SQLException {
        //given
        String sql = "SELECT * FROM products";
        Statement stmt  = getConnection().createStatement();
        int countTableSize = 0;
        //when
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            countTableSize++;
        }
        final Query query = getSession().createSQLQuery(sql).addEntity(ProductsEntity.class);
        //then
        Assertions.assertEquals(10, countTableSize);
        Assertions.assertEquals(10, query.list().size());
    }
    @Test
    @Order(2)
    void addProduct_whenValid_shouldSave() {
        //given
        ProductsEntity entity = new ProductsEntity();
        entity.setProductId((short) 11);
        entity.setMenuName("Toppoky");
        entity.setPrice("280");
        //when
        Session session = getSession();
        session.beginTransaction();
        session.persist(entity);
        session.getTransaction().commit();

        final Query query = getSession()
                .createSQLQuery("SELECT * FROM products WHERE product_id="+11).addEntity(ProductsEntity.class);
        ProductsEntity productsEntity = (ProductsEntity) query.uniqueResult();
        //then
        Assertions.assertNotNull(productsEntity);
        Assertions.assertEquals("280", productsEntity.getPrice());
        Assertions.assertEquals("Toppoky", productsEntity.getMenuName());
    }

    @Test
    @Order(3)
    void deleteProduct_whenValid_shouldDelete() {
        //given
        final Query query = getSession()
                .createSQLQuery("SELECT * FROM products WHERE product_id=" + 11).addEntity(ProductsEntity.class);
        Optional<ProductsEntity> productsEntity = (Optional<ProductsEntity>) query.uniqueResult();
        Assumptions.assumeTrue(productsEntity.isPresent());
        //when
        Session session = getSession();
        session.beginTransaction();
        session.delete(productsEntity.get());
        session.getTransaction().commit();
        //then
        final Query queryAfterDelete = getSession()
                .createSQLQuery("SELECT * FROM products WHERE product_id=" + 2).addEntity(ProductsEntity.class);
        Optional<ProductsEntity> productEntityAfterDelete = (Optional<ProductsEntity>) queryAfterDelete.uniqueResult();
        Assertions.assertFalse(productEntityAfterDelete.isPresent());
    }
}
