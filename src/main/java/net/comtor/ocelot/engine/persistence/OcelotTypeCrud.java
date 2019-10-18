package net.comtor.ocelot.engine.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author Guido Cafiel
 */
public interface OcelotTypeCrud<E> extends MDao<E, String> {

    Page<E> findByCodeContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrderByEnabledDescCode(String code, String name, Pageable pageable);

    E findByCode(String code);

}
