package uz.pdp.appcompany.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String corpName;

    @Column(nullable = false)
    private String directorName;

    @OneToOne(optional = false)
    private Address address;

    @Column(columnDefinition = "boolean default true")
    private Boolean status = true;
}
