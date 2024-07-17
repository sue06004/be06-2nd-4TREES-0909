package org.example.fourtreesproject.groupbuy.service;


import lombok.RequiredArgsConstructor;
import org.example.fourtreesproject.groupbuy.model.entity.Category;
import org.example.fourtreesproject.groupbuy.model.entity.Groupbuy;
import org.example.fourtreesproject.groupbuy.model.request.GroupBuyCreateRequest;
import org.example.fourtreesproject.groupbuy.repository.CategoryRepository;
import org.example.fourtreesproject.groupbuy.repository.GroupBuyRepository;
import org.example.fourtreesproject.user.model.entity.User;
import org.example.fourtreesproject.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupBuyService {

    private final GroupBuyRepository gpbuyRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final

    public boolean save(GroupBuyCreateRequest request) {
        User user = userRepository.findById(request.getUserIdx()).get();
        Category category = categoryRepository.findByCategoryName(request.getCategory());
        Groupbuy groupbuy = Groupbuy.builder()
                .user(user)
                .category(category)
                .
                .build();

    }
}
