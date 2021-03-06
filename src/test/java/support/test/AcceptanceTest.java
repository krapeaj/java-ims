package support.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import codesquad.domain.Issue;
import codesquad.domain.IssueRepository;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import codesquad.domain.User;
import codesquad.domain.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public abstract class AcceptanceTest {
    private static final String DEFAULT_LOGIN_USER = "javajigi";
    private static final Logger logger = LoggerFactory.getLogger(AcceptanceTest.class);

    @Autowired
    protected TestRestTemplate template;
    
    @Autowired
    private UserRepository userRepository;
    
    public TestRestTemplate template() {
        return template;
    } 
    
    public TestRestTemplate basicAuthTemplate() {
        return basicAuthTemplate(findDefaultUser());
    }
    
    public TestRestTemplate basicAuthTemplate(User loginUser) {
        return template.withBasicAuth(loginUser.getUserId(), loginUser.getPassword());
    }
    
    protected User findDefaultUser() {
        return findByUserId(DEFAULT_LOGIN_USER);
    }
    
    protected User findByUserId(String userId) {
        return userRepository.findByUserId(userId).get();
    }
    
    protected String createResource(String path, Object bodyPayload) {
        ResponseEntity<String> response = template().postForEntity(path, bodyPayload, String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
        return response.getHeaders().getLocation().getPath();
    }

//    protected String createResource(String path, Object bodyPayload, TestRestTemplate template) {
//        ResponseEntity<String> response = template.postForEntity(path, bodyPayload, String.class);
//        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
//        return response.getHeaders().getLocation().getPath();
//    }

    protected <T> ResponseEntity<T> createResource(String path, T bodyPayload, Class<T> responseType, TestRestTemplate template) {
        return template.postForEntity(path, bodyPayload, responseType);
    }
    
    protected <T> T getResource(String location, Class<T> responseType, User loginUser) {
        return basicAuthTemplate(loginUser).getForObject(location, responseType);
    }

    protected <T> T getResource(String location, Class<T> responseType, TestRestTemplate template) {
        return template.getForObject(location, responseType);
    }
    
    protected ResponseEntity<String> getResource(String location, User loginUser) {
        return basicAuthTemplate(loginUser).getForEntity(location, String.class);
    }

    protected ResponseEntity<String> getResource(String url, TestRestTemplate template) {
        return template.getForEntity(url, String.class);
    }
}
