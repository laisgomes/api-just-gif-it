package com.apijustgifit;

import com.apijustgifit.domain.StorageProperties;
import com.apijustgifit.service.FileStorageService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.mock.web.MockMultipartFile;

import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class FileStorageServiceTests {
	private FileStorageService fileStorageService;

	private StorageProperties fileStorageProperties;

	@Mock
    FileInputStream inputStream;

	@Before
	public void setUp() throws Exception {
		fileStorageProperties = mock(StorageProperties.class);
		when(fileStorageProperties.getUploadDir())
				.thenReturn("");
		fileStorageService = new FileStorageService(fileStorageProperties);
	}

	@Test
	public void shouldUploadVideoFileMP4format() throws Exception {

		MockMultipartFile file = new MockMultipartFile(
				"myfiletest",
				"NameOfFileTest",
				"",
				inputStream);

		String fileResponse = fileStorageService.store(file);

		assertThat(fileResponse).isInstanceOf(String.class);
		assertThat(fileResponse).contains("NameOfFileTest");

	}

}