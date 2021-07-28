package uz.pdp.appcompany.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.pdp.appcompany.entity.Address;
import uz.pdp.appcompany.entity.Company;
import uz.pdp.appcompany.entity.Department;
import uz.pdp.appcompany.helpers.Utils;
import uz.pdp.appcompany.model.CompanyDto;
import uz.pdp.appcompany.model.ResponseCustom;
import uz.pdp.appcompany.repository.AddressRepository;
import uz.pdp.appcompany.repository.CompanyRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    private final AddressRepository addressRepository;

    private final DepartmentService departmentService;

    public ResponseEntity<List<Company>> get() {
        return ResponseEntity.ok(companyRepository.findAllByStatusTrue());
    }

    public ResponseEntity<Company> get(Integer id) {
        Optional<Company> optionalCompany = companyRepository.findByIdAndStatusTrue(id);
        return optionalCompany.map(ResponseEntity::ok).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<ResponseCustom> add(CompanyDto dto) {
        if (companyRepository.existsByCorpNameAndStatusTrue(dto.getCorpName()))
            return new ResponseEntity<>(new ResponseCustom("This company has already existed", false), HttpStatus.CONFLICT);

        Address address = addressRepository.save(Address.builder().homeNumber(dto.getHomeNumber()).street(dto.getStreet()).status(true).build());

        companyRepository.save(Company.builder()
                .corpName(dto.getCorpName())
                .address(address)
                .status(true)
                .directorName(dto.getDirectorName()).build());

        return ResponseEntity.ok(new ResponseCustom("Company created", true));
    }

    public ResponseEntity<ResponseCustom> edit(Integer id, CompanyDto dto) {
        if (companyRepository.existsByCorpNameAndIdNotAndStatusTrue(dto.getCorpName(), id))
            return new ResponseEntity<>(new ResponseCustom("This company has already existed", false), HttpStatus.CONFLICT);

        Optional<Company> optionalCompany = companyRepository.findByIdAndStatusTrue(id);
        if (optionalCompany.isPresent()) {
            Company company = optionalCompany.get();

            Address address = company.getAddress();
            address.setHomeNumber(dto.getHomeNumber());
            address.setStreet(dto.getStreet());
            addressRepository.save(address);

            company.setCorpName(dto.getCorpName());
            company.setDirectorName(dto.getDirectorName());
            companyRepository.save(company);

            return ResponseEntity.ok(new ResponseCustom("Company edited", true));
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<ResponseCustom> delete(Integer id) {
        Optional<Company> optionalCompany = companyRepository.findByIdAndStatusTrue(id);
        if (optionalCompany.isPresent()) {
            Company company = optionalCompany.get();

            Address address = company.getAddress();
            address.setStatus(false);
            addressRepository.save(address);

            List<Department> departments = departmentService.getByCompany(company.getId()).getBody();
            if (!Utils.isEmpty(departments)) {
                for (Department department : departments) {
                    departmentService.delete(department.getId());
                }
            }

            company.setStatus(false);
            companyRepository.save(company);
            return ResponseEntity.ok(new ResponseCustom("Company deleted", true));
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
