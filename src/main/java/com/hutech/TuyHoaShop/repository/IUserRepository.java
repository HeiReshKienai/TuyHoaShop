package com.hutech.TuyHoaShop.repository;

import com.hutech.TuyHoaShop.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface IUserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.email = :email, u.fullName = :fullName, u.address = :address, u.phone = :phone WHERE u.username = :username")
    void updateUserByUsername(String username, String email, String fullName, String address, String phone);

    @Query("SELECT COUNT(a) FROM User a")
    Long getTotalAccounts();

    @Query("SELECT COUNT(DISTINCT o.username) FROM Order o")
    Long getAccountsWithPurchase();

    @Query("SELECT SUM(o.productPrice * o.quantity) FROM OrderDetail o")
    Long getTotalSalesAmount();

    @Query("SELECT COUNT(o) FROM Order o")
    Long getTotalOrders();

    Optional<User> findByEmail(String email);

    Optional<User> findByPhone(String phone);

}