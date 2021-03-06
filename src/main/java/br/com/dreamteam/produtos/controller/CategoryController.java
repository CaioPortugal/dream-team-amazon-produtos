package br.com.dreamteam.produtos.controller;

import br.com.dreamteam.produtos.controller.base.BaseController;
import br.com.dreamteam.produtos.dto.CategoryDTO;
import br.com.dreamteam.produtos.service.CategoryService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Api(tags = "Category")
@RestController
@RequestMapping("api/category")
public class CategoryController extends BaseController<CategoryService, CategoryDTO> {

    @Override
    protected URI getURILocation(CategoryDTO category) {
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}")
                .buildAndExpand(category.getId()).toUri();
        return uri;
    }
}
