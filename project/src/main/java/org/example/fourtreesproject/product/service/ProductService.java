package org.example.fourtreesproject.product.service;

import lombok.RequiredArgsConstructor;
import org.example.fourtreesproject.company.repository.CompanyRepository;
import org.example.fourtreesproject.groupbuy.repository.CategoryRepository;
import org.example.fourtreesproject.product.model.entity.Product;
import org.example.fourtreesproject.product.model.entity.ProductImg;
import org.example.fourtreesproject.product.model.request.ProductRegisterRequest;
import org.example.fourtreesproject.product.repository.ProductImgRepository;
import org.example.fourtreesproject.product.repository.ProductRepository;
import org.example.fourtreesproject.user.model.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductImgRepository productImgRepository;
    private final CategoryRepository categoryRepository;
    private final CompanyRepository companyRepository;

    // 상품 등록
    public void register(User user, ProductRegisterRequest request, List<String> imgUrlList) {

        Product registerProduct = Product.builder()
                .company(companyRepository.findByUserIdx(user.getIdx()).get())
                .productName(request.getProductName())
                .productContent(request.getProductContent())
                .category(categoryRepository.findById(request.getCategoryIdx()).get())
                .build();

        productRepository.save(registerProduct);
        // 상품 이미지 등록
        for (int i = 0; i < imgUrlList.size(); i++) {
            ProductImg productImg = ProductImg.builder()
                    .productImgUrl(imgUrlList.get(i))
                    .productImgSequence(i)
                    .product(registerProduct)
                    .build();

            productImgRepository.save(productImg);

        }
    }
}
