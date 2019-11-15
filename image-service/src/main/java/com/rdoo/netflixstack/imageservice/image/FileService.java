package com.rdoo.netflixstack.imageservice.image;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

@Service
public class FileService {
    @Autowired
    private GridFsTemplate gridFsTemplate;

    public List<FileDTO> getAll(String username) {
        GridFSFindIterable iterable = this.gridFsTemplate.find(new Query(Criteria.where("metadata.user").is(username)));

        return StreamSupport.stream(iterable.spliterator(), false).map(item -> new FileDTO(item))
                .collect(Collectors.toList());
    }

    public Optional<FileDTO> getById(String id, String username) throws IllegalStateException, IOException {
        GridFSFile file = this.gridFsTemplate
                .findOne(new Query(Criteria.where("_id").is(id).and("metadata.user").is(username)));

        if (file == null) {
            return Optional.empty();
        }

        FileDTO fileDTO = new FileDTO(file);
        fileDTO.setInputStream(this.gridFsTemplate.getResource(file).getInputStream());
        return Optional.of(fileDTO);
    }

    public ObjectId create(InputStream inputStream, String filename, String username) {
        DBObject metadata = new BasicDBObject();
        metadata.put("user", username);
        return this.gridFsTemplate.store(inputStream, filename, metadata);
    }

    public void delete(String id, String username) {
        this.gridFsTemplate.delete(new Query(Criteria.where("_id").is(id).and("metadata.user").is(username)));
    }
}