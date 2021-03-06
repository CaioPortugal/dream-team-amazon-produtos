package br.com.dreamteam.produtos.service;

import br.com.dreamteam.produtos.dto.KeywordDTO;
import br.com.dreamteam.produtos.dto.ProductDTO;
import br.com.dreamteam.produtos.exception.ResourceNotFoundException;
import br.com.dreamteam.produtos.model.Keyword;
import br.com.dreamteam.produtos.model.Product;
import br.com.dreamteam.produtos.repository.KeywordRepository;
import br.com.dreamteam.produtos.repository.ProductRepository;
import br.com.dreamteam.produtos.service.base.CrudServiceImpl;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.persistence.Access;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService extends CrudServiceImpl<ProductRepository, Product, ProductDTO> {

    @Autowired
    private KeywordService keywordService;

    @Override
    @Transactional
    public ProductDTO createElement(ProductDTO productDTO) {
        productDTO.setId(null);
        productDTO.setViews(0L);

        Product product = convertToModel(productDTO);
        product = repository.save(product);

        List<Keyword> keywords = keywordService.createElements(product, productDTO.getKeywords());
        product.setKeywords(keywords);

        return convertToDto(product);
    }

    @Override
    @Transactional
    public ProductDTO updateElement(Long id, ProductDTO productDTO) {
        if (!repository.existsById(id))
            throw new ResourceNotFoundException("Resource not found for id: " + id);

        Long views = repository.findViewsById(id);
        productDTO.setId(id);
        productDTO.setViews(views);

        Product product = convertToModel(productDTO);
        product = repository.save(product);
        List<Keyword> keywords = keywordService.updateElements(product, productDTO.getKeywords());
        product.setKeywords(keywords);

        return convertToDto(product);

    }

    @Transactional
    public List<ProductDTO> findByCategoryId(Long categoryId) {
        List<Product> products = repository.findByCategoryIdOrderByViewsDesc(categoryId);
        updateViews(products);
        return convertToListDto(products);
    }

    @Transactional
    public List<ProductDTO> findBySubcategoryId(Long subcategoryId) {
        List<Product> products = repository.findBySubcategoryId(subcategoryId);
        updateViews(products);
        return convertToListDto(products);
    }

    @Override
    @Transactional
    public ProductDTO findById(Long id) {
        Optional<Product> optionalModel = repository.findById(id);
        Product product = optionalModel.orElseThrow(() -> new ResourceNotFoundException("Resource not found for id" + id));
        updateViews(Arrays.asList(product));

        return convertToDto(product);
    }

    @Override
    @Transactional
    public List<ProductDTO> findAll() {
        List<Product> products = repository.findAll();
        updateViews(products);
        return convertToListDto(products);
    }

    @Override
    @Transactional
    public Page<ProductDTO> findAll(Pageable pageable) {
        Page<Product> productPage = repository.findAll(pageable);
        updateViews(productPage.getContent());
        return productPage.map(product -> convertToDto(product));
    }

    @Transactional
    public List<ProductDTO> findAllByKeyword(String description) {
        List<Long> keywordsIds = keywordService.findKeywordsIds(description);

        List<Product> products = repository.findByKeywordsIdIn(keywordsIds);

        return convertToListDto(products);
    }

    @Override
    protected List<ProductDTO> convertToListDto(List<Product> products) {
        return modelMapper.map(products, new TypeToken<List<ProductDTO>>() {}.getType());
    }

    @Override
    protected ProductDTO convertToDto(Product product) {
        return modelMapper.map(product, ProductDTO.class);
    }

    @Override
    protected Product convertToModel(ProductDTO product) {
        return modelMapper.map(product, Product.class);
    }


    private void updateViews(List<Product> products) {
        new Thread(() -> {
            products.forEach(product -> product.setViews(product.getViews()+1L));
        }).start();
    }
}
