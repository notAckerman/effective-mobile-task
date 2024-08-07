package org.example.effectivemobiletask.util;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import java.util.List;

public class CustomModelMapper {

    private static final ModelMapper modelMapper = new ModelMapper();

    public static  <E, D> E toEntity(Class<E> entityClass, D dto) {
        return modelMapper.map(dto, entityClass);
    }

    public static  <E, D> D toDto(Class<D> dtoClass, E entity) {
        return modelMapper.map(entity, dtoClass);
    }

    public static <E, D> List<D> toDtoList(List<E> list, Class<D> dtoClass) {
        return list.stream()
                .map(entity -> toDto(dtoClass, entity))
                .toList();
    }
}
