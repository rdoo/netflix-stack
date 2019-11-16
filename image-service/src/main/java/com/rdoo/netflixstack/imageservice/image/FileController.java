package com.rdoo.netflixstack.imageservice.image;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.tomcat.util.http.fileupload.FileItemIterator;
import org.apache.tomcat.util.http.fileupload.FileItemStream;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/images") // TODO files
public class FileController {
    @Autowired
    private FileService fileService;

    @GetMapping
    public List<FileDTO> getAll(Authentication authentication) {
        return this.fileService.getAll(this.getName());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        try {
            return this.fileService.getById(id, this.getName()).map(file -> {
                return ResponseEntity.ok().contentLength(file.getSize())
                        .header(HttpHeaders.CONTENT_DISPOSITION,
                                "attachment; filename=" + file.getFilename().replace(" ", "_"))
                        .body(new InputStreamResource(file.getInputStream()));
            }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        } catch (IllegalStateException | IOException e) {
            return ResponseEntity.unprocessableEntity().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> create(HttpServletRequest request) {
        if (!ServletFileUpload.isMultipartContent(request)) {
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).build();
        }

        ObjectId createdFileId = null;
        try {
            ServletFileUpload fileUpload = new ServletFileUpload();
            FileItemIterator itemIterator = fileUpload.getItemIterator(request);
            while (itemIterator.hasNext()) {
                FileItemStream item = itemIterator.next();
                if (!item.isFormField()) {
                    createdFileId = this.fileService.create(item.openStream(), item.getName(), this.getName());
                    break;
                }
            }
        } catch (FileUploadException | IOException e) {
            e.printStackTrace();
            return ResponseEntity.unprocessableEntity().build();
        }

        if (createdFileId == null) {
            System.out.println("no id");
            return ResponseEntity.unprocessableEntity().build();

        }

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(createdFileId.toString()).toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        this.fileService.delete(id, this.getName());
        return ResponseEntity.noContent().build();
    }

    public String getName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            return "user";
        }

        // return authentication.getName(); // TODO

        System.out.println(authentication.getName());

        return "user";
    }
}