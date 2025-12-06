package core.config;

import core.domain.club.FitnessNetwork;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

/**
 * Spring configuration class for the fitness club management system.
 * Defines beans for the fitness network.
 * Note: Services are now annotated with @Service and will be auto-detected.
 */
@Configuration
public class AppConfig {
    
    @Value("${fitness.network.name:MyFitness Kyiv}")
    private String networkName;
    
    /**
     * Creates a FitnessNetwork bean.
     * Note: FitnessNetwork uses singleton pattern, so this ensures
     * Spring manages the instance properly.
     *
     * @return the FitnessNetwork instance
     */
    @Bean
    public FitnessNetwork fitnessNetwork() {
        return FitnessNetwork.getInstance(networkName);
    }
}

