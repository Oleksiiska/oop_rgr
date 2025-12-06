package core.util;

/**
 * Centralized constants for the fitness club management system.
 * Contains error messages, default values, and configuration constants.
 */
public final class Constants {
    
    private Constants() {
        throw new AssertionError("Constants class should not be instantiated");
    }
    
    // Error Messages - Membership
    public static final String ERROR_MEMBERSHIP_NULL = "Абонемент не може бути null.";
    public static final String ERROR_MEMBERSHIP_TYPE_NULL = "Тип абонемента не може бути null.";
    public static final String ERROR_MEMBERSHIP_START_DATE_NULL = "Дата початку не може бути null.";
    public static final String ERROR_MEMBERSHIP_DURATION_INVALID = "Тривалість абонемента має бути позитивною.";
    public static final String ERROR_MEMBERSHIP_CLUB_ID_REQUIRED = "Club ID має бути вказаний для типу абонемента SINGLE_CLUB.";
    public static final String ERROR_MEMBERSHIP_DATES_INVALID = "Дата кінця абонемента не може бути раніше дати початку.";
    public static final String ERROR_MEMBERSHIP_NOT_APPROVED = "Абонемент не було затверджено адміністратором.";
    public static final String ERROR_MEMBERSHIP_INACTIVE = "Абонемент неактивний.";
    public static final String ERROR_MEMBERSHIP_INVALID_CLUB = "Абонемент не дійсний для цього клубу.";
    
    // Error Messages - Client
    public static final String ERROR_CLIENT_PHONE_BLANK = "Номер телефону не може бути порожнім.";
    
    // Error Messages - Product
    public static final String ERROR_PRODUCT_NULL = "Продукт не може бути null.";
    public static final String ERROR_PRODUCT_PRICE_NEGATIVE = "Ціна лише додатня.";
    public static final String ERROR_PRODUCT_TYPE_NULL = "Тип продукту не може бути null.";
    public static final String ERROR_PRODUCT_TYPE_NOT_REGISTERED = "Тип продукту '%s' не зареєстровано.";
    public static final String ERROR_PRODUCT_QUANTITY_INVALID = "Кількість завжди додатня.";
    public static final String ERROR_CLOTHES_PARAMS_INSUFFICIENT = "Одяг потребує розмір та колір.";
    public static final String ERROR_SUPPLEMENT_PARAMS_INSUFFICIENT = "Добавка потребує смак.";
    
    // Error Messages - Employee
    public static final String ERROR_EMPLOYEE_NULL = "Співробітник не може бути null.";
    public static final String ERROR_EMPLOYEE_NAME_BLANK = "Ім'я не може бути пустим.";
    public static final String ERROR_EMPLOYEE_SALARY_NEGATIVE = "Зарплатня лише додатня.";
    public static final String ERROR_ADMINISTRATOR_NULL = "Адміністратор не може бути null.";
    
    // Error Messages - Studio
    public static final String ERROR_STUDIO_NAME_BLANK = "Назва студії не може бути порожньою.";
    public static final String ERROR_STUDIO_CAPACITY_INVALID = "Місткість студії має бути позитивною.";
    public static final String ERROR_CLEANER_NULL = "Прибиральник не може бути null.";
    
    // Error Messages - GroupClass
    public static final String ERROR_CLASS_DURATION_INVALID = "Тривалість заняття повинна бути позитивною";
    
    // Error Messages - Club
    public static final String ERROR_CLUB_NULL = "Клуб не може бути null.";
    
    // Error Messages - Factory
    public static final String ERROR_FACTORY_TYPE_NULL = "Тип та створювач не можуть бути null.";
    
    // Default Values
    public static final int DEFAULT_MEMBERSHIP_DURATION_DAYS = 30;
    public static final int LOW_STOCK_THRESHOLD = 5;
    
    // Job Titles
    public static final String JOB_TITLE_TRAINER = "Тренер";
    public static final String JOB_TITLE_ADMINISTRATOR = "Адміністратор";
    public static final String JOB_TITLE_CLEANER = "Прибиральник";
}

