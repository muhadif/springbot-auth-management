package id.muhadif.spring.authmanagement.controller;

import com.opencsv.CSVWriter;
import id.muhadif.spring.authmanagement.dto.UserDto;
import id.muhadif.spring.authmanagement.service.FileStorageService;
import jakarta.validation.Valid;
import id.muhadif.spring.authmanagement.dto.UpdateUserDto;
import id.muhadif.spring.authmanagement.dto.UserDto;
import id.muhadif.spring.authmanagement.entity.User;
import id.muhadif.spring.authmanagement.pkg.PdfGenerator;
import id.muhadif.spring.authmanagement.service.FileStorageService;
import id.muhadif.spring.authmanagement.service.UserService;
import org.hibernate.annotations.Any;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;


@Controller
public class AdminController {
    private UserService userService;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/uploads";

    @GetMapping("/admin")
    public String index(Model model) {
        List<UserDto> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "admin/index";
    }

    @GetMapping("/admin/user")
    public String addUser(Model model) {
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "admin/add-user";
    }

    @GetMapping("/admin/user/{id}")
    public String getUserById(@PathVariable Long id, Model model) {
        UserDto user = userService.findUserById(id);
        model.addAttribute("user", user);
        return "admin/show-user";
    }

    @PostMapping(path ="/admin/user/save", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public String saveUser(@Valid @ModelAttribute("user") UserDto userDto,
                               BindingResult result,
                               Model model
                           ) throws IOException {
        UserDto existingUser = userService.findUserByEmail(userDto.getEmail());

        if (existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()) {
            result.rejectValue("email", null,
                    "There is already an account registered with the same email");
        }

        fileStorageService.save(userDto.getAvatar());

        if (result.hasErrors()) {
            model.addAttribute("user", userDto);
            return "/admin/add-user";
        }

        userService.saveUser(userDto, false);
        return "redirect:/admin/user?success";
    }

    @PostMapping("/admin/user/{id}/update")
    public String updateUser(@Valid @ModelAttribute("user") UpdateUserDto userDto,
                           BindingResult result,
                           @PathVariable Long id,
                           Model model) {
        UserDto currentUser = userService.findUserById(id);
        if (currentUser == null) {
            result.rejectValue("email", null,
                    "Cannot update this user because user does not exist");
        }

        UserDto existingUser = userService.findUserByEmail(userDto.getEmail());
        if (existingUser != null && !Objects.equals(existingUser.getId(), id)) {
            result.rejectValue("email", null,
                    "Cannot update this user because other user have same data");
        }

        if (result.hasErrors()) {
            model.addAttribute("user", userDto);
            return "/admin/show-user";
        }

        userDto.setId(id);
        userService.updateUser(userDto);
        return "redirect:/admin/user/{id}?success";
    }

    @PostMapping("/admin/user/delete/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        userService.deleteUser(id);
        redirectAttributes.addFlashAttribute("success", "User deleted successfully!");
        return "redirect:/admin";
    }

    @GetMapping("/admin/user/export/csv")
    public ResponseEntity<byte[]> exportUsersToCSV() {
        List<UserDto> users = userService.findAllUsers();
        byte[] csvBytes;

        try (Writer writer = new StringWriter();
             CSVWriter csvWriter = new CSVWriter(writer)) {

            csvWriter.writeNext(new String[]{"Id", "First Name", "Last Name", "Email", "Phone", "Identity Number", "Birth Date"}); // CSV header

            for (UserDto user : users) {
                csvWriter.writeNext(new String[]{user.getId().toString(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPhone(), user.getIdentityNumber(), user.getStringDateBirth()});
            }

            csvWriter.flush();
            csvBytes = writer.toString().getBytes();

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.setContentDispositionFormData("filename", "users.csv");

        return new ResponseEntity<>(csvBytes, headers, HttpStatus.OK);
    }

    @GetMapping("/admin/user/export/pdf")
    public ResponseEntity<byte[]> exportUsersToPDF() throws Exception {
        List<UserDto> users = userService.findAllUsers();
        String html = PdfGenerator.generatePdfFromHtmlTemplate(users, "templates/admin/user-pdf");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);
        renderer.layout();
        renderer.createPDF(outputStream);
        renderer.finishPDF();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_HTML);
        headers.setContentDispositionFormData("filename", "users.pdf");

        return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);

    }
}
