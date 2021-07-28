package uz.pdp.appcompany.model;

import lombok.*;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentDto {
    @NotNull
    private String name;

    @NotNull
    private Integer companyId;
}
