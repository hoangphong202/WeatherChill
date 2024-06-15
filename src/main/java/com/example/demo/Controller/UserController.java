package com.example.demo.Controller;

import com.example.demo.Entity.*;
import com.example.demo.Repository.AlbumInfoImageRepository;
import com.example.demo.Repository.AlbumRepository;
import com.example.demo.Service.*;
import jakarta.persistence.Access;
import jakarta.servlet.http.HttpServletRequest;
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
@RequestMapping("/User")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private AlbumInfoService albumInfoService;
    @Autowired
    private AlbumInfoImageService albumInfoImageService;
    @Autowired
    private AlbumService albumService;
    @Autowired
    private CategoryAlbumService categoryAlbumService;

    @Autowired
    private CategoryImageService categoryImageService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private MusicService musicService;

    @Autowired
    private LikeAlbumService likeAlbumService;
    @GetMapping("")
    public String listAlbum(HttpSession session, Model model){


        UserEntity loggedInUser = (UserEntity) session.getAttribute("loggedInUser");
        // Sử dụng toán tử ba ngôi để kiểm tra và lấy tên, trả về chuỗi rỗng nếu loggedInUser là null
        String ten = (loggedInUser != null) ? loggedInUser.getName() : "";
        String email = (loggedInUser != null) ? loggedInUser.getEmail() : "";
        String describe = (loggedInUser != null) ? loggedInUser.getDescribe() : "";
        String imgpath = (loggedInUser != null) ? loggedInUser.getImgpath() : "";
        int userId =  (loggedInUser != null) ? loggedInUser.getId() : 1;


        // Retrieve all liked albums for the user
        List<LikeAlbumEntity> listAlbumlike = likeAlbumService.getAllLikeAlbumByIdUser(userId);
        // Count the number of liked albums
        int likedAlbumsCount = listAlbumlike.size();



        List<AlbumEntity> listAlbum = albumService.getAllAlbumByUserId(userId);
//        List<AlbumEntity> listAlbum = albumService.getAllAlbum();

        Collections.reverse(listAlbum); // đảo list album

        // Add likedAlbumsCount to the model
        model.addAttribute("likedAlbumsCount", likedAlbumsCount);

        model.addAttribute("listAlbum", listAlbum);

        model.addAttribute("ten", ten);
        model.addAttribute("email", email);
        model.addAttribute("describe", describe);
        model.addAttribute("imgpath", imgpath);
        return "user";
    }


    @GetMapping("/insert-album")
    public String insertAlbum(Model model) {

        model.addAttribute("listCategory", categoryAlbumService.getAllCategoryAlbum());
        return "add_album_client";
    }

    @PostMapping("/insert-album")
    public String insertAlbum(@RequestParam("file") MultipartFile file,
                              @RequestParam String name,
                              @RequestParam int categoryId,
                              HttpSession session){

        UserEntity loggedInUser = (UserEntity) session.getAttribute("loggedInUser");
        int userId =  (loggedInUser != null) ? loggedInUser.getId() : 1;

        try{
            if(albumService.InsertCoverClient(file, name, categoryId, userId)){
                System.out.println("Them album thanh cong");
                return "redirect:/User";
            }
            else {
                System.out.println("Them album that bai");
                return "redirect:/User";
            }
        }
        catch (Exception e){
            System.out.println("Loi them album: "+e.getLocalizedMessage());
            return "redirect:/User";
        }
    }


    @GetMapping("/listmusic/{albumId}")
    public String listMusic(@RequestParam(name = "ten", required = false) String ten,
                            HttpSession session,
                            @PathVariable int albumId,
                            Model model){

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
        model.addAttribute("ten", ten);
        return "album_music_user";
    }

    @Autowired
    private AlbumRepository albumRepository;
    @GetMapping("/listimage/{albumId}")
    public String listImage(@RequestParam(name = "ten", required = false) String ten,
                            HttpSession session,
                            @PathVariable int albumId,
                            Model model){

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
        model.addAttribute("ten", ten);
        return "album_image_user";
    }



    // xóa hình trong album
    @GetMapping("/deleteImage/{imageId}/{albumId}")
    public String deleteImage(@PathVariable(name = "imageId") int imageId,
                              @PathVariable(name = "albumId") int albumId,
                              Model model){
        if(albumInfoImageService.deleteAlbumInfoByImageIdAndAlbumId(imageId,albumId)){
            System.out.println("Xoa thanh cong");
            return "redirect:/User/listimage/"+albumId;
        }
        else {
            System.out.println("Xoa that bai");
            return "redirect:/User/listimage/"+albumId;
        }
    }


    // xóa nhạc trong album
    @GetMapping("/deleteMusic/{musicId}/{albumId}")
    public String deleteMusic(@PathVariable(name = "musicId") int musicId,
                              @PathVariable(name = "albumId") int albumId,
                              Model model){
        if(albumInfoService.deleteAlbumInfoByMusicIdAndAlbumId(musicId,albumId)){
            System.out.println("Xoa thanh cong");
            return "redirect:/User/listmusic/"+albumId;
        }
        else {
            System.out.println("Xoa that bai");
            return "redirect:/User/listmusic/"+albumId;
        }
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
        Collections.reverse(listImage); // đảo list image
        model.addAttribute("albumId", albumId);
        model.addAttribute("listCategoryImage", listCategoryImage);
        model.addAttribute("listImage", listImage);

        return "add_image_album_user";
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

            Collections.reverse(listImage); // đảo list image
            model.addAttribute("albumId", albumId);
            model.addAttribute("listCategoryImage", listCategoryImage);
            model.addAttribute("listImage", listImage);


            return "add_image_album_user";
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
            Collections.reverse(listImage); // đảo list image
            model.addAttribute("albumId", albumId);
            model.addAttribute("listCategoryImage", listCategoryImage);
            model.addAttribute("listImage", listImage);


            return "add_image_album_user";

//            return "redirect:/album";
        }

    }




    @GetMapping("/insertMusic")
    public String insertMusic(@RequestParam int albumId, Model model){
        List<CategoryEntity> listCategory = categoryService.getAllCategory();
        List<MusicEntity> listMusic = musicService.getAllMusic();
        model.addAttribute("albumId", albumId);
        model.addAttribute("listCategory", listCategory);
        model.addAttribute("listMusic", listMusic);
        return "add_music_album_user";
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
            return "add_music_album_user";
        }
        else {
            System.out.println("Them nhac vao album that bai");
            return "add_music_album_user";
        }

    }


    @GetMapping("/edit")
    public String edituser( HttpSession session,
                             Model model){


        UserEntity loggedInUser = (UserEntity) session.getAttribute("loggedInUser");
        // Sử dụng toán tử ba ngôi để kiểm tra và lấy tên, trả về chuỗi rỗng nếu loggedInUser là null
        String ten = (loggedInUser != null) ? loggedInUser.getName() : "";
        String email = (loggedInUser != null) ? loggedInUser.getEmail() : "";
        String describe = (loggedInUser != null) ? loggedInUser.getDescribe() : "";
        String imgpath = (loggedInUser != null) ? loggedInUser.getImgpath() : "";
        int userId =  (loggedInUser != null) ? loggedInUser.getId() : 0;


        // Retrieve all liked albums for the user
        List<LikeAlbumEntity> listAlbumlike = likeAlbumService.getAllLikeAlbumByIdUser(userId);
        // Count the number of liked albums
        int likedAlbumsCount = listAlbumlike.size();

        List<AlbumEntity> listAlbum = albumService.getAllAlbumByUserId(userId);
//        List<AlbumEntity> listAlbum = albumService.getAllAlbum();

        Collections.reverse(listAlbum); // đảo list album




        // Add likedAlbumsCount to the model
        model.addAttribute("likedAlbumsCount", likedAlbumsCount);

        model.addAttribute("listAlbum", listAlbum);

        model.addAttribute("ten", ten);
        model.addAttribute("email", email);
        model.addAttribute("describe", describe);
        model.addAttribute("imgpath", imgpath);
        return "user_edit";
    }

    @PostMapping("/edit")
    public String UpdateUser(
                               @ModelAttribute("userEntity") UserEntity userEntity,
                               HttpSession session){

        UserEntity loggedInUser = (UserEntity) session.getAttribute("loggedInUser");
        int userId =  (loggedInUser != null) ? loggedInUser.getId() : 0;

        if (!userService.EditUser(userEntity, userId)){
            System.out.println("edit thất bại");
            return "redirect:/User";
        }

        userService.EditUser(userEntity, userId);

        // Ghi đè thông tin người dùng trong phiên hiện tại với thông tin đã được cập nhật
        loggedInUser.setName(userEntity.getName());
        loggedInUser.setEmail(userEntity.getEmail());
        loggedInUser.setDescribe(userEntity.getDescribe());

        return "redirect:/User";

    }

    @PostMapping("/editfile")
    public String UpdateuserFile(@ModelAttribute("userEntity") UserEntity userEntity,
                                 @RequestParam("file") MultipartFile file,
                                 HttpSession session){

        UserEntity loggedInUser = (UserEntity) session.getAttribute("loggedInUser");
        int userId =  (loggedInUser != null) ? loggedInUser.getId() : 0;

        if (!userService.EditUserFile(userEntity,userId, file)){
            System.out.println("edit thất bại");
            return "redirect:/User/edit";
        }

        userService.EditUserFile(userEntity,userId, file);

        // Ghi đè thông tin người dùng trong phiên hiện tại với thông tin đã được cập nhật

        loggedInUser.setImgpath(file.getOriginalFilename());

        return "redirect:/User/edit";

    }

    @GetMapping("/Logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.invalidate(); // Xóa tất cả các thuộc tính của session

        // Sau khi đăng xuất, chuyển hướng người dùng đến trang đăng nhập
        return "redirect:/login";
    }




    @GetMapping("/delete")
    public String listAlbumdelete(HttpSession session, Model model){


        UserEntity loggedInUser = (UserEntity) session.getAttribute("loggedInUser");
        // Sử dụng toán tử ba ngôi để kiểm tra và lấy tên, trả về chuỗi rỗng nếu loggedInUser là null
        String ten = (loggedInUser != null) ? loggedInUser.getName() : "";
        String email = (loggedInUser != null) ? loggedInUser.getEmail() : "";
        String describe = (loggedInUser != null) ? loggedInUser.getDescribe() : "";
        String imgpath = (loggedInUser != null) ? loggedInUser.getImgpath() : "";
        int userId =  (loggedInUser != null) ? loggedInUser.getId() : 1;


        // Retrieve all liked albums for the user
        List<LikeAlbumEntity> listAlbumlike = likeAlbumService.getAllLikeAlbumByIdUser(userId);
        // Count the number of liked albums
        int likedAlbumsCount = listAlbumlike.size();



        List<AlbumEntity> listAlbum = albumService.getAllAlbumByUserId(userId);
//        List<AlbumEntity> listAlbum = albumService.getAllAlbum();

        Collections.reverse(listAlbum); // đảo list album

        // Add likedAlbumsCount to the model
        model.addAttribute("likedAlbumsCount", likedAlbumsCount);

        model.addAttribute("listAlbum", listAlbum);

        model.addAttribute("ten", ten);
        model.addAttribute("email", email);
        model.addAttribute("describe", describe);
        model.addAttribute("imgpath", imgpath);
        return "user_delete_album";
    }


    // xóa album
    @GetMapping("/delete/{albumId}")
    public String deleteMusic(@PathVariable int albumId) {
        System.out.println("albumId: "+albumId);

        if(albumInfoService.deleteAlbumInfoByAlbumId(albumId)){
            albumInfoImageService.deleteAlbumInfoImageByAlbumId(albumId);
            albumService.deleteAlbumById(albumId);
            System.out.println("Xoa thanh cong");
            return "redirect:/User/delete";
        }
        else{
            System.out.println("Xoa that bai");
            return "redirect:User/delete";
        }
    }




    //    sửa album

    @GetMapping("/update")
    public String updateAlbum(Model model, HttpSession session) {

        UserEntity loggedInUser = (UserEntity) session.getAttribute("loggedInUser");
        // Sử dụng toán tử ba ngôi để kiểm tra và lấy tên, trả về chuỗi rỗng nếu loggedInUser là null
        String ten = (loggedInUser != null) ? loggedInUser.getName() : "";
        String email = (loggedInUser != null) ? loggedInUser.getEmail() : "";
        String describe = (loggedInUser != null) ? loggedInUser.getDescribe() : "";
        String imgpath = (loggedInUser != null) ? loggedInUser.getImgpath() : "";
        int userId =  (loggedInUser != null) ? loggedInUser.getId() : 1;


        // Retrieve all liked albums for the user
        List<LikeAlbumEntity> listAlbumlike = likeAlbumService.getAllLikeAlbumByIdUser(userId);
        // Count the number of liked albums
        int likedAlbumsCount = listAlbumlike.size();



        List<AlbumEntity> listAlbum = albumService.getAllAlbumByUserId(userId);
//        List<AlbumEntity> listAlbum = albumService.getAllAlbum();

        Collections.reverse(listAlbum); // đảo list album

        // Add likedAlbumsCount to the model
        model.addAttribute("likedAlbumsCount", likedAlbumsCount);

        model.addAttribute("listAlbum", listAlbum);

        model.addAttribute("ten", ten);
        model.addAttribute("email", email);
        model.addAttribute("describe", describe);
        model.addAttribute("imgpath", imgpath);
        return "user_edit_album";
    }


    @GetMapping("/update/{albumId}")
    public String updateAlbum(Model model, @PathVariable int albumId) {

        List<CategoryAlbumEntity> categoryAlbum = categoryAlbumService.getAllCategoryAlbum();
        AlbumEntity album = albumService.findAlbumById(albumId);


        model.addAttribute("categoryAlbum",categoryAlbum);
        model.addAttribute("album",album);
        return "update_album_user";
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
            return "redirect:/User/update" ; // Adjust the URL as needed
        } else {
            // Handle the case where the album with the given ID is not found
            return "error"; // Redirect to an error page or handle accordingly
        }
    }

    @PostMapping("/filter")
    public String Filter(HttpSession session,Model model, @RequestParam int categoryId){
        System.out.println("Ktra: "+categoryId);
        if(categoryId==0){
            return "redirect:/music";
        }
        else{
            List<MusicEntity> listMusic = musicService.FilterByCategoryId(categoryId);
            session.setAttribute("filterMusic",listMusic);
            List<CategoryEntity> listCategory = categoryService.getAllCategory();
            model.addAttribute("categoryId", categoryId);
            model.addAttribute("listMusic", listMusic);
            model.addAttribute("listCategory", listCategory);
            return "add_music_album_user";
        }
    }

}