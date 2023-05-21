package com.segroup2023.lab.controller;

import com.segroup2023.lab.database.entity.Address;
import com.segroup2023.lab.database.entity.User;
import com.segroup2023.lab.exception.type.LoginFailureException;
import com.segroup2023.lab.service.AddressService;
import com.segroup2023.lab.service.UserAuthorization;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AddressController {
    private static class CreateRequest {
        @NotNull @Valid
        public UserAuthorization.Request authorize;
        @NotNull
        public String name, phone, address;
    }

    @PostMapping("/address/create")
    public void createAddress(@RequestBody @Valid @NotNull CreateRequest createRequest) throws LoginFailureException {
        User user = UserAuthorization.authorize(createRequest.authorize);
        AddressService.createAddress(user.getId(), createRequest.name, createRequest.phone, createRequest.address);
    }

    private static class QueryRequest {
        @NotNull @Valid
        public UserAuthorization.Request authorize;
    }

    private static class QueryResponse {
        public List<Address> items;
    }

    @PostMapping("/address")
    public QueryResponse getAddresses(@RequestBody @Valid @NotNull QueryRequest request) throws LoginFailureException {
        User user = UserAuthorization.authorize(request.authorize);
        QueryResponse response = new QueryResponse();
        response.items = AddressService.getAddresses(user.getId());
        return response;
    }

    private static class ModifyRequest {
        @NotNull @Valid
        public UserAuthorization.Request authorize;
        @NotNull
        public Long id;
        @NotNull
        public String name, phone, address;
    }

    @PostMapping("/address/modify")
    public void modifyAddress(@RequestBody @Valid @NotNull ModifyRequest request)
        throws LoginFailureException {
        User user = UserAuthorization.authorize(request.authorize);
        AddressService.modifyAddress(request.id, request.name, request.phone, request.address);
    }

    private static class DeleteRequest {
        @NotNull @Valid
        public UserAuthorization.Request authorize;
        @NotNull
        public Long id;
    }

    @PostMapping("/address/delete")
    public void deleteAddress(@RequestBody @Valid @NotNull DeleteRequest request) throws LoginFailureException{
        User user = UserAuthorization.authorize(request.authorize);
        AddressService.deleteAddress(request.id);
    }

}
