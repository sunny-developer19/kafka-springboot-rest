package com.threerivers.bankingapi.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.threerivers.bankingapi.models.Account;

@Repository
public interface AccountRepository extends CrudRepository<Account, String> {

}
