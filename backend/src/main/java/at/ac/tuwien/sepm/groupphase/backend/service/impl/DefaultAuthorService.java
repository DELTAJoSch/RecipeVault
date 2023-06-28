package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.AuthorCreateDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.AuthorDetailsDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Author;
import at.ac.tuwien.sepm.groupphase.backend.exception.DeletionException;
import at.ac.tuwien.sepm.groupphase.backend.exception.InternalServerException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ObjectAlreadyExistsException;
import at.ac.tuwien.sepm.groupphase.backend.exception.UserPermissionException;
import at.ac.tuwien.sepm.groupphase.backend.repository.AuthorRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.RecipeRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.AuthorService;
import at.ac.tuwien.sepm.groupphase.backend.service.ImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;

/**
 * Default implementation of AuthorService.
 */
@Service
public class DefaultAuthorService implements AuthorService {
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ImageService imageService;
    @Autowired
    private RecipeRepository recipeRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public Author getAuthorById(Long id) throws NotFoundException {
        LOGGER.trace("getAuthorById({})", id);

        var result = this.authorRepository.findById(id);
        if (result.isEmpty()) {
            throw new NotFoundException("Autor mit ID " + id + " konnte nicht gefunden werden!");
        }
        return result.get();
    }

    @Override
    public List<Author> getAuthors(Pageable pageable) {
        LOGGER.trace("getAuthors({})", pageable);
        return authorRepository.findAll(pageable).getContent();
    }

    @Override
    public long countAll() {
        return this.authorRepository.count();
    }

    @Override
    public void createAuthor(AuthorCreateDto author) throws ObjectAlreadyExistsException {
        LOGGER.trace("createAuthor({})", author);

        try {
            Author entity = new Author();
            entity.setFirstname(author.getFirstname());
            entity.setLastname(author.getLastname());
            entity.setDescription(author.getDescription());
            entity.setImageId(author.getImageId());

            this.authorRepository.saveAndFlush(entity);
        } catch (DataIntegrityViolationException e) {
            throw new ObjectAlreadyExistsException("Ein Autor mit demselben Vor- und Nachnamen existiert bereits.");
        }
    }

    @Override
    public Author updateAuthor(AuthorDetailsDto updated) throws ObjectAlreadyExistsException {
        LOGGER.trace("updateAuthor({})", updated);

        var dbEntry = this.authorRepository.findById(updated.getId());
        if (dbEntry.isEmpty()) {
            throw new NotFoundException("Der Autor konnte in der Datenbank nicht gefunden werden.");
        }

        Author entity = dbEntry.get();
        try {
            entity.setFirstname(updated.getFirstname());
            entity.setLastname(updated.getLastname());
            entity.setDescription(updated.getDescription());
            entity.setImageId(updated.getImageId());

            this.authorRepository.saveAndFlush(entity);
        } catch (DataIntegrityViolationException e) {
            throw new ObjectAlreadyExistsException("Ein Autor mit demselben Vor- und Nachnamen existiert bereits.");
        }

        return entity;
    }

    @Override
    public void deleteAuthor(Long id, String username) throws NotFoundException, UserPermissionException, DeletionException {
        LOGGER.trace("deleteAuthor({}, {})", id, username);

        var issuer = this.userRepository.findByEmail(username);
        if (issuer == null) {
            throw new InternalServerException("Der Benutzer konnte in der Datenbank nicht gefunden werden.");
        }

        if (!issuer.getAdmin()) {
            throw new UserPermissionException("Der ausstellende Benutzer ist kein Administrator!");
        }

        var dbEntry = this.authorRepository.findById(id);
        if (dbEntry.isEmpty()) {
            throw new NotFoundException("Der Autor konnte in der Datenbank nicht gefunden werden!");
        }

        if (this.recipeRepository.findByAuthorId(id).isEmpty()) {
            Author authorEntity = dbEntry.get();
            Long imageId = authorEntity.getImageId();

            this.authorRepository.delete(authorEntity);

            if (imageId != null) {
                imageService.deleteImage(imageId);
            }
        } else {
            throw new DeletionException("Der Autor wird verwendet und kann nicht gelöscht werden.");
        }
    }

    @Override
    public List<Author> search(String searchParameters) {
        LOGGER.trace("search({})", searchParameters);
        return authorRepository.findTop10ByName(searchParameters);
    }
}
