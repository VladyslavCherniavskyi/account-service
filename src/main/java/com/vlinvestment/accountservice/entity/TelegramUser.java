package com.vlinvestment.accountservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "telegram_users")
public class TelegramUser {

    @Id
    private Long chatId;

    @Column(name = "phone", nullable = false, length = 15, unique = true)
    private String phone;

}
