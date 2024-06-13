    // Chức năng sắp xếp danh sách album theo lượt xem
    function sortAlbumsByViews(order) {
        // Lấy danh sách album và chuyển đổi thành mảng để sử dụng các hàm mảng
        var gallery = document.getElementsByClassName('gallery')[0];
        var albumItems = Array.from(gallery.getElementsByClassName('album-item'));

        // Sắp xếp danh sách album dựa trên giá trị lượt xem
        albumItems.sort(function (a, b) {
            // Lấy giá trị lượt xem từ các phần tử HTML và chuyển đổi thành số nguyên
            var viewCountA = parseInt(a.querySelector('.font1').textContent.replace(/[^\d]/g, ''), 10);
            var viewCountB = parseInt(b.querySelector('.font1').textContent.replace(/[^\d]/g, ''), 10);

            // Sắp xếp theo thứ tự tăng dần hoặc giảm dần
            return order === 'asc' ? viewCountA - viewCountB : viewCountB - viewCountA;
        });

        // Xóa tất cả các phần tử cũ khỏi danh sách
        while (gallery.firstChild) {
            gallery.removeChild(gallery.firstChild);
        }

        // Thêm lại các phần tử đã được sắp xếp vào danh sách
        albumItems.forEach(function (albumItem) {
            gallery.appendChild(albumItem);
        });
    }