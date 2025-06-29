package com.jc4balos.user_service.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.jc4balos.user_service.values.UserSex;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users", indexes = {
        @Index(name = "username_index", columnList = "username"),
        @Index(name = "email_index", columnList = "email"),
        @Index(name = "uuid_index", columnList = "user_uuid")
})
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "user_id")
    private Long userId;

    @Column(nullable = false, name = "user_uuid", unique = true, length = 36)
    private String userUUID;

    @Column(nullable = false, length = 256, name = "first_name")
    private String firstName;

    @Column(nullable = true, length = 256, name = "mother_surname")
    private String motherSurname;

    @Column(nullable = false, length = 256, name = "father_surname")
    private String fatherSurname;

    @Column(nullable = true, length = 256, name = "husband_surname")
    private String husbandSurname;

    @Column(nullable = false, unique = true, length = 256, name = "username")
    private String username;

    @Column(nullable = false, length = 256, name = "password")
    private String password;

    @Column(nullable = false, unique = true, length = 256, name = "email")
    private String email;

    @Column(nullable = true, length = 256, name = "address_line_1")
    private String addressLine1;

    @Column(nullable = true, length = 256, name = "address_line_2")
    private String addressLine2;

    @Column(nullable = true, length = 256, name = "address_line_3")
    private String addressLine3;

    @Column(nullable = false, length = 16, name = "contact_number")
    private String contactNumber;

    @Column(nullable = false, name = "birth_date")
    private LocalDate birthDate;

    @Column(nullable = false, name = "sex")
    @Enumerated(EnumType.STRING)
    private UserSex sex;

    @Column(nullable = false, name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(nullable = false, name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(nullable = false, name = "is_active")
    private Boolean isActive;

    @PrePersist
    public void generateUUID() {
        this.userUUID = UUID.randomUUID().toString();
    }
}
