package com.switchwon.user.domain;

import com.switchwon.payment.domain.Wallet;
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

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Wallet wallet;

    public User(String userId) {
        this.userId = userId;
    }

    public Wallet getWallet() {
        if (wallet == null) {
            throw new EntityNotFoundException("userBalance가 존재하지 않습니다.");
        }

        return this.wallet;
    }
}
