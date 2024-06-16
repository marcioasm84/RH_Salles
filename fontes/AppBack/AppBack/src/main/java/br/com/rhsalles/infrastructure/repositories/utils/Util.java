package br.com.rhsalles.infrastructure.repositories.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;

import org.springframework.web.multipart.MultipartFile;

public class Util {
	
	public static String saveFile(MultipartFile file, String dirUpload) throws IOException {
		
        String fileName = LocalDateTime.now().getSecond()+"_" +file.getOriginalFilename();
        InputStream inputStream = file.getInputStream();
        OutputStream outputStream = new FileOutputStream(dirUpload + "/" + fileName);

        int readBytes;
        byte[] buffer = new byte[8192];
        while ((readBytes = inputStream.read(buffer, 0, 8192)) != -1) {
            outputStream.write(buffer, 0, readBytes);
        }

        outputStream.flush();
        outputStream.close();
        inputStream.close();
        return fileName;
    }
	
	public static void moveFileToDir(String pathOrigem, String pathDestino) throws IOException {
		Path origem = Paths.get(pathOrigem);
		Path destino = Paths.get(pathDestino);
		Files.move(origem, destino, StandardCopyOption.REPLACE_EXISTING);
	}
}
