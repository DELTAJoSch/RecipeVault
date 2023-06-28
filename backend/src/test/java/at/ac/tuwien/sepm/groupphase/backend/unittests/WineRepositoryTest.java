package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.entity.Wine;
import at.ac.tuwien.sepm.groupphase.backend.entity.WineCategory;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.WineRepository;
import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles({"test"})
public class WineRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private WineRepository wineRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    @Sql(scripts = {"classpath:/sql/WineRepositoryTests.sql"})
    public void newWine_insert_insertsData() throws Exception {
        String name = "newWine_insert_insertsData";
        String description = "DESC";
        String vinyard = "TESTVINYARD";
        double temperature = 10;
        WineCategory category = WineCategory.DESSERT;

        var owner = userRepository.findByEmail("test@12122392.xyz");

        Wine insert = new Wine();
        insert.setCategory(category);
        insert.setName(name);
        insert.setDescription(description);
        insert.setVinyard(vinyard);
        insert.setTemperature(temperature);
        insert.setOwner(owner);


        var inserted = wineRepository.save(insert);

        var pulled = wineRepository.findById(insert.getId());

        var content = pulled.get();
        Assert.notNull(content);
        Assertions.assertEquals(name, content.getName());
        Assertions.assertEquals(vinyard, content.getVinyard());
        Assertions.assertEquals(description, content.getDescription());
        Assertions.assertEquals(category, content.getCategory());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/WineRepositoryTests.sql"})
    public void newWine_insertWithExistingData_throwsError() {
        String name = "test throws";
        String vinyard = "test vinyard throws";
        double temperature = 10;
        WineCategory category = WineCategory.ROSE;

        var owner = userRepository.findByEmail("test@12122392.xyz");

        Wine insert = new Wine();
        insert.setCategory(category);
        insert.setName(name);
        insert.setVinyard(vinyard);
        insert.setTemperature(temperature);
        insert.setOwner(owner);

        Wine thrower = new Wine();
        thrower.setCategory(category);
        thrower.setName(name);
        thrower.setVinyard(vinyard);
        thrower.setTemperature(temperature);
        thrower.setOwner(owner);

        wineRepository.saveAndFlush(insert);

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            wineRepository.saveAndFlush(thrower);
        });
    }

    @Test
    @Sql(scripts = {"classpath:/sql/WineRepositoryTests.sql"})
    public void findByCategory_findsAll_withCorrectCategory() {
        var result = wineRepository.findByCategory(WineCategory.SPARKLING);

        Assertions.assertEquals(3, result.size());

        var vinyards = result.stream().map(w -> w.getVinyard()).toList();
        assertThat(vinyards).contains("test vinyard one", "test vinyard two");
    }

    @Test
    @Sql(scripts = {"classpath:/sql/WineRepositoryTests.sql"})
    public void findByCategory_findNonExistent_findsNone() {
        var result = wineRepository.findByCategory(WineCategory.LIGHT_RED);

        Assertions.assertEquals(0, result.size());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/WineRepositoryTests.sql"})
    public void findByVinyardContains_lookForVinyard_findsAll() {
        var result = wineRepository.findByVinyardContaining("vinyard");

        Assertions.assertEquals(4, result.size());

        var vinyards = result.stream().map(w -> w.getVinyard()).toList();
        assertThat(vinyards).contains("test vinyard one", "test vinyard two");
    }

    @Test
    @Sql(scripts = {"classpath:/sql/WineRepositoryTests.sql"})
    public void findByVinyardContains_lookForSpecificVinyard_findsSpecific() {
        var result = wineRepository.findByVinyardContaining("test vinyard two");

        Assertions.assertEquals(1, result.size());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/WineRepositoryTests.sql"})
    public void findByVinyardContains_lookForNonexistant_findsNone() {
        var result = wineRepository.findByVinyardContaining("test vinyard two does not exist?");

        Assertions.assertEquals(0, result.size());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/WineRepositoryTests.sql"})
    public void findAll_does_findAll() {
        var result = wineRepository.findAll();

        Assertions.assertEquals(5, result.size());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/WineRepositoryTests.sql"})
    public void findAll_Paged_findsAllInPage() {
        Pageable pageRequest = PageRequest.of(0, 2);

        var result = wineRepository.findAll(pageRequest);

        Assertions.assertEquals(2, result.getContent().size());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/WineRepositoryTests.sql"})
    public void findByNameContains_lookForName_findsAll() {
        var result = wineRepository.findByNameContaining("test");

        Assertions.assertEquals(4, result.size());

        var vinyards = result.stream().map(w -> w.getName()).toList();
        assertThat(vinyards).contains("test wine one", "test wine two", "test wine three", "test wine four");
    }

    @Test
    @Sql(scripts = {"classpath:/sql/WineRepositoryTests.sql"})
    public void findByNameContains_lookForSpecificName_findsSpecific() {
        var result = wineRepository.findByNameContaining("test wine two");

        Assertions.assertEquals(1, result.size());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/WineRepositoryTests.sql"})
    public void findByNameContains_lookForNonexistant_findsNone() {
        var result = wineRepository.findByNameContaining("test wine two does not exist?");

        Assertions.assertEquals(0, result.size());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/WineRepositoryTests.sql"})
    public void find_lookForAllNull_findsAll() {
        Pageable pageable = PageRequest.of(0, 25);
        var result = wineRepository.find(null, null, null, null, pageable);

        Assertions.assertEquals(5, result.getContent().size());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/WineRepositoryTests.sql"})
    public void find_lookForName_findsMatches() {
        Pageable pageable = PageRequest.of(0, 25);
        var result = wineRepository.find("test ", null, null, null, pageable);

        Assertions.assertEquals(4, result.getContent().size());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/WineRepositoryTests.sql"})
    public void find_lookForVinyard_findsMatches() {
        Pageable pageable = PageRequest.of(0, 25);
        var result = wineRepository.find(null, "test vinyard o", null, null, pageable);

        Assertions.assertEquals(3, result.getContent().size());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/WineRepositoryTests.sql"})
    public void find_lookForNonexistant_findsNone() {
        Pageable pageable = PageRequest.of(0, 25);
        var result = wineRepository.find(
            "test wine two does not exist?",
            null,
            null,
            null,
            pageable);

        Assertions.assertEquals(0, result.getContent().size());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/WineRepositoryTests.sql"})
    public void find_lookForCategory_findsMatches() {
        Pageable pageable = PageRequest.of(0, 25);
        var result = wineRepository.find(null, null, WineCategory.SPARKLING, null, pageable);

        Assertions.assertEquals(3, result.getContent().size());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/WineRepositoryTests.sql"})
    public void find_lookForCountryNonExistant_findsNone() {
        Pageable pageable = PageRequest.of(0, 25);
        var result = wineRepository.find(null, null, null, "AT", pageable);

        Assertions.assertEquals(0, result.getContent().size());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/WineRepositoryTests.sql"})
    public void find_lookForComplexQuery_findsMatch() {
        Pageable pageable = PageRequest.of(0, 25);
        var result = wineRepository.find("test wine", "one", WineCategory.ROSE, null, pageable);

        Assertions.assertEquals(1, result.getContent().size());
        Assertions.assertEquals("test wine three", result.getContent().get(0).getName());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/WineRepositoryTests.sql"})
    public void countOfFind_lookForAllNull_findsAll() {
        var result = wineRepository.countOfFind(null, null, null, null);

        Assertions.assertEquals(5, result);
    }

    @Test
    @Sql(scripts = {"classpath:/sql/WineRepositoryTests.sql"})
    public void countOfFind_lookForName_findsMatches() {
        var result = wineRepository.countOfFind("test ", null, null, null);

        Assertions.assertEquals(4, result);
    }

    @Test
    @Sql(scripts = {"classpath:/sql/WineRepositoryTests.sql"})
    public void countOfFind_lookForVinyard_findsMatches() {
        var result = wineRepository.countOfFind(null, "test vinyard o", null, null);

        Assertions.assertEquals(3, result);
    }

    @Test
    @Sql(scripts = {"classpath:/sql/WineRepositoryTests.sql"})
    public void countOfFind_lookForNonexistant_findsNone() {
        var result = wineRepository.countOfFind("test wine two does not exist?", null, null, null);

        Assertions.assertEquals(0, result);
    }

    @Test
    @Sql(scripts = {"classpath:/sql/WineRepositoryTests.sql"})
    public void countOfFind_lookForCategory_findsMatches() {
        var result = wineRepository.countOfFind(null, null, WineCategory.SPARKLING, null);

        Assertions.assertEquals(3, result);
    }

    @Test
    @Sql(scripts = {"classpath:/sql/WineRepositoryTests.sql"})
    public void countOfFind_lookForCountryNonExistant_findsNone() {
        var result = wineRepository.countOfFind(null, null, null, "AT");

        Assertions.assertEquals(0, result);
    }

    @Test
    @Sql(scripts = {"classpath:/sql/WineRepositoryTests.sql"})
    public void countOfFind_lookForComplexQuery_findsMatch() {
        var result = wineRepository.countOfFind("test wine", "one", WineCategory.ROSE, null);

        Assertions.assertEquals(1, result);
    }


}
