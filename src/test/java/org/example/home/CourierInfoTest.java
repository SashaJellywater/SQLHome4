package org.example.home;

import org.hibernate.Query;
import org.hibernate.Session;
import org.junit.jupiter.api.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CourierInfoTest extends AbstractHomeTest{
        @Test
        @Order(1)
        void getCourier_whenValid_shouldReturn() throws SQLException {
            //given
            String sql = "SELECT * FROM courier_info";
            Statement stmt  = getConnection().createStatement();
            int countTableSize = 0;
            //when
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                countTableSize++;
            }
            final Query query = getSession().createSQLQuery(sql).addEntity(CourierInfoEntity.class);
            //then
            Assertions.assertEquals(4, countTableSize);
            Assertions.assertEquals(4, query.list().size());
        }
        @Test
        @Order(2)
        void addCourier_whenValid_shouldSave() {
            //given
            CourierInfoEntity entity = new CourierInfoEntity();
            entity.setCourierId((short) 5);
            entity.setFirstName("Fran");
            entity.setLastName("Bow");
            entity.setPhoneNumber("9999999999");
            entity.setDeliveryType("car");

            //when
            Session session = getSession();
            session.beginTransaction();
            session.persist(entity);
            session.getTransaction().commit();

            final Query query = getSession()
                    .createSQLQuery("SELECT * FROM courier_info WHERE courier_id="+5).addEntity(CourierInfoEntity.class);
            CourierInfoEntity courierInfoEntity = (CourierInfoEntity) query.uniqueResult();
            //then
            Assertions.assertNotNull(courierInfoEntity);
            Assertions.assertEquals(5, courierInfoEntity.getCourierId());
            Assertions.assertEquals("Fran", courierInfoEntity.getFirstName());
            Assertions.assertEquals("Bow", courierInfoEntity.getLastName());
            Assertions.assertEquals("car", courierInfoEntity.getDeliveryType());
        }

        @Test
        @Order(3)
        void deleteProduct_whenValid_shouldDelete() {
            //given
            final Query query = getSession()
                    .createSQLQuery("SELECT * FROM courier_info WHERE courier_id=" + 5).addEntity(CourierInfoEntity.class);
            Optional<CourierInfoEntity> courierInfoEntity = (Optional<CourierInfoEntity>) query.uniqueResult();
            Assumptions.assumeTrue(courierInfoEntity.isPresent());
            //when
            Session session = getSession();
            session.beginTransaction();
            session.delete(courierInfoEntity.get());
            session.getTransaction().commit();
            //then
            final Query queryAfterDelete = getSession()
                    .createSQLQuery("SELECT * FROM courier_info WHERE courier_id=" + 5).addEntity(CourierInfoEntity.class);
            Optional<CourierInfoEntity> courierInfoEntity1 = (Optional<CourierInfoEntity>) queryAfterDelete.uniqueResult();
            Assertions.assertFalse(courierInfoEntity1.isPresent());
        }
}
