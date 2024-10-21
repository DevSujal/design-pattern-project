import java.util.*;

// Base interface for historical eras
interface Era {
    void describeEra();
    List<String> getKeyEvents();
}

// Concrete classes for Indian historical eras
class IndusValleyCivilization implements Era {
    public void describeEra() {
        System.out.println("Welcome to the Indus Valley Civilization! Experience the advanced urban planning and trade culture of ancient India.");
    }

    public List<String> getKeyEvents() {
        return Arrays.asList("Development of Harappa and Mohenjo-Daro", "Trade with Mesopotamia", "Innovative city planning and drainage systems");
    }
}

class GuptaEmpire implements Era {
    public void describeEra() {
        System.out.println("Welcome to the Gupta Empire! Known as the Golden Age of India, it was a time of cultural and scientific advancements.");
    }

    public List<String> getKeyEvents() {
        return Arrays.asList("Flourishing of classical Indian art and literature", "Mathematical advances by Aryabhata", "Establishment of Nalanda University");
    }
}

class MughalEmpire implements Era {
    public void describeEra() {
        System.out.println("Welcome to the Mughal Empire! Experience the cultural synthesis, grand architecture, and bustling trade routes.");
    }

    public List<String> getKeyEvents() {
        return Arrays.asList("Reign of Akbar, the Great", "Construction of the Taj Mahal", "Flourishing trade and cultural synthesis");
    }
}

class MauryaEmpire implements Era {
    public void describeEra() {
        System.out.println("Welcome to the Maurya Empire! Witness the unification of India under Ashoka and the spread of Buddhism.");
    }

    public List<String> getKeyEvents() {
        return Arrays.asList("Reign of Emperor Chandragupta Maurya", "Kautilya's Arthashastra", "Ashoka's propagation of Buddhism");
    }
}

class MarathaEmpire implements Era {
    public void describeEra() {
        System.out.println("Welcome to the Maratha Empire! Explore the rise of the Marathas and their resistance against the Mughal Empire.");
    }

    public List<String> getKeyEvents() {
        return Arrays.asList("Shivaji's establishment of the Maratha kingdom", "Battles of Panipat", "Expansion under the Peshwas");
    }
}

// Factory to create instances of Indian historical eras
class IndianEraFactory {
    public Era getEra(String eraName) {
        switch (eraName.toLowerCase()) {
            case "indus valley civilization":
                return new IndusValleyCivilization();
            case "gupta empire":
                return new GuptaEmpire();
            case "mughal empire":
                return new MughalEmpire();
            case "maurya empire":
                return new MauryaEmpire();
            case "maratha empire":
                return new MarathaEmpire();
            default:
                return null;
        }
    }
}

// Memento class to save the state of the user
class TimeTravelerMemento {
    private final String era;

    public TimeTravelerMemento(String era) {
        this.era = era;
    }

    public String getSavedEra() {
        return era;
    }
}

// Originator class - Time Traveler
class TimeTraveler {
    private String currentEra;

    public void setEra(String era) {
        this.currentEra = era;
        System.out.println("You are now in: " + era);
    }

    public String getEra() {
        return currentEra;
    }

    public TimeTravelerMemento saveState() {
        return new TimeTravelerMemento(currentEra);
    }

    public void restoreState(TimeTravelerMemento memento) {
        currentEra = memento.getSavedEra();
        System.out.println("Restored to: " + currentEra);
    }
}

// Caretaker class to manage mementos
class TimeTravelHistory {
    private final List<TimeTravelerMemento> history = new ArrayList<>();

    public void save(TimeTraveler timeTraveler) {
        history.add(timeTraveler.saveState());
    }

    public void undo(TimeTraveler timeTraveler) {
        if (!history.isEmpty()) {
            timeTraveler.restoreState(history.remove(history.size() - 1));
        } else {
            System.out.println("No previous state to restore.");
        }
    }
}

// Composite pattern for events
class HistoricalEvent {
    private final String eventName;

    public HistoricalEvent(String eventName) {
        this.eventName = eventName;
    }

    public void display() {
        System.out.println("Historical Event: " + eventName);
    }
}

class EventGroup {
    private final List<HistoricalEvent> events = new ArrayList<>();

    public void addEvent(HistoricalEvent event) {
        events.add(event);
    }

    public void displayEvents() {
        for (HistoricalEvent event : events) {
            event.display();
        }
    }
}

// Main class to run the simulator
public class TimeTravel {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        IndianEraFactory eraFactory = new IndianEraFactory();
        TimeTraveler timeTraveler = new TimeTraveler();
        TimeTravelHistory history = new TimeTravelHistory();

        while (true) {
            System.out.println("\nChoose an era to travel to:");
            System.out.println("1. Indus Valley Civilization");
            System.out.println("2. Gupta Empire");
            System.out.println("3. Mughal Empire");
            System.out.println("4. Maurya Empire");
            System.out.println("5. Maratha Empire");
            System.out.println("6. Undo last travel");
            System.out.println("7. Exit");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    travelToEra("Indus Valley Civilization", eraFactory, timeTraveler, history);
                    break;
                case 2:
                    travelToEra("Gupta Empire", eraFactory, timeTraveler, history);
                    break;
                case 3:
                    travelToEra("Mughal Empire", eraFactory, timeTraveler, history);
                    break;
                case 4:
                    travelToEra("Maurya Empire", eraFactory, timeTraveler, history);
                    break;
                case 5:
                    travelToEra("Maratha Empire", eraFactory, timeTraveler, history);
                    break;
                case 6:
                    history.undo(timeTraveler);
                    break;
                case 7:
                    System.out.println("Exiting the Time Travel Simulator. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void travelToEra(String eraName, IndianEraFactory eraFactory, TimeTraveler timeTraveler, TimeTravelHistory history) {
        Era era = eraFactory.getEra(eraName);
        if (era != null) {
            timeTraveler.setEra(eraName);
            history.save(timeTraveler);
            era.describeEra();

            // Display historical events using Composite pattern
            EventGroup eventGroup = new EventGroup();
            for (String event : era.getKeyEvents()) {
                eventGroup.addEvent(new HistoricalEvent(event));
            }
            eventGroup.displayEvents();
        } else {
            System.out.println("Era not found.");
        }
    }
}
