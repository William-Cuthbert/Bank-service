package com.project.service.mapper;

import com.project.dto.transaction.TransactionDtoRequest;
import com.project.dto.transaction.TransactionDtoResponse;
import com.project.repository.entity.Transaction;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);
    TransactionDtoResponse toDto(Transaction transaction);
    Transaction toEntity(TransactionDtoRequest request);
}
