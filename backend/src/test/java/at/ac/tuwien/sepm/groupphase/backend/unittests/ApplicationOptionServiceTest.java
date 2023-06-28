package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.basetest.ApplicationTestData;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ApplicationOptionDto;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.ApplicationOptionRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.ApplicationOptionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class ApplicationOptionServiceTest implements ApplicationTestData {
    @Autowired
    private ApplicationOptionService applicationOptionService;

    @Autowired
    private ApplicationOptionRepository applicationOptionRepository;

    @Test
    @Sql(scripts = {"classpath:/sql/ApplicationOptionTests.sql"})
    public void findAll_whenGivenLargePage_findsAll() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        var result = applicationOptionService.findAll(pageable);

        assertEquals(4, result.size());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/ApplicationOptionTests.sql"})
    public void findAll_whenGivenSmallPage_findsCorrectNumber() throws Exception {
        Pageable pageable = PageRequest.of(0, 2);
        var result = applicationOptionService.findAll(pageable);

        assertEquals(2, result.size());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/ApplicationOptionTests.sql"})
    public void findLike_whenGivenLargePageAndNoName_findsAll() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        var result = applicationOptionService.findLike(null, pageable);

        assertEquals(4, result.size());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/ApplicationOptionTests.sql"})
    public void findLike_whenGivenSmallPageAndNoName_findsCorrectNumber() throws Exception {
        Pageable pageable = PageRequest.of(0, 2);
        var result = applicationOptionService.findLike("", pageable);

        assertEquals(2, result.size());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/ApplicationOptionTests.sql"})
    public void findLike_whenGivenName_findsAllMatching() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        var result = applicationOptionService.findLike("one", pageable);

        assertEquals(1, result.size());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/ApplicationOptionTests.sql"})
    public void findLike_whenGivenInvalidName_findsNone() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        var result = applicationOptionService.findLike("HEINZ-WILHELM - DIE OPTION", pageable);

        assertEquals(0, result.size());
    }


    @Test
    @Sql(scripts = {"classpath:/sql/ApplicationOptionTests.sql"})
    public void countLike_whenGivenLargePageAndNoName_findsAll() throws Exception {
        var result = applicationOptionService.countAll(null);

        assertEquals(4, result);
    }

    @Test
    @Sql(scripts = {"classpath:/sql/ApplicationOptionTests.sql"})
    public void countLike_whenGivenNoName_findsCorrectNumber() throws Exception {
        var result = applicationOptionService.countAll(null);

        assertEquals(4, result);
    }

    @Test
    @Sql(scripts = {"classpath:/sql/ApplicationOptionTests.sql"})
    public void countLike_whenGivenName_findsAllMatching() throws Exception {
        var result = applicationOptionService.countAll("one");

        assertEquals(1, result);
    }

    @Test
    @Sql(scripts = {"classpath:/sql/ApplicationOptionTests.sql"})
    public void countLike_whenGivenInvalidName_findsNone() throws Exception {
        var result = applicationOptionService.countAll("HEINZ-WILHELM - DIE OPTION");

        assertEquals(0, result);
    }

    @Test
    @Sql(scripts = {"classpath:/sql/ApplicationOptionTests.sql"})
    public void findByName_WhenGivenValidName_findsCorrect() throws Exception {
        var result = applicationOptionService.findByName("test option one");

        assertEquals("test option one", result.getName());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/ApplicationOptionTests.sql"})
    public void findByName_WhenGivenInvalidName_throwsNotFoundException() throws Exception {
        String fail = "ALALLALALALALLAALALALA";
        assertThrows(NotFoundException.class, () -> applicationOptionService.findByName(fail));
    }


    @Test
    @Sql(scripts = {"classpath:/sql/ApplicationOptionTests.sql"})
    public void update_WhenGivenInvalidId_throwsNotFoundException() throws Exception {
        assertThrows(NotFoundException.class, () -> applicationOptionService.update(null, 100L));
    }

    @Test
    @Sql(scripts = {"classpath:/sql/ApplicationOptionTests.sql"})
    public void update_WhenGivenValid_UpdatesOption() throws Exception {
        var dto = new ApplicationOptionDto.ApplicationOptionBuilder()
            .withId(-1L)
            .withName("test option one")
            .withValue("FALSE")
            .withDefaultValue(DEFAULT)
            .withOptionType(TYPE)
            .build();

        var result = applicationOptionService.update(dto, -1L);
        assertEquals("FALSE", result.getValue());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/ApplicationOptionTests.sql"})
    public void update_WhenGivenInvalidBoolean_throwsValidationException() throws Exception {
        var dto = new ApplicationOptionDto.ApplicationOptionBuilder()
            .withId(-1L)
            .withName("test option one")
            .withValue("LELELELE")
            .build();

        assertThrows(ValidationException.class, () -> applicationOptionService.update(dto, -1L));
    }

    @Test
    @Sql(scripts = {"classpath:/sql/ApplicationOptionTests.sql"})
    public void update_WhenGivenInvalidLongRange_throwsValidationException() throws Exception {
        var dto = new ApplicationOptionDto.ApplicationOptionBuilder()
            .withId(-2L)
            .withName("test option two")
            .withValue("65536")
            .build();

        assertThrows(ValidationException.class, () -> applicationOptionService.update(dto, -2L));
    }

    @Test
    @Sql(scripts = {"classpath:/sql/ApplicationOptionTests.sql"})
    public void update_WhenGivenInvalidShortRange_throwsValidationException() throws Exception {
        var dto = new ApplicationOptionDto.ApplicationOptionBuilder()
            .withId(-3L)
            .withName("test option three")
            .withValue("280")
            .build();

        assertThrows(ValidationException.class, () -> applicationOptionService.update(dto, -3L));
    }

    @Test
    @Sql(scripts = {"classpath:/sql/ApplicationOptionTests.sql"})
    public void update_WhenGivenInvalidStringRange_throwsValidationException() throws Exception {
        String fail = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
            + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
            + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
            + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";

        var dto = new ApplicationOptionDto.ApplicationOptionBuilder()
            .withId(-4L)
            .withName("test option four")
            .withValue(fail)
            .build();

        assertThrows(ValidationException.class, () -> applicationOptionService.update(dto, -4L));
    }
}

