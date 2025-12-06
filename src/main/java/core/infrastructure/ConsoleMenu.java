package core.infrastructure;

import core.domain.client.Client;
import core.domain.client.Membership;
import core.domain.client.MembershipType;
import core.domain.club.FitnessClub;
import core.domain.club.FitnessNetwork;
import core.domain.club.Studio;
import core.domain.scheduling.GroupClass;
import core.domain.shop.DiscountStrategy;
import core.domain.shop.Product;
import core.domain.shop.ProductFactory;
import core.domain.staff.Administrator;
import core.domain.staff.Cleaner;
import core.domain.staff.Employee;
import core.domain.staff.Trainer;
import core.exceptions.BookingException;
import core.exceptions.MembershipAccessException;
import core.services.BookingService;
import core.services.FitnessClubServiceManager;
import core.services.MembershipService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Console-based user interface for the Fitness Club Network management system.
 * Provides an interactive menu-driven interface for managing clubs, clients,
 * memberships, bookings, inventory, and staff.
 */
public class ConsoleMenu {
    
    private final Scanner scanner;
    private final FitnessNetwork fitnessNetwork;
    private final ProductFactory productFactory;
    private final MembershipService membershipService;
    private final BookingService bookingService;
    private FitnessClubServiceManager currentServiceManager;
    private FitnessClub currentClub;
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    
    /**
     * Creates a new ConsoleMenu with the required dependencies.
     *
     * @param fitnessNetwork the fitness network instance
     * @param productFactory the product factory
     * @param membershipService the membership service
     * @param bookingService the booking service
     */
    public ConsoleMenu(FitnessNetwork fitnessNetwork,
                      ProductFactory productFactory,
                      MembershipService membershipService,
                      BookingService bookingService) {
        this.scanner = new Scanner(System.in);
        this.fitnessNetwork = fitnessNetwork;
        this.productFactory = productFactory;
        this.membershipService = membershipService;
        this.bookingService = bookingService;
    }
    
    public void start() {
        System.out.println("=== Система управління мережею фітнес-клубів ===\n");
        
        while (true) {
            displayMainMenu();
            int choice = readInt("Виберіть опцію: ");
            
            switch (choice) {
                case 1:
                    manageClubs();
                    break;
                case 2:
                    manageClients();
                    break;
                case 3:
                    manageMemberships();
                    break;
                case 4:
                    manageBookings();
                    break;
                case 5:
                    manageInventory();
                    break;
                case 6:
                    manageStaff();
                    break;
                case 7:
                    viewSchedule();
                    break;
                case 8:
                    viewNetworkInfo();
                    break;
                case 0:
                    System.out.println("До побачення!");
                    return;
                default:
                    System.out.println("Невірний вибір. Спробуйте ще раз.\n");
            }
        }
    }
    
    private void displayMainMenu() {
        System.out.println("\n=== ГОЛОВНЕ МЕНЮ ===");
        System.out.println("1. Управління клубами");
        System.out.println("2. Управління клієнтами");
        System.out.println("3. Управління абонементами");
        System.out.println("4. Управління бронюваннями");
        System.out.println("5. Управління складом");
        System.out.println("6. Управління персоналом");
        System.out.println("7. Перегляд розкладу");
        System.out.println("8. Інформація про мережу");
        System.out.println("0. Вихід");
    }
    
    private void manageClubs() {
        while (true) {
            System.out.println("\n=== УПРАВЛІННЯ КЛУБАМИ ===");
            System.out.println("1. Створити новий клуб");
            System.out.println("2. Вибрати клуб");
            System.out.println("3. Додати студію до клубу");
            System.out.println("4. Переглянути всі клуби");
            System.out.println("5. Переглянути студії поточного клубу");
            System.out.println("0. Назад");
            
            int choice = readInt("Виберіть опцію: ");
            
            switch (choice) {
                case 1:
                    createClub();
                    break;
                case 2:
                    selectClub();
                    break;
                case 3:
                    addStudioToClub();
                    break;
                case 4:
                    viewAllClubs();
                    break;
                case 5:
                    viewStudios();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Невірний вибір.");
            }
        }
    }
    
    private void createClub() {
        String address = readString("Введіть адресу клубу: ");
        FitnessClub club = new FitnessClub(address);
        fitnessNetwork.addClub(club);
        System.out.println("Клуб створено: " + club.getAddress() + " (ID: " + club.getId() + ")");
    }
    
    private void selectClub() {
        List<FitnessClub> clubs = fitnessNetwork.getClubs();
        if (clubs.isEmpty()) {
            System.out.println("Немає доступних клубів. Створіть клуб спочатку.");
            return;
        }
        
        System.out.println("\nДоступні клуби:");
        for (int i = 0; i < clubs.size(); i++) {
            System.out.println((i + 1) + ". " + clubs.get(i).getAddress() + " (ID: " + clubs.get(i).getId() + ")");
        }
        
        int choice = readInt("Виберіть клуб: ");
        if (choice > 0 && choice <= clubs.size()) {
            currentClub = clubs.get(choice - 1);
            currentServiceManager = new FitnessClubServiceManager(currentClub);
            System.out.println("Вибрано клуб: " + currentClub.getAddress());
        } else {
            System.out.println("Невірний вибір.");
        }
    }
    
    private void checkCurrentClub() {
        if (currentClub == null) {
            System.out.println("Спочатку виберіть клуб.");
            selectClub();
        }
    }
    
    private void addStudioToClub() {
        checkCurrentClub();
        if (currentClub == null) return;
        
        String name = readString("Введіть назву студії: ");
        int capacity = readInt("Введіть місткість: ");
        boolean cleanliness = readBoolean("Чи чиста студія? (true/false): ");
        
        Studio studio = new Studio(name, capacity, cleanliness);
        currentClub.addStudio(studio);
        System.out.println("Студію додано: " + studio.getName());
    }
    
    private void viewAllClubs() {
        List<FitnessClub> clubs = fitnessNetwork.getClubs();
        if (clubs.isEmpty()) {
            System.out.println("Немає клубів у мережі.");
            return;
        }
        
        System.out.println("\n=== КЛУБИ МЕРЕЖІ ===");
        for (FitnessClub club : clubs) {
            System.out.println("Адреса: " + club.getAddress());
            System.out.println("ID: " + club.getId());
            System.out.println("Студій: " + club.getStudios().size());
            System.out.println("Співробітників: " + club.getStaff().size());
            System.out.println("---");
        }
    }
    
    private void viewStudios() {
        checkCurrentClub();
        if (currentClub == null) return;
        
        List<Studio> studios = currentClub.getStudios();
        if (studios.isEmpty()) {
            System.out.println("У клубі немає студій.");
            return;
        }
        
        System.out.println("\n=== СТУДІЇ КЛУБУ ===");
        for (Studio studio : studios) {
            System.out.println("Назва: " + studio.getName());
            System.out.println("Місткість: " + studio.getCapacity());
            System.out.println("Чистота: " + (studio.isClean() ? "Чиста" : "Потребує прибирання"));
            System.out.println("---");
        }
    }
    
    private void manageClients() {
        // This would typically use a client repository
        System.out.println("\n=== УПРАВЛІННЯ КЛІЄНТАМИ ===");
        System.out.println("Примітка: Клієнти створюються при реєстрації абонементів.");
        System.out.println("Натисніть Enter для повернення...");
        scanner.nextLine();
    }
    
    private void manageMemberships() {
        checkCurrentClub();
        if (currentClub == null) return;
        
        while (true) {
            System.out.println("\n=== УПРАВЛІННЯ АБОНЕМЕНТАМИ ===");
            System.out.println("1. Зареєструвати клієнта та видати абонемент");
            System.out.println("2. Перевірити статус абонемента");
            System.out.println("0. Назад");
            
            int choice = readInt("Виберіть опцію: ");
            
            switch (choice) {
                case 1:
                    registerClientWithMembership();
                    break;
                case 2:
                    checkMembershipStatus();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Невірний вибір.");
            }
        }
    }
    
    private void registerClientWithMembership() {
        checkCurrentClub();
        if (currentClub == null) return;
        
        String clientName = readString("Введіть ПІБ клієнта: ");
        String phoneNumber = readString("Введіть номер телефону: ");
        
        Client client = new Client(clientName, phoneNumber);
        
        System.out.println("\nТипи абонементів:");
        System.out.println("1. SINGLE_CLUB - для одного клубу");
        System.out.println("2. NETWORK_WIDE - для всієї мережі");
        int typeChoice = readInt("Виберіть тип: ");
        
        MembershipType type = (typeChoice == 2) ? MembershipType.NETWORK_WIDE : MembershipType.SINGLE_CLUB;
        
        float cost = readFloat("Введіть вартість абонемента: ");
        int durationDays = readInt("Введіть тривалість (днів): ");
        
        System.out.println("\nЗнижка:");
        System.out.println("1. Без знижки");
        System.out.println("2. Відсоткова знижка");
        System.out.println("3. Фіксована знижка");
        int discountChoice = readInt("Виберіть тип знижки: ");
        
        core.domain.shop.DiscountOperation discount = DiscountStrategy.noDiscount();
        if (discountChoice == 2) {
            float percentage = readFloat("Введіть відсоток знижки: ");
            discount = DiscountStrategy.percentageDiscount(percentage);
        } else if (discountChoice == 3) {
            float fixedAmount = readFloat("Введіть фіксовану суму знижки: ");
            discount = DiscountStrategy.fixedDiscount(fixedAmount);
        }
        
        Membership membership = new Membership.Builder(type, LocalDate.now(), cost)
                .withDurationInDays(durationDays)
                .forClub(type == MembershipType.SINGLE_CLUB ? currentClub.getId() : null)
                .withDiscount(discount)
                .build();
        
        Administrator admin = getAdministrator();
        if (admin == null) {
            System.out.println("Помилка: У клубі немає адміністратора.");
            return;
        }
        
        try {
            membershipService.assignMembership(client, membership, admin);
            System.out.println("Клієнта зареєстровано: " + client.getFullName());
            System.out.println("Абонемент: " + membership.getType() + ", вартість: " + membership.getCost() + " грн");
            System.out.println("Активний: " + (client.hasActiveMembership() ? "Так" : "Ні"));
        } catch (IllegalArgumentException e) {
            System.out.println("Помилка: " + e.getMessage());
        }
    }
    
    private void checkMembershipStatus() {
        // Simplified - in a real app, you'd have a client repository
        System.out.println("Для перевірки статусу необхідно знати клієнта.");
        System.out.println("Ця функція вимагає реалізації репозиторію клієнтів.");
    }
    
    private Administrator getAdministrator() {
        if (currentClub == null) return null;
        
        return currentClub.getStaff().stream()
                .filter(employee -> employee instanceof Administrator)
                .map(employee -> (Administrator) employee)
                .findFirst()
                .orElse(null);
    }
    
    private void manageBookings() {
        checkCurrentClub();
        if (currentClub == null) return;
        
        while (true) {
            System.out.println("\n=== УПРАВЛІННЯ БРОНЮВАННЯМИ ===");
            System.out.println("1. Створити заняття");
            System.out.println("2. Записати клієнта на заняття");
            System.out.println("3. Скасувати бронювання");
            System.out.println("4. Переглянути заняття");
            System.out.println("0. Назад");
            
            int choice = readInt("Виберіть опцію: ");
            
            switch (choice) {
                case 1:
                    createGroupClass();
                    break;
                case 2:
                    bookClass();
                    break;
                case 3:
                    cancelBooking();
                    break;
                case 4:
                    viewClasses();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Невірний вибір.");
            }
        }
    }
    
    private void createGroupClass() {
        checkCurrentClub();
        if (currentClub == null) return;
        
        String className = readString("Введіть назву заняття: ");
        
        Trainer trainer = getTrainer();
        if (trainer == null) {
            System.out.println("Помилка: У клубі немає тренера.");
            return;
        }
        
        List<Studio> studios = currentClub.getStudios();
        if (studios.isEmpty()) {
            System.out.println("Помилка: У клубі немає студій.");
            return;
        }
        
        System.out.println("Доступні студії:");
        for (int i = 0; i < studios.size(); i++) {
            System.out.println((i + 1) + ". " + studios.get(i).getName());
        }
        int studioChoice = readInt("Виберіть студію: ");
        if (studioChoice < 1 || studioChoice > studios.size()) {
            System.out.println("Невірний вибір.");
            return;
        }
        Studio studio = studios.get(studioChoice - 1);
        
        LocalDateTime startTime = readDateTime("Введіть час початку (yyyy-MM-dd HH:mm): ");
        int durationMinutes = readInt("Введіть тривалість (хвилин): ");
        
        GroupClass groupClass = new GroupClass(className, trainer, studio, startTime, durationMinutes);
        boolean added = currentClub.getSchedule().addClass(groupClass);
        
        if (added) {
            System.out.println("Заняття створено: " + groupClass.getName() + " на " + startTime);
        } else {
            System.out.println("Не вдалося додати заняття (можливо, конфлікт часу).");
        }
    }
    
    private void bookClass() {
        checkCurrentClub();
        if (currentClub == null) return;
        
        String clientName = readString("Введіть ПІБ клієнта: ");
        String phoneNumber = readString("Введіть номер телефону: ");
        Client client = new Client(clientName, phoneNumber);
        
        // For demo purposes, we'll create a temporary membership
        Administrator admin = getAdministrator();
        if (admin != null) {
            Membership tempMembership = new Membership.Builder(
                    MembershipType.SINGLE_CLUB, LocalDate.now(), 500)
                    .withDurationInDays(30)
                    .forClub(currentClub.getId())
                    .build();
            try {
                membershipService.assignMembership(client, tempMembership, admin);
            } catch (IllegalArgumentException e) {
                System.out.println("Помилка при створенні тимчасового абонемента: " + e.getMessage());
            }
        }
        
        List<GroupClass> classes = currentClub.getSchedule().getAllClasses();
        if (classes.isEmpty()) {
            System.out.println("Немає доступних занять.");
            return;
        }
        
        System.out.println("Доступні заняття:");
        for (int i = 0; i < classes.size(); i++) {
            GroupClass gc = classes.get(i);
            System.out.println((i + 1) + ". " + gc.getName() + " - " + gc.getStartTime() + 
                             " (" + gc.getCurrentSize() + "/" + gc.getMaxCapacity() + ")");
        }
        
        int choice = readInt("Виберіть заняття: ");
        if (choice < 1 || choice > classes.size()) {
            System.out.println("Невірний вибір.");
            return;
        }
        
        GroupClass selectedClass = classes.get(choice - 1);
        
        try {
            bookingService.bookClass(client, selectedClass, currentClub);
            System.out.println("Клієнта записано на заняття: " + selectedClass.getName());
        } catch (MembershipAccessException e) {
            System.out.println("Помилка доступу: " + e.getMessage());
        } catch (BookingException e) {
            System.out.println("Помилка бронювання: " + e.getMessage());
        }
    }
    
    private void cancelBooking() {
        checkCurrentClub();
        if (currentClub == null) return;
        
        String clientName = readString("Введіть ПІБ клієнта: ");
        String phoneNumber = readString("Введіть номер телефону: ");
        Client client = new Client(clientName, phoneNumber);
        
        List<GroupClass> classes = currentClub.getSchedule().getAllClasses();
        if (classes.isEmpty()) {
            System.out.println("Немає доступних занять.");
            return;
        }
        
        System.out.println("Заняття:");
        for (int i = 0; i < classes.size(); i++) {
            System.out.println((i + 1) + ". " + classes.get(i).getName());
        }
        
        int choice = readInt("Виберіть заняття: ");
        if (choice < 1 || choice > classes.size()) {
            System.out.println("Невірний вибір.");
            return;
        }
        
        GroupClass selectedClass = classes.get(choice - 1);
        bookingService.cancelBooking(client, selectedClass);
        System.out.println("Бронювання скасовано.");
    }
    
    private void viewClasses() {
        checkCurrentClub();
        if (currentClub == null) return;
        
        List<GroupClass> classes = currentClub.getSchedule().getAllClasses();
        if (classes.isEmpty()) {
            System.out.println("Немає запланованих занять.");
            return;
        }
        
        System.out.println("\n=== ЗАНЯТТЯ ===");
        for (GroupClass gc : classes) {
            System.out.println("Назва: " + gc.getName());
            System.out.println("Тренер: " + gc.getTrainer().getFullName());
            System.out.println("Студія: " + gc.getStudio().getName());
            System.out.println("Час: " + gc.getStartTime() + " - " + gc.getEndTime());
            System.out.println("Учасників: " + gc.getCurrentSize() + "/" + gc.getMaxCapacity());
            System.out.println("Статус: " + gc.getCurrentState().getStatusDescription());
            System.out.println("---");
        }
    }
    
    private Trainer getTrainer() {
        if (currentClub == null) return null;
        
        return currentClub.getStaff().stream()
                .filter(employee -> employee instanceof Trainer)
                .map(employee -> (Trainer) employee)
                .findFirst()
                .orElse(null);
    }
    
    private void manageInventory() {
        checkCurrentClub();
        if (currentClub == null) return;
        
        while (true) {
            System.out.println("\n=== УПРАВЛІННЯ СКЛАДОМ ===");
            System.out.println("1. Додати товар на склад");
            System.out.println("2. Продати товар");
            System.out.println("3. Переглянути склад");
            System.out.println("4. Перевірити наявність товару");
            System.out.println("0. Назад");
            
            int choice = readInt("Виберіть опцію: ");
            
            switch (choice) {
                case 1:
                    addProductToInventory();
                    break;
                case 2:
                    sellProduct();
                    break;
                case 3:
                    viewInventory();
                    break;
                case 4:
                    checkProductStock();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Невірний вибір.");
            }
        }
    }
    
    private void addProductToInventory() {
        checkCurrentClub();
        if (currentClub == null) return;
        
        System.out.println("Типи продуктів:");
        System.out.println("1. CLOSE - Одяг");
        System.out.println("2. SUPPLEMENT - Додатки");
        
        int typeChoice = readInt("Виберіть тип: ");
        String productType = (typeChoice == 1) ? "CLOSE" : "SUPPLEMENT";
        
        String name = readString("Введіть назву продукту: ");
        double price = readDouble("Введіть ціну: ");
        
        Product product;
        if (productType.equals("CLOSE")) {
            String size = readString("Введіть розмір: ");
            String color = readString("Введіть колір: ");
            product = productFactory.createProduct(productType, name, price, size, color);
        } else {
            String flavor = readString("Введіть смак: ");
            product = productFactory.createProduct(productType, name, price, flavor);
        }
        
        int quantity = readInt("Введіть кількість: ");
        
        try {
            currentServiceManager.getInventoryService().addProduct(product, quantity);
            System.out.println("Товар додано: " + name + " x" + quantity);
        } catch (IllegalArgumentException e) {
            System.out.println("Помилка: " + e.getMessage());
        }
    }
    
    private void sellProduct() {
        checkCurrentClub();
        if (currentClub == null) return;
        
        // Simplified - in a real app, you'd select from available products
        System.out.println("Для продажу товару необхідно знати ID продукту.");
        System.out.println("Ця функція вимагає реалізації пошуку продуктів.");
    }
    
    private void viewInventory() {
        checkCurrentClub();
        if (currentClub == null) return;
        
        // Simplified - Inventory doesn't expose a list method
        System.out.println("Для перегляду складу необхідна реалізація методу отримання всіх продуктів.");
    }
    
    private void checkProductStock() {
        checkCurrentClub();
        if (currentClub == null) return;
        
        System.out.println("Для перевірки наявності необхідно знати продукт.");
        System.out.println("Ця функція вимагає реалізації пошуку продуктів.");
    }
    
    private void manageStaff() {
        checkCurrentClub();
        if (currentClub == null) return;
        
        while (true) {
            System.out.println("\n=== УПРАВЛІННЯ ПЕРСОНАЛОМ ===");
            System.out.println("1. Найняти тренера");
            System.out.println("2. Найняти адміністратора");
            System.out.println("3. Найняти прибиральника");
            System.out.println("4. Переглянути персонал");
            System.out.println("0. Назад");
            
            int choice = readInt("Виберіть опцію: ");
            
            switch (choice) {
                case 1:
                    hireTrainer();
                    break;
                case 2:
                    hireAdministrator();
                    break;
                case 3:
                    hireCleaner();
                    break;
                case 4:
                    viewStaff();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Невірний вибір.");
            }
        }
    }
    
    private void hireTrainer() {
        checkCurrentClub();
        if (currentClub == null) return;
        
        String name = readString("Введіть ПІБ тренера: ");
        double salary = readDouble("Введіть зарплату: ");
        String specialization = readString("Введіть спеціалізацію: ");
        
        Trainer trainer = new Trainer(name, salary, specialization);
        currentClub.addStaff(trainer);
        System.out.println("Тренера найнято: " + trainer.getFullName());
    }
    
    private void hireAdministrator() {
        checkCurrentClub();
        if (currentClub == null) return;
        
        String name = readString("Введіть ПІБ адміністратора: ");
        double salary = readDouble("Введіть зарплату: ");
        
        Administrator admin = new Administrator(name, salary);
        currentClub.addStaff(admin);
        System.out.println("Адміністратора найнято: " + admin.getFullName());
    }
    
    private void hireCleaner() {
        checkCurrentClub();
        if (currentClub == null) return;
        
        String name = readString("Введіть ПІБ прибиральника: ");
        double salary = readDouble("Введіть зарплату: ");
        
        System.out.println("Введіть зони відповідальності (через кому): ");
        String zonesInput = scanner.nextLine();
        List<String> zones = List.of(zonesInput.split(","));
        
        Cleaner cleaner = new Cleaner(name, salary, zones);
        currentClub.addStaff(cleaner);
        System.out.println("Прибиральника найнято: " + cleaner.getFullName());
    }
    
    private void viewStaff() {
        checkCurrentClub();
        if (currentClub == null) return;
        
        List<Employee> staff = new ArrayList<>();
        for (var employee : currentClub.getStaff()) {
            if (employee instanceof Employee) {
                staff.add((Employee) employee);
            }
        }
        
        if (staff.isEmpty()) {
            System.out.println("У клубі немає персоналу.");
            return;
        }
        
        System.out.println("\n=== ПЕРСОНАЛ ===");
        for (Employee employee : staff) {
            System.out.println("ПІБ: " + employee.getFullName());
            System.out.println("Зарплата: " + employee.getSalary() + " грн");
            if (employee instanceof Trainer) {
                System.out.println("Посада: Тренер");
                System.out.println("Спеціалізація: " + ((Trainer) employee).getSpecialization());
            } else if (employee instanceof Administrator) {
                System.out.println("Посада: Адміністратор");
            } else if (employee instanceof Cleaner) {
                System.out.println("Посада: Прибиральник");
                System.out.println("Зони: " + ((Cleaner) employee).getAssignedZones());
            }
            System.out.println("---");
        }
    }
    
    private void viewSchedule() {
        checkCurrentClub();
        if (currentClub == null) return;
        
        viewClasses();
    }
    
    private void viewNetworkInfo() {
        System.out.println("\n=== ІНФОРМАЦІЯ ПРО МЕРЕЖУ ===");
        System.out.println("Назва: " + fitnessNetwork.getName());
        System.out.println("Кількість клубів: " + fitnessNetwork.getClubs().size());
        
        int totalStudios = 0;
        int totalStaff = 0;
        for (FitnessClub club : fitnessNetwork.getClubs()) {
            totalStudios += club.getStudios().size();
            totalStaff += club.getStaff().size();
        }
        
        System.out.println("Всього студій: " + totalStudios);
        System.out.println("Всього персоналу: " + totalStaff);
    }
    
    // Utility methods for input
    
    private String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
    
    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Будь ласка, введіть ціле число.");
            }
        }
    }
    
    private float readFloat(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Float.parseFloat(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Будь ласка, введіть число.");
            }
        }
    }
    
    private double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Будь ласка, введіть число.");
            }
        }
    }
    
    private boolean readBoolean(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("true") || input.equals("yes") || input.equals("так")) {
                return true;
            } else if (input.equals("false") || input.equals("no") || input.equals("ні")) {
                return false;
            } else {
                System.out.println("Будь ласка, введіть true/false, yes/no, або так/ні.");
            }
        }
    }
    
    private LocalDate readDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return LocalDate.parse(scanner.nextLine().trim(), DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                System.out.println("Будь ласка, введіть дату у форматі yyyy-MM-dd.");
            }
        }
    }
    
    private LocalDateTime readDateTime(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return LocalDateTime.parse(scanner.nextLine().trim(), DATETIME_FORMATTER);
            } catch (DateTimeParseException e) {
                System.out.println("Будь ласка, введіть дату та час у форматі yyyy-MM-dd HH:mm.");
            }
        }
    }
    
    /**
     * Closes the scanner and releases resources.
     */
    public void close() {
        scanner.close();
    }
}

