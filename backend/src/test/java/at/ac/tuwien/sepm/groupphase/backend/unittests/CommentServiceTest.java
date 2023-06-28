package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CommentDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Comment;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.UserPermissionException;
import at.ac.tuwien.sepm.groupphase.backend.repository.CommentRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.RecipeRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.CommentService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class CommentServiceTest {

    private final String ADMIN_DETAILS = "admin@example.com";
    private final String USER_DETAILS = "user@example.com";
    private final String USER2_DETAILS = "user2@example.com";

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Test
    @Transactional
    @Sql(scripts = { "classpath:/sql/CommentServiceTest.sql" })
    public void getComments_withExistingRecipe_getsCorrectComments() throws Exception {
        List<Comment> entities = commentService.getComments(-1L);
        assertEquals(2, entities.size());
        assertEquals("sample comment content", entities.get(0).getContent());
        assertTrue(entities.get(1).getCreator().getId().equals(-43L) || entities.get(1).getCreator().getId().equals(-45L));
    }

    @Test
    @Transactional
    @Sql(scripts = { "classpath:/sql/CommentServiceTest.sql" })
    public void getComments_withNonexistentRecipe_throwsNotFound() throws Exception {
        Assertions.assertThrows(NotFoundException.class, () -> {
            commentService.getComments(-10L);
        });
    }

    @Test
    @Transactional
    @Sql(scripts = { "classpath:/sql/CommentServiceTest.sql" })
    public void createComment_withCorrectData_createsComment() throws Exception {
        long entityCount = commentRepository.count();
        var inserted = new CommentDto(-42L, -2L, LocalDateTime.of(2023, 5, 14, 10, 12, 9), "very good recipe");

        commentService.createComment(inserted, USER2_DETAILS);
        assertEquals(entityCount + 1, commentRepository.count());

        List<Comment> entities = commentService.getComments(-2L);
        assertEquals(entities.size(), 1);
        assertEquals("very good recipe", entities.get(0).getContent());
    }


    @Test
    @Sql(scripts = { "classpath:/sql/CommentServiceTest.sql" })
    public void deleteComment_byAdmin_deletesComment() throws Exception {
        long entityCount = commentRepository.count();
        long usersCount = userRepository.count();
        long recipeCount = recipeRepository.count();
        var deleted = new CommentDto(-43L , -3L, LocalDateTime.of(2023, 5, 10, 15, 20, 3), "sample comment content");

        commentService.deleteComment(deleted, ADMIN_DETAILS);
        assertEquals(entityCount - 1, commentRepository.count());
        assertEquals(usersCount, userRepository.count());
        assertEquals(recipeCount, recipeRepository.count());
    }

    @Test
    @Sql(scripts = { "classpath:/sql/CommentServiceTest.sql" })
    public void deleteComment_byOtherUser_throwsPermissionDenied() throws Exception {
        long entityCount = commentRepository.count();
        long usersCount = userRepository.count();
        long recipeCount = recipeRepository.count();
        var deleted = new CommentDto(-43L , -1L, LocalDateTime.of(2023, 5, 10, 15, 20, 3), "sample comment content");

        Assertions.assertThrows(UserPermissionException.class, () -> {
            commentService.deleteComment(deleted, USER2_DETAILS);
        });
        assertEquals(entityCount, commentRepository.count());
        assertEquals(usersCount, userRepository.count());
        assertEquals(recipeCount, recipeRepository.count());
    }

}