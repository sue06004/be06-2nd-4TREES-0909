package org.example.fourtreesproject.user.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Table(name = "user_detail")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Builder.Default
    private Integer point = 0;

    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    public void updatePoint(Integer usePoint){
        this.point -= usePoint;
    }

}
