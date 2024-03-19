package com.vlinvestment.accountservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
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

    @CreatedDate
    @Column(name = "created_time", nullable = false)
    private LocalDateTime createdTime;

}
