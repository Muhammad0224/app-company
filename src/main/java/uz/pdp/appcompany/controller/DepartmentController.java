package uz.pdp.appcompany.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appcompany.entity.Company;
import uz.pdp.appcompany.entity.Department;
import uz.pdp.appcompany.model.CompanyDto;
import uz.pdp.appcompany.model.DepartmentDto;
import uz.pdp.appcompany.model.ResponseCustom;
import uz.pdp.appcompany.service.CompanyService;
import uz.pdp.appcompany.service.DepartmentService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/department")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @GetMapping
    public ResponseEntity<List<Department>> get(){
        return departmentService.get();
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<Department>> getByCompany(@PathVariable Integer companyId){
        return departmentService.getByCompany(companyId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Department> get(@PathVariable Integer id){
        return departmentService.get(id);
    }

    @PostMapping
    public ResponseEntity<ResponseCustom> add(@Valid @RequestBody DepartmentDto dto){
        return departmentService.add(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseCustom> edit(@Valid @PathVariable Integer id, @RequestBody DepartmentDto dto){
        return departmentService.edit(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseCustom> delete(@PathVariable Integer id){
        return departmentService.delete(id);
    }
}
