package util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAUtil {
    // Static EntityManagerFactory to create EntityManager instances
    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("primeiroPU");

    // Method to get an EntityManager instance
    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
}
