package com.groupx.simplenote.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.groupx.simplenote.entity.Account;

@Dao
public interface AccountDao {

    @Insert
    void insert(Account account);

    @Query("SELECT * FROM ACCOUNT WHERE ACCOUNT.email == :email")
    Account getAccountByEmail(String email);

    @Query("SELECT * FROM ACCOUNT WHERE ACCOUNT.accountId == :accountId")
    Account getAccountById(int accountId);

    @Update
    void update(Account account);

    @Insert
    void registerAccount(Account accountEntity);

    @Query("select * from Account where email=(:email) and password=(:password)")
    Account login(String email, String password);
}