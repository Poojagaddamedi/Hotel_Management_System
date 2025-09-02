package com.hotel.erp.service;

import com.hotel.erp.dto.AccountHeadDTO;
import java.util.List;

public interface AccountHeadService {
    AccountHeadDTO createAccountHead(AccountHeadDTO accountHeadDTO);

    AccountHeadDTO updateAccountHead(Integer id, AccountHeadDTO accountHeadDTO);

    AccountHeadDTO getAccountHeadById(Integer id);

    AccountHeadDTO getAccountHeadByName(String name);

    List<AccountHeadDTO> getAllAccountHeads();

    void deleteAccountHead(Integer id);
}
