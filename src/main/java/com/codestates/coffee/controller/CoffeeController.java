package com.codestates.coffee.controller;

import com.codestates.coffee.dto.CoffeePatchDto;
import com.codestates.coffee.dto.CoffeePostDto;
import com.codestates.coffee.dto.CoffeeResponseDto;
import com.codestates.coffee.entity.Coffee;
import com.codestates.coffee.mapstruct.CoffeeMapper;
import com.codestates.coffee.service.CoffeeService;
import com.codestates.member.dto.MemberResponseDto;
import com.codestates.member.entity.Member;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v5/coffees")
public class CoffeeController {

    private final CoffeeService coffeeService;
    private final CoffeeMapper coffeeMapper;

    private CoffeeController(CoffeeService coffeeService, CoffeeMapper coffeeMapper){
        this.coffeeService = coffeeService;
        this.coffeeMapper = coffeeMapper;
    }
    @PostMapping
    public ResponseEntity postOrder(@RequestBody CoffeePostDto coffeePostDto) {
        // 매퍼를 이용해서 coffeePostDto Coffee로 변환
        Coffee coffee = coffeeMapper.coffeePostDtoToCoffee(coffeePostDto);

        Coffee response = coffeeService.createCoffee(coffee);

        //매퍼를 이용해서 Coffee를 CoffeeResponseDto로 변환
        return new ResponseEntity<>(coffeeMapper.coffeeToCoffeeResponseDto(response), HttpStatus.CREATED);
    }

    @PatchMapping("/{coffee-id}") //price변경만. 이름바꾸고싶으면 아래 주석처리 후 요청 Json에 같이 넣으면 된다.
    public ResponseEntity patchCoffee(@PathVariable("coffee-id") long coffeeId,
                                      @RequestBody CoffeePatchDto coffeePatchDto) {
        coffeePatchDto.setCoffeeId(coffeeId);

        // 매퍼를 이용해서 CoffeePatchDto를 Coffee로 변환
        Coffee response = coffeeService.updateCoffee(coffeeMapper.coffeePatchDtoToCoffee(coffeePatchDto));

        // 매퍼를 이용해서 Coffee를 CoffeeResponseDto로 변환
        return new ResponseEntity<>(coffeeMapper.coffeeToCoffeeResponseDto(response), HttpStatus.OK);
    }

    @GetMapping("/{coffee-id}")
    public ResponseEntity getCoffee(@PathVariable("coffee-id") long coffeeId) {
        Coffee response = coffeeService.findCoffee(coffeeId);

        // 매퍼를 이용해서 Member를 MemberResponseDto로 변환
        return new ResponseEntity<>(coffeeMapper.coffeeToCoffeeResponseDto(response), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity getCoffees() {
        List<Coffee> coffees = coffeeService.findCoffees();

        // 매퍼를 이용해서 List<Member>를 MemberResponseDto로 변환
        List<CoffeeResponseDto> response = coffees.stream()
                .map(coffee -> coffeeMapper.coffeeToCoffeeResponseDto(coffee))
                .collect(Collectors.toList());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 커피 정보 삭제를 위한 핸들러 서드 구현
    @DeleteMapping("/{coffeeId}")
    public ResponseEntity deleteCoffee(@PathVariable("coffee-Id")long coffeeId){
        System.out.println("# delete coffeeId");
        coffeeService.deleteCoffe(coffeeId);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
