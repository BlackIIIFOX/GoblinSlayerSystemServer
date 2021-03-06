package com.itmo.goblinslayersystemserver.services.implementation;

import com.itmo.goblinslayersystemserver.dao.FileDao;
import com.itmo.goblinslayersystemserver.dao.UserDao;
import com.itmo.goblinslayersystemserver.exceptions.NotFoundException;
import com.itmo.goblinslayersystemserver.models.FileInfo;
import com.itmo.goblinslayersystemserver.models.FileStorageResult;
import com.itmo.goblinslayersystemserver.models.GetFileResult;
import com.itmo.goblinslayersystemserver.repositories.FilesRepository;
import com.itmo.goblinslayersystemserver.services.IFileStorageService;
import com.itmo.goblinslayersystemserver.services.IFilesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class FilesService implements IFilesService {
    @Autowired
    FilesRepository filesRepository;

    @Autowired
    IFileStorageService fileStorageService;

    @Override
    public GetFileResult get(Integer id) throws IOException {
        Optional<FileDao> fileInfoOptional = filesRepository.findById(id);
        if (!fileInfoOptional.isPresent()) {
            throw new NotFoundException();
        }

        FileDao fileInfoDao = fileInfoOptional.get();
        GetFileResult result = new GetFileResult();
        result.setOriginalName(fileInfoDao.getOriginalName());

        try {
            Resource fileResource = fileStorageService.loadFileAsResource(fileInfoDao.getLocalName());
            result.setFileResource(fileResource);
            return result;
        } catch (Exception ex) {
            throw new IOException(ex);
        }
    }

    @Override
    public FileInfo upload(MultipartFile file, UserDao user) {

        FileStorageResult fileStorageResult = fileStorageService.storeFile(file);
        FileDao fileInfoDao = new FileDao();
        fileInfoDao.setUser(user);
        fileInfoDao.setOriginalName(fileStorageResult.getOriginalName());
        fileInfoDao.setLocalName(fileStorageResult.getStorageName());

        filesRepository.save(fileInfoDao);

        FileInfo fileInfo = new FileInfo();
        fileInfo.setFileName(fileInfoDao.getOriginalName());
        fileInfo.setCreateTime(fileInfoDao.getCreated());
        fileInfo.setId(fileInfoDao.getId());
        fileInfo.setUser(fileInfoDao.getUser());

        return fileInfo;
    }
}
