package org.example.fourtreesproject.product.service;

import lombok.RequiredArgsConstructor;
import org.example.fourtreesproject.groupbuy.repository.CategoryRepository;
import org.example.fourtreesproject.product.model.entity.Product;
import org.example.fourtreesproject.product.model.entity.ProductImg;
import org.example.fourtreesproject.product.model.request.ProductRegisterRequest;
import org.example.fourtreesproject.product.repository.ProductImgRepository;
import org.example.fourtreesproject.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductImgRepository productImgRepository;
    private final CategoryRepository categoryRepository;

    // 상품 등록
    public void register(ProductRegisterRequest request, List<String> imgUrlList){

        Product registerProduct = Product.builder()
                .productName(request.getProductName())
                .productContent(request.getProductContent())
                .category(categoryRepository.findById(request.getCategoryIdx()).get())
                .build();

        productRepository.save(registerProduct);

        for(int i =0; i < imgUrlList.size(); i++){
            ProductImg productImg = ProductImg.builder()
                    .productImgUrl(imgUrlList.get(i))
                    .productImgSequence(i)
                    .product(registerProduct)
                    .build();

            productImgRepository.save(productImg);

        }
    }
}
