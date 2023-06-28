package at.ac.tuwien.sepm.groupphase.backend.unittests.repository;


import at.ac.tuwien.sepm.groupphase.backend.basetest.ListTestData;
import at.ac.tuwien.sepm.groupphase.backend.entity.RecipeList;
import at.ac.tuwien.sepm.groupphase.backend.repository.ListRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles({"test"})
@DataJpaTest
public class ListRepositoryTest implements ListTestData {
    @Autowired
    ListRepository listRepository;

    @Test
    @Sql(scripts = "classpath:/sql/ListTests.sql")
    public void createList_withValidData_createsList() {
        RecipeList list = new RecipeList.RecipeListBuilder()
            .withName("wowie")
            .withUser(USER)
            .build();

        listRepository.saveAndFlush(list);
        assertThat(listRepository.findByUserIdAndName(USER.getId(), "wowie")).isNotEmpty();
    }

    @Test
    @Sql(scripts = "classpath:/sql/ListTests.sql")
    public void getAllByUserId_findsAllLists_ofUser() {
        assertThat(listRepository.getAllByUserId(-43L)).size().isEqualTo(2);
    }

    @Test
    @Sql(scripts = "classpath:/sql/ListTests.sql")
    public void getAllByUserId_givenInvalidId_findsNone() {
        assertThat(listRepository.getAllByUserId(-430L)).size().isEqualTo(0);
    }

    @Test
    @Sql(scripts = "classpath:/sql/ListTests.sql")
    public void getRecipesByNameAndUserId_whenGivenSmallPage_returnsCorrectNumberOfEntries() {
        Pageable page = PageRequest.of(0, 1);
        assertThat(listRepository.getRecipesByNameAndUserId("my wonderful list 2", -43L, page)).size().isEqualTo(1);
    }

    @Test
    @Sql(scripts = "classpath:/sql/ListTests.sql")
    public void getRecipesByNameAndUserId_whenGivenBigPage_returnsCorrectNumberOfEntries() {
        Pageable page = PageRequest.of(0, 100);
        assertThat(listRepository.getRecipesByNameAndUserId("my wonderful list 2", -43L, page)).size().isEqualTo(2);
    }

    @Test
    @Sql(scripts = "classpath:/sql/ListTests.sql")
    public void getRecipesByNameAndUserId_withNonExistingUser_returnsNoRecipes() {
        Pageable page = PageRequest.of(0, 100);
        assertThat(listRepository.getRecipesByNameAndUserId("my wonderful list 3", -43L, page)).size().isEqualTo(0);
    }
}
