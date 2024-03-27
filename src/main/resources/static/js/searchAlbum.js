    function searchAlbum() {
        // Lấy giá trị từ hộp văn bản tìm kiếm
        var input, filter, gallery, albumItems, a, p, i, txtValue;
        input = document.getElementById('searchInput');
        filter = input.value.toUpperCase();
        gallery = document.getElementsByClassName('gallery')[0];
        albumItems = gallery.getElementsByClassName('album-item');

        // Duyệt qua danh sách album và kiểm tra tên album
        for (i = 0; i < albumItems.length; i++) {
            a = albumItems[i].getElementsByTagName('a')[0];
            p = albumItems[i].getElementsByTagName('p')[0];
            txtValue = p.textContent || p.innerText;

            // So sánh tên album với từ khóa tìm kiếm
            if (txtValue.toUpperCase().indexOf(filter) > -1) {
                albumItems[i].style.display = ''; // Hiển thị nếu trùng khớp
            } else {
                albumItems[i].style.display = 'none'; // Ẩn nếu không trùng khớp
            }
        }
    }