import src.core.domain.client.Client;
import src.core.domain.client.Membership;
import src.core.domain.client.MembershipType;
import src.core.domain.club.FitnessClub;
import src.core.domain.club.FitnessNetwork;
import src.core.domain.club.Studio;
import src.core.domain.scheduling.GroupClass;
import src.core.domain.shop.Apparel;
import src.core.domain.shop.Product;
import src.core.domain.staff.Administrator;
import src.core.domain.staff.Cleaner;
import src.core.domain.staff.Trainer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("--- 1. Створення інфраструктури мережі ---");

        FitnessNetwork network = new FitnessNetwork("MyFitness Kyiv");
        System.out.println("Створено мережу: " + network.getName());

        FitnessClub clubOnObolon = new FitnessClub("м. Київ, пр. Оболонський, 1");
        network.addClub(clubOnObolon);
        System.out.println("Додано клуб за адресою: " + clubOnObolon.getAddress());

        Studio yogaStudio = new Studio("Зал для йоги", 20);
        Studio pool = new Studio("Басейн", 50);
        clubOnObolon.addStudio(yogaStudio);
        clubOnObolon.addStudio(pool);
        System.out.println("Додано студії: " + yogaStudio.getName() + ", " + pool.getName());

        System.out.println("\n--- 2. Найм персоналу ---");

        Trainer trainerAnna = new Trainer("Анна Шевченко", 25000, "Йога");
        Administrator adminPetro = new Administrator("Петро Іваненко", 30000);
        Cleaner cleanerMaria = new Cleaner("Марія Сидоренко", 15000, List.of("Роздягальні", "Басейн"));

        clubOnObolon.addEmployee(trainerAnna);
        clubOnObolon.addEmployee(adminPetro);
        clubOnObolon.addEmployee(cleanerMaria);
        System.out.println("Найнято співробітників: " + trainerAnna.getFullName() + " (" + trainerAnna.getJobTitle() + "), " + adminPetro.getFullName() + ", " + cleanerMaria.getFullName());

        System.out.println("\n--- 3. Наповнення складу товарів ---");

        Product yogaMat = new Apparel("Килимок для йоги", 800, "Standard", "Синій");
        clubOnObolon.getInventory().addProduct(yogaMat, 15); // 15 килимків
        System.out.println("Додано на склад: '" + yogaMat.getName() + "', кількість: " + clubOnObolon.getInventory().getStockLevel(yogaMat));

        System.out.println("\n--- 4. Реєстрація клієнта та продаж абонемента ---");
        Client clientOlena = new Client("Олена Ковальчук", "+380991234567");

        Membership membership = new Membership(MembershipType.SINGLE_CLUB, LocalDate.now(), 30, clubOnObolon.getId());
        clientOlena.assignMembership(membership);
        System.out.println("Зареєстровано клієнта: " + clientOlena.getFullName());
        System.out.println("Тип абонемента: " + clientOlena.getMembership().getType());
        System.out.println("Чи активний абонемент? -> " + clientOlena.hasActiveMembership());

        System.out.println("\n--- 5. Імітація бізнес-процесу: Запис на заняття ---");

        LocalDateTime yogaClassTime = LocalDateTime.now().plusDays(1).withHour(18).withMinute(0);
        GroupClass yogaClass = new GroupClass("Вечірня йога", trainerAnna, yogaStudio, yogaClassTime, 60);

        boolean addedSuccessfully = clubOnObolon.getSchedule().addClass(yogaClass);
        if (addedSuccessfully) {
            System.out.println("Заняття '" + yogaClass.getName() + "' успішно додано до розкладу на " + yogaClass.getStartTime());
        }


        System.out.println("Клієнт " + clientOlena.getFullName() + " намагається записатися на йогу...");

        if (clientOlena.hasActiveMembership()) {
            if (clientOlena.getMembership().hasAccessToClub(clubOnObolon.getId())) {
                boolean booked = yogaClass.addParticipant(clientOlena);
                if (booked) {
                    System.out.println("Успіх! Клієнт записаний. Кількість учасників: " + yogaClass.getCurrentSize() + "/" + yogaClass.getMaxCapacity());
                }
            } else {
                System.out.println("Відмова: Абонемент не дійсний для цього клубу.");
            }
        } else {
            System.out.println("Відмова: Абонемент неактивний.");
        }

        System.out.println("\n--- Демонстрація завершена ---");
    }
}
