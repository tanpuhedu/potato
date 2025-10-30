package com.ktpm.potatoapi.rating.entity;

import com.ktpm.potatoapi.merchant.entity.Merchant;
import com.ktpm.potatoapi.order.entity.Order;
import com.ktpm.potatoapi.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    Merchant merchant;

    @ManyToOne
    User customer;

    @ManyToOne
    @JoinColumn(unique = true)
    Order order;

    Integer rating;
}
