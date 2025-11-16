package core.domain.shop;

public enum ProductType {
    CLOTHES("CLOSE"),
    SUPPLEMENT("SUPPLEMENT");
    
    private final String code;
    
    ProductType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return code;
    }
    
    public static ProductType fromCode(String code) {
        for (ProductType type : values()) {
            if (type.code.equalsIgnoreCase(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Невідомий тип продукту: " + code);
    }
}

