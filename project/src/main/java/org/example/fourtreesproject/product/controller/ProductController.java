package org.example.fourtreesproject.product.controller;

import lombok.RequiredArgsConstructor;
import org.example.fourtreesproject.common.BaseResponse;
import org.example.fourtreesproject.file.FileUploadService;
import org.example.fourtreesproject.product.model.request.ProductRegisterRequest;
import org.example.fourtreesproject.product.service.ProductService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final FileUploadService fileUploadService;

    @RequestMapping(method = RequestMethod.POST, value = "/register")
    public BaseResponse<String> register(@RequestPart ProductRegisterRequest productInfo, @RequestPart MultipartFile[] images) {
        List<String> ImgUrlList = fileUploadService.upload(images); //경로를 productImgList 반환하고
        productService.register(productInfo, ImgUrlList);
        return new BaseResponse<>();
    }
}
