package org.example.fourtreesproject.groupbuy.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.fourtreesproject.bid.model.entity.Bid;
import org.example.fourtreesproject.orders.model.entity.Orders;
import org.example.fourtreesproject.user.model.entity.User;
import org.hibernate.annotations.BatchSize;

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
    @BatchSize(size = 10)
    private List<Likes> likesList;

    @OneToMany(mappedBy = "groupBuy")
    @BatchSize(size = 20)
    private List<Bid> bidList;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "groupBuy")
    private List<Orders> ordersList;

    //제목
    @Column(nullable = false, length = 100)
    private String gpbuyTitle;
    //내용
    @Column(nullable = false)
    private String gpbuyContent;

    //등록일시
    @Column(nullable = false)
    private LocalDateTime gpbuyRegedAt;
    //최종 마감 일시(보류종료 시점)
    private LocalDateTime gpbuyFinEndedAt;
    //입찰마감일시
    private LocalDateTime gpbuyBidEndedAt;
    //공구 시작 일시
    private LocalDateTime gpbuyStartedAt;
    //공구 마감 일시
    private LocalDateTime gpbuyEndedAt;

    //공구상태
    @Column(nullable = false,length = 50)
    @Builder.Default
    private String gpbuyStatus = "대기";
    //목표 수량
    @Column(nullable = false)
    private Integer gpbuyQuantity;
    //남은 수량
    @Column(nullable = false)
    private Integer gpbuyRemainQuantity;



    public void updateStatus(String status){
        this.gpbuyStatus = status;
    }
    public void updateStartedAt(LocalDateTime time){
        this.gpbuyStartedAt = time;
    }

    public void updateRemainQuantity(Integer orderQuantity){
        this.gpbuyRemainQuantity -= orderQuantity;
    }

    public void cancleRemainQuantity(Integer orderQuantity){
        this.gpbuyRemainQuantity += orderQuantity;
    }

    public void startGroupBuy(Integer deadline){
        this.gpbuyStatus = "진행";
        this.gpbuyStartedAt = LocalDateTime.now();
        this.gpbuyEndedAt = LocalDateTime.now().plusDays(deadline);
        this.gpbuyFinEndedAt = this.gpbuyEndedAt.plusDays(1);
    }


}
