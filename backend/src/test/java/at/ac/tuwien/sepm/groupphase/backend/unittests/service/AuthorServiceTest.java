package at.ac.tuwien.sepm.groupphase.backend.unittests.service;

import at.ac.tuwien.sepm.groupphase.backend.basetest.AuthorTestData;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.AuthorCreateDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.AuthorDetailsDto;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.UserPermissionException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.AuthorRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.AuthorService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class AuthorServiceTest implements AuthorTestData {

    @Autowired
    private AuthorService authorService;
    @Autowired
    private AuthorRepository authorRepository;


    @Test
    @Sql(scripts={"classpath:/sql/AuthorRepositoryTest.sql"})
    public void getAuthors_whenGivenPage_returnsListOfAllAuthors() {
        Pageable pageable = PageRequest.of(0, 3);
        var authors = authorService.getAuthors(pageable);

        assertEquals(3, authors.size());
    }

    @Test
    @Sql(scripts={"classpath:/sql/AuthorRepositoryTest.sql"})
    public void getAuthorById_withValidId_returnsCorrectAuthor() {
        var author = authorService.getAuthorById(-1L);

        assertEquals(-1L, author.getId());
        assertEquals("one", author.getFirstname());
        assertEquals("author", author.getLastname());
        assertEquals("test", author.getDescription());
    }

    @Test
    @Sql(scripts={"classpath:/sql/AuthorRepositoryTest.sql"})
    public void getAuthorById_withInvalidId_throwsNotFoundException() {
        Assertions.assertThrows(NotFoundException.class, () -> authorService.getAuthorById(-10L));

    }


    @Test
    @Sql(scripts={"classpath:/sql/AuthorRepositoryTest.sql"})
    public void createAuthor_whenGivenAuthorwithValidData_createsNewAuthorInDatabase() throws Exception {
        AuthorCreateDto createDto = new AuthorCreateDto.AuthorCreateDtoBuilder()
            .withFirstname("Hugo")
            .withLastname("Liebling")
            .build();

        authorService.createAuthor(createDto);

        var read = authorRepository.findByFirstnameAndLastname("Hugo", "Liebling");

        var author = read.get();
        assertEquals("Hugo", author.getFirstname());
        assertEquals("Liebling", author.getLastname());
        assertNull(author.getDescription());
        assertNotNull(author.getId());
    }

    @Test
    @Sql(scripts={"classpath:/sql/AuthorRepositoryTest.sql"})
    public void updateAuthor_whenGivenAuthorWithValidData_updatesAuthorInDatabase() throws Exception {
        AuthorDetailsDto update = new AuthorDetailsDto.AuthorDetailsDtoBuilder()
            .withId(-2L)
            .withFirstname("Hugo")
            .withLastname("Liebling")
            .build();

        authorService.updateAuthor(update);

        var read = authorRepository.findById(-2L);

        var author = read.get();
        assertEquals("Hugo", author.getFirstname());
        assertEquals("Liebling", author.getLastname());
        assertNull(author.getDescription());
        assertEquals(-2L, author.getId());
    }

    @Test
    @Sql(scripts={"classpath:/sql/AuthorRepositoryTest.sql"})
    public void deleteAuthor_whenGivenIdValid_calledByAdmin_deletesCorrectAuthor() throws Exception{
        authorService.deleteAuthor(-1L, ADMIN_USER);

        var read = authorRepository.findByFirstnameAndLastname("one", "author");

        assertTrue(read.isEmpty());
    }

    @Test
    @Sql(scripts={"classpath:/sql/AuthorRepositoryTest.sql"})
    public void deleteAuthor_whenGivenIdInvalid_calledByAdmin_throwsNotFoundException() {
        Assertions.assertThrows(NotFoundException.class, () -> authorService.deleteAuthor(-100L, ADMIN_USER));
    }

    @Test
    @Sql(scripts={"classpath:/sql/AuthorRepositoryTest.sql"})
    public void deleteAuthor_whenGivenIdValid_notCalledByAdmin_throwsUserPermissionException() {
        Assertions.assertThrows(UserPermissionException.class, () -> authorService.deleteAuthor(-1L, DEFAULT_USER));
    }

    @Test
    @Sql(scripts={"classpath:/sql/AuthorRepositoryTest.sql"})
    public void deleteAuthor_whenGivenIdInvalid_notCalledByAdmin_throwsUserPermissionException() {
        Assertions.assertThrows(UserPermissionException.class, () -> authorService.deleteAuthor(-100L, DEFAULT_USER));
    }


}
