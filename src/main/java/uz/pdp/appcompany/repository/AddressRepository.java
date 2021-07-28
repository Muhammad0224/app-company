package uz.pdp.appcompany.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.appcompany.entity.Address;
import uz.pdp.appcompany.entity.Company;

public interface AddressRepository extends JpaRepository<Address, Integer> {
}
