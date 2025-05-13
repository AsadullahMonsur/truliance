package org.web.truliance.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.beans.factory.annotation.Value;

@Controller
public class FileTransferController {

    @Value("${upload.directory}")
    private String upload_directory;

    @RequestMapping("transfer")
    private String landing_page(Model model){
        try(final DatagramSocket socket = new DatagramSocket()){
            socket.connect(InetAddress.getByName("9.4.2.2"), 9822);
            String ip = socket.getLocalAddress().getHostAddress();
            model.addAttribute("ip","http://"+ip+":7295/transfer");
        }
        catch (SocketException | UnknownHostException e) {
            System.out.println("Error Fetching in Lan IP of Local Device");
        }

        return "file_transfer_view";
    }

    @RequestMapping("upload")
    private String upload_page(Model model){

        return "upload_view";
    }

    @RequestMapping("download")
    private String download_page(Model model){

        return "download_view";
    }

    @PostMapping("uploading")
    private String files_upload(@RequestParam("files") List<MultipartFile> files,
                                RedirectAttributes redirect){

        redirect.addFlashAttribute("hint", (files.size()-1)+" files selected");

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                try {
                    String file_name = StringUtils.cleanPath(file.getOriginalFilename());

                    Path path = Paths.get(upload_directory, file_name);
                    Files.createDirectories(path.getParent());

                    file.transferTo(path);
                }

                catch (IOException e) {
                    redirect.addFlashAttribute("message", "Failed to Upload Files.");
                    return "redirect:/upload";
                }
            }
        }

        redirect.addFlashAttribute("message", "Files Uploaded Successfully!");

        return "redirect:/upload";
    }

    @PostMapping("downloading")
    public void files_download(HttpServletResponse response) throws IOException {
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=files.zip");

        Path root_folder = Paths.get("files");

        try (Stream<Path> paths = Files.walk(root_folder)) {
            ZipOutputStream output_stream = new ZipOutputStream(response.getOutputStream());

            paths.filter(Files::isRegularFile)
                    .forEach(path ->
                            process_files(path,response,output_stream));

            output_stream.finish();
        }
        catch (Exception e) {
            System.out.println("Error in Managing Download");
        }

    }

    private void process_files(Path path, HttpServletResponse response, ZipOutputStream output_stream) {
        try{
            InputStream input_stream = Files.newInputStream(path);
            ZipEntry entry = new ZipEntry(path.getFileName().toString());
            output_stream.putNextEntry(entry);

            StreamUtils.copy(input_stream, output_stream);
            output_stream.closeEntry();
        }
        catch (Exception e) {
            System.out.println("Error Reading Inputstream of a File");
        }
    }
}
