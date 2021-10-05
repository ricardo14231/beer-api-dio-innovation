package dio.innovation.beerAPI.controller;

import dio.innovation.beerAPI.dto.BeerDTO;
import dio.innovation.beerAPI.exception.BeerAlreadyRegisteredException;
import dio.innovation.beerAPI.exception.BeerNoSuchElementException;
import dio.innovation.beerAPI.exception.BeerQuantityException;
import dio.innovation.beerAPI.service.BeerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("beer")
@Api("Endpoint beer.")
public class BeerController {

    private final BeerService beerService;

    public BeerController(BeerService beerService) {
        this.beerService = beerService;
    }

    @PostMapping("/create")
    @ApiOperation("Adiciona uma nova cerveja")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Cerveja adicionada com sucesso."),
            @ApiResponse(code = 400, message = "Falha ao adicionar a ceveja."),
    })
    public ResponseEntity<String> createBeer(@RequestBody @Valid BeerDTO beerDTO) {

        try {
            String res = beerService.createBeer(beerDTO);
            return new ResponseEntity<>(res, HttpStatus.CREATED);
        }catch (BeerAlreadyRegisteredException err) {
            return new ResponseEntity<>(err.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/list")
    @ResponseStatus(HttpStatus.OK)
    public List<BeerDTO> listAllBeer() {
        return beerService.listAllBeer();
    }

    @GetMapping("/{id}")
    @ApiOperation("Retorna uma cerveja por ID.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Cerveja encontrada."),
            @ApiResponse(code = 404, message = "Ceveja não encontrada."),
    })
    public ResponseEntity findByIdBeer(@PathVariable Long id) {

        try {
            BeerDTO beerDTO = beerService.findByIdBeer(id);
            return new ResponseEntity<>(beerDTO, HttpStatus.OK);
        }catch (BeerNoSuchElementException err) {
            return new ResponseEntity<>(null , HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/findName/{name}")
    @ApiOperation("Retorna uma cerveja por nome.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Cerveja encontrada."),
            @ApiResponse(code = 404, message = "Ceveja não encontrada."),
    })
    public ResponseEntity findByNameBeer(@PathVariable String name) {
        try {
            BeerDTO beerDTO = beerService.findByNameBeer(name);
            return new ResponseEntity<>(beerDTO, HttpStatus.OK);
        }catch (BeerNoSuchElementException err) {
            return new ResponseEntity<>(err.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    @PutMapping("/update/{id}")
    @ApiOperation("Atualiza uma cerveja..")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Cerveja atualizada com sucesso."),
            @ApiResponse(code = 404, message = "Ceveja não encontrada ou cerveja já cadastrada."),
    })
    public ResponseEntity<String> updateBeer(@PathVariable Long id, @RequestBody @Valid BeerDTO beerDTO) {
        try {
            String res = beerService.updateBeer(id, beerDTO);
            return new ResponseEntity<>(res, HttpStatus.OK);
        }catch (BeerAlreadyRegisteredException | BeerNoSuchElementException err) {
            return new ResponseEntity<>(err.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation("Deleta uma cerveja.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Cerveja deletada com sucesso."),
            @ApiResponse(code = 404, message = "Ceveja não encontrada."),
    })
    public ResponseEntity<String> deleteBeer(@PathVariable Long id) {
        try {
            String res = beerService.deleteBeer(id);
            return new ResponseEntity<>(res, HttpStatus.OK);
        }catch (BeerNoSuchElementException err) {
            return new ResponseEntity<>(err.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{id}/increment/{quantity}")
    @ApiOperation("Atualiza a quantidade de cerveja no estoque.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Atualização do estoque com sucesso."),
            @ApiResponse(code = 400, message = "Ceveja não encontrada."),
    })
    public ResponseEntity incrementBeer(@PathVariable Long id, @PathVariable int quantity) {
        try {
            return new ResponseEntity<>(beerService.incrementQuantityBeer(id, quantity), HttpStatus.OK);
        }catch (BeerNoSuchElementException | BeerQuantityException err) {
            return new ResponseEntity<>(err.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
