package com.tradesim.model;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class UserWallet {
  @Id
  private UUID userId;
  private double balance = 100000;

  public UUID getUserId() { return userId; }
  public void setUserId(UUID userId) { this.userId = userId; }
  public double getBalance() { return balance; }
  public void setBalance(double balance) { this.balance = balance; }
}