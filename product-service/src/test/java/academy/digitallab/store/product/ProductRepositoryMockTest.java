package academy.digitallab.store.product;

import academy.digitallab.store.product.entity.Category;
import academy.digitallab.store.product.entity.Product;
import academy.digitallab.store.product.repository.ProductRepository;
import java.util.Date;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class ProductRepositoryMockTest {

  @Autowired
  private ProductRepository productRepository;

  @Test
  public void whenFindByCategory_thenResultListProduct() {
    Product product01 = Product.builder()
        .name("computer")
        .category(Category.builder().id(1L).build())
        .description("")
        .stock(10.0)
        .price(1240.99)
        .status("created")
        .createAt(new Date())
        .build();
    productRepository.save(product01);

    List<Product> founds = productRepository.findByCategory(product01.getCategory());

    Assertions.assertThat(founds.size()).isEqualTo(3);
  }
}
