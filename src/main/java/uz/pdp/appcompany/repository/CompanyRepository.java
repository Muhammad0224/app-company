package uz.pdp.appcompany.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.appcompany.entity.Company;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Integer> {
    List<Company> findAllByStatusTrue();

    Optional<Company> findByIdAndStatusTrue(Integer id);

    boolean existsByCorpNameAndStatusTrue(String corpName);

    boolean existsByCorpNameAndIdNotAndStatusTrue(String corpName, Integer id);
}
