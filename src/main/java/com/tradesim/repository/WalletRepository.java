package com.tradesim.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tradesim.model.UserWallet;

public interface WalletRepository extends JpaRepository<UserWallet, UUID> {
}