package com.example.demo.Controller;

import com.example.demo.Entity.*;
import com.example.demo.Repository.AlbumInfoImageRepository;
import com.example.demo.Repository.AlbumRepository;
import com.example.demo.Service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/album")
public class AlbumController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryAlbumService categoryAlbumService;
    @Autowired
    private CategoryImageService categoryImageService;
    @Autowired
    private AlbumService albumService;

    @Autowired
    private MusicService musicService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private AlbumInfoService albumInfoService;

    @Autowired
    private AlbumInfoImageService albumInfoImageService;

    @Autowired
    private AlbumRepository albumRepository;

    @GetMapping("")
    public String listAlbum(Model model){

        List<CategoryAlbumEntity> categoryAlbum = categoryAlbumService.getAllCategoryAlbum();
        List<AlbumEntity> listAlbum = albumService.getAllAlbum();

        Collections.reverse(listAlbum); // đảo list album

        model.addAttribute("categoryAlbum", categoryAlbum);
        model.addAttribute("listAlbum", listAlbum);

        return "list_album";
    }

    @PostMapping("/filter")
    public String FilterAlbum(Model model, @RequestParam int categoryId){

        if(categoryId==0){
            return "redirect:/album";
        }
        else{

            List<AlbumEntity> listAlbum = albumService.getAlbumsByCategory(categoryId);

            List<CategoryAlbumEntity> categoryAlbum = categoryAlbumService.getAllCategoryAlbum();


            model.addAttribute("listAlbum", listAlbum);
            model.addAttribute("categoryAlbum", categoryAlbum);
            return "list_album";
        }
    }

    @GetMapping("/insert")
    public String insertAlbum(Model model) {

        model.addAttribute("listCategory", categoryAlbumService.getAllCategoryAlbum());
        return "add_album";
    }

    @PostMapping("/insert")
    public String insertAlbum(@RequestParam("file") MultipartFile file,
                              @RequestParam String name,
                              @RequestParam int categoryId){
        try{
            System.out.println("Ktra: "+file.getOriginalFilename()+" - "+name+" - "+categoryId);
            if(albumService.InsertCover(file, name, categoryId)){
                System.out.println("Them album thanh cong");
                return "redirect:/album/insert";
            }
            else {
                System.out.println("Them album that bai");
                return "redirect:/album/insert";
            }
        }
        catch (Exception e){
            System.out.println("Loi them album: "+e.getLocalizedMessage());
            return "redirect:/album/insert";
        }
    }



    @GetMapping("/listmusic/{albumId}")
    public String listMusic(HttpSession session, @PathVariable int albumId, Model model){

        // Lấy album cụ thể theo albumId và tăng lượt view cho album đó
        AlbumEntity album = albumService.findAlbumById(albumId);
        if (album != null) {
            album.setViewCount(album.getViewCount() + 1);
            albumRepository.save(album);
        }

        List<AlbumInfoEntity> listMusicInAlbum = albumInfoService.findAllMusicByAlbumId(albumId);
        List<AlbuminfoImageEntity> listImageInAlbum = albumInfoImageService.findAllImageByAlbumId(albumId);

        List<MusicEntity> listMusic = listMusicInAlbum.stream()
                .map(AlbumInfoEntity::getMusic)
                .collect(Collectors.toList());

        List<ImageEntity> listImage = listImageInAlbum.stream()
                .map(AlbuminfoImageEntity::getImage)
                .collect(Collectors.toList());

        System.out.printf("List music size: "+listMusic.size());

        session.setAttribute("filterMusic",listMusic);
        session.setAttribute("filterImage",listImage);

        model.addAttribute("listImage", listImage);
        model.addAttribute("listMusic", listMusic);

        model.addAttribute("albumId", albumId);
        return "album_music";
    }



    @GetMapping("/insertMusic")
    public String insertMusic(@RequestParam int albumId, Model model){
        List<CategoryEntity> listCategory = categoryService.getAllCategory();
        List<MusicEntity> listMusic = musicService.getAllMusic();
        model.addAttribute("albumId", albumId);
        model.addAttribute("listCategory", listCategory);
        model.addAttribute("listMusic", listMusic);
        return "add_music_album";
    }


    @GetMapping("/insertImage")
    public String insertImage(@RequestParam int albumId,
                              @RequestParam(name = "categoryId", required = false) Integer categoryId,
                              Model model){

        List<CategoryImageEntity> listCategoryImage = categoryImageService.findAllCategoryImage();

        List<ImageEntity> listImage ;

        // Kiểm tra xem categoryId có được truyền vào không
        if (categoryId != null) {
            // Nếu có, lọc danh sách hình ảnh theo categoryId
            listImage = imageService.findImagesByCategoryId(categoryId);
        } else {
            // Nếu không, lấy tất cả hình ảnh
            listImage = imageService.getAllImage();
        }

        model.addAttribute("albumId", albumId);
        model.addAttribute("listCategoryImage", listCategoryImage);
        model.addAttribute("listImage", listImage);

        return "add_image_album";
    }

    @GetMapping("/insertMusic/filter")
    public String insertMusicFilter(@RequestParam int albumId, Model model){
        List<CategoryEntity> listCategory = categoryService.getAllCategory();
        List<MusicEntity> listMusic = musicService.getAllMusic();

        model.addAttribute("listCategory", listCategory);
        model.addAttribute("listMusic", listMusic);
        model.addAttribute("albumId",albumId);
        System.out.printf("Ktra"+albumId);
        return "add_music_album";
    }


    //chức năng thêm nhạc vào album
    @GetMapping("/insertMusic/{musicId}/{albumId}")
    public String insertMusic(@PathVariable(name = "musicId") int musicId,
                              @PathVariable(name = "albumId") int albumId,
                              Model model){

        if(albumInfoService.insertMusic(musicId,albumId)){
            List<CategoryEntity> listCategory = categoryService.getAllCategory();
            List<MusicEntity> listMusic = musicService.getAllMusic();

            model.addAttribute("listCategory",listCategory);
            model.addAttribute("listMusic",listMusic);
            model.addAttribute("albumId", albumId);
            System.out.println("Them nhac vao album thanh cong");
            return "add_music_album";
        }
        else {
            System.out.println("Them nhac vao album that bai");
            return "add_music_album";
        }

    }

    @Autowired
    private AlbumInfoImageRepository albumInfoImageRepository;
    //chức năng thêm hình vào album
    @GetMapping("/insertImage/{imageId}/{albumId}")
    public String insertImage(@PathVariable(name = "imageId") int imageId,
                              @PathVariable(name = "albumId") int albumId,
                              @RequestParam(name = "categoryId", required = false) Integer categoryId,
                              Model model){



        if(albumInfoImageService.insertImage(imageId,albumId)){

            List<CategoryImageEntity> listCategoryImage = categoryImageService.findAllCategoryImage();

            List<ImageEntity> listImage ;

            // Kiểm tra xem categoryId có được truyền vào không
            if (categoryId != null) {
                // Nếu có, lọc danh sách hình ảnh theo categoryId
                listImage = imageService.findImagesByCategoryId(categoryId);
            } else {
                // Nếu không, lấy tất cả hình ảnh
                listImage = imageService.getAllImage();
            }


            model.addAttribute("albumId", albumId);
            model.addAttribute("listCategoryImage", listCategoryImage);
            model.addAttribute("listImage", listImage);


            return "add_image_album";
        }
        else {
            System.out.println("Them hinh vao album that bai");

            List<CategoryImageEntity> listCategoryImage = categoryImageService.findAllCategoryImage();

            List<ImageEntity> listImage ;

            // Kiểm tra xem categoryId có được truyền vào không
            if (categoryId != null) {
                // Nếu có, lọc danh sách hình ảnh theo categoryId
                listImage = imageService.findImagesByCategoryId(categoryId);
            } else {
                // Nếu không, lấy tất cả hình ảnh
                listImage = imageService.getAllImage();
            }

            model.addAttribute("albumId", albumId);
            model.addAttribute("listCategoryImage", listCategoryImage);
            model.addAttribute("listImage", listImage);


            return "add_image_album";

//            return "redirect:/album";
        }

    }



    // xóa album
    @GetMapping("/delete/{albumId}")
    public String deleteMusic(@PathVariable int albumId) {
        System.out.println("albumId: "+albumId);

        if(albumInfoService.deleteAlbumInfoByAlbumId(albumId)){
            albumInfoImageService.deleteAlbumInfoImageByAlbumId(albumId);
            albumService.deleteAlbumById(albumId);
            System.out.println("Xoa thanh cong");
            return "redirect:/album";
        }
        else{
            System.out.println("Xoa that bai");
            return "redirect:/album";
        }
    }



    // xóa nhạc trong album
    @GetMapping("/deleteMusic/{musicId}/{albumId}")
    public String deleteMusic(@PathVariable(name = "musicId") int musicId,
                              @PathVariable(name = "albumId") int albumId,
                              Model model){
        if(albumInfoService.deleteAlbumInfoByMusicIdAndAlbumId(musicId,albumId)){
            System.out.println("Xoa thanh cong");
            return "redirect:/album/listmusic/"+albumId;
        }
        else {
            System.out.println("Xoa that bai");
            return "redirect:/album/listmusic/"+albumId;
        }
    }

    // xóa hình trong album
    @GetMapping("/deleteImage/{imageId}/{albumId}")
    public String deleteImage(@PathVariable(name = "imageId") int imageId,
                              @PathVariable(name = "albumId") int albumId,
                              Model model){
        if(albumInfoImageService.deleteAlbumInfoByImageIdAndAlbumId(imageId,albumId)){
            System.out.println("Xoa thanh cong");
            return "redirect:/album/listmusic/"+albumId;
        }
        else {
            System.out.println("Xoa that bai");
            return "redirect:/album/listmusic/"+albumId;
        }
    }

    //    sửa album
    @GetMapping("/update/{albumId}")
    public String updateAlbum(Model model, @PathVariable int albumId) {

        List<CategoryAlbumEntity> categoryAlbum = categoryAlbumService.getAllCategoryAlbum();
        AlbumEntity album = albumService.findAlbumById(albumId);


        model.addAttribute("categoryAlbum",categoryAlbum);
        model.addAttribute("album",album);
        return "update_album";
    }

    @PostMapping("/update/{albumId}")
    public String UpdateAlbums(@RequestParam("file") MultipartFile file,
                               @ModelAttribute("album") AlbumEntity album,
                               @PathVariable int albumId){
        // Update album details
        AlbumEntity existingAlbum = albumService.findAlbumById(albumId);


        if (existingAlbum != null) {
            existingAlbum.setName(album.getName());

            existingAlbum.setCategoryAlbum(album.getCategoryAlbum());

            // nếu file rỗng thì giữ nguyên ko rỗng thì sửa thành cái mới
            if (!file.isEmpty()) {
                try {
                    String uploadDirectory = "src/main/resources/static/Album";
                    String fileName = StringUtils.cleanPath(file.getOriginalFilename());

//                    // Delete the old file
//                    if (existingAlbum.getImgPath()!= null && !existingAlbum.getImgPath().isEmpty()) {
//                        Path oldFilePath = Paths.get(uploadDirectory).resolve(existingAlbum.getImgPath());
//                        Files.deleteIfExists(oldFilePath);
//                    }

                    // Save the file to the specified directory
                    Path uploadPath = Paths.get(uploadDirectory);
                    try (InputStream inputStream = file.getInputStream()) {
                        Files.copy(inputStream, uploadPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
                    }

                    existingAlbum.setImgPath(fileName);
                } catch (IOException e) {
                    e.printStackTrace(); // Handle the exception appropriately
                }
            }

            // Save the updated album
            albumRepository.save(existingAlbum);

            // Redirect to the album details page or list
            return "redirect:/album/update/" + albumId; // Adjust the URL as needed
        } else {
            // Handle the case where the album with the given ID is not found
            return "error"; // Redirect to an error page or handle accordingly
        }
    }

}
