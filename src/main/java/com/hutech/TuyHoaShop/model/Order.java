package com.hutech.TuyHoaShop.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerName;

    private String phoneNumber;

    private String address;

    private LocalDateTime paymentDateTime;

    private String email;

    private String note;

    private String paymentMethod;

    private String status;

    @OneToMany(mappedBy = "order")
    private List<OrderDetail> orderDetails;

    private String username;
}
