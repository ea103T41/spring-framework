package com.learn.euphy.model.common;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;

import java.io.Serializable;
import java.util.List;

public abstract class AbstractHibernateDao <T extends Serializable>
        extends AbstractDao<T> implements IOperations<T> {

//    @Autowired
//    protected SessionFactory sessionFactory;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public T findById(final long id) {
        return getCurrentSession().get(clazz, id);
    }

    @Override
    public List<T> findAll() {
        return getCurrentSession()
                .createQuery("from " + clazz.getName(), clazz)
                .getResultList();
    }

    @Override
    public void save(final T entity) {
        getCurrentSession().persist(entity);
    }

    @Override
    public T update(final T entity) {
        return getCurrentSession().merge(entity);
    }

    @Override
    public void delete(final T entity) {
        getCurrentSession().remove(entity);
    }

    @Override
    public void deleteById(final long entityId) {
        getCurrentSession()
                .createMutationQuery("delete from " + clazz.getName() + " where id = :id ")
                .setParameter("id", entityId)
                .executeUpdate();
    }

    @Override
    public void deleteAll() {
        getCurrentSession()
                .createQuery("delete from " + clazz.getName(), clazz)
                .executeUpdate();
    }

    /*
     * 通過entityManager.unwrap(Session.class)，可以直接從EntityManager獲取底層的Hibernate Session。
     * 這種方式無需額外配置CurrentSessionContext，因為EntityManager已經被Spring正確配置和管理
     */
    protected Session getCurrentSession() {
//        return sessionFactory.getCurrentSession();
        return entityManager.unwrap(Session.class);
    }

}
