package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.repository.ApplicationOptionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("test")
public class ApplicationOptionRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ApplicationOptionRepository applicationOptionRepository;

    @Test
    @Sql(scripts = {"classpath:/sql/ApplicationOptionTests.sql"})
    public void findByName_WhenGivenValidName_findsCorrectEntry() throws Exception {
        var result = applicationOptionRepository.findByName("test option one");

        assertEquals(true, result.isPresent());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/ApplicationOptionTests.sql"})
    public void findByName_WhenGivenInvalidName_findsNoEntry() throws Exception {
        var result = applicationOptionRepository.findByName("test one");

        assertEquals(false, result.isPresent());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/ApplicationOptionTests.sql"})
    public void findAll_WhenGivenValidPage_findsAll() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        var result = applicationOptionRepository.findAll(pageable);

        assertEquals(4, result.getTotalElements());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/ApplicationOptionTests.sql"})
    public void findByNameContaining_WhenGivenValidName_findsAll() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        var result = applicationOptionRepository.findByNameContaining("test", pageable);

        assertEquals(4, result.getTotalElements());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/ApplicationOptionTests.sql"})
    public void findByNameContaining_WhenGivenInvalidName_findsNone() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        var result = applicationOptionRepository.findByNameContaining("askdalskjasjkdlkasd", pageable);

        assertEquals(0, result.getTotalElements());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/ApplicationOptionTests.sql"})
    public void countByNameContaining_WhenGivenValidName_findsAll() throws Exception {
        var result = applicationOptionRepository.countByNameContaining("test");

        assertEquals(4, result);
    }

    @Test
    @Sql(scripts = {"classpath:/sql/ApplicationOptionTests.sql"})
    public void countByNameContaining_WhenGivenInvalidName_findsNone() throws Exception {
        var result = applicationOptionRepository.countByNameContaining("askdalskjasjkdlkasd");

        assertEquals(0, result);
    }
}
