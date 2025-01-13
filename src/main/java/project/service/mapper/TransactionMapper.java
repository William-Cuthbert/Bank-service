package project.service.mapper;

import project.dto.transaction.TransactionDtoRequest;
import project.dto.transaction.TransactionDtoResponse;
import project.repository.entity.Transaction;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TransactionMapper {
    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);
    TransactionDtoResponse toDto(Transaction transaction);
    Transaction toEntity(TransactionDtoRequest request);
}
