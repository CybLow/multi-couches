package fr.esaip.petstore.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaQuery;

import java.util.List;

/**
 * Base abstraite d'un DAO CRUD. Une sous-classe par agrégat.
 *
 * <p>Le DAO ne gère PAS les transactions : c'est la responsabilité de
 * l'appelant (service ou Main). Il reçoit un {@link EntityManager} via
 * constructeur, ce qui permet à la fois de partager un EM pour une unité
 * de travail et d'injecter facilement un mock en test.</p>
 *
 * @param <T>  type de l'entité persistée
 * @param <ID> type de sa clé primaire
 */
public abstract class GenericDao<T, ID> {

    protected final EntityManager em;
    protected final Class<T> entityClass;

    protected GenericDao(EntityManager em, Class<T> entityClass) {
        this.em = em;
        this.entityClass = entityClass;
    }

    /** Persiste une entité nouvelle. */
    public void persist(T entity) {
        em.persist(entity);
    }

    /** Récupère une entité par sa clé primaire. {@code null} si absente. */
    public T find(ID id) {
        return em.find(entityClass, id);
    }

    /** Récupère toutes les entités du type. Pratique pour des TPs / petites tables. */
    public List<T> findAll() {
        CriteriaQuery<T> cq = em.getCriteriaBuilder().createQuery(entityClass);
        cq.select(cq.from(entityClass));
        return em.createQuery(cq).getResultList();
    }

    /** Retourne l'entité fusionnée (état persistant). */
    public T merge(T entity) {
        return em.merge(entity);
    }

    /** Supprime l'entité fournie. */
    public void remove(T entity) {
        em.remove(em.contains(entity) ? entity : em.merge(entity));
    }
}
