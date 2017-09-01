package io.pivotal.fe.samples.music.springmusicgemfire.repositories;

import io.pivotal.fe.samples.music.springmusicgemfire.domain.Album;
import org.springframework.data.gemfire.repository.GemfireRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


public interface AlbumRepository extends GemfireRepository<Album, String> {
}
