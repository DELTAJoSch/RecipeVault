package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.IngredientCreateDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.IngredientDetailsDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.IngredientSearchDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ingredient;
import at.ac.tuwien.sepm.groupphase.backend.exception.DeletionException;
import at.ac.tuwien.sepm.groupphase.backend.exception.InternalServerException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ObjectAlreadyExistsException;
import at.ac.tuwien.sepm.groupphase.backend.exception.UserPermissionException;
import at.ac.tuwien.sepm.groupphase.backend.repository.IngredientRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.IngredientService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;

/**
 * Default implementation of IngredientService.
 */
@Service
public class DefaultIngredientService implements IngredientService {
    @Autowired
    private IngredientRepository ingredientRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public List<Ingredient> search(IngredientSearchDto searchParameters) {
        LOGGER.trace("search({})", searchParameters);

        return ingredientRepository.findTop10ByNameContaining(searchParameters.getName());
    }

    @Override
    public long countFindBy(IngredientSearchDto searchDto) {
        LOGGER.trace("countFindBy({})", searchDto);

        return this.ingredientRepository.countOfFind(
            searchDto.getName(),
            searchDto.getCategory()
        );
    }

    @Override
    public List<Ingredient> findIngredients(IngredientSearchDto ingredientSearchDto, Pageable pageable) {
        LOGGER.trace("findIngredients({}, {})", ingredientSearchDto, pageable);

        return this.ingredientRepository.find(
                ingredientSearchDto.getName(),
                ingredientSearchDto.getCategory(),
                pageable)
            .getContent();
    }

    @Override
    public void createIngredient(IngredientCreateDto ingredient) throws ObjectAlreadyExistsException {
        LOGGER.trace("createIngredient({})", ingredient);

        try {
            Ingredient entity = new Ingredient();
            entity.setName(ingredient.getName());
            entity.setCategory(ingredient.getCategory());

            this.ingredientRepository.saveAndFlush(entity);
        } catch (DataIntegrityViolationException e) {
            throw new ObjectAlreadyExistsException("A similar ingredient already exists!", e);
        }
    }

    @Override
    public Ingredient updateIngredient(IngredientDetailsDto updated, String username) throws NotFoundException,
        UserPermissionException, ObjectAlreadyExistsException {
        LOGGER.trace("updateIngredient({}, {})", updated, username);

        var issuer = this.userRepository.findByEmail(username);
        if (issuer == null) {
            throw new InternalServerException("Der ausstellende Benutzer konnte nicht in der Datenbank gefunden werden.");
        }

        var dbEntry = this.ingredientRepository.findById(updated.getId());
        if (dbEntry.isEmpty()) {
            throw new NotFoundException("Die Zutat konnte nicht in der Datenbank gefunden werden.");
        }

        Ingredient entity = dbEntry.get();

        if (!issuer.getAdmin()) {
            throw new UserPermissionException("Der ausstellende Benutzer ist kein Administrator!");
        }

        try {
            entity.setName(updated.getName());
            entity.setCategory(updated.getCategory());
            this.ingredientRepository.saveAndFlush(entity);

        } catch (DataIntegrityViolationException e) {
            throw new ObjectAlreadyExistsException("Eine ähnliche Zutat existiert bereits. Versuchen Sie, danach zu suchen!", e);
        }

        return entity;
    }

    @Transactional
    @Override
    public void delete(Long id, String username) throws NotFoundException, UserPermissionException, InternalServerException, DeletionException {
        LOGGER.trace("delete({}, {})", id, username);

        var issuer = this.userRepository.findByEmail(username);
        if (issuer == null) {
            throw new InternalServerException("Der ausstellende Benutzer konnte nicht in der Datenbank gefunden werden.");
        }
        if (!issuer.getAdmin()) {
            throw new UserPermissionException("Der ausstellende Benutzer ist kein Administrator!");
        }

        var dbEntry = this.ingredientRepository.findById(id);
        if (dbEntry.isEmpty()) {
            throw new NotFoundException("Die Zutat konnte nicht in der Datenbank gefunden werden.");
        }
        if (dbEntry.get().getAmounts().isEmpty()) {
            Ingredient entity = dbEntry.get();
            this.ingredientRepository.delete(entity);
        } else {
            throw new DeletionException("Zutat ist in Verwendung.");
        }

    }
}
