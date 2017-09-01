package io.pivotal.fe.samples.music.springmusicgemfire.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.gemfire.mapping.GemfireMappingContext;
import org.springframework.data.gemfire.mapping.GemfirePersistentEntity;
import org.springframework.data.util.TypeInformation;

public class GemFireMappingContextWrapper extends GemfireMappingContext {

    @Autowired
    private Environment env;

    public GemFireMappingContextWrapper() {
        super();
    }

    @Override
    protected <T> GemfirePersistentEntity<T> createPersistentEntity(TypeInformation<T> typeInformation) {
        return new ProfileAppendingPersistentEntityWrapper(typeInformation, env);
    }
}
