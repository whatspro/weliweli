package admin.auth;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    /**
     *  @Field("client_name") clientName: String,
     *             @Field("redirect_uris") redirectUris: String,
     *             @Field("scopes") scopes: String,
     *             @Field("website") website: String
     */

    @PostMapping("api/v1/apps")
    public void createApplication(@RequestParam String clientName,
                                  @RequestParam String redirectUrl,
                                  @RequestParam String scopes,
                                  @RequestParam String website) {

    }
}
