package com.proyekakhir.manajemendokumen.auth;

import com.google.gson.Gson;
import com.proyekakhir.manajemendokumen.Application;
import com.proyekakhir.manajemendokumen.core.exception.AlurKerjaException;
import com.proyekakhir.manajemendokumen.menu.Menu;
import com.proyekakhir.manajemendokumen.position.Position;
import com.proyekakhir.manajemendokumen.user.User;
import com.proyekakhir.manajemendokumen.user.UserDto;
import com.proyekakhir.manajemendokumen.user.UserService;
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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(classes = Application.class)
public class LoginAuthTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService mockUserService;

    @Test
    public void testLogin() throws Exception {
        // Setup
        // Configure UserService.cekUser(...).
        final Map<String, Object> map = Map.ofEntries(Map.entry("1811081002", "password-test"));
        when(mockUserService.cekUser(any(UserDto.class))).thenReturn(map);

        String payload = "{\n" +
                "    \"username\":\"1811081002\",\n" +
                "    \"password\":\"password-test\"\n" +
                "}";

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/auth/login")
                        .content(payload).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
//        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    public void testRegister() throws Exception {
        // Setup
        // Configure UserService.createFromDto(...).
        final User user = new User();
        user.setName("Dika");
        user.setUsername("dikaandrajoni");
        user.setEmail("dika@gmail.com");
        user.setRole("admin");
        final Position position = new Position();
        position.setName("administrasi");
        position.setEmailUser("dika@gmail.com");
        position.setNamaUser("Dika");
        position.setNipUser("12345678");
        user.setPosition(position);
        user.setPassword("password-test");
        user.setSignatureId("signatureMinioId");
        user.setSignaturePath("signatureMinioPath");
        user.setSignatureName("signatureFileName");
        final Menu menu = new Menu();
        menu.setName("name");
        user.setMenu(List.of(menu));
        when(mockUserService.createFromDto(any(UserDto.class))).thenReturn(user);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/auth/register")
                        .content(new Gson().toJson(user)).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
//        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    public void testRegister_UserServiceThrowsAlurKerjaException() throws Exception {
        // Setup
        when(mockUserService.createFromDto(any(UserDto.class))).thenThrow(AlurKerjaException.class);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/auth/register")
                        .content("content").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }
}
