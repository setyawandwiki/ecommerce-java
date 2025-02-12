package com.stwn.ecommerce_java.controller;

import com.stwn.ecommerce_java.model.UserAddressRequest;
import com.stwn.ecommerce_java.model.UserAddressResponse;
import com.stwn.ecommerce_java.model.UserInfo;
import com.stwn.ecommerce_java.service.UserAddressService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("address")
@SecurityRequirement(name = "Bearer")
@RequiredArgsConstructor
public class AddressController {
    private final UserAddressService userAddressService;
    @PostMapping
    public ResponseEntity<UserAddressResponse> create(@Valid @RequestBody UserAddressRequest request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfo userInfo = (UserInfo) authentication.getPrincipal();

        UserAddressResponse response = userAddressService.create(userInfo.getUser().getUserId(), request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<UserAddressResponse>> findAddressByUserId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfo userInfo = (UserInfo) authentication.getPrincipal();

        List<UserAddressResponse> addressResponses = userAddressService.findByUserId(userInfo.getUser().getUserId());
        return ResponseEntity.ok(addressResponses);
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<UserAddressResponse> update(@Valid @RequestBody UserAddressRequest request,
                                                      @PathVariable Long addressId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfo userInfo = (UserInfo) authentication.getPrincipal();

            UserAddressResponse response = userAddressService.update(userInfo.getUser().getUserId(), request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{addressId}")
    public ResponseEntity<UserAddressResponse> get(@PathVariable Long addressId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfo userInfo = (UserInfo) authentication.getPrincipal();


        UserAddressResponse userAddressResponse = userAddressService.findById(addressId);
        userAddressService.delete(addressId);
        return ResponseEntity.ok(userAddressResponse);
    }

    @PutMapping("/{addressId}/set-default")
    public ResponseEntity<UserAddressResponse> setDefaultAddress(@PathVariable Long addressId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfo userInfo = (UserInfo) authentication.getPrincipal();

        UserAddressResponse response = userAddressService.setDefaultAddress(userInfo.getUser().getUserId(), addressId);
        return ResponseEntity.ok(response);
    }
}
