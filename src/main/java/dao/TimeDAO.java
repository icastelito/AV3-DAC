package dao;

import entidades.Time;
import util.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;

public class TimeDAO {

    private EntityManager getEntityManager() {
        return JPAUtil.getEntityManager();
    }

    public void salvar(Time time) {
        EntityManager em = getEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();
            em.merge(time);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public List<Time> listar() {
        EntityManager em = getEntityManager();
        List<Time> times = null;

        try {
            TypedQuery<Time> query = em.createQuery("SELECT t FROM Time t ORDER BY t.pontuacao DESC", Time.class);
            times = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }

        return times;
    }

    public void excluirTodos() {
        EntityManager em = getEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();
            em.createQuery("DELETE FROM Time").executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}
