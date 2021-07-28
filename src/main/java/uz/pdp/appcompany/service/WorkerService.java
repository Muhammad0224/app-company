package uz.pdp.appcompany.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.pdp.appcompany.entity.Address;
import uz.pdp.appcompany.entity.Company;
import uz.pdp.appcompany.entity.Department;
import uz.pdp.appcompany.entity.Worker;
import uz.pdp.appcompany.model.CompanyDto;
import uz.pdp.appcompany.model.ResponseCustom;
import uz.pdp.appcompany.model.WorkerDto;
import uz.pdp.appcompany.repository.AddressRepository;
import uz.pdp.appcompany.repository.CompanyRepository;
import uz.pdp.appcompany.repository.DepartmentRepository;
import uz.pdp.appcompany.repository.WorkerRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WorkerService {

    private final DepartmentRepository departmentRepository;

    private final WorkerRepository workerRepository;

    private final AddressRepository addressRepository;

    public ResponseEntity<List<Worker>> get() {
        return ResponseEntity.ok(workerRepository.findAllByStatusTrue());
    }

    public ResponseEntity<Worker> get(Integer id) {
        Optional<Worker> optionalWorker = workerRepository.findByIdAndStatusTrue(id);
        return optionalWorker.map(ResponseEntity::ok).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<List<Worker>> getByDepartment(Integer departmentId) {
        return ResponseEntity.ok(workerRepository.findAllByDepartmentIdAndStatusTrue(departmentId));
    }

    public ResponseEntity<List<Worker>> getByCompany(Integer companyId) {
        return ResponseEntity.ok(workerRepository.findAllByStatusTrueAndDepartment_CompanyId(companyId));
    }

    public ResponseEntity<ResponseCustom> add(WorkerDto dto) {
        if (workerRepository.existsByPhoneNumberAndStatusTrue(dto.getPhoneNumber()))
            return new ResponseEntity<>(new ResponseCustom("This worker has already existed", false), HttpStatus.CONFLICT);

        Optional<Department> optionalDepartment = departmentRepository.findByIdAndStatusTrue(dto.getDepartmentId());
        if (!optionalDepartment.isPresent())
            return new ResponseEntity<>(new ResponseCustom("Department not found", false), HttpStatus.NOT_FOUND);

        Department department = optionalDepartment.get();
        Address address = addressRepository.save(Address.builder().homeNumber(dto.getHomeNumber()).status(true).street(dto.getStreet()).build());

        workerRepository.save(Worker.builder()
                .department(department)
                .address(address)
                .status(true)
                .phoneNumber(dto.getPhoneNumber())
                .name(dto.getName()).build());

        return ResponseEntity.ok(new ResponseCustom("Worker created", true));
    }

    public ResponseEntity<ResponseCustom> edit(Integer id, WorkerDto dto) {
        if (workerRepository.existsByPhoneNumberAndIdNotAndStatusTrue(dto.getPhoneNumber(), id))
            return new ResponseEntity<>(new ResponseCustom("This worker has already existed", false), HttpStatus.CONFLICT);

        Optional<Worker> optionalWorker = workerRepository.findByIdAndStatusTrue(id);
        if (!optionalWorker.isPresent())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Worker worker = optionalWorker.get();
        Optional<Department> optionalDepartment = departmentRepository.findByIdAndStatusTrue(dto.getDepartmentId());
        if (optionalDepartment.isPresent()) {
            Department department = optionalDepartment.get();

            Address address = worker.getAddress();
            address.setHomeNumber(dto.getHomeNumber());
            address.setStreet(dto.getStreet());
            addressRepository.save(address);

            worker.setAddress(address);
            worker.setDepartment(department);
            worker.setName(dto.getName());
            worker.setPhoneNumber(dto.getPhoneNumber());
            workerRepository.save(worker);

            return ResponseEntity.ok(new ResponseCustom("Worker edited", true));
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<ResponseCustom> delete(Integer id) {
        Optional<Worker> optionalWorker = workerRepository.findByIdAndStatusTrue(id);
        if (optionalWorker.isPresent()) {
            Worker worker = optionalWorker.get();

            Address address = worker.getAddress();
            address.setStatus(false);
            addressRepository.save(address);

            worker.setStatus(false);
            workerRepository.save(worker);
            return ResponseEntity.ok(new ResponseCustom("Worker deleted", true));
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
