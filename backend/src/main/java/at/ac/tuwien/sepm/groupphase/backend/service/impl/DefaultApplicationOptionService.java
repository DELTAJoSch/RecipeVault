package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ApplicationOptionDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationOption;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.ApplicationOptionRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.ApplicationOptionService;
import at.ac.tuwien.sepm.groupphase.backend.validators.ApplicationOptionValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The default ApplicationOptionService.
 */
@Service
public class DefaultApplicationOptionService implements ApplicationOptionService {
    private final ApplicationOptionRepository applicationOptionRepository;

    @Autowired
    public DefaultApplicationOptionService(ApplicationOptionRepository applicationOptionRepository) {
        this.applicationOptionRepository = applicationOptionRepository;
    }

    @Override
    public List<ApplicationOption> findAll(Pageable pageable) {
        return applicationOptionRepository.findAll(pageable).toList();
    }

    @Override
    public List<ApplicationOption> findLike(String name, Pageable pageable) throws ValidationException {
        return applicationOptionRepository.findByNameContaining(name, pageable).toList();
    }

    @Override
    public ApplicationOption findByName(String name) throws NotFoundException, ValidationException {
        var result = applicationOptionRepository.findByName(name);

        if (result.isPresent()) {
            return result.get();
        } else {
            throw new NotFoundException("Anwendungsoption mit dem gegebenen Namen konnte nicht gefunden werden.");
        }
    }

    @Override
    public ApplicationOption update(ApplicationOptionDto option, Long id) throws ValidationException, NotFoundException {
        var old = applicationOptionRepository.findById(id);
        if (!old.isPresent()) {
            throw new NotFoundException("Anwendungsoption mit id " + id + " konnte nicht gefunden werden.");
        }

        var oldOption = old.get();

        ApplicationOptionValidator.validateForUpdate(option, oldOption);

        oldOption.setValue(option.getValue());
        applicationOptionRepository.saveAndFlush(oldOption);

        return oldOption;
    }

    @Override
    public long countAll(String name) throws ValidationException {
        return applicationOptionRepository.countByNameContaining(name);
    }
}
