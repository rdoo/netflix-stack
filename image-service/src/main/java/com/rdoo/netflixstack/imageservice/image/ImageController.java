package com.rdoo.netflixstack.imageservice.image;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;

import org.apache.tomcat.util.http.fileupload.FileItemIterator;
import org.apache.tomcat.util.http.fileupload.FileItemStream;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.util.Streams;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/images")
public class ImageController {
    @Autowired
    private ImageService imageService;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @GetMapping
    public List<Image> getAll(Authentication authentication) {

        GridFSFindIterable iter = this.gridFsTemplate
                .find(new Query(Criteria.where("metadata.user").is(authentication.getName())));
        // return iter.map(item -> {
        // Image image = new Image();
        // image.setName(item.getFilename());
        // // new Image()
        // return image;
        // }).;

        return StreamSupport.stream(iter.spliterator(), false).map(item -> {
            Image image = new Image();
            image.setId(item.getObjectId().toString());
            image.setName(item.getFilename());
            image.setDescription((String) item.getMetadata().get("description"));
            image.setCreatedDate(item.getUploadDate());
            // new Image()
            return image;
        }).collect(Collectors.toList());

        // return this.imageRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) throws IllegalStateException, IOException {
        // Authentication authentication =
        // SecurityContextHolder.getContext().getAuthentication();
        GridFSFile file = this.gridFsTemplate.findOne(new Query(Criteria.where("_id").is(id)));
        // return this.userService.getById(id).map(user -> ResponseEntity.ok(user))
        // .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        // GridFsResource resource = this.gridFsTemplate.getResource(file);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentLength(file.getLength());

        // Files.size(Paths.get(filePath)

        // return ResponseEntity.ok(new InputStreamResource(new
        // GridFsResource(file).getInputStream()));
        // return new ResponseEntity(new InputStreamResource(new
        // GridFsResource(file).getInputStream()),headers, HttpStatus.OK);

        InputStream stream = this.gridFsTemplate.getResource(file).getInputStream();
        // new GridFsResource(file).getInputStream();

        return ResponseEntity.ok().contentLength(file.getLength())
                // .contentType(MediaType.parseMediaType("image/jpeg"))
                .body(new InputStreamResource(stream));
    }

    @PostMapping
    public ResponseEntity<?> create(HttpServletRequest request) throws IOException, FileUploadException {
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);

        System.out.println("is multipart " + isMultipart);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        DBObject metadata = new BasicDBObject(); 
        // metadata.put("description", description);  
        metadata.put("user", authentication.getName()); 

        ServletFileUpload upload = new ServletFileUpload();

        FileItemIterator iter = upload.getItemIterator(request);
        String imageName = null;
        InputStream imageStream = null;
        System.out.println(iter.hasNext());
        while (iter.hasNext()) {
            FileItemStream item = iter.next();
            String name = item.getFieldName();
            InputStream stream = item.openStream();
            if (item.isFormField()) {
                if (name.equals("description")) {
                    String desc = Streams.asString(stream);
                    System.out.println("Form field " + name + " with value "
                    + desc + " detected.");
                    metadata.put("description", desc);  
                }

            } else {
                System.out.println("File field " + name + " with file name "
                    + item.getName() + " detected.");
                // Process the input stream
                // ...
                imageName = item.getName();
                imageStream = stream;
            }
        }

        // if (imageItem != null) {
            this.gridFsTemplate.store(imageStream, imageName, metadata);
        // }

        // Image image = new Image();
        // image.setImage(new Binary(BsonBinarySubType.BINARY, multipartFile.getBytes()));
        // image.setName(multipartFile.getOriginalFilename());
        // image.setDescription(description);
        // this.imageRepository.insert(image);
        // Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // DBObject metadata = new BasicDBObject(); 
        // metadata.put("description", description);  
        // metadata.put("user", authentication.getName()); 
        // this.gridFsTemplate.store(multipartFile.getInputStream(), multipartFile.getOriginalFilename(), metadata);

        return ResponseEntity.noContent().build();
    }

    // @GetMapping
    // @PreAuthorize("hasRole('USER')") // TODO ADMIN
    // public List<User> getAll() {
    //     return this.userService.getAll();
    // }

    // @GetMapping("/{id}")
    // @PreAuthorize("hasRole('ADMIN')")
    // public ResponseEntity<?> getById(@PathVariable String id) {
    //     return this.userService.getById(id).map(user -> ResponseEntity.ok(user))
    //             .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    // }

    // @PutMapping("/{id}")
    // @PreAuthorize("hasRole('ADMIN')")
    // public ResponseEntity<?> update(@PathVariable String id, @Valid @RequestBody User user) {
    //     return this.userService.update(id, user).map(updatedUser -> ResponseEntity.noContent().build())
    //             .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    // }

    // @DeleteMapping("/{id}")
    // @PreAuthorize("hasRole('ADMIN')")
    // public ResponseEntity<?> delete(@PathVariable String id) {
    //     return this.userService.delete(id).map(deletedUser -> ResponseEntity.noContent().build())
    //             .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    // }
}