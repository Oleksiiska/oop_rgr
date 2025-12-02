package core;

import core.domain.client.Client;
import core.domain.client.Membership;
import core.domain.client.MembershipType;
import core.domain.club.FitnessClub;
import core.domain.club.FitnessNetwork;
import core.domain.club.Studio;
import core.domain.shop.Product;
import core.domain.shop.ProductFactory;
import core.domain.staff.Administrator;
import core.domain.staff.Cleaner;
import core.domain.staff.Trainer;
import core.domain.scheduling.GroupClass;
import core.services.BookingService;
import core.services.MembershipService;

import core.exceptions.BookingException;
import core.exceptions.MembershipAccessException;
import core.exceptions.ProductOutOfStockException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Main Spring Boot application class for the Fitness Club Management System.
 * Demonstrates the functionality of the fitness club management system.
 */
@SpringBootApplication
@ComponentScan(basePackages = "core")
public class Main implements CommandLineRunner {
    
    private final FitnessNetwork fitnessNetwork;
    private final ProductFactory productFactory;
    private final MembershipService membershipService;
    private final BookingService bookingService;
    
    /**
     * Constructor with dependency injection.
     *
     * @param fitnessNetwork the fitness network instance
     * @param productFactory the product factory
     * @param membershipService the membership service
     * @param bookingService the booking service
     */
    @Autowired
    public Main(FitnessNetwork fitnessNetwork,
                ProductFactory productFactory,
                MembershipService membershipService,
                BookingService bookingService) {
        this.fitnessNetwork = fitnessNetwork;
        this.productFactory = productFactory;
        this.membershipService = membershipService;
        this.bookingService = bookingService;
    }
    
    /**
     * Main entry point of the application.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
    
    /**
     * Runs the demonstration of the fitness club management system.
     * This method is called after the Spring context is initialized.
     *
     * @param args command line arguments
     */
    @Override
    public void run(String... args) {
        System.out.println("=== Fitness Club Management System ===\n");
        
        // Step 1: Setup network and club
        FitnessClub club = demonstrateNetworkSetup();
        Studio yogaStudio = club.getStudios().stream()
                .filter(s -> s.getName().equals("Зал для йоги"))
                .findFirst()
                .orElseThrow();
        
        // Step 2: Hire staff
        Object[] staff = demonstrateStaffHiring(club);
        Trainer trainer = (Trainer) staff[0];
        Administrator admin = (Administrator) staff[1];
        
        // Step 3: Manage inventory
        demonstrateInventoryManagement(club);
        
        // Step 4: Register client
        Client client = demonstrateClientRegistration(club, admin);
        if (client == null) {
            System.out.println("Не вдалося зареєструвати клієнта. Продовження неможливе.");
            return;
        }
        
        // Step 5: Book class
        demonstrateClassBooking(club, client, trainer, yogaStudio);
        
        System.out.println("\n=== Демонстрація завершена ===");
    }
    
    /**
     * Demonstrates network and club infrastructure setup.
     *
     * @return the created fitness club
     */
    private FitnessClub demonstrateNetworkSetup() {
        System.out.println("--- 1. Створення інфраструктури мережі ---");
        
        System.out.println("Створено мережу: " + fitnessNetwork.getName());
        
        FitnessClub clubOnObolon = new FitnessClub("м. Київ, пр. Оболонський, 1");
        fitnessNetwork.addClub(clubOnObolon);
        System.out.println("Додано клуб за адресою: " + clubOnObolon.getAddress());
        
        Studio yogaStudio = new Studio("Зал для йоги", 20, false);
        Studio pool = new Studio("Басейн", 50, true);
        clubOnObolon.addStudio(yogaStudio);
        clubOnObolon.addStudio(pool);
        System.out.println("Додано студії: " + yogaStudio.getName() + ", " + pool.getName());
        
        return clubOnObolon;
    }
    
    /**
     * Demonstrates staff hiring process.
     *
     * @param club the fitness club to add staff to
     * @return an array containing [trainer, administrator, cleaner]
     */
    private Object[] demonstrateStaffHiring(FitnessClub club) {
        System.out.println("\n--- 2. Найм персоналу ---");
        
        Trainer trainerAnna = new Trainer("Анна Шевченко", 25000, "Йога");
        Administrator adminPetro = new Administrator("Петро Іваненко", 30000);
        Cleaner cleanerMaria = new Cleaner("Марія Сидоренко", 15000, List.of("Роздягальні", "Басейн"));
        
        club.addStaff(trainerAnna);
        club.addStaff(adminPetro);
        club.addStaff(cleanerMaria);
        System.out.println("Найнято співробітників: " + trainerAnna.getFullName() + ", " + 
                          adminPetro.getFullName() + ", " + cleanerMaria.getFullName());
        
        return new Object[]{trainerAnna, adminPetro, cleanerMaria};
    }
    
    /**
     * Demonstrates inventory management with exception handling.
     *
     * @param club the fitness club
     */
    private void demonstrateInventoryManagement(FitnessClub club) {
        System.out.println("\n--- 3. Наповнення складу товарів (з обробкою винятків) ---");
        
        Product yogaMat = productFactory.createProduct("CLOSE", "Килимок для йоги", 800, "Standard", "Синій");
        
        try {
            club.getInventory().addProduct(yogaMat, 15);
            System.out.println("Додано на склад: '" + yogaMat.getName() + "', кількість: " + 
                             club.getInventory().getStockLevel(yogaMat));
            
            System.out.println("Намагаємося продати 20 килимків...");
            club.getInventory().removeProduct(yogaMat, 20);
            System.out.println("Товар продано (ЦЕ ПОВІДОМЛЕННЯ НЕ МАЄ З'ЯВИТИСЯ)");
            
        } catch (ProductOutOfStockException e) {
            System.out.println("ПОМИЛКА СКЛАДУ: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("ПОМИЛКА ВАЛІДАЦІЇ: " + e.getMessage());
        }
    }
    
    /**
     * Demonstrates client registration and membership assignment.
     *
     * @param club the fitness club
     * @param administrator the administrator approving the membership
     * @return the registered client
     */
    private Client demonstrateClientRegistration(FitnessClub club, Administrator administrator) {
        System.out.println("\n--- 4. Реєстрація клієнта та продаж абонемента ---");
        
        Client clientOlena = new Client("Олена Ковальчук", "+380991234567");
        
        Membership membership = new Membership.Builder(MembershipType.SINGLE_CLUB, LocalDate.now(), 500)
                .withDurationInDays(30)
                .forClub(club.getId())
                .build();
        
        try {
            membershipService.assignMembership(clientOlena, membership, administrator);
            System.out.println("Зареєстровано клієнта: " + clientOlena.getFullName());
            System.out.println("Тип абонемента: " + clientOlena.getMembership().getType());
            System.out.println("Чи активний абонемент? -> " + clientOlena.hasActiveMembership());
            
            return clientOlena;
        } catch (IllegalArgumentException e) {
            System.out.println("ПОМИЛКА: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Demonstrates class booking process with exception handling.
     *
     * @param club the fitness club
     * @param client the client to book for
     * @param trainer the trainer for the class
     * @param studio the studio for the class
     */
    private void demonstrateClassBooking(FitnessClub club, Client client, Trainer trainer, Studio studio) {
        System.out.println("\n--- 5. Імітація бізнес-процесу: Запис на заняття (з обробкою винятків) ---");
        
        LocalDateTime yogaClassTime = LocalDateTime.now().plusDays(1).withHour(18).withMinute(0);
        GroupClass yogaClass = new GroupClass("Вечірня йога", trainer, studio, yogaClassTime, 60);
        
        boolean addedSuccessfully = club.getSchedule().addClass(yogaClass);
        if (addedSuccessfully) {
            System.out.println("Заняття '" + yogaClass.getName() + "' успішно додано до розкладу на " + 
                             yogaClass.getStartTime());
        }
        
        System.out.println("Клієнт " + client.getFullName() + " намагається записатися на йогу...");
        try {
            bookingService.bookClass(client, yogaClass, club);
            System.out.println("УСПІХ! Клієнт записаний. Кількість учасників: " + 
                            yogaClass.getCurrentSize() + "/" + yogaClass.getMaxCapacity());
        } catch (MembershipAccessException e) {
            System.out.println("ПОМИЛКА ДОСТУПУ: " + e.getMessage());
        } catch (BookingException e) {
            System.out.println("ПОМИЛКА БРОНЮВАННЯ: " + e.getMessage());
        }
    }
}