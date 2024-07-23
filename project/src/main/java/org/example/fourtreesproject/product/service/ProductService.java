package org.example.fourtreesproject.product.service;

import lombok.RequiredArgsConstructor;
import org.example.fourtreesproject.company.model.entity.Company;
import org.example.fourtreesproject.company.repository.CompanyRepository;
import org.example.fourtreesproject.exception.custom.InvalidCompanyException;
import org.example.fourtreesproject.groupbuy.model.entity.Category;
import org.example.fourtreesproject.groupbuy.repository.CategoryRepository;
import org.example.fourtreesproject.product.model.entity.Product;
import org.example.fourtreesproject.product.model.entity.ProductImg;
import org.example.fourtreesproject.product.model.request.ProductRegisterRequest;
import org.example.fourtreesproject.product.model.response.ProductCategoryResponse;
import org.example.fourtreesproject.product.model.response.ProductImgResponse;
import org.example.fourtreesproject.product.model.response.ProductMylistResponse;
import org.example.fourtreesproject.product.model.response.ProductRegisterResponse;
import org.example.fourtreesproject.product.repository.ProductImgRepository;
import org.example.fourtreesproject.product.repository.ProductRepository;
import org.example.fourtreesproject.user.model.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.example.fourtreesproject.common.BaseResponseStatus.COMPANY_REGIST_DETAIL_FAIL;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductImgRepository productImgRepository;
    private final CategoryRepository categoryRepository;
    private final CompanyRepository companyRepository;

    // 상품 등록
    @Transactional
    public ProductRegisterResponse register(User user, ProductRegisterRequest request, List<String> imgUrlList) {

        Product registerProduct = Product.builder()
                .company(companyRepository.findByUserIdx(user.getIdx()).get())
                .productName(request.getProductName())
                .productContent(request.getProductContent())
                .category(categoryRepository.findById(request.getCategoryIdx()).get())
                .build();

        Product product = productRepository.save(registerProduct);
        // 상품 이미지 등록
        for (int i = 0; i < imgUrlList.size(); i++) {
            ProductImg productImg = ProductImg.builder()
                    .productImgUrl(imgUrlList.get(i))
                    .productImgSequence(i)
                    .product(registerProduct)
                    .build();

            productImgRepository.save(productImg);
        }

        return ProductRegisterResponse.builder()
                .productIdx(product.getIdx())
                .build();
    }

    // 상품 전체 조회
    public List<ProductMylistResponse> mylist(User user) {
        Company company = companyRepository.findByUserIdx(user.getIdx()).orElseThrow(() -> new InvalidCompanyException(COMPANY_REGIST_DETAIL_FAIL));
        List<Product> productList = productRepository.findByCompanyIdx(company.getIdx());
        List<ProductMylistResponse> responseList = new ArrayList<>();

        for (int i = 0; i < productList.size(); i++) {
            Product product = productList.get(i);
            List<ProductImg> productImgList = product.getProductImgList();
            System.out.println(productImgList.get(0).getProductImgUrl());
            List<ProductImgResponse> productImgResponseList = new ArrayList<>(); //한 상품의 이미지들 넣음
            for (ProductImg productImg : productImgList) {
                ProductImgResponse productImgResponse = ProductImgResponse.builder()
                        .idx(productImg.getIdx())
                        .productImgSequence(productImg.getProductImgSequence())
                        .productImgUrl(productImg.getProductImgUrl())
                        .build();
                productImgResponseList.add(productImgResponse);
            }
            // 카테고리를 DTO에 담아 반환
            Category category = product.getCategory();
            ProductCategoryResponse productCategoryResponse = ProductCategoryResponse.builder()
                    .idx(category.getIdx())
                    .categoryName(category.getCategoryName())
                    .build();

            ProductMylistResponse mylistResponse = ProductMylistResponse.builder()
                    .productIdx(product.getIdx())
                    .productName(productList.get(i).getProductName())
                    .productContent(productList.get(i).getProductContent())
                    .category(productCategoryResponse)
                    .productImgList(productImgResponseList)
                    .build();

            responseList.add(mylistResponse);
        }
        return responseList;
    }
}
