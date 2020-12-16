package academy.digitallab.store.product;

import academy.digitallab.store.product.entity.Category;
import academy.digitallab.store.product.entity.Product;
import academy.digitallab.store.product.repository.ProductRepository;
import academy.digitallab.store.product.service.ProductService;
import academy.digitallab.store.product.service.ProductServiceImpl;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ProductServiceMockTest {

  @Mock
  private ProductRepository productRepository;

  private ProductService productService;

  @BeforeEach
  public void setup(){
    MockitoAnnotations.openMocks(this);
    productService = new ProductServiceImpl(productRepository);
    Product p1 = Product.builder()
        .id(1L)
        .name("computer")
        .category(Category.builder().id(1L).build())
        .price(12.5)
        .stock(5.0)
        .build();

    Mockito.when(productRepository.findById(1L))
        .thenReturn(Optional.of(p1));
    Mockito.when(productRepository.save(p1))
        .thenReturn(p1);

  }

  @Test
  public void shouldFindProductById() {
    Product found = productService.getProduct(1L);
    Assertions.assertThat(found.getName()).isEqualTo("computer");
  }

  @Test
  public void shouldUpdateStock(){
    Product newStock = productService.updateStock(1L, 8.0);
    Assertions.assertThat(newStock.getStock()).isEqualTo(13);
  }
}
