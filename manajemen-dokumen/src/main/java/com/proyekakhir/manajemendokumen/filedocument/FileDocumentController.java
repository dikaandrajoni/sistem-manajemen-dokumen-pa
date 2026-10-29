package com.proyekakhir.manajemendokumen.filedocument;

import com.proyekakhir.manajemendokumen.core.controller.CrudController;
import com.proyekakhir.manajemendokumen.core.exception.AlurKerjaException;
import com.proyekakhir.manajemendokumen.services.StorageService;
import com.proyekakhir.manajemendokumen.user.UserRepository;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.NotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/api/v1/filedocument")
public class FileDocumentController extends CrudController<FileDocument, FileDocumentDto, FileDocumentService, FileDocumentRepository> {
    @Autowired
    StorageService minioService;

    @Autowired
    FileDocumentService fileDocumentService;

    @Autowired
    FileDocumentRepository fileDocumentRepository;

    @Autowired
    UserRepository userRepository;

    protected FileDocumentController(FileDocumentService fileDocumentService) {
        super(fileDocumentService);
    }

    @GetMapping("/generateMinioUrl")
    public ResponseEntity<Object> getDownloadUrl(@RequestParam String minioId) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        FileDocument file = fileDocumentRepository.findByFileId(minioId);
        if (file != null){
            return success(minioService.getDownloadUrl(file.getFilePath()));
        } else {
            return notFound("File not found");
        }
    }

    @PostMapping("/uploadTemplate")
    public ResponseEntity<Object> uploadProposal(@RequestParam MultipartFile file) throws IOException, AlurKerjaException {
        return success(fileDocumentService.uploadTemplate(file));
    }

    @GetMapping("/downloadTemplate")
    public ResponseEntity<Resource> downloadProposal(){
        FileDocument document = fileDocumentRepository.findByTypeEquals("template");
        if (document != null){
            ByteArrayResource resource = minioService.download(document.getFilePath());

            HttpHeaders header = new HttpHeaders();
            String name = document.getFileName().replaceAll(" ", "_");

            header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+name);
            header.add("Cache-Control", "no-cache, no-store, must-revalidate");
            header.add("Pragma", "no-cache");
            header.add("Expires", "0");
            header.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Authorization,Content-Type, Content-Range, Content-Disposition, Content-Description,Origin, X-Requested-With, sessionId");

            return ResponseEntity.ok()
                    .headers(header)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } else {
            throw new NotFoundException("Template belum diupload!");
        }
    }

}
