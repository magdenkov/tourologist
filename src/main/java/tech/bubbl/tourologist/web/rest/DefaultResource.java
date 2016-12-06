package tech.bubbl.tourologist.web.rest;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


@RestController
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

    @GetMapping(value = "/.well-known/acme-challenge/KwZvI-Bv0pYa_btiUJ_WqFnKksLO7pVSbLilzBJODgs",  produces={MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
    @ResponseBody()
    public String acmeChallenge(HttpServletResponse res) {
        return "KwZvI-Bv0pYa_btiUJ_WqFnKksLO7pVSbLilzBJODgs";
    }
}
