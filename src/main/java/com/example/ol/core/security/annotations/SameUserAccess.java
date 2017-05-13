package com.example.ol.core.security.annotations;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("@securityHelper.isSameUser(principal.username, #username, #id)")
public @interface SameUserAccess {
}
