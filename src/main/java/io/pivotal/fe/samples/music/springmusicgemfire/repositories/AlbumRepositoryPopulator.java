package io.pivotal.fe.samples.music.springmusicgemfire.repositories;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.pivotal.fe.samples.music.springmusicgemfire.domain.Album;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.gemfire.repository.GemfireRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.init.Jackson2ResourceReader;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.UUID;

@Component
public class AlbumRepositoryPopulator  implements CommandLineRunner{
    private final Jackson2ResourceReader resourceReader;
    private final Resource sourceData;

    private ApplicationContext applicationContext;

    public AlbumRepositoryPopulator() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        resourceReader = new Jackson2ResourceReader(mapper);
        sourceData = new ClassPathResource("albums.json");
    }

    @Autowired
    AlbumRepository albumRepository;


    @Override
    public void run(String... args) {
        if (albumRepository != null && albumRepository.count() == 0) {
            populate(albumRepository);
        }
//        if (event.getApplicationContext().equals(applicationContext)) {
//            CrudRepository albumRepository =
//                    BeanFactoryUtils.beanOfTypeIncludingAncestors(applicationContext, CrudRepository.class);
//
//            if (albumRepository != null && albumRepository.count() == 0) {
//                populate(albumRepository);
//            }
//        }

    }

    @SuppressWarnings("unchecked")
    private void populate(CrudRepository repository) {
        Object entity = getEntityFromResource(sourceData);

        if (entity instanceof Collection) {
            for (Album album : (Collection<Album>) entity) {
                if (album != null) {
                    if(album.getId() == null){
                        album.setId(UUID.randomUUID().toString());
                    }
                    repository.save(album);
                }
            }
        } else {
            repository.save(entity);
        }
    }

    private Object getEntityFromResource(Resource resource) {
        try {
            return resourceReader.readFrom(resource, this.getClass().getClassLoader());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
