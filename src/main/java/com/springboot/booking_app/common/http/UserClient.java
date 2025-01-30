package com.springboot.booking_app.common.http;

import com.springboot.booking_app.dto.request.CreateUserRequestDTO;
import com.springboot.booking_app.dto.response.BaseCRUDResponseDTO;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service", url = "${application.config.user-host}")
public interface UserClient {
    @PostMapping("user")
    BaseCRUDResponseDTO createUser(@Valid @RequestBody CreateUserRequestDTO bodyDTO);

}
