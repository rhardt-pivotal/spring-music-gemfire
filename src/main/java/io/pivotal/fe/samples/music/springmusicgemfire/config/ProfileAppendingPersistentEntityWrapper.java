package io.pivotal.fe.samples.music.springmusicgemfire.config;

import io.pivotal.fe.samples.music.springmusicgemfire.ProfileAppendUtils;
import org.springframework.core.env.Environment;
import org.springframework.data.gemfire.mapping.GemfirePersistentEntity;
import org.springframework.data.util.TypeInformation;

public class ProfileAppendingPersistentEntityWrapper extends GemfirePersistentEntity {

    private Environment env;

    public ProfileAppendingPersistentEntityWrapper(TypeInformation information, Environment env) {
        super(information);
        assert (env != null);
        this.env = env;
    }
    @Override
    public String getRegionName() {
        String parentName = super.getRegionName();
        return ProfileAppendUtils.appendRegion(parentName, env);
    }

}
