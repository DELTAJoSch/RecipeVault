package at.ac.tuwien.sepm.groupphase.backend.unittests.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Author;
import at.ac.tuwien.sepm.groupphase.backend.repository.AuthorRepository;
import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("test")
public class AuthorRepositoryTest {
    @Autowired
    private AuthorRepository authorRepository;

    @Test
    @Sql(scripts = {"classpath:/sql/AuthorRepositoryTest.sql"})
    public void findAll_findsAll() {
        var authorList = authorRepository.findAll();
        Assert.notNull(authorList);
        assertEquals(3, authorList.size());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/AuthorRepositoryTest.sql"})
    public void findById_withValidId_findsCorrectAuthor() {
        var content = authorRepository.findById(-1L);
        var author = content.get();
        Assert.notNull(author);
        assertEquals(-1L, author.getId());
        assertEquals("one", author.getFirstname());
        assertEquals("author", author.getLastname());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/AuthorRepositoryTest.sql"})
    public void findById_withInvalidId_isNotPresent() {
        Assertions.assertTrue(authorRepository.findById(-10L).isEmpty());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/AuthorRepositoryTest.sql"})
    public void findByFirstnameAndLastname_withValidData_findsCorrectAuthor() {
        var content = authorRepository.findByFirstnameAndLastname("one", "author");
        var author = content.get();
        Assert.notNull(author);
        assertEquals(-1L, author.getId());
        assertEquals("one", author.getFirstname());
        assertEquals("author", author.getLastname());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/AuthorRepositoryTest.sql"})
    public void findByFirstnameAndLastname_withInvalidData_notPresent() {
        Assertions.assertTrue(authorRepository.findByFirstnameAndLastname("thkls", "jk").isEmpty());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/AuthorRepositoryTest.sql"})
    public void newAuthor_insert_insertsData() {
        String firstname = "newAuthor_insert";
        String lastname = "insertsData";
        String description = "DESC";

        Author insert = new Author();
        insert.setFirstname(firstname);
        insert.setLastname(lastname);
        insert.setDescription(description);

        var inserted = authorRepository.save(insert);

        var pulled = authorRepository.findById(inserted.getId());

        var content = pulled.get();
        Assert.notNull(content);
        assertEquals(firstname, content.getFirstname());
        assertEquals(lastname, content.getLastname());
        assertEquals(description, content.getDescription());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/AuthorRepositoryTest.sql"})
    public void newAuthor_insertWithExistingData_insertsData() {
        String firstname = "one";
        String lastname = "author";
        String description = "DESC";

        Author insert = new Author();
        insert.setFirstname(firstname);
        insert.setLastname(lastname);
        insert.setDescription(description);

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> authorRepository.saveAndFlush(insert));
    }
}
