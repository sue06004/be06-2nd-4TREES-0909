package org.example.fourtreesproject.groupbuy.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.fourtreesproject.user.model.entity.User;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Table(name = "likes")
public class Likes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @ManyToOne
    @JoinColumn(name = "gpbuy_idx")
    private GroupBuy groupBuy;

    @ManyToOne
    @JoinColumn(name = "user_idx")
    private User user;
}
