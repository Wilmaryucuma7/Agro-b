package usco.agrosoft.models;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Table(name = "user", schema = "public")
public class User {
    @Id
    @Setter
    @Getter
    //@GeneratedValue(generator = "uuid2")
    @Column(name = "id_user")
    //@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String idUser;

    @Getter
    @Setter
    @Column(name = "name")
    private String name;

    @Getter
    @Setter
    @Column(name = "last_name")
    private String lastName;

    @Getter
    @Setter
    @Column(name = "phone_number")
    private String phoneNumber;

    @Getter
    @Setter
    @Column(name = "token_user")
    private String tokenUser;

    @Getter
    @Setter
    @Column(name = "password")
    private String password;

    @Getter
    @Setter
    @Column(name = "email")
    private String email;

    @Getter
    @Setter
    @Column(name = "is_active", columnDefinition = "boolean default false")
    private boolean isActive;

    @Getter
    @Setter
    @Column(name = "enrollment_date")
    private LocalDateTime enrollmentDate;
}