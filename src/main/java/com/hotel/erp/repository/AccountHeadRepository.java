package com.hotel.erp.repository;

import com.hotel.erp.entity.AccountHead;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AccountHeadRepository extends JpaRepository<AccountHead, Integer> {
    Optional<AccountHead> findByAccountHeadName(String name);

    boolean existsByAccountHeadName(String name);
}
