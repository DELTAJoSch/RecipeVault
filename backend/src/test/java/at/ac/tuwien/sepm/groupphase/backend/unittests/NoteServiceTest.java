package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.NoteDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Note;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.UserPermissionException;
import at.ac.tuwien.sepm.groupphase.backend.repository.NoteRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.RecipeRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.NoteService;
import jakarta.transaction.Transactional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class NoteServiceTest {

    private final String ADMIN_DETAILS = "admin@example.com";
    private final String USER_DETAILS = "user@example.com";
    private final String USER2_DETAILS = "user2@example.com";

    @Autowired
    private NoteService noteService;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Test
    @Transactional
    @Sql(scripts = { "classpath:/sql/NoteServiceTest.sql" })
    public void findNote_Success() throws Exception {
        Note entity = noteService.findNote(USER_DETAILS, -1L, USER_DETAILS);
        assertTrue(entity.getContent().equals("sample note content"));
        assertTrue(entity.getRecipe().getId() == -1L);
        assertTrue(entity.getOwner().getId() == -43L);
    }

    @Test
    @Transactional
    @Sql(scripts = { "classpath:/sql/NoteServiceTest.sql" })
    public void userFindNote_WrongUserAndNotAdmin_NotFound() throws Exception {
        Assertions.assertThrows(NotFoundException.class, () -> {
            noteService.findNote(USER_DETAILS, -1L, USER2_DETAILS);
        });
    }

    @Test
    @Transactional
    @Sql(scripts = { "classpath:/sql/NoteServiceTest.sql" })
    public void findNoteOtherUsersNote_AndAdmin_Success() throws Exception {
        Note entity = noteService.findNote(USER_DETAILS, -1L, ADMIN_DETAILS);
        assertTrue(entity.getContent().equals("sample note content"));
        assertTrue(entity.getRecipe().getId() == -1L);
        assertTrue(entity.getOwner().getId() == -43L);
    }

    @Test
    @Transactional
    @Sql(scripts = { "classpath:/sql/NoteServiceTest.sql" })
    public void createNote_Success() throws Exception {
        long entityCount = noteRepository.count();
        var inserted = new NoteDto(-44L, -1L, "sdkljfalöskjf vsdkljsaiuböaerkr");

        noteService.updateOrCreateNote(inserted, USER2_DETAILS);
        assertEquals(entityCount + 1, noteRepository.count());

        Note entity = noteService.findNote(USER2_DETAILS, -1L, USER2_DETAILS);
        assertTrue(entity.getContent().equals("sdkljfalöskjf vsdkljsaiuböaerkr"));
        assertTrue(entity.getRecipe().getId() == -1L);
        assertTrue(entity.getOwner().getId() == -44L);
    }

    @Test
    @Transactional
    @Sql(scripts = { "classpath:/sql/NoteServiceTest.sql" })
    public void createNote_AsAdmin_Success() throws Exception {
        long entityCount = noteRepository.count();
        var inserted = new NoteDto(-44L, -1L, "sdkljfalöskjf vsdkljsaiuböaerkr");

        noteService.updateOrCreateNote(inserted, ADMIN_DETAILS);
        assertEquals(entityCount + 1, noteRepository.count());

        Note entity = noteService.findNote(USER2_DETAILS, -1L, ADMIN_DETAILS);
        assertTrue(entity.getContent().equals("sdkljfalöskjf vsdkljsaiuböaerkr"));
        assertTrue(entity.getRecipe().getId() == -1L);
        assertTrue(entity.getOwner().getId() == -44L);
    }

    @Test
    @Transactional
    @Sql(scripts = { "classpath:/sql/NoteServiceTest.sql" })
    public void createNote_ForOtherUser_PermissionsDenied() throws Exception {
        long entityCount = noteRepository.count();
        var inserted = new NoteDto(-44L, -1L, "sdkljfalöskjf vsdkljsaiuböaerkr");

        Assertions.assertThrows(UserPermissionException.class, () -> {
            noteService.updateOrCreateNote(inserted, USER_DETAILS);
        });
    }

    @Test
    @Transactional
    @Sql(scripts = { "classpath:/sql/NoteServiceTest.sql" })
    public void updateNote_Success() throws Exception {
        long entityCount = noteRepository.count();
        var inserted = new NoteDto(-43L, -1L, "sdkljfalöskjf vsdkljsaiuböaerkr");

        noteService.updateOrCreateNote(inserted, USER_DETAILS);
        assertEquals(entityCount, noteRepository.count());

        Note entity = noteService.findNote(USER_DETAILS, -1L, USER_DETAILS);
        assertTrue(entity.getContent().equals("sdkljfalöskjf vsdkljsaiuböaerkr"));
        assertTrue(entity.getRecipe().getId() == -1L);
        assertTrue(entity.getOwner().getId() == -43L);
    }

    @Test
    @Transactional
    @Sql(scripts = { "classpath:/sql/NoteServiceTest.sql" })
    public void updateNote_AsAdmin_Success() throws Exception {
        long entityCount = noteRepository.count();
        var inserted = new NoteDto(-43L, -1L, "sdkljfalöskjf vsdkljsaiuböaerkr");

        noteService.updateOrCreateNote(inserted, ADMIN_DETAILS);

        assertEquals(entityCount, noteRepository.count());

        Note entity = noteService.findNote(USER_DETAILS, -1L, USER_DETAILS);
        assertTrue(entity.getContent().equals("sdkljfalöskjf vsdkljsaiuböaerkr"));
        assertTrue(entity.getRecipe().getId() == -1L);
        assertTrue(entity.getOwner().getId() == -43L);
    }

    @Test
    @Transactional
    @Sql(scripts = { "classpath:/sql/NoteServiceTest.sql" })
    public void updateNote_ForOtherUser_PermissionsDenied() throws Exception {
        long entityCount = noteRepository.count();
        var inserted = new NoteDto(-43L, -1L, "sdkljfalöskjf vsdkljsaiuböaerkr");

        Assertions.assertThrows(UserPermissionException.class, () -> {
            noteService.updateOrCreateNote(inserted, USER2_DETAILS);
        });
    }

    @Test
    @Sql(scripts = { "classpath:/sql/NoteServiceTest.sql" })
    public void deleteNote_Success() throws Exception {
        long entityCount = noteRepository.count();
        long usersCount = userRepository.count();
        long recipeCount = recipeRepository.count();

        noteService.deleteNote(-1L, USER_DETAILS, USER_DETAILS);
        assertEquals(usersCount, userRepository.count());
        assertEquals(recipeCount, recipeRepository.count());
        assertEquals(entityCount - 1, noteRepository.count());
    }

    @Test
    @Sql(scripts = { "classpath:/sql/NoteServiceTest.sql" })
    public void deleteNote_AsAdmin_Success() throws Exception {
        long entityCount = noteRepository.count();
        long usersCount = userRepository.count();
        long recipeCount = recipeRepository.count();
        noteService.deleteNote(-1L, USER_DETAILS, ADMIN_DETAILS);
        assertEquals(entityCount - 1, noteRepository.count());
        assertEquals(usersCount, userRepository.count());
        assertEquals(recipeCount, recipeRepository.count());
    }

    @Test
    @Sql(scripts = { "classpath:/sql/NoteServiceTest.sql" })
    public void deleteNote_ForOtherUser_PermissionsDenied() throws Exception {
        long entityCount = noteRepository.count();
        long usersCount = userRepository.count();
        long recipeCount = recipeRepository.count();

        Assertions.assertThrows(UserPermissionException.class, () -> {
            noteService.deleteNote(-1L, USER_DETAILS, USER2_DETAILS);
        });
        assertEquals(entityCount, noteRepository.count());
        assertEquals(usersCount, userRepository.count());
        assertEquals(recipeCount, recipeRepository.count());
    }

}
