package com.esys.framework.base.repository;

import com.esys.framework.base.entity.Currency;
import com.esys.framework.base.entity.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.Optional;

/**
 * @author Atakan Atik (atakan.atik@everva.com.tr)

 */

@Repository
@Transactional
public interface IProductRepository extends CrudRepository<Product,Long> {

    boolean existsByName(String name);

    Optional<Product> findByName(String name);

}
