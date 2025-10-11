package com.ktpm.potatoapi.merchant.entity;

import com.ktpm.potatoapi.auth.entity.User;
import com.ktpm.potatoapi.cuisinetype.entity.CuisineType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Merchant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(unique = true)
    String name;

    String introduction;
    String address;

    @JdbcTypeCode(SqlTypes.JSON) // map JSON từ db sang entity.
    @Column(columnDefinition = "json")
    Map<String, String> openingHours;

    @Column(precision = 2, scale = 1) // Decimal(2,1) in sql
    BigDecimal avgRating;

    int ratingCount;
    boolean isActive;
    boolean isOpen;

    String imgUrl;

    @OneToOne
    User merchantAdmin;

    @ManyToMany
    Set<CuisineType> cuisineTypes;

    @PrePersist
    protected void onCreate() {
        this.avgRating = BigDecimal.ZERO;
        this.ratingCount = 0;
        this.isActive = true;
        this.isOpen = true;
    }
}