package dao;

import entidades.Campeonato;
import util.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;

public class CampeonatoDAO {

    public void salvar(Campeonato campeonato) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            if (campeonato.getId() == null) {
                em.persist(campeonato);
            } else {
                em.merge(campeonato);
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

    public List<Campeonato> listar() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Campeonato> query = em.createQuery("SELECT c FROM Campeonato c", Campeonato.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public Campeonato buscarPorNome(String nome) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Campeonato> query = em.createQuery("SELECT c FROM Campeonato c WHERE c.nome = :nome", Campeonato.class);
            query.setParameter("nome", nome);
            List<Campeonato> resultados = query.getResultList();
            if (resultados.isEmpty()) {
                return null;
            } else {
                return resultados.get(0);
            }
        } finally {
            em.close();
        }
    }
    
    public Campeonato buscarPorId(Integer id) {
    	EntityManager em = JPAUtil.getEntityManager();
    	 try {
             TypedQuery<Campeonato> query = em.createQuery("SELECT c FROM Campeonato c WHERE c.id = :id", Campeonato.class);
             query.setParameter("id", id);
             List<Campeonato> resultados = query.getResultList();
             if (resultados.isEmpty()) {
                 return null;
             } else {
                 return resultados.get(0);
             }
         } finally {
             em.close();
         }
    	
    }
}


