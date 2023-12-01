package com.example.DATN_API.Service;

import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class ImageService implements IStorageSerivce{

    private final Path storageFolder = Paths.get("uploads");
    private  List<String> images = new ArrayList<>();




    public ImageService() {
        try {
            Files.createDirectories(storageFolder);
        }catch (IOException exception) {
            throw new RuntimeException("Cannot initialize storage", exception);
        }
    }
    // check file image
    private boolean isImageFile(MultipartFile file) {
        //Let install FileNameUtils
        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        return Arrays.asList(new String[] {"png","jpg","jpeg", "bmp"})
                .contains(fileExtension.trim().toLowerCase());
    }
    @Override
    public String storeFile(MultipartFile file) {
        String generatedFileName;

        if (file.isEmpty()) {
            throw new RuntimeException("Failed to store empty file.");
        }
        if (!isImageFile(file)) {
            throw new RuntimeException("You can only upload image file");
        }

        String fileExtention = FilenameUtils.getExtension(file.getOriginalFilename());

        generatedFileName = UUID.randomUUID().toString().replace("-", "");

        generatedFileName = generatedFileName +"."+ fileExtention;


        Path destinationFilePath = this.storageFolder.resolve(
                Paths.get(generatedFileName)
        ).normalize().toAbsolutePath();

        if (!destinationFilePath.getParent().equals(this.storageFolder.toAbsolutePath())) {
            throw new RuntimeException("cannot store file outside current directory");
        }
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, destinationFilePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }


        images.add(generatedFileName);
        return generatedFileName;
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.storageFolder, 1)
                    .filter(path -> !path.equals(this.storageFolder))
                    .map(this.storageFolder::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files!");
        }

    }

    @Override
    public byte[] readFileContent(String fileName) {
        try {
            Path file = storageFolder.resolve(fileName);
            Resource resource = new UrlResource(file.toUri());

            if(resource.exists() || resource.isReadable()){
                byte[] bytes = StreamUtils.copyToByteArray(resource.getInputStream());
                return bytes;
            }else {
                throw new RuntimeException("Could not read file "+fileName);
            }
        }catch (IOException ioException){
            throw new RuntimeException("Could not read file "+fileName, ioException);
        }
    }

    @Override
    public void deleteFiles() {
        FileSystemUtils.deleteRecursively(storageFolder.toFile());
    }

    public List<String> getImages() {
        return images;
    }

    public void removeImages(){
        images.removeAll(getImages());
        System.out.println("imgaes size : "+getImages().size());
    }

    public static void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            if (file.delete()) {
                System.out.println("File đã được xóa thành công!");
            } else {
                System.out.println("Không thể xóa file.");
            }
        } else {
            System.out.println("File không tồn tại.");
        }
    }
}

