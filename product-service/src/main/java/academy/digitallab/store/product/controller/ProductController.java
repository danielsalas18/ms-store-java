package academy.digitallab.store.product.controller;

import academy.digitallab.store.product.entity.Category;
import academy.digitallab.store.product.entity.Product;
import academy.digitallab.store.product.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping (value = "/products")
public class ProductController {

  @Autowired
  private ProductService productService;

  @GetMapping
  public ResponseEntity<List<Product>> listProduct (@RequestParam(name = "categoryId", required = false) Long categoryId){
    List<Product> products;
    if(categoryId == null){
     products  = productService.listAllProduct();
      if(products.isEmpty()){
        return ResponseEntity.noContent().build();
      }
    }else {
      products = productService.findByCategory(Category.builder().id((categoryId)).build());
      if(products.isEmpty()){
        return ResponseEntity.notFound().build();
      }
    }
    return ResponseEntity.ok(products);
  }

  @GetMapping(value = "/{id}")
  public ResponseEntity<Product> getProduct (@PathVariable("id") Long productId){
    Product p = productService.getProduct(productId);
    if(p == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(p);
  }

  @PostMapping
  public ResponseEntity<Product> createProduct (@Valid @RequestBody Product product, BindingResult validationResult) {
    if(validationResult.hasErrors()){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.formatMessage(validationResult));
    }
    Product productCreated = productService.createProduct(product);
    return ResponseEntity.status(HttpStatus.CREATED).body(productCreated);
  }

  @PutMapping(value = "/{id}")
  public ResponseEntity<Product> updateProduct (@PathVariable("id") Long id, @RequestBody Product product) {
    product.setId(id);
    Product p = productService.updateProduct(product);
    if(p == null){
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(p);
  }

  @DeleteMapping (value = "/{id}")
  public ResponseEntity<Product> deleteProduct (@PathVariable("id") Long id) {
    Product p = productService.deleteProduct(id);
    if(p == null){
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(p);
  }

  @GetMapping (value = "/{id}/stock")
  public ResponseEntity<Product> updateStock (@PathVariable("id") Long id,@RequestParam(name="quantity", required = true) Double quantity) {
    Product p = productService.updateStock(id, quantity);
    if(p == null){
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(p);
  }

  private String formatMessage(BindingResult result){
    List<Map<String, String>> errors = result.getFieldErrors().stream()
        .map(item -> {
          Map<String, String> error = new HashMap<>();
          error.put(item.getField(), item.getDefaultMessage());
          return error;
        }).collect(Collectors.toList());
    ErrorMessage errorMessage = ErrorMessage.builder().code("01").messages(errors).build();
    //convierte mapa en jsonString
    ObjectMapper mapper = new ObjectMapper();
    String jsonString ="";
    try {
      jsonString = mapper.writeValueAsString(errorMessage);
    }catch (JsonProcessingException e){
      e.printStackTrace();
    }
    return jsonString;
  }

}
