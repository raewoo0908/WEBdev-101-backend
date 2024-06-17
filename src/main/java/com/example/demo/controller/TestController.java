package com.example.demo.controller;

import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.TestRequestBodyDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController //이 컨트롤러가 REST 컨트롤러임을 명시.
@RequestMapping("test") //URI 경로에 매핑. test라는 리소스가 왔을 때 이 TestController로 연결해준다
public class TestController {

    @GetMapping //HTTP 매서드(GET, POST, PUT 등등..)에 매핑.
    // 이 메서드의 리소스와 HTTP 메서드를 지정한다. 클라이언트가 이 리소스에 대해 GET 메서드로 요청하면, @GetMapping에 연결된 컨트롤러가 실행된다.
    //localhost:8080/test
    public String testController(){
        return "Hello World!"; //{리소스}
    }

    @GetMapping("/testGetMapping") //()안에 매핑하고 싶은 URI를 넣음으로써
    // @GetMapping을 이용해서 HTTP 메서드와 URI에 모두 매핑할 수도 있다.
    //localhost:8080/test/testGetMapping
    public String testControllerWithPath(){
        return "HelloWorld!-testGetMapping";
    }

    @GetMapping("/{id}") //Get요청이 들어왔을 때 경로로 들어오는 임의의 숫자 또는 문자를 변수 id에 매핑해라. test/뒤에 오는 정수가 id에 매핑된다.
    //localhost:8080/test/123
    public String testControllerWithPathVariable(@PathVariable(required = false) int id){ //(required = false)는 이 매개변수가 꼭 필요한 것은 아니라는 뜻.
        return "HelloWorld" + id;
    }

    @GetMapping("/testRequestParam")
    //localhost:8080/test/testRequestParam?id=123
    public String testControllerRequestParam(@RequestParam(required = false) int id){
        return "Hello World!" + id;
    }

    @GetMapping("/testRequestBody")
    public String testControllerRequestBody(@RequestBody TestRequestBodyDTO testRequestBodyDTO){
        return "HelloWorld! ID " + testRequestBodyDTO.getId() + " Message : " + testRequestBodyDTO.getMessage();
    }

    @GetMapping("/testResponseBody")
    public ResponseDTO<String> testControllerResponseBody(){
        List<String> list = new ArrayList<>();
        list.add("Hello World! I'm ResponseDTO");
        ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();
        return response;
    }

    @GetMapping("/testResponseEntity")
    public ResponseEntity<?> testControllerResponseEntity(){
        List<String> list = new ArrayList<>();
        list.add("Hello World! I am ResponseEntity. And you got 400!");
        ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();
        return ResponseEntity.ok().body(response);
    }

}
