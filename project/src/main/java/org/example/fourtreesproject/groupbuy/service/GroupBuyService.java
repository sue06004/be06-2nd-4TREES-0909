package org.example.fourtreesproject.groupbuy.service;


import lombok.RequiredArgsConstructor;
import org.example.fourtreesproject.groupbuy.model.entity.Category;
import org.example.fourtreesproject.groupbuy.model.entity.GroupBuy;
import org.example.fourtreesproject.groupbuy.model.request.GroupBuyCreateRequest;
import org.example.fourtreesproject.groupbuy.repository.CategoryRepository;
import org.example.fourtreesproject.groupbuy.repository.GroupBuyRepository;
import org.example.fourtreesproject.user.model.entity.User;
import org.example.fourtreesproject.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class GroupBuyService {

    private final GroupBuyRepository gpbuyRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public boolean save(GroupBuyCreateRequest request) {
        User user = userRepository.findById(request.getUserIdx()).get();
        Category category = categoryRepository.findByCategoryName(request.getCategory());
        GroupBuy groupbuy = GroupBuy.builder()
                .user(user)
                .category(category)
                .gpbuyTitle(request.getTitle())
                .gpbuyQuantity(request.getGpbuyQuantity())
                .gpbuyContent(request.getContent())
                .gpbuyRegedAt(LocalDateTime.now())
                .gpbuyBidEndedAt(LocalDateTime.now().plusDays(2))
                .build();

        GroupBuy res = gpbuyRepository.save(groupbuy);
        if (res == null){
            return false;
        }

        return true;

    }
}
