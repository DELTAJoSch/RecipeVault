package at.ac.tuwien.sepm.groupphase.backend.config;

import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.stereotype.Component;

/**
 * Runs code on startup just before ports are opened.
 */
@Component
public class StartupInitializer implements SmartInitializingSingleton {
    @Override
    public void afterSingletonsInstantiated() {
        // Load OpenCV
        nu.pattern.OpenCV.loadLocally();
        //-System.loadLibrary(org.opencv.core.Core.NATIVE_LIBRARY_NAME);
    }
}
