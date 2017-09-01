package io.pivotal.fe.samples.music.springmusicgemfire;

import org.springframework.core.env.Environment;

import java.util.*;
import java.util.stream.Collectors;

public class ProfileAppendUtils {

    private static final String PROFILE_SEPARATOR = "_";
    private static final Set<String> expectedProfiles = new HashSet<>(Arrays.asList("local", "dev","qa","staging","prod"));


    public static String appendRegion(String originalName, Environment env){
        String ret = "default";
        String profiles = env.getProperty("spring.profiles.active");
        if (profiles != null) {
            Set<String> profileSet = Arrays.stream(profiles.split(",")).map(String::toLowerCase).map(s -> s.trim()).collect(Collectors.toSet());
            profileSet.retainAll(expectedProfiles);
            if (!profileSet.isEmpty()) {
                //slightly wasteful but deterministic
                List<String> profs = new ArrayList(profileSet);
                Collections.sort(profs);
                ret = profs.get(0);
            }
        }
        return originalName+PROFILE_SEPARATOR+ret;

    }


}
