package br.com.dreamteam.produtos.controller;

import br.com.dreamteam.produtos.controller.base.BaseController;
import br.com.dreamteam.produtos.dto.ProductDTO;
import br.com.dreamteam.produtos.model.Product;
import br.com.dreamteam.produtos.service.ProductService;
import io.swagger.annotations.Api;
import org.modelmapper.TypeToken;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Api(tags = "Product")
@RestController
@RequestMapping("api/product")
public class ProductController extends BaseController<ProductService, ProductDTO> {

    @GetMapping("category/{categoryId}")
    public List<ProductDTO> findByCategoryId(@PathVariable Long categoryId) {
        List<ProductDTO>  products = service.findByCategoryId(categoryId);
        return products;
    }

    @GetMapping("subcategory/{subcategoryId}")
    public List<ProductDTO> findBySubcategoryId(@PathVariable Long subcategoryId) {
        List<ProductDTO>  products = service.findBySubcategoryId(subcategoryId);
        return products;
    }

    @GetMapping("keyword")
    public List<ProductDTO> findAll(@RequestParam(name = "palavraChave") String keyword) {
        List<ProductDTO> products = service.findAllByKeyword(keyword);
        return products;
    }

    @Override
    protected URI getURILocation(ProductDTO product) {
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}")
                .buildAndExpand(product.getId()).toUri();
        return uri;
    }
}
