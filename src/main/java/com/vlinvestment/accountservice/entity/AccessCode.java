package com.vlinvestment.accountservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "access_codes")
public class AccessCode {

    @Id
    @SequenceGenerator(name = "access_codes_generator", sequenceName = "access_codes_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "access_codes_generator")
    private Long id;

    @Column(name = "source", nullable = false, unique = true)
    private String source;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "expiration_time", nullable = false)
    private LocalDateTime expirationTime;

}
