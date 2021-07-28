package uz.pdp.appcompany.model;

import lombok.*;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyDto {
    @NotNull
    private String corpName;

    @NotNull
    private String directorName;

    @NotNull
    private String street;

    private String homeNumber;
}
