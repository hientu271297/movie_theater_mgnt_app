package fa.training.movietheater_mockproject.service.impl;

import com.google.gson.Gson;
import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.resource.Emailv31;
import fa.training.movietheater_mockproject.model.dto.EmailDto;
import fa.training.movietheater_mockproject.service.EmailService;
import lombok.AllArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MailjetService implements EmailService {
    @Value("${app.send-mailjet.api-key}")
    String apiKey;
    @Value("${app.send-mailjet.secret-key}")
    String secretKey;
    @Value("${app.send-mailjet.mailJetVersion}")
    String mailJetVersion;
    @Override
    public boolean sendEmail(EmailDto emailDto) {
        MailjetClient client = new MailjetClient(apiKey, secretKey, new ClientOptions(mailJetVersion));
        MailjetRequest request = new MailjetRequest(Emailv31.resource)
                .property(Emailv31.MESSAGES, new JSONArray()
                        .put(new JSONObject(new Gson().toJson(emailDto))));
        try{
            MailjetResponse response = client.post(request);
            System.out.println(response.getStatus());
            System.out.println(response.getData());
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
