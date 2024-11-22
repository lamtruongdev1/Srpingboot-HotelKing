HotelKing Spring Boot Project
HotelKing là một ứng dụng quản lý khách sạn được xây dựng bằng Spring Boot. Nó giúp các quản trị viên khách sạn có thể dễ dàng quản lý thông tin phòng, khách hàng và các dịch vụ trong khách sạn. Ứng dụng này cung cấp giao diện người dùng thân thiện, dễ sử dụng và hỗ trợ các tính năng cần thiết cho việc quản lý hoạt động của khách sạn.

Mục Lục
Giới thiệu
Các Tính Năng
Cài Đặt
Hướng Dẫn Sử Dụng
Công Nghệ Sử Dụng
Chạy Ứng Dụng
Giấy Phép
Giới Thiệu
HotelKing là một ứng dụng giúp quản lý phòng, đặt phòng, thông tin khách hàng, và thanh toán cho khách sạn. Dự án được xây dựng với Spring Boot và sử dụng các công nghệ hiện đại để đảm bảo tính bảo mật và hiệu suất cao.

Ứng dụng này có thể dễ dàng mở rộng và tích hợp thêm các tính năng mới, phục vụ nhu cầu quản lý khách sạn hiệu quả hơn.

Các Tính Năng
Quản lý thông tin khách sạn (phòng, dịch vụ, khách hàng)
Đặt phòng trực tuyến
Quản lý các loại phòng và mức giá
Theo dõi trạng thái phòng (đã đặt, trống, đang sử dụng)
Quản lý thanh toán và hóa đơn
Cài Đặt
Cài Đặt Phần Mềm
Để chạy ứng dụng trên máy tính của bạn, bạn cần cài đặt những công cụ sau:

Java 8+: Đảm bảo bạn đã cài đặt JDK 8 hoặc phiên bản cao hơn.
Maven: Sử dụng Maven để quản lý các phụ thuộc (dependencies) và build ứng dụng.
IDE (Eclipse, IntelliJ, hoặc bất kỳ IDE Java nào): Để dễ dàng phát triển và chạy ứng dụng.
Clone Dự Án
Clone repository về máy tính của bạn:

git clone https://github.com/lamtruongdev1/Springboot-HotelKing.git
cd Springboot-HotelKing
Hướng Dẫn Sử Dụng
Cấu Hình Dự Án Sau khi clone về, bạn cần cấu hình các tham số kết nối cơ sở dữ liệu trong file application.properties:

spring.datasource.url=jdbc:mysql://localhost:3306/hotelking
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
Chạy Ứng Dụng Để chạy ứng dụng, bạn chỉ cần sử dụng lệnh Maven:

mvn spring-boot:run
Sau đó, ứng dụng sẽ chạy trên http://localhost:8080.

Giao Diện Người Dùng Bạn có thể truy cập vào giao diện người dùng qua trình duyệt tại http://localhost:8080 để quản lý khách sạn.

Công Nghệ Sử Dụng
Spring Boot: Framework chính để xây dựng ứng dụng.
Thymeleaf: Công cụ để xử lý giao diện người dùng.
MySQL: Cơ sở dữ liệu để lưu trữ thông tin.
Spring Data JPA: Để kết nối và thao tác với cơ sở dữ liệu.
Chạy Ứng Dụng
Sau khi cài đặt và cấu hình, bạn có thể chạy ứng dụng trực tiếp từ IDE hoặc dùng Maven để chạy lệnh:

mvn spring-boot:run
Ứng dụng sẽ khởi chạy trên cổng 8080 (hoặc cổng bạn cấu hình trong application.properties).

Giấy Phép
Dự án này được phát hành dưới giấy phép MIT License.

Lưu Ý:
Đảm bảo bạn đã cài đặt Java và Maven đúng cách.
Thay đổi thông tin kết nối cơ sở dữ liệu nếu cần thiết.
Nếu có bất kỳ vấn đề nào trong quá trình cài đặt hoặc sử dụng, hãy mở issue trên GitHub để chúng tôi có thể hỗ trợ.
Bạn có thể thay đổi và bổ sung các thông tin khác tùy theo yêu cầu của dự án của bạn. Chúc bạn thành công với dự án HotelKing!
