package dio.innovation.beerAPI.controller;

import dio.innovation.beerAPI.dto.BeerDTO;
import dio.innovation.beerAPI.exception.BeerAlreadyRegisteredException;
import dio.innovation.beerAPI.exception.BeerNoSuchElementException;
import dio.innovation.beerAPI.service.BeerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("beer")
public class BeerController {

    @Autowired
    BeerService beerService;

    @PostMapping("/create")
    public ResponseEntity<String> createBeer(@RequestBody @Valid BeerDTO beerDTO) throws BeerAlreadyRegisteredException {

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
    public ResponseEntity<BeerDTO> findByIdBeer(@PathVariable Long id) {

        try {
            BeerDTO beerDTO = beerService.findByIdBeer(id);
            return new ResponseEntity<>(beerDTO, HttpStatus.OK);
        }catch (BeerNoSuchElementException err) {
            return new ResponseEntity<>(null , HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateBeer(@PathVariable Long id, @RequestBody @Valid BeerDTO beerDTO) {

        try {
            String res = beerService.updateBeer(id, beerDTO);
            return new ResponseEntity<>(res, HttpStatus.OK);
        }catch (BeerAlreadyRegisteredException err) {
            return new ResponseEntity<>(err.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteBeer(@PathVariable Long id) {
        try {
            String res = beerService.deleteBeer(id);
            return new ResponseEntity<>(res, HttpStatus.OK);
        }catch (BeerNoSuchElementException err) {
            return new ResponseEntity<>(err.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
