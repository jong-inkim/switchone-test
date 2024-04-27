package com.switchwon.user.domain;

import com.switchwon.payment.domain.CurrencyCode;
import com.switchwon.payment.domain.UserBalance;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String userId;

    public User(String userId) {
        this.userId = userId;
    }
}
