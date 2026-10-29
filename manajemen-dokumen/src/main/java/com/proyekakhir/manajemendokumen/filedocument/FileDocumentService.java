package com.proyekakhir.manajemendokumen.filedocument;

import com.proyekakhir.manajemendokumen.constant.ApplicationConstant;
import com.proyekakhir.manajemendokumen.constant.ApplicationEnum;
import com.proyekakhir.manajemendokumen.constant.CurrentUser;
import com.proyekakhir.manajemendokumen.core.exception.AlurKerjaException;
import com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.StorageDto;
import com.proyekakhir.manajemendokumen.services.CrudManajemenService;
import com.proyekakhir.manajemendokumen.services.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.logging.Logger;

@Service
public class FileDocumentService extends CrudManajemenService<FileDocument, FileDocumentDto, FileDocumentRepository> {

    @Autowired
    StorageService storageService;

    protected FileDocumentService(FileDocumentRepository fileDocumentRepository, CurrentUser currentUser) {
        super(fileDocumentRepository, currentUser);
    }

    public FileDocumentDto uploadTemplate(MultipartFile file) throws AlurKerjaException {
        FileDocumentDto fileDocument = new FileDocumentDto();
        StorageDto storageDto = null;
        try {
            storageDto = storageService.upload(file.getBytes(), ApplicationEnum.minioFolder.DOCUMENT.name(), null);
        } catch (IOException e) {
            Logger.getLogger(getClass().getName()).info(e.getMessage());
        }
        if (storageDto != null){
            fileDocument.setFileId(storageDto.getIdentifier());
            fileDocument.setFilePath(storageDto.getFilePath());
            fileDocument.setFileName(file.getOriginalFilename());
            fileDocument.setType("template");
            this.createFromDto(fileDocument);
        }
        return fileDocument;
    }
}
