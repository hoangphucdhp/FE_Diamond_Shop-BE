package com.example.DATN_API.Service;

import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface IStorageSerivce {

    public String storeFile(MultipartFile file);

    public Stream<Path> loadAll();

    public byte[] readFileContent(String fileName);

    public void deleteFiles();
}
