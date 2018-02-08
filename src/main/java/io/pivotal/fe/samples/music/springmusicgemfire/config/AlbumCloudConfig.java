package io.pivotal.fe.samples.music.springmusicgemfire.config;

import com.gemstone.gemfire.cache.client.ClientCache;
import com.gemstone.gemfire.cache.client.ClientCacheFactory;
//import com.gemstone.gemfire.pdx.ReflectionBasedAutoSerializer;
import io.pivotal.spring.cloud.service.common.GemfireServiceInfo;
import org.apache.geode.security.AuthInitialize;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.Cloud;
import org.springframework.cloud.CloudFactory;
//import org.springframework.cloud.service.ServiceInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.gemfire.config.xml.GemfireConstants;

import java.net.URI;
import java.util.Properties;

@Configuration
@Profile("cloud")
public class AlbumCloudConfig extends AlbumConfig{


    private static final String SECURITY_CLIENT = "security-client-auth-init";
    private static final String SECURITY_USERNAME = "security-username";
    private static final String SECURITY_PASSWORD = "security-password";

    @Override
    @Bean(name = GemfireConstants.DEFAULT_GEMFIRE_CACHE_NAME)
    ClientCache gemfireCache(@Qualifier("gemfireProperties") Properties gemfireProperties,
                             @Value("${gemfire.locator.host:localhost}") String locatorHost,
                             @Value("${gemfire.locator.port:10334}") int locatorPort) {

        Cloud cloud = new CloudFactory().getCloud();
        GemfireServiceInfo serviceInfo = (GemfireServiceInfo) cloud.getServiceInfos().get(0);
        ClientCacheFactory factory = new ClientCacheFactory();
        for (URI locator : serviceInfo.getLocators()) {
            factory.addPoolLocator(locator.getHost(), locator.getPort());
        }
        factory.set(SECURITY_CLIENT, "io.pivotal.fe.samples.music.springmusicgemfire.config.UserAuthInitialize.create");
        factory.set(SECURITY_USERNAME, serviceInfo.getDevUsername());
        factory.set(SECURITY_PASSWORD, serviceInfo.getDevPassword());
        factory.setPdxSerializer(pdxSerializer());
        //factory.setPoolSubscriptionEnabled(true); // to enable CQ
        return factory.create();

    }



}
