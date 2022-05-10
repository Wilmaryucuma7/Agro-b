package usco.agrosoft.dao;

import usco.agrosoft.models.Identifier;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class IdentifierDaoImplement implements IdentifierDao {
    @PersistenceContext
    EntityManager entityManager;

    @Override
    @Transactional
    public List<Identifier> getIdentifier() {
        String query = "FROM Identifier u";
        List<Identifier> result = entityManager.createQuery(query).getResultList();
        System.out.println(result.size());
        return result;
    }
}
