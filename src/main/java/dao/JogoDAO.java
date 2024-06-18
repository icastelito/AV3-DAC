package dao;

import entidades.Jogo;
import util.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;

public class JogoDAO {

    private EntityManager getEntityManager() {
        return JPAUtil.getEntityManager();
    }

    public void salvar(Jogo jogo) {
        EntityManager em = getEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();
            if (jogo.getId() == null) {
                em.persist(jogo);
            } else {
                em.merge(jogo);
            }
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

    public List<Jogo> listar() {
        EntityManager em = getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        List<Jogo> jogos = null;

        try {
            transaction.begin();
            TypedQuery<Jogo> query = em.createQuery("SELECT DISTINCT j FROM Jogo j LEFT JOIN FETCH j.campeonato", Jogo.class);
            jogos = query.getResultList();
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }

        return jogos;
    }

    public void editar(Jogo jogo) {
        EntityManager em = getEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();
            em.merge(jogo);
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

    public void excluir(Long id) {
        EntityManager em = getEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();
            Jogo jogo = em.find(Jogo.class, id);
            if (jogo != null) {
                em.remove(jogo);
            }
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
