package com.esys.framework.core.service;

import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

@Slf4j
public class PersistenceService {
    private static final EntityManagerFactory emf;
    private static final ThreadLocal<EntityManager> threadLocal;

    static
    {
        emf = Persistence.createEntityManagerFactory("PersistenceUnit");
        threadLocal = new ThreadLocal<EntityManager>();
    }

    public static EntityManager getEntityManager()
    {
        EntityManager em = threadLocal.get();

        if (em == null)
        {
            em = emf.createEntityManager();
            threadLocal.set(em);
        }
        return em;
    }

    public static <T>T get(Class<T> clazz, Object id)
    {
        return getEntityManager().find(clazz, id);
    }

    public static void save(Object object)
    {
        EntityManager em = getEntityManager();
        EntityTransaction et = em.getTransaction();
        et.begin();
        try
        {
            em.persist(object);
            et.commit();
        }
        catch (Exception e)
        {
            et.rollback();
            throw new RuntimeException("Error saving object", e);
        }
    }
}