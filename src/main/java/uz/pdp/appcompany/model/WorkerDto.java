package uz.pdp.appcompany.model;

import lombok.*;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkerDto {
    @NotNull
    private String name;

    @NotNull
    private String phoneNumber;

    @NotNull
    private String street;

    private String homeNumber;

    @NotNull
    private Integer departmentId;
}
