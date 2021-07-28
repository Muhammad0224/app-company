package uz.pdp.appcompany.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Worker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true)
    private String phoneNumber;

    @OneToOne(optional = false)
    private Address address;

    @ManyToOne(optional = false)
    private Department department;

    private Boolean status = true;
}
