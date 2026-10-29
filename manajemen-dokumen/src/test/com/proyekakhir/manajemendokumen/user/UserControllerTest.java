package com.proyekakhir.manajemendokumen.user;

import com.proyekakhir.manajemendokumen.Application;
import com.proyekakhir.manajemendokumen.core.service.email.EmailService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(classes = Application.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmailService emailService;

    @MockBean
    private UserService mockCrudService;

    @Test
    public void testUploadSignature() throws Exception {
        // Setup
        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(
                        multipart("/api/v1/user/{id}/upload-signature", "47c12d15-3234-472b-b1a5-7c002e3c0df2")
                                .file(new MockMultipartFile("file", "originalFilename", MediaType.APPLICATION_JSON_VALUE,
                                        "content".getBytes()))
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    public void testEmail(){
        Map<String, Object> content = new HashMap<>();
        content.put("nama", "Dika Andra Joni");
        content.put("header", "Seminar Proposal");
        content.put("bp", "1811081002");
        content.put("judul", "Analisis Sistem Manajemen Dokumen");
        content.put("pembimbing1", "Rahmat Hidayat");
        content.put("pembimbing2", "Deddy Prayama");
        content.put("penguji", "Yulherniwati");
        SimpleDateFormat formatter = new SimpleDateFormat("E, dd MMM yyyy HH:mm");
        String jadwal = formatter.format(new Date());
        content.put("jadwal", jadwal);

        emailService.sendWithHtml("info@gmail.com", "dika.andra.joni@gmail.com", "Notifikasi Jadwal Seminar Proposal", "Jadwal.html", content);

    }

    @Test
    public void testUploadSignature_ThrowsIOException() throws Exception {
        // Setup
        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(
                        multipart("/api/v1/user/{id}/upload-signature", "47c12d15-3234-472b-b1a5-7c002e3c0df2")
                                .file(new MockMultipartFile("file", "originalFilename", MediaType.APPLICATION_JSON_VALUE,
                                        "content".getBytes()))
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    public void testSetPassword() throws Exception {
        // Setup
        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(
                        post("/api/v1/user/{id}/setPassword", "e6ca67e3-e0ff-4212-8ee8-7e9a7321ec88")
                                .content("content").contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    public void testUbahPassword() throws Exception {
        // Setup
        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/api/v1/user/ubah-password")
                        .content("content").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }
}
