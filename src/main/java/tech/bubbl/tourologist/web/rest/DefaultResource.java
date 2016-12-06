package tech.bubbl.tourologist.web.rest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Controller
public class DefaultResource {

//    @RequestMapping(value = "/loaderio-a33cd9ecac0e58c6fd6cc78dd198e659")
//    @ResponseBody()
//    public void plaintext(HttpServletResponse res) {
//        try {
//            PrintWriter out = res.getWriter();
//            out.println("loaderio-a33cd9ecac0e58c6fd6cc78dd198e659");
//            out.close();
//            return;
//        } catch (IOException ex) {
//        }
//        return;
//    }
//
//    @RequestMapping(value = "/loaderio-{key}")
//    @ResponseBody()
//    public void plaintext2(HttpServletResponse res, @PathVariable String key) {
//        try {
//            PrintWriter out = res.getWriter();
//            out.println("loaderio-" + key);
//            out.close();
//            return;
//        } catch (IOException ex) {
//        }
//        return;
//    }

    @RequestMapping(value = "/.well-known/acme-challenge/_TdnJjsWUaq2DsDNrhEGIhG7mHE5oqxj9_NURWhFbT8", method = RequestMethod.GET)
    @ResponseBody()
    public void acmeChallenge(HttpServletResponse res) {
        try {
            PrintWriter out = res.getWriter();
            out.println("_TdnJjsWUaq2DsDNrhEGIhG7mHE5oqxj9_NURWhFbT8" + ".w562IOnNZJ9f5DUY6htPWDCqhdH02EdZEGrzD4yHm5s");
            out.close();
        } catch (IOException ignored) {
        }
    }
}
