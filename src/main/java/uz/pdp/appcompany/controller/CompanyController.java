package uz.pdp.appcompany.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appcompany.entity.Company;
import uz.pdp.appcompany.model.CompanyDto;
import uz.pdp.appcompany.model.ResponseCustom;
import uz.pdp.appcompany.service.CompanyService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/company")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @GetMapping
    public ResponseEntity<List<Company>> get(){
        return companyService.get();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Company> get(@PathVariable Integer id){
        return companyService.get(id);
    }

    @PostMapping
    public ResponseEntity<ResponseCustom> add(@Valid @RequestBody CompanyDto dto){
        return companyService.add(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseCustom> edit(@Valid @PathVariable Integer id, @RequestBody CompanyDto dto){
        return companyService.edit(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseCustom> delete(@PathVariable Integer id){
        return companyService.delete(id);
    }
}
