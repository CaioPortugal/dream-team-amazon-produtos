package br.com.dreamteam.produtos.dto;

import br.com.dreamteam.produtos.dto.base.BaseDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubcategoryDTO extends BaseDTO {

    private String name;
}
