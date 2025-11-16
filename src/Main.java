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

import core.exceptions.BookingException;
import core.exceptions.MembershipAccessException;
import core.exceptions.ProductOutOfStockException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("--- 1. Створення інфраструктури мережі ---");

        FitnessNetwork network = FitnessNetwork.getInstance("MyFitness Kyiv");
        System.out.println("Створено мережу: " + network.getName());

        FitnessClub clubOnObolon = new FitnessClub("м. Київ, пр. Оболонський, 1");
        network.addClub(clubOnObolon);
        System.out.println("Додано клуб за адресою: " + clubOnObolon.getAddress());

        Studio yogaStudio = new Studio("Зал для йоги", 20, false);
        Studio pool = new Studio("Басейн", 50, true);
        clubOnObolon.addStudio(yogaStudio);
        clubOnObolon.addStudio(pool);
        System.out.println("Додано студії: " + yogaStudio.getName() + ", " + pool.getName());

        System.out.println("\n--- 2. Найм персоналу ---");

        Trainer trainerAnna = new Trainer("Анна Шевченко", 25000, "Йога");
        Administrator adminPetro = new Administrator("Петро Іваненко", 30000);
        Cleaner cleanerMaria = new Cleaner("Марія Сидоренко", 15000, List.of("Роздягальні", "Басейн"));

        clubOnObolon.addStaff(trainerAnna);
        clubOnObolon.addStaff(adminPetro);
        clubOnObolon.addStaff(cleanerMaria);
        System.out.println("Найнято співробітників: " + trainerAnna.getFullName() + ", " + adminPetro.getFullName() + ", " + cleanerMaria.getFullName());

        System.out.println("\n--- 3. Наповнення складу товарів (з обробкою винятків) ---");

        ProductFactory productFactory = new ProductFactory();
        Product yogaMat = productFactory.createProduct("CLOSE", "Килимок для йоги", 800, "Standard", "Синій");

        try {
            clubOnObolon.getInventory().addProduct(yogaMat, 15);
            System.out.println("Додано на склад: '" + yogaMat.getName() + "', кількість: " + clubOnObolon.getInventory().getStockLevel(yogaMat));

            System.out.println("Намагаємося продати 20 килимків...");
            clubOnObolon.getInventory().removeProduct(yogaMat, 20);
            System.out.println("Товар продано (ЦЕ ПОВІДОМЛЕННЯ НЕ МАЄ З'ЯВИТИСЯ)");

        } catch (ProductOutOfStockException e) {
            System.out.println("ПОМИЛКА СКЛАДУ: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("ПОМИЛКА ВАЛІДАЦІЇ: " + e.getMessage());
        }

        System.out.println("\n--- 4. Реєстрація клієнта та продаж абонемента ---");

        Client clientOlena = new Client("Олена Ковальчук", "+380991234567");

        Membership membership = new Membership.Builder(MembershipType.SINGLE_CLUB, LocalDate.now(), 500)
                .withDurationInDays(30)
                .forClub(clubOnObolon.getId())
                .build();

        clientOlena.assignMembership(membership, adminPetro);
        System.out.println("Зареєстровано клієнта: " + clientOlena.getFullName());
        System.out.println("Тип абонемента: " + clientOlena.getMembership().getType());
        System.out.println("Чи активний абонемент? -> " + clientOlena.hasActiveMembership());

        System.out.println("\n--- 5. Імітація бізнес-процесу: Запис на заняття (з обробкою винятків) ---");

        LocalDateTime yogaClassTime = LocalDateTime.now().plusDays(1).withHour(18).withMinute(0);
        GroupClass yogaClass = new GroupClass("Вечірня йога", trainerAnna, yogaStudio, yogaClassTime, 60);

        boolean addedSuccessfully = clubOnObolon.getSchedule().addClass(yogaClass);
        if (addedSuccessfully) {
            System.out.println("Заняття '" + yogaClass.getName() + "' успішно додано до розкладу на " + yogaClass.getStartTime());
        }

        System.out.println("Клієнт " + clientOlena.getFullName() + " намагається записатися на йогу...");
        try {

            if (!clientOlena.hasActiveMembership()) {
                throw new MembershipAccessException("Абонемент неактивний.");
            }

            if (!clientOlena.getMembership().hasAccessToClub(clubOnObolon.getId())) {
                throw new MembershipAccessException("Абонемент не дійсний для цього клубу.");
            }

            yogaClass.addParticipant(clientOlena);
            System.out.println("УСПІХ! Клієнт записаний. Кількість учасників: " + yogaClass.getCurrentSize() + "/" + yogaClass.getMaxCapacity());

        } catch (BookingException e) {
            System.out.println("ПОМИЛКА БРОНЮВАННЯ: " + e.getMessage());
        }

        System.out.println("\n--- Демонстрація завершена ---");
    }
}