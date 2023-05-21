package com.segroup2023.lab.database.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.Serializable;

@Entity
@Table(name = "user")
@ResponseBody
@NoArgsConstructor
public class User implements Serializable {
    public enum Identity{
        USER, VENDOR, ADMIN
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;
    @Enumerated(EnumType.STRING)
    @Getter
    private Identity identity;
    @Getter @Setter
    private String username, phone, idCard, email;
    @Getter @Setter
    @JsonIgnore
    private String password;
    public User(String username, String phone, String idCard, String email, String password, Identity identity) {
        this.username = username;
        this.phone = phone;
        this.idCard = idCard;
        this.email = email;
        this.password = password;
        this.identity = identity;
    }

    public boolean equals(Object obj) {
        if(obj == null)
            return false;
        if(!(obj instanceof User))
            return false;
        return id.equals(((User)obj).id);
    }

}

