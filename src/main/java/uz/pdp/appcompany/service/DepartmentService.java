package uz.pdp.appcompany.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.pdp.appcompany.entity.Address;
import uz.pdp.appcompany.entity.Company;
import uz.pdp.appcompany.entity.Department;
import uz.pdp.appcompany.entity.Worker;
import uz.pdp.appcompany.helpers.Utils;
import uz.pdp.appcompany.model.CompanyDto;
import uz.pdp.appcompany.model.DepartmentDto;
import uz.pdp.appcompany.model.ResponseCustom;
import uz.pdp.appcompany.repository.AddressRepository;
import uz.pdp.appcompany.repository.CompanyRepository;
import uz.pdp.appcompany.repository.DepartmentRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final CompanyRepository companyRepository;

    private final DepartmentRepository departmentRepository;

    private final WorkerService workerService;

    public ResponseEntity<List<Department>> get() {
        return ResponseEntity.ok(departmentRepository.findAllByStatusTrue());
    }

    public ResponseEntity<List<Department>> getByCompany(Integer companyId) {
        return ResponseEntity.ok(departmentRepository.findAllByCompanyIdAndStatusTrue(companyId));
    }

    public ResponseEntity<Department> get(Integer id) {
        Optional<Department> optionalDepartment = departmentRepository.findByIdAndStatusTrue(id);
        return optionalDepartment.map(ResponseEntity::ok).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<ResponseCustom> add(DepartmentDto dto) {
        if (departmentRepository.existsByNameAndCompanyIdAndStatusTrue(dto.getName(), dto.getCompanyId()))
            return new ResponseEntity<>(new ResponseCustom("This department has already existed", false), HttpStatus.CONFLICT);

        Optional<Company> optionalCompany = companyRepository.findByIdAndStatusTrue(dto.getCompanyId());
        if (!optionalCompany.isPresent())
            return new ResponseEntity<>(new ResponseCustom("Company not found", false), HttpStatus.NOT_FOUND);

        Company company = optionalCompany.get();

        departmentRepository.save(Department.builder()
                .name(dto.getName())
                .status(true)
                .company(company).build());

        return ResponseEntity.ok(new ResponseCustom("Department created", true));
    }

    public ResponseEntity<ResponseCustom> edit(Integer id, DepartmentDto dto) {
        if (departmentRepository.existsByNameAndCompanyIdAndStatusTrueAndIdNot(dto.getName(), dto.getCompanyId(), id))
            return new ResponseEntity<>(new ResponseCustom("This department has already existed", false), HttpStatus.CONFLICT);

        Optional<Department> optionalDepartment = departmentRepository.findByIdAndStatusTrue(id);
        if (optionalDepartment.isPresent()) {
            Department department = optionalDepartment.get();

            Optional<Company> optionalCompany = companyRepository.findByIdAndStatusTrue(dto.getCompanyId());
            if (optionalCompany.isPresent()) {
                Company company = optionalCompany.get();

                department.setCompany(company);
                department.setName(dto.getName());
                departmentRepository.save(department);

                return ResponseEntity.ok(new ResponseCustom("Department edited", true));
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<ResponseCustom> delete(Integer id) {
        Optional<Department> optionalDepartment = departmentRepository.findByIdAndStatusTrue(id);
        if (optionalDepartment.isPresent()) {
            Department department = optionalDepartment.get();

            List<Worker> workers = workerService.getByDepartment(department.getId()).getBody();

            if (!Utils.isEmpty(workers)){
                for (Worker worker : workers) {
                    workerService.delete(worker.getId());
                }
            }

            department.setStatus(false);
            departmentRepository.save(department);
            return ResponseEntity.ok(new ResponseCustom("Department deleted", true));
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
