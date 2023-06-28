package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.WineCreateDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.WineDetailsDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.WineSearchDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Wine;
import at.ac.tuwien.sepm.groupphase.backend.exception.InternalServerException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ObjectAlreadyExistsException;
import at.ac.tuwien.sepm.groupphase.backend.exception.UserPermissionException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.WineRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.WineService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;

/**
 * Default implementation of WineService.
 */
@Service
public class DefaultWineService implements WineService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private WineRepository wineRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Wine getWineById(Long id) throws NotFoundException {
        LOGGER.trace("getWineById({})", id);
        var result = this.wineRepository.findById(id);

        if (!result.isPresent()) {
            throw new NotFoundException("Wein mit ID " + id + " konnte nicht gefunden werden!");
        }

        return result.get();
    }

    @Override
    public long countAll() {
        LOGGER.trace("countAll()");
        return this.wineRepository.count();
    }

    @Override
    public long countFindBy(WineSearchDto searchDto) throws ValidationException {
        LOGGER.trace("countByFind({})", searchDto);

        return this.wineRepository.countOfFind(
            searchDto.getName(),
            searchDto.getVinyard(),
            searchDto.getCategory(),
            searchDto.getCountry()
        );
    }

    @Override
    public List<Wine> findWines(WineSearchDto wineSearchDto, Pageable pageable) throws ValidationException {
        LOGGER.trace("findWines({}, {})", wineSearchDto, pageable);

        return this.wineRepository.find(
                wineSearchDto.getName(),
                wineSearchDto.getVinyard(),
                wineSearchDto.getCategory(),
                wineSearchDto.getCountry(),
                pageable)
            .getContent();
    }

    @Override
    public List<Wine> getWines(Pageable pageable) {
        LOGGER.trace("getWines({})", pageable);
        return wineRepository.findAll(pageable).getContent();
    }

    @Override
    public void createWine(WineCreateDto wine, String username) throws  ObjectAlreadyExistsException {
        LOGGER.trace("createWine({}, {})", wine, username);

        var user = this.userRepository.findByEmail(username);
        if (user == null) {
            throw new InternalServerException("Benutzer konnte nicht in der Datenbank gefunden werden");
        }

        try {
            Wine entity = new Wine();
            entity.setVinyard(wine.getVinyard());
            entity.setTemperature(wine.getTemperature());
            entity.setName(wine.getName());
            entity.setDescription(wine.getDescription());
            entity.setCategory(wine.getCategory());
            entity.setCountry(wine.getCountry());
            entity.setGrape(wine.getGrape());
            entity.setLink(wine.getLink());
            entity.setOwner(user);

            this.wineRepository.saveAndFlush(entity);
        } catch (DataIntegrityViolationException e) {
            throw new ObjectAlreadyExistsException("Ein ähnlicher Wein existiert bereits!", e);
        }
    }

    @Override
    public Wine updateWine(WineDetailsDto updated, String username) throws NotFoundException,
        UserPermissionException,
        ObjectAlreadyExistsException {
        LOGGER.trace("updateWine({}, {})", updated, username);
        
        var issuer = this.userRepository.findByEmail(username);
        if (issuer == null) {
            throw new InternalServerException("Benutzer konnte nicht in der Datenbank gefunden werden");
        }

        var dbEntry = this.wineRepository.findById(updated.getId());
        if (!dbEntry.isPresent()) {
            throw new NotFoundException("Wein konnte nicht in der Datenbank gefunden werden.");
        }

        Wine entity = dbEntry.get();

        if (issuer.getAdmin()) { // if issuer is admin, update all including owner
            var owner = this.userRepository.findByEmail(updated.getOwner().getEmail());
            if (owner == null) {
                throw new InternalServerException("Besitzer konnte nicht in der Datenbank gefunden werden.");
            }

            entity.setOwner(owner);
        } else if (issuer.getId() != entity.getOwner().getId()) { //otherwise compare owner and issuer
            throw new UserPermissionException("Ausführender Benutzer ist nicht Besitzer oder Admin");
        }

        try {
            entity.setVinyard(updated.getVinyard());
            entity.setTemperature(updated.getTemperature());
            entity.setName(updated.getName());
            entity.setDescription(updated.getDescription());
            entity.setCategory(updated.getCategory());
            entity.setCountry(updated.getCountry());
            entity.setGrape(updated.getGrape());
            entity.setLink(updated.getLink());

            this.wineRepository.saveAndFlush(entity);
        } catch (DataIntegrityViolationException e) {
            throw new ObjectAlreadyExistsException("Ein ähnlicher Wein existiert schon. Versuche danach zu suchen!", e);
        }

        return entity;
    }

    @Override
    public void delete(Long id, String username) throws NotFoundException, UserPermissionException {
        LOGGER.trace("delete({}, {})", id, username);
        var issuer = this.userRepository.findByEmail(username);
        if (issuer == null) {
            throw new InternalServerException("Benutzer konnte nicht in der Datenbank gefunden werden");
        }

        var dbEntry = this.wineRepository.findById(id);
        if (!dbEntry.isPresent()) {
            throw new NotFoundException("Wein konnte nicht in der Datenbank gefunden werden.");
        }

        Wine entity = dbEntry.get();

        if (issuer.getId() != entity.getOwner().getId() && !issuer.getAdmin()) {
            throw new UserPermissionException("Ausführender Benutzer ist nicht Besitzer oder Admin");
        }

        this.wineRepository.delete(entity);
    }
}
