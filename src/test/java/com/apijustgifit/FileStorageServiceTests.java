package com.apijustgifit;

import com.apijustgifit.domain.StorageProperties;
import com.apijustgifit.service.FileStorageService;
import com.apijustgifit.validation.FileStorageException;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThatIOException;
import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class FileStorageServiceTests {
	private FileStorageService fileStorageService;

	private StorageProperties fileStorageProperties;

	private MockMultipartFile file;
	@Mock
    FileInputStream inputStream;

	String testFileName;
	@Before
	public void setUp() throws Exception {
		fileStorageProperties = mock(StorageProperties.class);
		when(fileStorageProperties.getUploadDir())
				.thenReturn("uploads/");
		fileStorageService = new FileStorageService(fileStorageProperties);

	}

	@Test
	public void shouldUploadVideoFileMP4format() throws Exception {

		MockMultipartFile file = new MockMultipartFile(
				"myfiletest",
				"NameOfFileTest",
				"video/mp4",
				inputStream);

		String fileResponse = fileStorageService.storeFile(file);

		assertThat(fileResponse).isInstanceOf(String.class);
		assertThat(fileResponse).contains("NameOfFileTest");

	}

    @Test
    public void shoudLoadReadFileInDirectory() throws IOException {
        testFileName = "NameOfFileTest";
        Path filePath = Paths.get(fileStorageProperties.getUploadDir()).resolve(testFileName);

        Resource expectedResource = new UrlResource(filePath.toUri());

        Resource resource = fileStorageService.loadFile(testFileName);
        assertThat(resource).isEqualTo(expectedResource);
        File deleteFile = new File(fileStorageProperties.getUploadDir());
        FileUtils.cleanDirectory(deleteFile);

    }
    @Test
    public void shouldReturnErroMessageWhenFileContainsInvalidCharacters() throws IOException {

	    MockMultipartFile fileInvalid = new MockMultipartFile(
                "myfiletest",
               "..",
                "video/mp4",
                inputStream);

        Throwable thrown = catchThrowable(() -> {
            fileStorageService.storeFile(fileInvalid);
        });

        assertThat(thrown).isInstanceOf(FileStorageException.class)
                .hasMessageContaining("Sorry! Filename contains invalid path sequence ..");

    }
}