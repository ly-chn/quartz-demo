package kim.nzxy.quartzdemo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping
@RestController
public class DemoController {
    @GetMapping
    public String demo() {
        return "hello, world";
    }
}
