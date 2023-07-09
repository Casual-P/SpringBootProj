package com.startup.forum.infrastructure.storage.entity;

import com.startup.forum.domain.model.rest.Roles;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity(name = "roles")
public class Role {
    @Column(unique = true, nullable = false)
    @Enumerated(EnumType.STRING)
    @Id
    private Roles id;

    public Role(Roles role) {
        this.id = role;
    }
}
