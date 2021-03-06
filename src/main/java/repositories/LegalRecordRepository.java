
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.LegalRecord;

@Repository
public interface LegalRecordRepository extends JpaRepository<LegalRecord, Integer> {

	//The legal records from a  brotherhood
	@Query("select l from LegalRecord l where l.brotherhood.id = ?1")
	Collection<LegalRecord> legalRecordsfromBrotherhood(int id);

}
