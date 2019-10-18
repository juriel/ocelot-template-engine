package net.comtor.ocelot.engine.persistence;

import java.io.Serializable;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 *
 * @author Guido A. Cafiel Vellojin
 */
@NoRepositoryBean
public interface MDao<E, ID extends Serializable> extends JpaRepository<E, ID>, JpaSpecificationExecutor<E> {

    @Override
    void delete(E deleted);

    @Override
    List<E> findAll();

    @Override
    E getOne(ID id);

    @Override
    boolean existsById(ID primaryKey);

}
