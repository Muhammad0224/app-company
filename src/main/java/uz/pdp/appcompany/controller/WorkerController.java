package uz.pdp.appcompany.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appcompany.entity.Company;
import uz.pdp.appcompany.entity.Department;
import uz.pdp.appcompany.entity.Worker;
import uz.pdp.appcompany.model.CompanyDto;
import uz.pdp.appcompany.model.ResponseCustom;
import uz.pdp.appcompany.model.WorkerDto;
import uz.pdp.appcompany.service.CompanyService;
import uz.pdp.appcompany.service.WorkerService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/worker")
public class WorkerController {

    @Autowired
    private WorkerService workerService;

    @GetMapping
    public ResponseEntity<List<Worker>> get(){
        return workerService.get();
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<Worker>> getByDepartment(@PathVariable Integer departmentId){
        return workerService.getByDepartment(departmentId);
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<Worker>> getByCompany(@PathVariable Integer companyId){
        return workerService.getByCompany(companyId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Worker> get(@PathVariable Integer id){
        return workerService.get(id);
    }

    @PostMapping
    public ResponseEntity<ResponseCustom> add(@Valid @RequestBody WorkerDto dto){
        return workerService.add(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseCustom> edit(@Valid @PathVariable Integer id, @RequestBody WorkerDto dto){
        return workerService.edit(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseCustom> delete(@PathVariable Integer id){
        return workerService.delete(id);
    }
}
