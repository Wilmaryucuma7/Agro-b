package usco.agrosoft.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "identifier", schema = "public")
public class Identifier {
    @Id
    @Getter
    @Setter
    @Column(name = "id_identifier")
    private int idIdentifier;

    @Getter
    @Setter
    @Column(name = "name")
    private String name;

    @Getter
    @Setter
    @Column(name = "phone_code")
    private String phoneCode;

}
