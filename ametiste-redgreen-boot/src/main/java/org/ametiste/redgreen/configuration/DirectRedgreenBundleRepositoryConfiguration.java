package org.ametiste.redgreen.configuration;

import org.ametiste.redgreen.RedgreenComponentsFactory;
import org.ametiste.redgreen.data.RedgreenBundleRepostitory;
import org.ametiste.redgreen.infrastructure.DirectRedgreenBundleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.MalformedURLException;

/**
 * <p>
 *  Configures {@link DirectRedgreenBundleRepository} using provided properties.
 * </p>
 *
 * <p>
 *  See {@link DirectRedgreenBundleRepositoryProperties} for properties layout details.
 * </p>
 *
 * @see DirectRedgreenBundleRepositoryProperties
 * @since 0.1.0
 */
@Configuration
@EnableConfigurationProperties(DirectRedgreenBundleRepositoryProperties.class)
public class DirectRedgreenBundleRepositoryConfiguration {

    @Autowired
    private DirectRedgreenBundleRepositoryProperties properties;

    @Autowired
    private RedgreenComponentsFactory redgreenComponentsFactory;

    @Bean
    public RedgreenBundleRepostitory directRedgreenBundleRepository() throws MalformedURLException {
         return new DirectRedgreenBundleRepository(
                 properties.getComposedBundles(),
                 properties.getComposedErrorBundle()
         );
    }

}
