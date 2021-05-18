#### Note

Server gửi lên filename, IP:Port cho MS
MS lưu các thông tin đó vào FileInfo rồi add vào ArrayList<FileInfo> 
Client request thì MS trả về các filename, IP:Port trong ArrayList<FileInfo> đó
Khi Server down thì find các filename đó để remove ra khỏi list

- filename là key dùng để tìm kiếm trong mảng
- IP:Port dùng để kết nối tới Server chứa file

Khi server khởi chạy thì gửi lên mã "1" để MS nhận diện input, sau đó gửi Object fileinfo hoặc thông tin lên cho MS
Khi server down thì gửi lên mã "2" để xóa các file đó
Client thì tương tự với mã 3