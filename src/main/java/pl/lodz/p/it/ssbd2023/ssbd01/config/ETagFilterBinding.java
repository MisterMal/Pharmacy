package pl.lodz.p.it.ssbd2023.ssbd01.config;

import jakarta.ws.rs.NameBinding;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@NameBinding
@Retention(RetentionPolicy.RUNTIME)
public @interface ETagFilterBinding {}
