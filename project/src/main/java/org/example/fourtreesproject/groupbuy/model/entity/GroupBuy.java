package org.example.fourtreesproject.groupbuy.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.fourtreesproject.user.model.entity.User;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "group_buy")
public class GroupBuy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_idx")
    private Category category;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_idx")
    private User user;

    @OneToMany(mappedBy = "groupBuy")
    private List<Likes> likesList;

    //제목
    @Column(nullable = false, length = 100)
    private String gpbuyTitle;
    //내용
    @Column(nullable = false)
    private String gpbuyContent;
    //등록일시
    @Column(nullable = false)
    private LocalDateTime gpbuyRegedAt;
    //입찰마감일시
    private LocalDateTime gpbuyBidEndedAt;
    //공구상태
    @Column(nullable = false,length = 50)
    @Builder.Default
    private String gpbuyStatus = "대기";
    //목표 수량
    @Column(nullable = false)
    private Integer gpbuyQuantity;
    //최종 마감 일시(보류종료 시점)
    private LocalDateTime gpbuyFinEndedAt;


}
