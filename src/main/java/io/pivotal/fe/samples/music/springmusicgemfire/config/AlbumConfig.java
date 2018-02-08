package io.pivotal.fe.samples.music.springmusicgemfire.config;

import com.gemstone.gemfire.cache.GemFireCache;
import com.gemstone.gemfire.cache.client.ClientCache;
import com.gemstone.gemfire.cache.client.ClientCacheFactory;
import com.gemstone.gemfire.cache.client.ClientRegionShortcut;
import com.gemstone.gemfire.pdx.PdxSerializer;
import com.gemstone.gemfire.pdx.ReflectionBasedAutoSerializer;
import io.pivotal.fe.samples.music.springmusicgemfire.ProfileAppendUtils;
import io.pivotal.fe.samples.music.springmusicgemfire.domain.Album;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.data.gemfire.client.ClientRegionFactoryBean;
import org.springframework.data.gemfire.config.xml.GemfireConstants;
import org.springframework.data.gemfire.mapping.GemfireMappingContext;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;

import java.util.Properties;

@Configuration
@EnableGemfireRepositories
@Profile("default")
public class AlbumConfig {

    @Bean
    Properties gemfireProperties(@Value("${gemfire.log.level:config}") String logLevel,
                                 @Value("${application.name:GemFireClientDemo}") String applicationName) {
        Properties gemfireProperties = new Properties();

        gemfireProperties.setProperty("name", applicationName);
        gemfireProperties.setProperty("log-level", logLevel);

        return gemfireProperties;
    }

    PdxSerializer pdxSerializer() {

        PdxSerializer pdxSerializer = new ReflectionBasedAutoSerializer(".*");
        return pdxSerializer;
    }

    @Bean(name = GemfireConstants.DEFAULT_GEMFIRE_CACHE_NAME)
    ClientCache gemfireCache(@Qualifier("gemfireProperties") Properties gemfireProperties,
                             @Value("${gemfire.locator.host:localhost}") String locatorHost,
                             @Value("${gemfire.locator.port:10334}") int locatorPort) {

        ClientCache gemfireCache = new ClientCacheFactory(gemfireProperties)
                .addPoolLocator(locatorHost, locatorPort)
                .setPdxSerializer(pdxSerializer())
                .setPdxReadSerialized(false).create();

        return gemfireCache;
    }


    @Bean
    ClientRegionFactoryBean<String, Album> customerRegion(final GemFireCache cache) {
        ClientRegionFactoryBean<String, Album> helloRegion = new ClientRegionFactoryBean<>();
        helloRegion.setCache(cache);
        helloRegion.setClose(false);
        helloRegion.setPersistent(false);
        helloRegion.setShortcut(ClientRegionShortcut.PROXY);
        helloRegion.setName(ProfileAppendUtils.appendRegion("Album", env));
        return helloRegion;
    }


    @Autowired
    private Environment env;


    @Bean("myMappingContext")
    public GemfireMappingContext wrappedGFMappingContext() {
        return new GemFireMappingContextWrapper();
    }


}
