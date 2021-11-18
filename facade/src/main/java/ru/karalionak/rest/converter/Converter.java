package ru.karalionak.rest.converter;

import java.util.Collection;
import java.util.stream.Collectors;

public interface Converter<DTO, MODEL> {
    DTO convertToDto(MODEL obj);

    MODEL convertToModel(DTO obj);

    default Collection<DTO> convertAllToDto(Collection<MODEL> models){
        return models.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    default Collection<MODEL> convertAllToModel(Collection<DTO> models){
        return models.stream()
                .map(this::convertToModel)
                .collect(Collectors.toList());
    }
}
