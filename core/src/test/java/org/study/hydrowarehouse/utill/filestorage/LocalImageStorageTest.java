package org.study.hydrowarehouse.utill.filestorage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;
import org.study.hydrowarehouse.exception.CoreException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LocalImageStorageTest {
    @Mock
    private FileNameConverter fileNameConverter;

    @InjectMocks
    private LocalImageStorage storage;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setup() throws Exception {
        ReflectionTestUtils.setField(storage, "maxSizeFile", 1024L);
        ReflectionTestUtils.setField(storage, "maxLengthFile", 100);
        ReflectionTestUtils.setField(storage, "maxAllowedFiles", 3);
    }

    @Test
    void save_shouldSaveFileSuccessfully() throws Exception {
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getSize()).thenReturn(512L);
        when(mockFile.getOriginalFilename()).thenReturn("test.jpg");
        when(mockFile.getInputStream()).thenReturn(new ByteArrayInputStream("test content".getBytes()));
        when(fileNameConverter.sanitizeAndTransliterateFileName("test.jpg")).thenReturn("test.jpg");

        String resultPath = storage.save(mockFile, tempDir.toString());

        assertTrue(Files.exists(Paths.get(resultPath)));
    }

    @Test
    void save_shouldThrowException_whenFileTooLarge() {
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getSize()).thenReturn(2048L);

        CoreException ex = assertThrows(CoreException.class,
                () -> storage.save(mockFile, tempDir.toString()));
        assertTrue(ex.getMessage().contains("File size exceeded"));
    }

    @Test
    void generateUniqueName_shouldReturnTransformedName() throws Exception {
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getOriginalFilename()).thenReturn("file.png");
        when(fileNameConverter.sanitizeAndTransliterateFileName("file.png")).thenReturn("file.png");

        String uniqueName = storage.generateUniqueName(mockFile);

        assertTrue(uniqueName.startsWith("file_"));
        assertTrue(uniqueName.endsWith(".png"));
    }

    @Test
    void isBigLengthName_shouldThrowException_whenTooLong() {
        String longName = "a".repeat(200);
        CoreException ex = assertThrows(CoreException.class,
                () -> storage.isBigLengthName(longName));
        assertTrue(ex.getMessage().contains("length exceeded"));
    }

    @Test
    void isNull_shouldThrowException_whenObjectIsNull() {
        CoreException ex = assertThrows(CoreException.class,
                () -> storage.isNull(null, "TestOperation"));
        assertTrue(ex.getMessage().contains("Object is null"));
    }

    @Test
    void isEmptyString_shouldThrowException_whenBlank() {
        CoreException ex = assertThrows(CoreException.class,
                () -> storage.isEmptyString("  ", "EmptyCheck"));
        assertTrue(ex.getMessage().contains("Line is empty"));
    }

    @Test
    void removeFile_shouldDeleteFileSuccessfully() throws Exception {
        Path filePath = Files.createFile(tempDir.resolve("delete.txt"));

        boolean result = storage.removeFile(filePath.toString());

        assertTrue(result);
        assertFalse(Files.exists(filePath));
    }

    @Test
    void saveAll_shouldThrowException_whenTooManyFiles() {
        List<MultipartFile> files = List.of(mock(MultipartFile.class), mock(MultipartFile.class),
                mock(MultipartFile.class), mock(MultipartFile.class));

        CoreException ex = assertThrows(CoreException.class,
                () -> storage.saveAll(files, tempDir.toString()));
        assertTrue(ex.getMessage().contains("limit exceeded"));
    }

    @Test
    void save_shouldThrowException_whenOriginalFilenameIsNull() {
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getSize()).thenReturn(100L);
        when(mockFile.getOriginalFilename()).thenReturn(null);

        CoreException ex = assertThrows(CoreException.class,
                () -> storage.save(mockFile, tempDir.toString()));

        assertTrue(ex.getMessage().contains("Object is null"));
    }

    @Test
    void save_shouldThrowException_whenUploadDirIsNull() {
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getSize()).thenReturn(100L);
        when(mockFile.getOriginalFilename()).thenReturn("file.txt");

        CoreException ex = assertThrows(CoreException.class,
                () -> storage.save(mockFile, null));

        assertTrue(ex.getMessage().contains("Object is null"));
    }

    @Test
    void save_shouldThrowException_whenUploadDirIsBlank() {
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getSize()).thenReturn(100L);
        when(mockFile.getOriginalFilename()).thenReturn("file.txt");

        CoreException ex = assertThrows(CoreException.class,
                () -> storage.save(mockFile, "  "));

        assertTrue(ex.getMessage().contains("Line is empty"));
    }

    @Test
    void save_shouldThrowException_whenCopyFails() throws IOException {
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getSize()).thenReturn(100L);
        when(mockFile.getOriginalFilename()).thenReturn("file.txt");
        when(mockFile.getInputStream()).thenThrow(new IOException("Disk error"));

        when(fileNameConverter.sanitizeAndTransliterateFileName("file.txt"))
                .thenReturn("file.txt");

        CoreException ex = assertThrows(CoreException.class,
                () -> storage.save(mockFile, tempDir.toString()));

        assertTrue(ex.getMessage().contains("The file wasn't save."));
    }

    @Test
    void generateUniqueName_shouldThrowException_whenFilenameIsNull() {
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getOriginalFilename()).thenReturn(null);

        CoreException ex = assertThrows(CoreException.class,
                () -> storage.generateUniqueName(mockFile));

        assertTrue(ex.getMessage().contains("Object is null"));
    }

    @Test
    void generateUniqueName_shouldThrowException_whenFilenameIsBlank() {
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getOriginalFilename()).thenReturn("   ");

        CoreException ex = assertThrows(CoreException.class,
                () -> storage.generateUniqueName(mockFile));

        assertTrue(ex.getMessage().contains("Line is empty"));
    }

    @Test
    void addAdditionalInfoBeforeExtension_shouldHandleNoExtension() throws Exception {
        String result = storage.addAdditionalInfoBeforeExtension("filename", "123456");
        assertEquals("filename_123456", result);
    }

    @Test
    void addAdditionalInfoBeforeExtension_shouldHandleMultipleDots() throws Exception {
        String result = storage.addAdditionalInfoBeforeExtension("image.edit.png", "7890");
        assertEquals("image.edit_7890.png", result);
    }

    @Test
    void removeFile_shouldThrowException_whenPathIsNull() {
        CoreException exception = assertThrows(CoreException.class,
                () -> storage.removeFile(null));
        assertTrue(exception.getMessage().contains("Object is null"));
    }

    @Test
    void removeFile_shouldThrowException_whenPathIsEmpty() {
        CoreException ex = assertThrows(CoreException.class,
                () -> storage.removeFile(""));

        assertTrue(ex.getMessage().contains("Line is empty"));
    }

    @Test
    void removeFile_shouldThrowException_whenDeleteFails() {
        String invalidPath = tempDir.resolve("non_existing_file.txt").toString();

        CoreException ex = assertThrows(CoreException.class,
                () -> storage.removeFile(invalidPath + "/.."));

        assertTrue(ex.getMessage().contains("File does not exist"));
    }

    @Test
    void saveAll_shouldRollback_whenOneFileFails() throws Exception {
        MultipartFile file1 = mock(MultipartFile.class);
        MultipartFile file2 = mock(MultipartFile.class);
        when(file1.getSize()).thenReturn(10L);
        when(file1.getOriginalFilename()).thenReturn("file1.txt");
        when(file1.getInputStream()).thenReturn(new ByteArrayInputStream("file1".getBytes()));

        when(file2.getSize()).thenReturn(2048L);

        when(fileNameConverter.sanitizeAndTransliterateFileName(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        List<MultipartFile> files = List.of(file1, file2);

        CoreException ex = assertThrows(CoreException.class,
                () -> storage.saveAll(files, tempDir.toString()));

        assertTrue(ex.getMessage().contains("Failed to save all files"));

        assertEquals(0, Files.list(tempDir).count());
    }

    @Test
    void saveAll_shouldReturnEmptyList_whenNoFiles() throws Exception {
        List<String> result = storage.saveAll(List.of(), tempDir.toString());
        assertTrue(result.isEmpty());
    }

}