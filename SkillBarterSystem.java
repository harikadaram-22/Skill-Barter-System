import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SkillBarterSystem {

    // ==========================================
    // 1. DATA MODELS
    // ==========================================
    
    static class User {
        private final String id;
        private final String name;
        private final List<String> offeredSkills;
        private final List<String> desiredSkills;

        public User(String id, String name) {
            this.id = id;
            this.name = name;
            this.offeredSkills = new ArrayList<>();
            this.desiredSkills = new ArrayList<>();
        }

        public String getId() { return id; }
        public String getName() { return name; }
        public List<String> getOfferedSkills() { return offeredSkills; }
        public List<String> getDesiredSkills() { return desiredSkills; }

        public void addOfferedSkill(String skill) {
            this.offeredSkills.add(skill.toLowerCase().trim());
        }

        public void addDesiredSkill(String skill) {
            this.desiredSkills.add(skill.toLowerCase().trim());
        }

        @Override
        public String toString() {
            return name + " [Offers: " + offeredSkills + " | Wants: " + desiredSkills + "]";
        }
    }

    static class BarterMatch {
        private final User userA;
        private final User userB;
        private final String skillFromAToB;
        private final String skillFromBToA;

        public BarterMatch(User userA, User userB, String skillFromAToB, String skillFromBToA) {
            this.userA = userA;
            this.userB = userB;
            this.skillFromAToB = skillFromAToB;
            this.skillFromBToA = skillFromBToA;
        }

        public void displayMatch() {
            System.out.println("\n🤝 MUTUAL MATCH FOUND!");
            System.out.println("----------------------------------------------");
            System.out.println("👉 " + userA.getName() + " teaches [" + skillFromAToB.toUpperCase() + "] to " + userB.getName());
            System.out.println("👈 " + userB.getName() + " teaches [" + skillFromBToA.toUpperCase() + "] to " + userA.getName());
            System.out.println("----------------------------------------------");
        }
    }

    // ==========================================
    // 2. CORE SYSTEM ENGINE
    // ==========================================
    
    static class BarterEngine {
        private final List<User> users = new ArrayList<>();

        public void registerUser(User user) {
            users.add(user);
        }

        public List<User> getUsers() {
            return users;
        }

        public List<BarterMatch> findMatches() {
            List<BarterMatch> matches = new ArrayList<>();
            
            for (int i = 0; i < users.size(); i++) {
                User userA = users.get(i);
                for (int j = i + 1; j < users.size(); j++) {
                    User userB = users.get(j);
                    
                    String skillFromAToB = getMatchingSkill(userA.getOfferedSkills(), userB.getDesiredSkills());
                    String skillFromBToA = getMatchingSkill(userB.getOfferedSkills(), userA.getDesiredSkills());
                    
                    if (skillFromAToB != null && skillFromBToA != null) {
                        matches.add(new BarterMatch(userA, userB, skillFromAToB, skillFromBToA));
                    }
                }
            }
            return matches;
        }

        private String getMatchingSkill(List<String> offered, List<String> desired) {
            for (String skill : offered) {
                if (desired.contains(skill)) {
                    return skill;
                }
            }
            return null;
        }
    }

    // ==========================================
    // 3. EXECUTION INTERACTIVE MENU
    // ==========================================
    
    public static void main(String[] args) {
        BarterEngine engine = new BarterEngine();
        Scanner scanner = new Scanner(System.in);
        int userIdCounter = 1;

        // Seed with quick mock data for instant testing validation
        User mockAlice = new User("U1", "Alice");
        mockAlice.addOfferedSkill("Java");
        mockAlice.addDesiredSkill("Spanish");
        engine.registerUser(mockAlice);

        User mockBob = new User("U2", "Bob");
        mockBob.addOfferedSkill("Spanish");
        mockBob.addDesiredSkill("Java");
        engine.registerUser(mockBob);
        userIdCounter = 3;

        System.out.println("==========================================");
        System.out.println("    WELCOME TO THE SKILL BARTER SYSTEM   ");
        System.out.println("==========================================");

        while (true) {
            System.out.println("\n--- MAIN MENU ---");
            System.out.println("1. Register a New User & Skills");
            System.out.println("2. View Registered Profiles");
            System.out.println("3. Run Engine (Find Barter Matches)");
            System.out.println("4. Exit Application");
            System.out.print("Choose an option (1-4): ");
            
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    System.out.print("\nEnter profile name: ");
                    String name = scanner.nextLine().trim();
                    if (name.isEmpty()) {
                        System.out.println("❌ Name cannot be empty.");
                        break;
                    }

                    User newUser = new User("U" + userIdCounter++, name);

                    System.out.print("Enter skills you can TEACH (comma separated): ");
                    String[] offered = scanner.nextLine().split(",");
                    for (String s : offered) {
                        if (!s.trim().isEmpty()) newUser.addOfferedSkill(s);
                    }

                    System.out.print("Enter skills you want to LEARN (comma separated): ");
                    String[] desired = scanner.nextLine().split(",");
                    for (String s : desired) {
                        if (!s.trim().isEmpty()) newUser.addDesiredSkill(s);
                    }

                    engine.registerUser(newUser);
                    System.out.println("✔️ User '" + name + "' registered successfully!");
                    break;

                case "2":
                    System.out.println("\n--- CURRENT USER DATABASE ---");
                    List<User> userList = engine.getUsers();
                    if (userList.isEmpty()) {
                        System.out.println("No active profiles registered yet.");
                    } else {
                        for (User u : userList) {
                            System.out.println("- " + u);
                        }
                    }
                    break;

                case "3":
                    System.out.println("\n--- RUNNING MATCHING ENGINE ---");
                    List<BarterMatch> matches = engine.findMatches();
                    if (matches.isEmpty()) {
                        System.out.println("❌ No direct mutual trades match up right now.");
                    } else {
                        for (BarterMatch match : matches) {
                            match.displayMatch();
                        }
                    }
                    break;

                case "4":
                    System.out.println("\nThank you for using the Skill Barter System! Goodbye.");
                    scanner.close();
                    System.exit(0);

                default:
                    System.out.println("❌ Invalid choice selection. Please type a number between 1 and 4.");
            }
        }
    }
}
