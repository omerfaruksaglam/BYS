package com.baskent.Nursapp.Auth.Controller;


import com.baskent.Nursapp.Auth.Models.*;
import com.baskent.Nursapp.Auth.Service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.baskent.Nursapp.Auth.Models.GetById;

@RestController
@RequestMapping("/nursapp/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request){
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponseWithInfos> register(@RequestBody AuthenticationRequest request){

        return ResponseEntity.ok(service.authenticate(request));
    }


    @PostMapping("/getbolmfacuni")
    public BolmFacUni getAll(@RequestBody GetById getById) {
        return service.getBolFacUni(getById);
    }
}