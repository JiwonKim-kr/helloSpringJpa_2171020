package kr.ac.hansung.cse.exception;

public class CategoryInUseException extends RuntimeException {
    private final int productCount;

    public CategoryInUseException(int productCount) {
        super("이 카테고리에 속한 상품이 있어 삭제할 수 없습니다. 연결된 상품 수: " + productCount);
        this.productCount = productCount;
    }

    public int getProductCount() {
        return productCount;
    }
}
