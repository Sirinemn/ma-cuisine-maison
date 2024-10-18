package fr.sirine.starter.mapper;

import java.io.IOException;
import java.util.List;

public interface EntityMapper<D, E> {

    E toEntity(D dto);

    D toDto(E entity) throws IOException;

    List<E> toEntity(List<D> dtoList);

    List<D> toDto(List<E> entityList);
}
