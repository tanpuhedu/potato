package com.ktpm.potatoapi.order.entity;

import com.ktpm.potatoapi.merchant.entity.Merchant;
import com.ktpm.potatoapi.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "`order`")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    User customer;

    @ManyToOne
    Merchant merchant;

    String fullName;
    String phone;
    String note;
    String deliveryAddress;
    Long deliveryFee;
    Long totalAmount;
    String cancelReason;

    @Enumerated(EnumType.STRING)
    OrderStatus status;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    List<OrderItem> orderItems;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = null;
        this.status = OrderStatus.CONFIRMED;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
