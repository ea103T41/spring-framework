import com.euphy.learn.Application;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = Application.class)
@ActiveProfiles("local")
public class EmailTest {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.sendTo}")
    private String sendTo;

    @Test
    public void sendTestEmail() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(sendTo); //設置收件人信箱
        message.setSubject("Test Email"); //設置信箱主題
        message.setText("This is a test email."); //設置信箱內容
        javaMailSender.send(message); //發送郵件
    }
}
