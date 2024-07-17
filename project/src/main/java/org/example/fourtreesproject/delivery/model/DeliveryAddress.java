package org.example.fourtreesproject.delivery.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.fourtreesproject.user.model.entity.User;

@Entity
@Builder
@Table(name="delivery_address")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class DeliveryAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;
    @Column(length = 100, nullable = false)
    private String addressName;
    @Column(nullable = false)
    private String addressInfo;
    @Column(nullable = false)
    private Boolean addressDefault;
    @Column(nullable = false)
    private Integer postCode;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public void updateDefault(){
        this.addressDefault = !this.addressDefault;
    }

}
