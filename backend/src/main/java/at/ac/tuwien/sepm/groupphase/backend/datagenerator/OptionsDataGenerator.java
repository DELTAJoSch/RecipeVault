package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationOption;
import at.ac.tuwien.sepm.groupphase.backend.repository.ApplicationOptionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

@Profile("generateData")
@Component
public class OptionsDataGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final ApplicationOptionRepository optionRepository;

    @Autowired
    public OptionsDataGenerator(ApplicationOptionRepository optionRepository) {
        this.optionRepository = optionRepository;
    }

    /**
     * Generate default options.
     */
    public void generate() {
        LOGGER.trace("OptionsDataGenerator: generate()");
        // ADD OPTIONS HERE
    }

    /**
     * Adds the option if it doesn't already exist.
     *
     * @param option The option to add
     */
    private void addIfNotExists(ApplicationOption option) {
        LOGGER.trace("addIfNotExists({})", option);
        if (this.optionRepository.findByName(option.getName()).isEmpty()) {
            this.optionRepository.saveAndFlush(option);
        }
    }
}
