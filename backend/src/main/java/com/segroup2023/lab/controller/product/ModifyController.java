package com.segroup2023.lab.controller.product;

import com.segroup2023.lab.database.entity.ProductInfo;
import com.segroup2023.lab.database.entity.User;
import com.segroup2023.lab.exception.type.BadRequestException;
import com.segroup2023.lab.exception.type.LoginFailureException;
import com.segroup2023.lab.exception.type.PermissionDeniedException;
import com.segroup2023.lab.service.ProductService;
import com.segroup2023.lab.service.ShopService;
import com.segroup2023.lab.service.UserAuthorization;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/product/modify")
public class ModifyController {
    @Data
    private static class ApplicationRequest {
        @NotNull
        @Setter
        public String username;
        @NotNull
        @Setter
        public Long user_id;
        @NotNull
        @Setter
        public Long product_id;
        @NotNull
        @Setter
        public Double price;
        @NotNull
        @Setter
        public String name, description;
        @NotNull
        @Setter
        public MultipartFile[] images;
    }

    private static class PageRequest {
        @Valid
        @NotNull
        public UserAuthorization.Request authorize;
        @NotNull
        public Integer page_num;
    }

    private static class ApprovalRequest {
        @Valid
        @NotNull
        public UserAuthorization.Request authorize;
        @NotNull
        public Long info_id;
        @NotNull
        public Boolean approved;
    }

    private static class PageResponse {
        public int total_pages;
        public List<ProductInfo> products;
    }

    @PostMapping("")
    public void application(@Valid @NotNull ApplicationRequest request)
        throws LoginFailureException, BadRequestException {
        UserAuthorization.Request authorize = new UserAuthorization.Request();
        authorize.setId(request.user_id);
        authorize.setUsername(request.username);
        User user = UserAuthorization.authorize(authorize);
        if(!ShopService.belongTo(user.getId(), ProductService.getShopId(request.product_id)))
            throw new BadRequestException("Product does not belong to user.");
        ProductService.modify(request.product_id, request.name, request.description, request.price, request.images);
    }

    @PostMapping("/review")
    public PageResponse review(@RequestBody @Valid @NotNull PageRequest request)
        throws LoginFailureException, PermissionDeniedException {
        User user = UserAuthorization.authorize(request.authorize);
        if(!user.getIdentity().equals(User.Identity.ADMIN))
            throw new PermissionDeniedException(user.getIdentity(), User.Identity.ADMIN);
        Page<ProductInfo> productPage = ProductService.modifyReview(request.page_num - 1);
        PageResponse pageResponse = new PageResponse();
        pageResponse.total_pages = productPage.getTotalPages();
        pageResponse.products = productPage.toList();
        return pageResponse;
    }

    @PostMapping("/approval")
    public void approve(@RequestBody @Valid @NotNull ApprovalRequest request)
        throws LoginFailureException, PermissionDeniedException {
        User user = UserAuthorization.authorize(request.authorize);
        if(!user.getIdentity().equals(User.Identity.ADMIN))
            throw new PermissionDeniedException(user.getIdentity(), User.Identity.ADMIN);
        ProductService.modifyApproval(request.info_id, request.approved);
    }

}
