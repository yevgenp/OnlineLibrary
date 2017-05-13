package com.example.ol.registration;

import com.example.ol.model.hibernate.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class RegistrationController {
  @Autowired private RegistrationService service;

  @RequestMapping(path = "register", method = RequestMethod.POST)
  public ResponseEntity<?> registerUser(@RequestBody @Valid User user, Errors errors) {
    if(!service.isValid(user, errors))
      return new ResponseEntity<>(errors.getFieldErrors(), HttpStatus.BAD_REQUEST);
    user = service.register(user);
    Resource<User> resource = new Resource<>(user);
    resource.add(service.getSelfLink(user));
    return new ResponseEntity<>(resource, HttpStatus.OK);
  }

}
