package com.project.service.mapper;

import com.project.dto.account.AccountDtoResponse;
import com.project.repository.entity.Account;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AccountMapper {
  AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);
  AccountDtoResponse toDto(Account account);
  Account toEntity(AccountDtoResponse accountResponse);
  List<AccountDtoResponse> toListOfDto(List<Account> accounts);
}
