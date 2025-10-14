package com.ktpm.potatoapi.option.entity;

import com.ktpm.potatoapi.merchant.entity.Merchant;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Table(name = "`option`", uniqueConstraints = {@UniqueConstraint(columnNames = {"merchant_id", "name"})})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Option {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;
    boolean isRequired;
    boolean isVisible;
    boolean isActive;

    @ManyToOne
    Merchant merchant;

    @OneToMany(mappedBy = "option", cascade = CascadeType.ALL, orphanRemoval = true)
    List<OptionValue> optionValues;

    @PrePersist
    protected void onCreate() {
        this.isActive = true;
        this.isVisible = true;
    }
}
