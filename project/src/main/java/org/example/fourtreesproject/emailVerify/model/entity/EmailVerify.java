package org.example.fourtreesproject.emailVerify.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(name="email_verify")
@AllArgsConstructor
@Getter
@Builder
public class EmailVerify {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String uuid;

    public void updateEmailVerify(String uuid){
        this.uuid = uuid;
    }
}
