package com.example.protetion_logiciels.Repository;

import com.example.protetion_logiciels.Entity.LicenseHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LicenseHistoryRepository extends JpaRepository<LicenseHistory, Integer> {
    List<LicenseHistory> findByLicenceId(Integer licenceId);
    List<LicenseHistory> findByUtilisateurId(Integer utilisateurId);
}
