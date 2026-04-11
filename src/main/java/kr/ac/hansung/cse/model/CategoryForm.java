package kr.ac.hansung.cse.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * 카테고리 등록 폼 DTO
 */
@Getter
@Setter
public class CategoryForm {

    @NotBlank(message = "카테고리 이름을 입력해 주세요.")
    @Size(max = 50, message = "카테고리 이름은 50자 이하로 입력해 주세요.")
    private String name;

}
